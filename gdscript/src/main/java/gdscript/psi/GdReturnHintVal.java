package gdscript.psi;

import org.jetbrains.annotations.*;
import com.intellij.psi.PsiElement;

public interface GdReturnHintVal extends PsiElement {

  @Nullable
  GdTypedVal getTypedVal();

}
