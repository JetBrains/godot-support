package gdscript.polySymbols.psi

import com.intellij.polySymbols.declarations.PolySymbolDeclaration
import com.intellij.polySymbols.declarations.PolySymbolDeclarationProvider
import com.intellij.polySymbols.utils.PolySymbolDeclaredInPsi
import com.intellij.psi.PsiElement
import com.intellij.psi.util.PsiTreeUtil
import gdscript.polySymbols.sdk.GdSdkPolySymbol
import gdscript.psi.GdBindingPattern
import gdscript.psi.GdCallEx
import gdscript.psi.GdClassNameNmi
import gdscript.psi.GdClassNaming
import gdscript.psi.GdClassVarDeclTl
import gdscript.psi.GdConstDeclSt
import gdscript.psi.GdConstDeclTl
import gdscript.psi.GdEnumDeclNmi
import gdscript.psi.GdEnumValueNmi
import gdscript.psi.GdExpr
import gdscript.psi.GdFile
import gdscript.psi.GdForSt
import gdscript.psi.GdKeyNmi
import gdscript.psi.GdMethodDeclTl
import gdscript.psi.GdMethodIdNmi
import gdscript.psi.GdParam
import gdscript.psi.GdSignalIdNmi
import gdscript.psi.GdVarDeclSt
import gdscript.psi.GdVarNmi
import project.psi.util.ProjectAutoloadUtil

/**
 * Provides [PolySymbolDeclaration]s for PSI elements in real (non-synthetic) GDScript source
 * files, mirroring [GdSdkPolySymbolDeclarationProvider][gdscript.polySymbols.sdk.GdSdkPolySymbolDeclarationProvider]'s
 * dispatch shape but for user-authored code rather than synthetic SDK files.
 *
 * This provider is pure dispatch: it only decides which symbol(s) a PSI element backs, then asks
 * each symbol for its own [PolySymbolDeclaredInPsi.declaration]. It never builds a
 * [PolySymbolDeclaration] itself.
 */
class GdPsiPolySymbolDeclarationProvider : PolySymbolDeclarationProvider {

    override fun getDeclarations(element: PsiElement, offsetInElement: Int): Collection<PolySymbolDeclaration> {
        // Synthetic SDK-generated files are handled exclusively by GdSdkPolySymbolDeclarationProvider.
        if (element.containingFile?.getUserData(GdSdkPolySymbol.SYNTHETIC_SDK_CLASS_KEY) != null) return emptyList()

        return symbolsFor(element).mapNotNull { it.declaration }
    }

    private fun symbolsFor(element: PsiElement): List<PolySymbolDeclaredInPsi> = when (element) {
        is GdClassNameNmi -> listOf(GdPsiClassSymbol(element))

        is GdFile -> buildList {
            // A file with its own `class_name` declaration is represented by that identifier's own
            // GdClassNameNmi declaration (above), not by the file element itself.
            if (PsiTreeUtil.getStubChildOfType(element, GdClassNaming::class.java) == null) {
                add(GdPsiResourceClassSymbol(element))
            }
            autoloadSymbolFor(element)?.let(::add)
        }

        is GdMethodIdNmi -> {
            val parent = element.parent as? GdMethodDeclTl ?: return emptyList()
            listOf(if (parent.isConstructor) GdPsiConstructorSymbol(element) else GdPsiMethodSymbol(element))
        }

        is GdSignalIdNmi -> listOf(GdPsiSignalSymbol(element))
        is GdEnumDeclNmi -> listOf(GdPsiEnumSymbol(element))
        is GdEnumValueNmi -> listOf(GdPsiEnumValueSymbol(element))
        is GdKeyNmi -> listOf(GdPsiDictKeySymbol(element))

        is GdVarNmi -> when (val parent = element.parent) {
            is GdClassVarDeclTl -> buildList {
                add(GdPsiPropertySymbol(element))
                loadedClassAliasIfAny(parent.expr, element)?.let(::add)
            }
            is GdConstDeclTl -> buildList {
                add(GdPsiConstantSymbol(element))
                loadedClassAliasIfAny(parent.expr, element)?.let(::add)
            }
            is GdVarDeclSt -> listOf(GdPsiLocalVariableSymbol(element))
            is GdConstDeclSt -> listOf(GdPsiLocalConstantSymbol(element))
            is GdForSt -> listOf(GdPsiForVariableSymbol(element))
            is GdParam -> listOf(GdPsiParameterSymbol(element))
            is GdBindingPattern -> listOf(GdPsiBindingPatternSymbol(element))
            else -> emptyList()
        }

        else -> emptyList()
    }

    private fun autoloadSymbolFor(file: GdFile): GdPsiAutoloadSymbol? {
        val key = ProjectAutoloadUtil.listGlobals(file.project).firstOrNull { it.element == file }?.key
            ?: return null
        return GdPsiAutoloadSymbol(file, key)
    }

    // Mirrors GdPsiClassMemberScope.loadedClassAliases()'s preload/load detection.
    private fun loadedClassAliasIfAny(expr: GdExpr?, element: GdVarNmi): GdPsiLoadedClassAliasSymbol? {
        if (expr !is GdCallEx || expr.expr.text !in setOf("preload", "load")) return null
        return GdPsiLoadedClassAliasSymbol(element)
    }
}
