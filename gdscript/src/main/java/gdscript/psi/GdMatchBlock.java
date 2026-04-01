package gdscript.psi;

import org.jetbrains.annotations.*;
import com.intellij.psi.PsiElement;

public interface GdMatchBlock extends PsiElement {

  @NotNull
  GdPatternList getPatternList();

  @Nullable
  GdStmtOrSuite getStmtOrSuite();

}
