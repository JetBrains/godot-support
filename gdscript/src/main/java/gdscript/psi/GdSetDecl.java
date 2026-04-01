package gdscript.psi;

import org.jetbrains.annotations.*;
import com.intellij.psi.PsiElement;

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
