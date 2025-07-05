// This is a generated file. Not intended for manual editing.
package gdscript.psi;

import java.util.List;

import gdscript.psi.types.GdDocumented;
import org.jetbrains.annotations.*;
import com.intellij.psi.PsiElement;

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
