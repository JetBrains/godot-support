package gdscript.psi;

import com.intellij.psi.PsiElement;
import org.jetbrains.annotations.Nullable;

public interface GdEndStmt extends PsiElement {

  @Nullable
  GdNewLineEnd getNewLineEnd();

}
