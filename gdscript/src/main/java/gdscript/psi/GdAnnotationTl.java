package gdscript.psi;

import org.jetbrains.annotations.*;

public interface GdAnnotationTl extends GdTopLevelDecl {

  @Nullable
  GdAnnotationParams getAnnotationParams();

  @NotNull
  GdAnnotationType getAnnotationType();

  @Nullable
  GdEndStmt getEndStmt();

}
