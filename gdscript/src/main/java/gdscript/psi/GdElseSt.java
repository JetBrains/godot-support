package gdscript.psi;

import org.jetbrains.annotations.*;

public interface GdElseSt extends GdStmt {

  @Nullable
  GdStmtOrSuite getStmtOrSuite();

}
