package gdscript.psi;

import com.intellij.psi.PsiElement;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

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
