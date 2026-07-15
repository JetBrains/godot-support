package gdscript.psi;

import com.intellij.psi.PsiElement;
import org.jetbrains.annotations.Nullable;

public interface GdSetDecl extends PsiElement {

  @Nullable
  GdEndStmt getEndStmt();

  @Nullable
  GdSetMethodIdRef getSetMethodIdNm();

  @Nullable
  GdStmtOrSuite getStmtOrSuite();

  @Nullable
  GdTyped getTyped();

  @Nullable
  GdVarNmi getVarNmi();

}
