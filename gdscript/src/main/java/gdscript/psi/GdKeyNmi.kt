package gdscript.psi;

import org.jetbrains.annotations.NotNull;
import com.intellij.psi.PsiElement;

public interface GdKeyNmi extends GdNamedIdElement {

    @Override
    @NotNull
    String getName();

    @Override
    @NotNull
    PsiElement setName(@NotNull String newName);

    @Override
    @NotNull
    PsiElement getNameIdentifier();

}
