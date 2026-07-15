package gdscript.psi;

import java.util.List;
import org.jetbrains.annotations.NotNull;

public interface GdAssignSt extends GdStmt {

  @NotNull
  GdAssignSign getAssignSign();

  @NotNull
  GdEndStmt getEndStmt();

  @NotNull
  List<GdExpr> getExprList();

}
