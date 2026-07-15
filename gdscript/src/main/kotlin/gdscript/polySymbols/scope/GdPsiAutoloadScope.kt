package gdscript.polySymbols.scope

import com.intellij.openapi.project.Project
import com.intellij.polySymbols.query.PolySymbolScope
import com.intellij.polySymbols.query.polySymbolScopeCached
import com.intellij.psi.PsiManager
import com.jetbrains.rider.godot.community.gdscript.GdLanguage
import com.jetbrains.rider.godot.community.tscn.TscnLanguage
import gdscript.polySymbols.GdPolySymbolKind
import gdscript.polySymbols.psi.GdPsiAutoloadSymbol
import gdscript.psi.GdFile
import project.ProjectLanguage
import project.psi.util.ProjectAutoloadUtil

/**
 * Scope that exposes [GdPsiAutoloadSymbol] for all autoload singletons registered in project.godot.
 */
fun gdPsiAutoloadScope(project: Project): PolySymbolScope =
    polySymbolScopeCached(project) {
        provides(GdPolySymbolKind.AUTOLOAD)
        initialize {
            val psiManager = PsiManager.getInstance(project)
            cacheDependencies(
                psiManager.modificationTracker.forLanguage(ProjectLanguage),
                psiManager.modificationTracker.forLanguage(GdLanguage),
                psiManager.modificationTracker.forLanguage(TscnLanguage),
            )
            ProjectAutoloadUtil.listGlobals(project)
                .filter { it.element is GdFile }
                .forEach { add(GdPsiAutoloadSymbol(it.element as GdFile, it.key)) }
        }
    }
