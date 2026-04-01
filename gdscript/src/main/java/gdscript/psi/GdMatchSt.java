package gdscript.psi;

import java.util.List;
import org.jetbrains.annotations.*;

public interface GdMatchSt extends GdStmt {

  @Nullable
  GdExpr getExpr();

  @NotNull
  List<GdMatchBlock> getMatchBlockList();

}
