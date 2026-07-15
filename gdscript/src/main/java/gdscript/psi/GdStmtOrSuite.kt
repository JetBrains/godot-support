package gdscript.psi;

import com.intellij.psi.PsiElement;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public interface GdStmtOrSuite extends PsiElement {

  @Nullable
  GdStmt getStmt();

  @NotNull
  List<GdSuite> getSuiteList();

}
