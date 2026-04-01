package gdscript.psi;

import org.jetbrains.annotations.*;
import com.intellij.psi.PsiElement;

public interface GdArgExpr extends PsiElement {

  @NotNull
  GdExpr getExpr();

  @NotNull
  String getReturnType();

}
