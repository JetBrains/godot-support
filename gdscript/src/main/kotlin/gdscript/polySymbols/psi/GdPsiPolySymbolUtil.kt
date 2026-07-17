package gdscript.polySymbols.psi

import com.intellij.openapi.project.Project
import com.intellij.openapi.util.TextRange
import com.intellij.polySymbols.PolySymbol
import com.intellij.polySymbols.query.PolySymbolQueryExecutorFactory
import com.intellij.polySymbols.query.PolySymbolScope
import com.intellij.polySymbols.query.polySymbolScopeCached
import com.intellij.psi.PsiElement
import com.intellij.psi.util.PsiModificationTracker
import com.intellij.psi.util.PsiTreeUtil
import gdscript.GdKeywords
import gdscript.polySymbols.GdPolySymbolKind
import gdscript.polySymbols.index.GdPolySymbolQueriesUtil
import gdscript.polySymbols.resolve.GdSymbolResolverUtil
import gdscript.polySymbols.scope.gdSdkGlobalPolySymbolScope
import gdscript.psi.GdCallEx
import gdscript.psi.GdClassDeclTl
import gdscript.psi.GdClassVarDeclTl
import gdscript.psi.GdEnumDeclTl
import gdscript.psi.GdEnumValue
import gdscript.psi.GdExpr
import gdscript.psi.GdFile
import gdscript.psi.GdPrimaryEx
import gdscript.psi.GdPsiUtils
import gdscript.psi.GdRefIdRef
import gdscript.psi.GdVarDeclSt
import gdscript.psi.utils.GdClassMemberUtil
import gdscript.psi.utils.GdClassUtil

object GdPsiPolySymbolUtil {

    /**
     * The range of [text] excluding a matching pair of surrounding quotes, if present (e.g. for a
     * resource-path inheritance clause's `"res://base.gd"`, or a string-literal dictionary key's
     * `"key1"`) - or the full range otherwise.
     */
    fun quotedContentRange(text: String): TextRange =
        if (text.length >= 2 && (text[0] == '"' || text[0] == '\'') && text.last() == text[0])
            TextRange(1, text.length - 1)
        else
            TextRange(0, text.length)

    fun getOwnerClassId(source: PsiElement): String {
        val ownerElement = getOwnerClassElement(source)
        return when (ownerElement) {
            is GdFile -> GdClassUtil.getOwningClassName(ownerElement)
            else -> GdClassUtil.getFullClassId(ownerElement)
        }
    }

    /**
     * Returns one of: [GdClassDeclTl], [GdFile].
     */
    fun getOwnerClassElement(source: PsiElement): PsiElement {
        return GdClassUtil.getOwningClassElement(source)
    }

    fun getClassId(source: PsiElement): String {
        return GdClassUtil.getFullClassId(source)
    }

    /**
     * Extracts the leaf (simple) name from a possibly-dotted [classId].
     */
    fun getLeafName(classId: String): String {
        if (classId.startsWith("\"")) {
            val closingQuote = classId.indexOf('"', 1)
            if (closingQuote >= 0 && closingQuote < classId.length - 1) {
                // There is content after the closing quote, e.g. ".Outer.Inner"
                val suffix = classId.substring(closingQuote + 1)
                val lastDot = suffix.lastIndexOf('.')
                return if (lastDot >= 0) suffix.substring(lastDot + 1) else classId
            }
            return classId
        }
        val dot = classId.lastIndexOf('.')
        return if (dot >= 0) classId.substring(dot + 1) else classId
    }


    fun isStatic(element: GdRefIdRef): Boolean {
        val qualifier = GdClassMemberUtil.calledUpon(element) ?: return false
        val typeName = GdPsiUtils.getReturnType(qualifier)
        return (typeName.isNotEmpty() && isStaticAccessByName(element, qualifier, typeName))
            || isBareClassValueQualifier(qualifier)
    }

