package gdscript.psi;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public interface GdMatchSt extends GdStmt {

  @Nullable
  GdExpr getExpr();

  @NotNull
  List<GdMatchBlock> getMatchBlockList();

}
