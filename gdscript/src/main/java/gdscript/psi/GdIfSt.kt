package gdscript.psi;

import java.util.List;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface GdIfSt extends GdStmt {

  @NotNull
  List<GdElifSt> getElifStList();

  @Nullable
  GdElseSt getElseSt();

  @Nullable
  GdExpr getExpr();

  @Nullable
  GdStmtOrSuite getStmtOrSuite();

}