    /**
     * Resolves `new` in a `ClassName.new(...)` call to every constructor symbol declared on the
     * class (PSI or SDK - reuses [GdSymbolResolverUtil.listConstructorSymbols]'s kind-only,
     * no-name-filter query, so a class with overloaded SDK constructors, e.g. `Vector2`, resolves
     * to all of them). `new` itself is not a real, queryable symbol name (GDScript constructors are
     * always named `_init` in PSI, or the class name in SDK data - never literally `"new"`), so each
     * result is wrapped in [GdAliasedNameSymbol]: own-reference ranges are computed from the
     * referenced symbol's `name.length`, and neither `_init` (5 chars) nor an SDK class name would
     * match the `new` token's own length/text, which would overflow the reference's range into the
     * call's parentheses. [GdSymbolResolverUtil.resolveSymbolReferences] unwraps the delegate back
     * to the real constructor symbol for callers.
     */
    fun resolveConstructorSymbols(element: GdRefIdRef): List<PolySymbol> {
        val qualifier = GdClassMemberUtil.calledUpon(element) ?: return emptyList()
        val typeName = GdPsiUtils.getReturnType(qualifier)
        if (typeName.isEmpty()) return emptyList()
        val classSymbol = GdSymbolResolverUtil.resolveCanonicalClassSymbol(element.project, typeName, element) ?: return emptyList()
        return GdSymbolResolverUtil.listConstructorSymbols(classSymbol).map { GdAliasedNameSymbol(it, "new") }
    }

    /**
     * Resolves an unqualified reference inside an enum value's own initializer expression (e.g.
     * `PASSED` in `enum { PASS1 = 0, PASSED = PASS1 + 1 }`) to an *earlier* sibling value of the
     * same enum. GDScript allows an enum value to reference sibling values declared before it,
     * but not ones declared after (`CANNOT = OK` where `OK` comes later stays unresolved) — order
     * matters here, unlike locals, so this can't reuse
     * [gdscript.polySymbols.scope.GdLocalSymbolsStructuredScope]'s "visible regardless of textual
     * order" scope-by-containment model. Returns `null` when [element] isn't
     * inside an enum value's expression, or when no earlier sibling matches its text.
     */
    fun resolveEarlierEnumValueSymbol(element: GdRefIdRef): GdPsiEnumValueSymbol? {
        val enumValue = PsiTreeUtil.getParentOfType(element, GdEnumValue::class.java) ?: return null
        val enumDecl = PsiTreeUtil.getParentOfType(enumValue, GdEnumDeclTl::class.java) ?: return null
        return enumDecl.enumValueList
            .takeWhile { it != enumValue }
            .find { it.enumValueNmi.name == element.text }
            ?.let { GdPsiEnumValueSymbol(it.enumValueNmi) }
    }

    /**
     * True when [qualifier] is a single identifier bound to a variable/constant whose initializer
     * is itself a bare class reference (e.g. `var t1 := A1.B1`) rather than an instance
     * (`A1.B1.new()`/`.instance()`) — such a qualifier denotes the class itself, so member access
     * on it is a static access. [isStaticAccessByName] can't catch this: it only compares the
     * qualifier's own text against its resolved type name, which never matches when the qualifier
     * is a variable name rather than the class name itself.
     */
    private fun isBareClassValueQualifier(qualifier: GdExpr): Boolean {
        val singleRef = PsiTreeUtil.getChildrenOfType(qualifier, GdRefIdRef::class.java)?.singleOrNull()
            ?: return false
        val decl = GdClassMemberUtil.findDeclaration(singleRef)
        val init = when (decl) {
            is GdClassVarDeclTl -> decl.expr
            is GdVarDeclSt -> decl.expr
            else -> null
        }
        if (init == null || init is GdCallEx) return false
        val initText = init.text.orEmpty()
        return initText.isNotEmpty() && resolvesToClassChain(initText, qualifier)
    }

