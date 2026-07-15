package gdscript.psi;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface GdAnnotationTl extends GdTopLevelDecl {

  @Nullable
  GdAnnotationParams getAnnotationParams();

  @NotNull
  GdAnnotationType getAnnotationType();

  @Nullable
  GdEndStmt getEndStmt();

}
