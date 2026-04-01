package gdscript.psi;

import org.jetbrains.annotations.*;
import com.intellij.psi.PsiElement;

public interface GdPattern extends PsiElement {

  @Nullable
  GdArrayPattern getArrayPattern();

  @Nullable
  GdBindingPattern getBindingPattern();

  @Nullable
  GdDictPattern getDictPattern();

  @Nullable
  GdExpr getExpr();

}
