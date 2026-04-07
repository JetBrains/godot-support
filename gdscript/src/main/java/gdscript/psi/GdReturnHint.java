package gdscript.psi;

import org.jetbrains.annotations.NotNull;
import com.intellij.psi.PsiElement;

public interface GdReturnHint extends PsiElement {

  @NotNull
  GdReturnHintVal getReturnHintVal();

}
