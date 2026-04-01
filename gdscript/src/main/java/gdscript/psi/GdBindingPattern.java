package gdscript.psi;

import org.jetbrains.annotations.*;
import com.intellij.psi.PsiElement;

public interface GdBindingPattern extends PsiElement {

  @NotNull
  GdVarNmi getVarNmi();

}
