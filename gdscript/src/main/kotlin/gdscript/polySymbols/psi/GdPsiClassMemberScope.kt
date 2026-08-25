package gdscript.polySymbols.psi

import com.intellij.polySymbols.PolySymbol
import com.intellij.polySymbols.query.PolySymbolScope
import com.intellij.polySymbols.query.polySymbolScopeCached
import com.intellij.psi.PsiElement
import com.intellij.psi.util.PsiModificationTracker
import com.intellij.psi.util.PsiTreeUtil
import com.intellij.psi.util.stubChildrenOfType
import gdscript.polySymbols.GdPolySymbolKind
import gdscript.psi.GdCallEx
import gdscript.psi.GdClassDeclTl
import gdscript.psi.GdClassVarDeclTl
import gdscript.psi.GdConstDeclTl
import gdscript.psi.GdEnumDeclTl
import gdscript.psi.GdEnumValue
import gdscript.psi.GdMethodDeclTl
import gdscript.psi.GdSignalDeclTl
import gdscript.psi.GdTopLevelDecl

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

            element.stubChildrenOfType<GdTopLevelDecl>().forEach {
                when (it) {
                    is GdClassDeclTl -> GdPsiClassSymbolFactory.create(it)?.let(::add)
                    is GdMethodDeclTl -> {
                        val id = it.methodIdNmi ?: return@forEach
                        if (it.isConstructor)
                            add(GdPsiConstructorSymbol(id))
                        else
                            add(GdPsiMethodSymbol(id))
                    }

                    is GdClassVarDeclTl -> it.varNmi?.let { id -> add(GdPsiPropertySymbol(id)) }
                    is GdConstDeclTl -> it.varNmi?.let { id -> add(GdPsiConstantSymbol(id)) }
                    is GdEnumDeclTl -> {
                        val id = it.enumDeclNmi
                        if (id != null) {
                            add(GdPsiEnumSymbol(id))
                        } else {
                            // Unnamed `enum { A, B }` blocks expose their values directly in the
                            // enclosing class scope - there is no enum name to qualify through.
                            PsiTreeUtil.getChildrenOfTypeAsList(it, GdEnumValue::class.java)
                                .forEach { value -> add(GdPsiEnumValueSymbol(value.enumValueNmi)) }
                        }
                    }

                    is GdSignalDeclTl -> it.signalIdNmi?.let { id -> add(GdPsiSignalSymbol(id)) }
                }
            }

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
