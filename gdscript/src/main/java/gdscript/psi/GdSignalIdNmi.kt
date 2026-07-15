package gdscript.psi;

import java.util.List;
import org.jetbrains.annotations.NotNull;
import com.intellij.psi.PsiElement;

public interface GdSignalIdNmi extends GdNamedIdElement {

  @NotNull
  String getName();

  @NotNull
  PsiElement setName(@NotNull String newName);

  @NotNull
  PsiElement getNameIdentifier();

}
