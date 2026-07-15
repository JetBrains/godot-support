package gdscript.psi;

import com.intellij.psi.PsiElement;
import gdscript.psi.types.GdDocumented;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface GdConstDeclSt extends GdStmt, GdDocumented {

  @Nullable
  GdAssignTyped getAssignTyped();

  @Nullable
  GdEndStmt getEndStmt();

  @Nullable
  GdExpr getExpr();

  @Nullable
  GdTyped getTyped();

  @Nullable
  GdVarNmi getVarNmi();

  @NotNull
  String getName();

  @NotNull
  String getReturnType();

  @Nullable
  PsiElement getReturnExpr();

}
