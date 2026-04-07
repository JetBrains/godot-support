package gdscript.psi;

import com.intellij.psi.PsiElement;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface GdMatchBlock extends PsiElement {

  @NotNull
  GdPatternList getPatternList();

  @Nullable
  GdStmtOrSuite getStmtOrSuite();

}
