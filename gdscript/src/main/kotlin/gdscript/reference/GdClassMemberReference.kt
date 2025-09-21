package gdscript.reference

import GdScriptPluginIcons
import com.intellij.codeInsight.highlighting.HighlightedReference
import com.intellij.codeInsight.lookup.LookupElement
import com.intellij.openapi.util.TextRange
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiFile
import com.intellij.psi.PsiReferenceBase
import com.intellij.psi.impl.source.resolve.ResolveCache
import com.intellij.psi.search.GlobalSearchScope
import com.intellij.psi.util.PsiTreeUtil
import gdscript.completion.GdLookup
import gdscript.completion.utils.GdCompletionUtil
import gdscript.index.impl.GdClassNamingIndex
import gdscript.psi.*
import gdscript.psi.utils.GdClassMemberUtil
import gdscript.psi.utils.GdClassUtil
import gdscript.settings.GdProjectSettingsState
import gdscript.utils.PsiElementUtil.psi

/**
 * RefId reference to ClassNames, Variables, Constants, etc...
 */
class GdClassMemberReference : PsiReferenceBase<GdRefIdRef>, HighlightedReference {

    companion object {
        fun resolveId(element: PsiElement?): PsiElement? {
            return when (element) {
                is GdClassVarDeclTl -> element.varNmi
                is GdClassDeclTl -> element.classNameNmi
                is GdConstDeclTl -> element.varNmi
                is GdVarDeclSt -> element.varNmi
                is GdConstDeclSt -> element.varNmi
                is GdEnumDeclTl -> element.enumDeclNmi
                is GdEnumValue -> element.enumValueNmi
                is GdMethodDeclTl -> element.methodIdNmi
                is GdSignalDeclTl -> element.signalIdNmi
                is GdForSt -> element.varNmi
                is GdParam -> element.varNmi
                is GdVarNmi -> element
                is GdBindingPattern -> element.varNmi
                is PsiFile -> element
                is GdClassNaming -> element.classNameNmi
                else -> null
            }
        }
    }

    private var key: String = ""

    constructor(element: GdRefIdRef) : super(element, TextRange(0, element.textLength)) {
        key = element.text
    }

    override fun handleElementRename(newElementName: String): PsiElement {
        return myElement.replace(GdElementFactory.refIdNm(myElement.project, newElementName))
    }

