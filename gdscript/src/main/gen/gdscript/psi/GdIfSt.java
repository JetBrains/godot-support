// This is a generated file. Not intended for manual editing.
package gdscript.psi;

import java.util.List;
import org.jetbrains.annotations.*;
import com.intellij.psi.PsiElement;

public interface GdIfSt extends GdStmt {

  @NotNull
  List<GdElifSt> getElifStList();

  @Nullable
  GdElseSt getElseSt();

  @Nullable
  GdExpr getExpr();

  @Nullable
  GdStmtOrSuite getStmtOrSuite();

}
