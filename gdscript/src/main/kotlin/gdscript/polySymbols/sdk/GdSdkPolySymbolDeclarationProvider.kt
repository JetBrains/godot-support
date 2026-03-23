package gdscript.polySymbols.sdk

import com.intellij.openapi.util.TextRange
import com.intellij.polySymbols.PolySymbol
import com.intellij.polySymbols.declarations.PolySymbolDeclaration
import com.intellij.polySymbols.declarations.PolySymbolDeclarationProvider
import com.intellij.psi.PsiElement
import gdscript.polySymbols.index.GdPolySymbolQueriesUtil
import gdscript.psi.GdClassNameNmi
import gdscript.psi.GdClassVarDeclTl
import gdscript.psi.GdConstDeclTl
import gdscript.psi.GdEnumDeclNmi
import gdscript.psi.GdMethodDeclTl
import gdscript.psi.GdMethodIdNmi
import gdscript.psi.GdNamedIdElement
import gdscript.psi.GdSignalIdNmi
import gdscript.psi.GdVarNmi

/**
 * Provides [PolySymbolDeclaration]s for PSI elements inside synthetic SDK-generated GDScript files.
 *
 * This provider detects such synthetic files via the [GdSdkPolySymbol.SYNTHETIC_SDK_CLASS_KEY] user
 * data and maps each named PSI element back to the corresponding [GdSdkPolySymbol].
 */
class GdSdkPolySymbolDeclarationProvider : PolySymbolDeclarationProvider {

    override fun getDeclarations(element: PsiElement, offsetInElement: Int): Collection<PolySymbolDeclaration> {
        if (element !is GdNamedIdElement) return emptyList()

        val className = element.containingFile?.getUserData(GdSdkPolySymbol.SYNTHETIC_SDK_CLASS_KEY)
            ?: return emptyList()

        val symbol = resolveSdkSymbol(element, className) ?: return emptyList()
        return listOf(SdkPolySymbolDeclaration(symbol, element))
    }

    private fun resolveSdkSymbol(element: GdNamedIdElement, className: String): GdSdkPolySymbol? {
        val name = element.name ?: return null
        val project = element.project
        return when (element) {
            is GdClassNameNmi -> GdPolySymbolQueriesUtil.getSdkClassSymbol(project, name)
            is GdMethodIdNmi -> {
                val parent = element.parent as? GdMethodDeclTl
                if (parent?.isConstructor == true) {
                    GdPolySymbolQueriesUtil.getSdkConstructorSymbol(project, className)
                } else {
                    GdPolySymbolQueriesUtil.getSdkMethodSymbol(project, className, name)
                }
            }

            is GdSignalIdNmi -> GdPolySymbolQueriesUtil.getSdkSignalSymbol(project, className, name)
            is GdEnumDeclNmi -> GdPolySymbolQueriesUtil.getSdkEnumSymbol(project, className, name)
            is GdVarNmi -> when (element.parent) {
                is GdClassVarDeclTl -> GdPolySymbolQueriesUtil.getSdkPropertySymbol(project, className, name)
                is GdConstDeclTl -> GdPolySymbolQueriesUtil.getSdkConstantSymbol(project, className, name)
                else -> null
            }
            // TODO add support for EnumValueNmi
            else -> null
        }
    }
}

private class SdkPolySymbolDeclaration(
    private val symbol: PolySymbol,
    private val element: PsiElement,
) : PolySymbolDeclaration {
    override fun getDeclaringElement(): PsiElement = element
    override fun getRangeInDeclaringElement(): TextRange = TextRange(0, element.textLength)
    override fun getSymbol(): PolySymbol = symbol
}
