package gdscript.polySymbols.psi

import com.intellij.openapi.project.Project
import com.intellij.polySymbols.query.PolySymbolQueryExecutorFactory
import com.intellij.psi.PsiElement
import com.intellij.psi.util.PsiTreeUtil
import gdscript.GdKeywords
import gdscript.polySymbols.index.GdPolySymbolQueriesUtil
import gdscript.polySymbols.scope.gdSdkGlobalPolySymbolScope
import gdscript.psi.GdCallEx
import gdscript.psi.GdClassDeclTl
import gdscript.psi.GdClassVarDeclTl
import gdscript.psi.GdExpr
import gdscript.psi.GdFile
import gdscript.psi.GdPsiUtils
import gdscript.psi.GdRefIdRef
import gdscript.psi.GdVarDeclSt
import gdscript.psi.utils.GdClassMemberUtil
import gdscript.psi.utils.GdClassUtil

object GdPsiPolySymbolUtil {

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
     * True when [qualifier] is a single identifier bound to a variable/constant whose initializer
     * is itself a bare class reference (e.g. `var t1 := A1.B1`) rather than an instance
     * (`A1.B1.new()`/`.instance()`) — such a qualifier denotes the class itself, so member access
     * on it is a static access. [isStaticAccessByName] can't catch this: it only compares the
     * qualifier's own text against its resolved type name, which never matches when the qualifier
     * is a variable name rather than the class name itself.
     *
     * Mirrors [gdscript.reference.GdClassMemberReference]'s equivalent check for the legacy resolver.
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
     *
     * Mirrors the `resolvesToClassChain` local helper in
     * [gdscript.reference.GdClassMemberReference]'s legacy resolver.
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
}
