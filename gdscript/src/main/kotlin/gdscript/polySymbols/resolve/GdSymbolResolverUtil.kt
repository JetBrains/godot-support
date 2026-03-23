package gdscript.polySymbols.resolve

import com.intellij.model.psi.PsiSymbolReferenceService
import com.intellij.openapi.project.Project
import com.intellij.polySymbols.PolySymbol
import com.intellij.psi.PsiElement
import gdscript.index.impl.GdClassIdIndex
import gdscript.polySymbols.GdClassSymbol
import gdscript.polySymbols.index.GdPolySymbolQueriesUtil.getSdkClassSymbol
import gdscript.polySymbols.psi.GdPsiClassSymbolFactory
import gdscript.psi.utils.GdClassUtil

object GdSymbolResolverUtil {

    /**
     * Resolve by class name to a [gdscript.polySymbols.sdk.GdSdkClassSymbol], [gdscript.polySymbols.psi.GdPsiClassSymbol],
     * or a [gdscript.polySymbols.psi.GdPsiResourceClassSymbol].
     */
    fun resolveCanonicalClassSymbol(project: Project, classId: String, context: PsiElement? = null): GdClassSymbol? {
        return getSdkClassSymbol(project, classId)
            ?: context?.let { getPsiClassSymbol(project, classId, context) }
            ?: getPsiClassSymbol(project, classId)
    }

    fun getPsiClassSymbol(project: Project, classId: String, context: PsiElement): GdClassSymbol? {
        return GdClassUtil.getClassIdElement(classId, context, project)
            ?.let { GdPsiClassSymbolFactory.create(it) }
    }

    fun getPsiClassSymbol(project: Project, classId: String): GdClassSymbol? {
        return GdClassIdIndex.INSTANCE.getGloballyResolved(classId, project)
            .firstOrNull()
            ?.let { GdPsiClassSymbolFactory.create(it) }
            ?: GdClassUtil.getClassIdElement(classId, project)?.let { GdPsiClassSymbolFactory.create(it) }
    }


    fun PsiElement.resolveSymbolReference(): PolySymbol? =
        this.resolveSymbolReferences().firstOrNull()

    fun PsiElement.resolveSymbolReferences(): List<PolySymbol> =
        PsiSymbolReferenceService.getService().getReferences(this)
            .flatMap { it.resolveReference() }
            .filterIsInstance<PolySymbol>()

}