    /**
     * True when [name] (e.g. `"A1.B1"`) denotes a class reachable from [anchor]'s containing file —
     * either directly indexed, or as a chain of nested classes declared directly inside one another
     * starting from a top-level class in the file. [GdClassUtil.getClassIdElement] alone only
     * resolves single-segment/globally-indexed names, not a dotted nested-class chain.
     */
    private fun resolvesToClassChain(name: String, anchor: PsiElement): Boolean {
        if (name.isEmpty()) return false
        if (GdClassUtil.getClassIdElement(name, anchor, anchor.project) != null) return true
        val parts = name.split('.')
        if (parts.isEmpty()) return false
        var current = PsiTreeUtil.getStubChildrenOfTypeAsList(anchor.containingFile, GdClassDeclTl::class.java)
            .firstOrNull { it.getName() == parts[0] }
        var i = 1
        while (current != null && i < parts.size) {
            current = PsiTreeUtil.getStubChildrenOfTypeAsList(current, GdClassDeclTl::class.java)
                .firstOrNull { it.getName() == parts[i] }
            i++
        }
        return current != null && i == parts.size
    }


    /**
     * @GlobalScope has matching variables with classes
     *
     * Mirrors [gdscript.psi.utils.GdClassMemberUtil.isStaticAccessByName]
     */
    fun isStaticAccessByName(element: PsiElement, qualifier: GdExpr, typeName: String): Boolean {
        // We consider it a static class access when:
        // - The resolved type name equals the qualifier text (e.g., 'Outer'), OR
        // - The resolved type name equals 'FullOwnerId.QualifierText' to handle nested or file-qualified contexts.
        // And we additionally ensure there is no conflicting global variable with the same name in @GlobalScope.
        val qualifierText = qualifier.text
        val fullOwnerId = getClassId(qualifier)
        val looksLikeClassName = (typeName == qualifierText) || (typeName == "$fullOwnerId.$qualifierText")
        return looksLikeClassName && checkGlobalStaticMatch(element.project, typeName)
    }

    /**
     * Returns true if there is no global variable with the same name as the provided type name.
     */
    private fun checkGlobalStaticMatch(project: Project, name: String): Boolean {
        val executor = PolySymbolQueryExecutorFactory.createCustom{
            addRootScope(gdSdkGlobalPolySymbolScope(project))
        }
        return GdPolySymbolQueriesUtil.getSdkPropertySymbol(executor, GdKeywords.GLOBAL_SCOPE, name) == null
    }

    /**
     * Scope exposing a dictionary literal's own keys as [GdPolySymbolKind.DICT_KEY] symbols, so
     * that `dict.key1` resolves via [GdRefIdRef]'s usual [GdPolySymbolKind.QUALIFIABLE_SYMBOLS]
     * bridging. [anchor] is the declaration/value PSI the dictionary literal is a direct child of -
     * a var/const declaration statement for the first hop (`dict.key1`), or a [gdscript.psi.GdKeyValue]
     * for chaining (`dict.key1.key11`, where `key1`'s own [GdPsiDictKeySymbol.sourceElement].parent
     * is its `GdKeyValue`). Returns `null` when [anchor] has no dictionary-literal child at all.
     */
    fun dictKeyQueryScope(anchor: PsiElement?): PolySymbolScope? {
        val primaryEx = PsiTreeUtil.getStubChildOfType(anchor, GdPrimaryEx::class.java) ?: return null
        val dictDecl = primaryEx.dictDecl ?: return null
        return polySymbolScopeCached(dictDecl) {
            provides(GdPolySymbolKind.DICT_KEY)
            initialize {
                cacheDependencies(PsiModificationTracker.MODIFICATION_COUNT)
                element.keyValueList.forEach { kv -> kv.keyNmi?.let { add(GdPsiDictKeySymbol(it)) } }
            }
        }
    }
}
