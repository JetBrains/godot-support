package gdscript.psi;

import org.jetbrains.annotations.*;
import com.intellij.psi.PsiElement;

public interface GdKeyValuePattern extends PsiElement {

  @Nullable
  GdPattern getPattern();

}
