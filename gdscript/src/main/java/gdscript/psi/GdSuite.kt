package gdscript.psi;

import java.util.List;
import org.jetbrains.annotations.NotNull;
import com.intellij.psi.PsiElement;

public interface GdSuite extends PsiElement {

  @NotNull
  List<GdStmt> getStmtList();

}
