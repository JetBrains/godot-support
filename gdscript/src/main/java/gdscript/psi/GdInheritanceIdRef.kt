package gdscript.psi;

import com.intellij.psi.PsiFile;
import org.jetbrains.annotations.Nullable;

public interface GdInheritanceIdRef extends GdRefElement {

  @Nullable
  PsiFile getPsiFile();

  boolean isClassName();

}
