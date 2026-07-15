package gdscript.psi;

import com.intellij.polySymbols.references.PolySymbolOwnReferences;
import com.intellij.polySymbols.references.PolySymbolOwnReferencesHost;
import gdscript.polySymbols.GdPolySymbolKind;
import gdscript.polySymbols.GdPolySymbolModifier;
import gdscript.polySymbols.psi.GdPsiPolySymbolUtil;
import org.jetbrains.annotations.NotNull;

public interface GdRefIdRef extends GdRefElement, PolySymbolOwnReferencesHost {

    @Override
    default void buildOwnReferences(@NotNull PolySymbolOwnReferences.Builder builder) {
        builder.fromNameMatchQuery(
            GdPolySymbolKind.INSTANCE.getQUALIFIABLE_SYMBOLS(), getText(),
            GdPsiPolySymbolUtil.INSTANCE.isStatic(this)
            ? (symbol) -> symbol.getModifiers().contains(GdPolySymbolModifier.INSTANCE.getSTATIC())
            : (_) -> true
        );
    }
}
