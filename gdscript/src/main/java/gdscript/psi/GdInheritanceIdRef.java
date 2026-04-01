package gdscript.psi;

import org.jetbrains.annotations.*;
import com.intellij.psi.PsiFile;

public interface GdInheritanceIdRef extends GdRefElement {

  @Nullable
  PsiFile getPsiFile();

  boolean isClassName();

}
