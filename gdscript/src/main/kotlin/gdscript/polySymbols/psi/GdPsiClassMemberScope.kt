package gdscript.polySymbols.psi

import com.intellij.polySymbols.PolySymbol
import com.intellij.polySymbols.query.PolySymbolScope
import com.intellij.polySymbols.query.polySymbolScopeCached
import com.intellij.psi.PsiElement
import com.intellij.psi.util.PsiModificationTracker
import com.intellij.psi.util.PsiTreeUtil
import gdscript.polySymbols.GdPolySymbolKind
import gdscript.psi.GdCallEx
import gdscript.psi.GdClassDeclTl
import gdscript.psi.GdClassVarDeclTl
import gdscript.psi.GdConstDeclTl
import gdscript.psi.GdEnumDeclTl
import gdscript.psi.GdEnumValue
import gdscript.psi.GdMethodDeclTl
import gdscript.psi.GdSignalDeclTl

/**
 * Member scope for a PSI-backed GDScript class, backed by the class's source PSI element ([gdscript.psi.GdFile] or [GdClassDeclTl]).
 */
fun gdPsiClassMemberScope(classElement: PsiElement): PolySymbolScope =
    polySymbolScopeCached(classElement) {
        provides(
            GdPolySymbolKind.CLASS,
            GdPolySymbolKind.METHOD,
            GdPolySymbolKind.CONSTRUCTOR,
            GdPolySymbolKind.PROPERTY,
            GdPolySymbolKind.CONSTANT,
            GdPolySymbolKind.ENUM,
            GdPolySymbolKind.ENUM_VALUE,
            GdPolySymbolKind.SIGNAL,
            GdPolySymbolKind.LOADED_CLASS_ALIAS,
        )
        initialize {
            cacheDependencies(PsiModificationTracker.MODIFICATION_COUNT)

            PsiTreeUtil.getStubChildrenOfTypeAsList(element, GdClassDeclTl::class.java)
                .forEach { GdPsiClassSymbolFactory.create(it)?.let(::add) }

            PsiTreeUtil.getStubChildrenOfTypeAsList(element, GdMethodDeclTl::class.java)
                .forEach { methodDecl ->
                    val id = methodDecl.methodIdNmi ?: return@forEach
                    if (methodDecl.isConstructor) add(GdPsiConstructorSymbol(id)) else add(GdPsiMethodSymbol(id))
                }

            PsiTreeUtil.getStubChildrenOfTypeAsList(element, GdClassVarDeclTl::class.java)
                .forEach { it.varNmi?.let { id -> add(GdPsiPropertySymbol(id)) } }

            PsiTreeUtil.getStubChildrenOfTypeAsList(element, GdConstDeclTl::class.java)
                .forEach { it.varNmi?.let { id -> add(GdPsiConstantSymbol(id)) } }

            PsiTreeUtil.getStubChildrenOfTypeAsList(element, GdEnumDeclTl::class.java)
                .forEach { enumDecl ->
                    val id = enumDecl.enumDeclNmi
                    if (id != null) {
                        add(GdPsiEnumSymbol(id))
                    } else {
                        // Unnamed `enum { A, B }` blocks expose their values directly in the
                        // enclosing class scope - there is no enum name to qualify through.
                        PsiTreeUtil.getChildrenOfTypeAsList(enumDecl, GdEnumValue::class.java)
                            .forEach { add(GdPsiEnumValueSymbol(it.enumValueNmi)) }
                    }
                }

            PsiTreeUtil.getStubChildrenOfTypeAsList(element, GdSignalDeclTl::class.java)
                .forEach { it.signalIdNmi?.let { id -> add(GdPsiSignalSymbol(id)) } }

            loadedClassAliases(element).forEach(::add)
        }
    }

// similar to GdTypeHintReference
private fun loadedClassAliases(classElement: PsiElement): List<PolySymbol> {
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
