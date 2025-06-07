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

    fun resolveDeclaration(): PsiElement? {
        val cache = ResolveCache.getInstance(element.project)
        return cache.resolveWithCaching(
            this,
            ResolveCache.Resolver { _, _ -> GdClassMemberUtil.findDeclaration(element)?.psi() },
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
        val members = GdClassMemberUtil.listDeclarations(element, allowResource = true)
        val hidePrivate = GdProjectSettingsState.getInstance(element).state.hidePrivate
            && GdClassMemberUtil.calledUpon(element) != null

        return members.flatMap {
            GdCompletionUtil.lookups(it, isCallable).mapNotNull { lookup ->
                if (!hidePrivate || !lookup.lookupString.startsWith("_")) lookup
                else null
            }
        }.toTypedArray() + arrayOf(
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
