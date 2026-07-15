package gdscript.psi;

import org.jetbrains.annotations.NotNull;
import com.intellij.psi.PsiElement;

public interface GdArgExpr extends PsiElement {

  @NotNull
  GdExpr getExpr();

  @NotNull
  String getReturnType();

}