    /**
     * Resolves the declaration associated with the given element in the context of the project.
     * Utilizes caching through the ResolveCache mechanism to enhance performance.
     * The resolution process includes determining the relevant class, analyzing the owning class
     * of the target, and validating access permissions for class members.
     *
     * @return a PsiElement representing the resolved declaration if found, or null otherwise
     */
    fun resolveDeclaration(): PsiElement? {
        val cache = ResolveCache.getInstance(element.project)
        return cache.resolveWithCaching(
            this,
            ResolveCache.Resolver { _, _ ->
                val qualifierExpr = GdClassMemberUtil.calledUpon(element)

                // Helper to detect if a string like "A.B.C" refers to a (possibly nested) class available in the current file/scope
                val resolvesToClassChain = fun(name: String): Boolean {
                    if (name.isEmpty()) return false
                    if (GdClassUtil.getClassIdElement(name, element, element.project) != null) return true
                    val parts = name.split('.')
                    if (parts.isEmpty()) return false
                    var parent: PsiElement = GdClassUtil.getOwningClassElement(element)
                    // Start from file scope
                    parent = parent as? GdFile ?: element.containingFile
                    var current = PsiTreeUtil.getStubChildrenOfTypeAsList(parent, GdClassDeclTl::class.java)
                        .firstOrNull { it.name == parts[0] }
                    var i = 1
                    while (current != null && i < parts.size) {
                        current = PsiTreeUtil.getStubChildrenOfTypeAsList(current, GdClassDeclTl::class.java)
                            .firstOrNull { it.name == parts[i] }
                        i++
                    }
                    return current != null && i == parts.size
                }
                val targetClassDecl = qualifierExpr?.let {
                    val type = it.getReturnType()
                    if (type.isNotEmpty()) {
                        val target = GdClassUtil.getClassIdElement(type, element, element.project)
                        if (target != null) GdClassUtil.getOwningClassElement(target) as? GdClassDeclTl else null
                    } else null
                }

                // Determine if the access is static (on a class) based on the qualifier's declaration
                var isStaticAccess: Boolean? = null
                run {
                    val allRefs = PsiTreeUtil.getChildrenOfType(qualifierExpr, GdRefIdRef::class.java)
                    val leftRef = allRefs?.firstOrNull { it.text != element.text } ?: allRefs?.firstOrNull()
                    val decl = leftRef?.let { GdClassMemberUtil.findDeclaration(it)?.psi() }
                    isStaticAccess = inferStaticAccessFromDecl(decl)
                }

                // If qualifier is a simple identifier bound to a class-typed variable (initializer is a class id without .new/.instance),
                // disallow resolving any non-constructor members on it.
                run {
                    if (qualifierExpr != null) {
                        val refs = PsiTreeUtil.getChildrenOfType(qualifierExpr, GdRefIdRef::class.java)
                        val singleRef = refs?.singleOrNull()
                        if (singleRef != null) {
                            val d = GdClassMemberUtil.findDeclaration(singleRef)?.psi()
                            val init = when (d) {
                                is GdClassVarDeclTl -> d.expr
                                is GdVarDeclSt -> d.expr
                                else -> null
                            }
                            if (init != null && init !is GdCallEx) {
                                val initText = init.text.orEmpty()
                                if (initText.isNotEmpty() && resolvesToClassChain(initText)) {
                                    val name = element.text
                                    if (name != "new" && name != "instance") return@Resolver null
                                }
                            }
                        }
                    }
                }

                // If qualifier points to a class (directly or via class-typed variable) but target class couldn't be inferred,
                // do NOT block resolution here; we'll enforce static/instance rules after resolving the candidate.

                val resolved = GdClassMemberUtil.findDeclaration(element)?.psi()

                // If statically accessed, disallow resolving non-static members even if target class couldn't be inferred
                if (isStaticAccess == true) {
                    when (resolved) {
                        is GdMethodDeclTl -> if (!resolved.isStatic) return@Resolver null
                        is GdClassVarDeclTl -> if (!resolved.isStatic) return@Resolver null
                    }
                }

                // Additional guard: if qualifier denotes a class (directly or via class-typed var), block non-static members
                if (qualifierExpr != null && qualifierQualifiesAsClass(qualifierExpr)) {
                    when (resolved) {
                        is GdClassDeclTl -> { /* ok */ }
                        is GdMethodDeclTl -> if (!resolved.isStatic) return@Resolver null
                        is GdClassVarDeclTl -> if (!resolved.isStatic) return@Resolver null
                    }
                }

                if (targetClassDecl != null && resolved is PsiElement) {
                    val owner = GdClassUtil.getOwningClassElement(resolved)

                    // Enforce static vs instance access rules
                    when (resolved) {
                        is GdMethodDeclTl -> {
                            if (isStaticAccess == true && !resolved.isStatic) return@Resolver null
                        }
                        is GdClassVarDeclTl -> {
                            if (isStaticAccess == true && !resolved.isStatic) return@Resolver null
                        }
                        is GdClassDeclTl -> {
                            // Accessing a class via instance (obj.ClassName) is invalid
                            if (isStaticAccess == false) return@Resolver null
                        }
                    }

                    // Allow accessing inner classes via their direct parent class (e.g., A1.B1)
                    if (resolved is GdClassDeclTl) {
                        val enclosing = PsiTreeUtil.getStubOrPsiParentOfType(resolved, GdClassDeclTl::class.java)
                        if (enclosing != null && enclosing == targetClassDecl) {
                            // OK: accessing inner class on its parent
                        } else if (owner is GdClassDeclTl && owner != targetClassDecl) {
                            return@Resolver null
                        }
                    } else {
                        // For non-class members, require exact owning class match
                        if (owner is GdClassDeclTl && owner != targetClassDecl) {
                            return@Resolver null
                        }
                    }
                }

                resolved
            },
            false,
            false,
        )
    }

    override fun resolve(): PsiElement? {
        val direct = resolveId(resolveDeclaration())
        if (direct != null) return direct

        return GdClassNamingIndex.INSTANCE
            .get(element.text, element.project, GlobalSearchScope.allScope(element.project))
            .firstOrNull()?.containingFile
    }

