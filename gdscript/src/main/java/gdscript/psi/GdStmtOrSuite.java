package gdscript.psi;

import java.util.List;
import org.jetbrains.annotations.*;
import com.intellij.psi.PsiElement;

public interface GdStmtOrSuite extends PsiElement {

  @Nullable
  GdStmt getStmt();

  @NotNull
  List<GdSuite> getSuiteList();

}
