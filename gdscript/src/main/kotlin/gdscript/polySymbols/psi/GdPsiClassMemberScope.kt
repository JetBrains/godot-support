package gdscript.polySymbols.psi

import com.intellij.model.Pointer
import com.intellij.polySymbols.PolySymbol
import com.intellij.polySymbols.PolySymbolKind
import com.intellij.polySymbols.PolySymbolQualifiedName
import com.intellij.polySymbols.completion.PolySymbolCodeCompletionItem
import com.intellij.polySymbols.query.PolySymbolCodeCompletionQueryParams
import com.intellij.polySymbols.query.PolySymbolListSymbolsQueryParams
import com.intellij.polySymbols.query.PolySymbolNameMatchQueryParams
import com.intellij.polySymbols.query.PolySymbolQueryStack
import com.intellij.polySymbols.query.PolySymbolScope
import com.intellij.polySymbols.utils.match
import com.intellij.psi.PsiElement
import com.intellij.psi.createSmartPointer
import com.intellij.psi.util.PsiTreeUtil
import gdscript.polySymbols.GdPolySymbolKind
import gdscript.polySymbols.GdPolySymbolNamespace
import gdscript.polySymbols.completion.gdCodeCompletions
import gdscript.psi.GdCallEx
import gdscript.psi.GdClassDeclTl
import gdscript.psi.GdClassVarDeclTl
import gdscript.psi.GdConstDeclTl
import gdscript.psi.GdEnumDeclTl
import gdscript.psi.GdMethodDeclTl
import gdscript.psi.GdSignalDeclTl

/**
 * Member scope for a PSI-backed GDScript class, backed by the class's source PSI element ([gdscript.psi.GdFile] or [GdClassDeclTl]).
 */
class GdPsiClassMemberScope(
    private val classElement: PsiElement,
) : PolySymbolScope {

    override fun createPointer(): Pointer<out PolySymbolScope> {
        val ptr = classElement.createSmartPointer()
        return Pointer { ptr.element?.let { GdPsiClassMemberScope(it) } }
    }

    private fun symbolsOfKind(kind: PolySymbolKind): List<PolySymbol> {
        return when (kind) {
            GdPolySymbolKind.CLASS ->
                PsiTreeUtil.getStubChildrenOfTypeAsList(classElement, GdClassDeclTl::class.java)
                    .mapNotNull { GdPsiClassSymbolFactory.create(it) }

            GdPolySymbolKind.METHOD ->
                PsiTreeUtil.getStubChildrenOfTypeAsList(classElement, GdMethodDeclTl::class.java)
                    .filter { !it.isConstructor }
                    .mapNotNull { it.methodIdNmi?.let { id -> GdPsiMethodSymbol(id) } }

            GdPolySymbolKind.CONSTRUCTOR ->
                PsiTreeUtil.getStubChildrenOfTypeAsList(classElement, GdMethodDeclTl::class.java)
                    .filter { it.isConstructor }
                    .mapNotNull { it.methodIdNmi?.let { id -> GdPsiConstructorSymbol(id) } }

            GdPolySymbolKind.PROPERTY ->
                PsiTreeUtil.getStubChildrenOfTypeAsList(classElement, GdClassVarDeclTl::class.java)
                    .mapNotNull { it.varNmi?.let { id -> GdPsiPropertySymbol(id) } }

            GdPolySymbolKind.CONSTANT ->
                PsiTreeUtil.getStubChildrenOfTypeAsList(classElement, GdConstDeclTl::class.java)
                    .mapNotNull { it.varNmi?.let { id -> GdPsiConstantSymbol(id) } }

            GdPolySymbolKind.ENUM ->
                PsiTreeUtil.getStubChildrenOfTypeAsList(classElement, GdEnumDeclTl::class.java)
                    .mapNotNull { it.enumDeclNmi?.let { id -> GdPsiEnumSymbol(id) } }

            GdPolySymbolKind.SIGNAL ->
                PsiTreeUtil.getStubChildrenOfTypeAsList(classElement, GdSignalDeclTl::class.java)
                    .mapNotNull { it.signalIdNmi?.let { id -> GdPsiSignalSymbol(id) } }

            GdPolySymbolKind.LOADED_CLASS_ALIAS ->
                loadedClassAliases()

            else -> emptyList()
        }
    }

    // similar to GdTypeHintReference
    private fun loadedClassAliases(): List<PolySymbol> {
        val list = mutableListOf<PolySymbol>()

        PsiTreeUtil.getChildrenOfAnyType(
            classElement,
            GdClassVarDeclTl::class.java,
            GdConstDeclTl::class.java,
        ).forEach { decl ->
            val expr = when (decl) {
                is GdClassVarDeclTl -> decl.expr
                is GdConstDeclTl -> decl.expr
                else -> return list
            }
            if (expr is GdCallEx && arrayOf("preload", "load").contains(expr.expr.text)) {
                val varNmi = when (decl) {
                    is GdClassVarDeclTl -> decl.varNmi
                    is GdConstDeclTl -> decl.varNmi
                    else -> null
                } ?: return list

                list.add(GdPsiLoadedClassAliasSymbol(varNmi))
            }
        }
        return list
    }

    override fun getMatchingSymbols(
        qualifiedName: PolySymbolQualifiedName,
        params: PolySymbolNameMatchQueryParams,
        stack: PolySymbolQueryStack,
    ): List<PolySymbol> {
        val kind = qualifiedName.kind
        if (kind.namespace != GdPolySymbolNamespace.NAMESPACE) return emptyList()

        val name = qualifiedName.name
        return symbolsOfKind(kind)
            .flatMap { it.match(name, params, stack) }
    }

    override fun getSymbols(
        kind: PolySymbolKind,
        params: PolySymbolListSymbolsQueryParams,
        stack: PolySymbolQueryStack,
    ): List<PolySymbol> {
        if (kind.namespace != GdPolySymbolNamespace.NAMESPACE) return emptyList()
        return symbolsOfKind(kind)
    }

    override fun getCodeCompletions(
        qualifiedName: PolySymbolQualifiedName,
        params: PolySymbolCodeCompletionQueryParams,
        stack: PolySymbolQueryStack,
    ): List<PolySymbolCodeCompletionItem> = gdCodeCompletions(qualifiedName, params, stack)

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is GdPsiClassMemberScope) return false
        return classElement == other.classElement
    }

    override fun hashCode(): Int = classElement.hashCode()
}