    override fun getVariants(): Array<LookupElement> {
        val isCallable = this.completionIntoCallableParam()

        // If there's a qualifier, compute the target class and collect only its direct members.
        val qualifierExpr = GdClassMemberUtil.calledUpon(element)
        var targetClassDecl: PsiElement? = qualifierExpr?.let {
            val type = it.getReturnType()
            if (type.isNotEmpty()) {
                val target = GdClassUtil.getClassIdElement(type, element, element.project)
                if (target != null) GdClassUtil.getOwningClassElement(target) else null
            } else null
        }
        // Fallback: if qualifier is a class chain like A1.B1, try to interpret it as a class id
        if (targetClassDecl == null && qualifierExpr != null) {
            val chain = qualifierExpr.text
            val t = GdClassUtil.getClassIdElement(chain, element, element.project)
            if (t != null) targetClassDecl = GdClassUtil.getOwningClassElement(t)
        }
        // Determine static vs instance access for completion context
        var isStaticAccess: Boolean? = null
        if (qualifierExpr is GdCallEx) {
            isStaticAccess = false
        } else if (qualifierExpr != null) {
            val leftRef = PsiTreeUtil.getChildrenOfType(qualifierExpr, GdRefIdRef::class.java)?.firstOrNull()
            val decl = leftRef?.let { GdClassMemberUtil.findDeclaration(it)?.psi() }
            isStaticAccess = inferStaticAccessFromDecl(decl)
            if (isStaticAccess == null && (targetClassDecl != null || qualifierQualifiesAsClass(qualifierExpr))) {
                // If we could resolve a class from the qualifier chain, assume static access
                isStaticAccess = true
            }
        }

        val members = if (targetClassDecl != null) {
            GdClassMemberUtil.listClassMemberDeclarations(targetClassDecl, isStaticAccess)
        } else {
            GdClassMemberUtil.listClassMemberDeclarations(element)
        }

        val hidePrivate = GdProjectSettingsState.getInstance(element).state.hidePrivate
            && qualifierExpr != null

        val baseLookups = members.flatMap {
            GdCompletionUtil.lookups(it, isCallable).mapNotNull { lookup ->
                if (!hidePrivate || !lookup.lookupString.startsWith("_")) lookup
                else null
            }
        }

        return baseLookups.toTypedArray() + arrayOf(
            addMethod("new"),
            addMethod("instance"),
        )
    }

    private fun completionIntoCallableParam(): Boolean {
        return PsiTreeUtil.getParentOfType(element, GdArgExpr::class.java)?.let { arg ->
            PsiTreeUtil.getParentOfType(arg, GdCallEx::class.java)?.let {
                val index = arg.parent.children.indexOf(arg)
                val refId = PsiTreeUtil.getChildrenOfType(it.expr, GdRefIdRef::class.java)?.lastOrNull() ?: return false
                val decl = GdClassMemberReference(refId).resolveDeclaration()
                if (decl is GdMethodDeclTl) {
                    return decl.parameters.values.toTypedArray().getOrNull(index).orEmpty() == "Callable"
                }
                return false
            }
        } ?: false
    }

    private fun addMethod(name: String): LookupElement {
        return GdLookup.create(
            name,
            lookup = "()",
            presentable = name,
            priority = GdLookup.BUILT_IN,
            icon = GdScriptPluginIcons.GDScriptIcons.METHOD_MARKER,
        )
    }

    // Helpers to keep resolve/completion logic concise
    private fun inferStaticAccessFromDecl(decl: PsiElement?): Boolean? {
        fun inferFromInitializer(expr: GdExpr?): Boolean? {
            val call = expr as? GdCallEx
            val callee = call?.expr?.text.orEmpty()
            return when {
                callee.endsWith(".new") || callee.endsWith(".instance") -> false
                else -> {
                    val initText = expr?.text.orEmpty()
                    if (initText.isNotEmpty()) {
                        GdClassUtil.getClassIdElement(initText, element, element.project) != null
                    } else null
                }
            }
        }
        return when (decl) {
            is GdClassDeclTl, is GdClassNaming -> true
            is GdClassVarDeclTl -> inferFromInitializer(decl.expr)
            is GdVarDeclSt -> inferFromInitializer(decl.expr)
            else -> null
        }
    }

    private fun qualifierQualifiesAsClass(qualifierExpr: GdExpr?): Boolean {
        if (qualifierExpr == null) return false
        val refs = PsiTreeUtil.getChildrenOfType(qualifierExpr, GdRefIdRef::class.java)
        val leftRef = refs?.firstOrNull()
        val leftDecl = leftRef?.let { GdClassMemberUtil.findDeclaration(it)?.psi() }
        return when (leftDecl) {
            is GdClassDeclTl, is GdClassNaming -> true
            is GdClassVarDeclTl -> inferStaticAccessFromDecl(leftDecl) == true
            is GdVarDeclSt -> inferStaticAccessFromDecl(leftDecl) == true
            null -> GdClassUtil.getClassIdElement(qualifierExpr.text, element, element.project) != null
            else -> false
        }
    }
}
