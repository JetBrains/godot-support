package gdscript.psi;

import gdscript.psi.types.GdDocumented;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface GdVarDeclSt extends GdStmt, GdDocumented {

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

}
