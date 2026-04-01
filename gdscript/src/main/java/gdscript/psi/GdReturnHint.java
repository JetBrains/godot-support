package gdscript.psi;

import org.jetbrains.annotations.*;
import com.intellij.psi.PsiElement;

public interface GdReturnHint extends PsiElement {

  @NotNull
  GdReturnHintVal getReturnHintVal();

}
