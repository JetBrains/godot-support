package gdscript.psi;

import org.jetbrains.annotations.*;
import com.intellij.psi.PsiElement;

public interface GdEndStmt extends PsiElement {

  @Nullable
  GdNewLineEnd getNewLineEnd();

}
