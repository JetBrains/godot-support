package gdscript.polySymbols.resolve

import com.intellij.model.psi.PsiSymbolReferenceService
import com.intellij.openapi.project.Project
import com.intellij.polySymbols.PolySymbol
import com.intellij.polySymbols.PolySymbolKind
import com.intellij.polySymbols.query.PolySymbolQueryExecutorFactory
import com.intellij.polySymbols.utils.PolySymbolDelegate.Companion.unwrapAllDelegates
import com.intellij.polySymbols.utils.unwrapMatchedSymbols
import com.intellij.psi.PsiElement
import gdscript.index.impl.GdClassIdIndex
import gdscript.polySymbols.GdClassSymbol
import gdscript.polySymbols.GdPolySymbolKind
import gdscript.polySymbols.index.GdPolySymbolQueriesUtil.getSdkClassSymbol
import gdscript.polySymbols.psi.GdPsiClassSymbolFactory
import gdscript.psi.utils.GdClassUtil
import gdscript.psi.utils.PsiGdClassUtil

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

    /**
     * The class symbol for the class directly enclosing [element] (a [gdscript.psi.GdClassDeclTl]/
     * [gdscript.psi.GdClassNaming]/[gdscript.psi.GdFile]).
     */
    fun resolveOwnClassSymbol(element: PsiElement): GdClassSymbol? =
        GdPsiClassSymbolFactory.create(PsiGdClassUtil.getParentClassElement(element))

    /**
     * SDK/engine-ancestor-aware replacement for a plain `classId == className` check —
     * true when [classId], or any of its resolvable ancestors (project or SDK), equals [className].
     */
    fun isExtendingCanonical(classId: String, project: Project, context: PsiElement?, className: String): Boolean {
        var currentId: String? = classId
        val visited = mutableSetOf<String>()
        while (currentId != null) {
            if (currentId == className) return true
            if (!visited.add(currentId)) return false
            currentId = resolveCanonicalClassSymbol(project, currentId, context)?.resolveSuperClassSymbol()?.classId
        }
        return false
    }

    /** All METHOD symbols visible on [classSymbol] - its own level plus every resolvable ancestor. */
    fun listMethodSymbols(classSymbol: GdClassSymbol?): List<PolySymbol> {
        classSymbol ?: return emptyList()
        val executor = PolySymbolQueryExecutorFactory.createCustom {
            addRootScope(classSymbol.directMemberScope)
            addRootScopes(classSymbol.inheritedQueryScopes())
        }
        return executor.listSymbolsQuery(GdPolySymbolKind.METHOD, false).run()
            .flatMap { it.unwrapMatchedSymbols() }
    }

    /** The nearest METHOD symbol named [name] visible on [classSymbol] - its own level or an ancestor. */
    fun findMethodSymbol(classSymbol: GdClassSymbol?, name: String): PolySymbol? {
        classSymbol ?: return null
        val executor = PolySymbolQueryExecutorFactory.createCustom {
            addRootScope(classSymbol.directMemberScope)
            addRootScopes(classSymbol.inheritedQueryScopes())
        }
        return executor.nameMatchQuery(GdPolySymbolKind.METHOD, name).run()
            .flatMap { it.unwrapMatchedSymbols() }
            .firstOrNull()
    }

    /** The `_init` CONSTRUCTOR symbol visible on [classSymbol], if any. */
    fun findConstructorSymbol(classSymbol: GdClassSymbol?): PolySymbol? {
        classSymbol ?: return null
        val executor = PolySymbolQueryExecutorFactory.createCustom {
            addRootScope(classSymbol.directMemberScope)
            addRootScopes(classSymbol.inheritedQueryScopes())
        }
        return executor.nameMatchQuery(GdPolySymbolKind.CONSTRUCTOR, "_init").run()
            .flatMap { it.unwrapMatchedSymbols() }
            .firstOrNull()
    }

    /**
     * All CONSTRUCTOR symbols declared directly on [classSymbol]. Unlike [listMethodSymbols], queries
     * only [GdClassSymbol.directMemberScope] - constructors are not inherited the way methods are;
     * mirrors [gdscript.psi.utils.GdClassMemberUtil.listClassMemberDeclarations]'s pre-existing
     * no-ancestor-walk behavior for constructors exactly. A class with an overloaded SDK constructor
     * (e.g. `Vector2`) legitimately returns more than one symbol here.
     */
    fun listConstructorSymbols(classSymbol: GdClassSymbol?): List<PolySymbol> {
        classSymbol ?: return emptyList()
        val executor = PolySymbolQueryExecutorFactory.createCustom {
            addRootScope(classSymbol.directMemberScope)
        }
        return executor.listSymbolsQuery(GdPolySymbolKind.CONSTRUCTOR, false).run()
            .flatMap { it.unwrapMatchedSymbols() }
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

    /** The first resolved own-reference whose [PolySymbol.kind] is one of [kinds], if any. */
    fun PsiElement.resolveSymbolReference(vararg kinds: PolySymbolKind): PolySymbol? =
        this.resolveSymbolReferences().firstOrNull { it.kind in kinds }

    fun PsiElement.resolveSymbolReferences(): List<PolySymbol> =
        PsiSymbolReferenceService.getService().getReferences(this)
            .flatMap { it.resolveReference() }
            .filterIsInstance<PolySymbol>()
            .map { it.unwrapAllDelegates() }

}
