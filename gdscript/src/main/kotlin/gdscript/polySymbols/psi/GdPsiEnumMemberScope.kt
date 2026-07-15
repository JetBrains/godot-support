package gdscript.polySymbols.psi

import com.intellij.polySymbols.query.PolySymbolScope
import com.intellij.polySymbols.query.polySymbolScopeCached
import com.intellij.psi.util.PsiModificationTracker
import com.intellij.psi.util.PsiTreeUtil
import gdscript.polySymbols.GdPolySymbolKind
import gdscript.psi.GdEnumDeclTl
import gdscript.psi.GdEnumValue

fun gdPsiEnumMemberScope(enumElement: GdEnumDeclTl): PolySymbolScope =
    polySymbolScopeCached(enumElement) {
        provides(GdPolySymbolKind.ENUM_VALUE)
        initialize {
            cacheDependencies(PsiModificationTracker.MODIFICATION_COUNT)
            PsiTreeUtil.getChildrenOfTypeAsList(element, GdEnumValue::class.java)
                .forEach { add(GdPsiEnumValueSymbol(it.enumValueNmi)) }
        }
    }
