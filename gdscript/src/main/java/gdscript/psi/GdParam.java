package gdscript.psi;

import org.jetbrains.annotations.*;
import com.intellij.psi.PsiElement;

public interface GdParam extends PsiElement {

  @Nullable
  GdAssignTyped getAssignTyped();

  @Nullable
  GdExpr getExpr();

  @Nullable
  GdTyped getTyped();

  @NotNull
  GdVarNmi getVarNmi();

  @NotNull
  String getReturnType();

}
