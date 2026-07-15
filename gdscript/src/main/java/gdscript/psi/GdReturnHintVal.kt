package gdscript.psi;

import com.intellij.psi.PsiElement;
import org.jetbrains.annotations.Nullable;

public interface GdReturnHintVal extends PsiElement {

  @Nullable
  GdTypedVal getTypedVal();

}
