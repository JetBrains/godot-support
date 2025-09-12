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
                val targetClassDecl = qualifierExpr?.let {
                    val type = it.getReturnType()
                    if (type.isNotEmpty()) {
                        val target = GdClassUtil.getClassIdElement(type, element, element.project)
                        if (target != null) GdClassUtil.getOwningClassElement(target) as? GdClassDeclTl else null
                    } else null
                }

                val resolved = GdClassMemberUtil.findDeclaration(element)?.psi()

                if (targetClassDecl != null && resolved is PsiElement) {
                    val owner = GdClassUtil.getOwningClassElement(resolved)
                    // Allow accessing inner classes via their parent class (e.g., A1.B1)
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
        val targetClassDecl: PsiElement? = qualifierExpr?.let {
            val type = it.getReturnType()
            if (type.isNotEmpty()) {
                val target = GdClassUtil.getClassIdElement(type, element, element.project)
                if (target != null) GdClassUtil.getOwningClassElement(target) else null
            } else null
        }

        val members = if (targetClassDecl != null) {
            GdClassMemberUtil.listClassMemberDeclarations(targetClassDecl)
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

}
