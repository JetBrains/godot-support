package gdscript.psi;

import org.jetbrains.annotations.*;

public interface GdPrimaryEx extends GdExpr {

  @Nullable
  GdArrayDecl getArrayDecl();

  @Nullable
  GdDictDecl getDictDecl();

  @Nullable
  GdExpr getExpr();

  @Nullable
  GdNodePath getNodePath();

}
