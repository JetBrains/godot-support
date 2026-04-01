package gdscript.psi;

import org.jetbrains.annotations.*;
import com.intellij.psi.PsiElement;

public interface GdTyped extends PsiElement {

  @NotNull
  GdTypedVal getTypedVal();

}
