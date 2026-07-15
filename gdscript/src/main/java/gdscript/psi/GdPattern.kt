package gdscript.psi;

import com.intellij.psi.PsiElement;
import org.jetbrains.annotations.Nullable;

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
