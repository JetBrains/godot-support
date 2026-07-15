package gdscript.psi;

import com.intellij.psi.PsiElement;
import org.jetbrains.annotations.Nullable;

public interface GdEmptyStmt extends PsiElement {

  @Nullable
  GdEndStmt getEndStmt();

}
