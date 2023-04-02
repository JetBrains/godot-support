// This is a generated file. Not intended for manual editing.
package gdscript.psi;

import java.util.List;
import org.jetbrains.annotations.*;
import com.intellij.psi.PsiElement;
import java.util.LinkedHashMap;

public interface GdFuncDeclEx extends GdExpr {

  @Nullable
  GdFuncDeclIdNmi getFuncDeclIdNmi();

  @Nullable
  GdParamList getParamList();

  @Nullable
  GdReturnHint getReturnHint();

  @Nullable
  GdStmtOrSuite getStmtOrSuite();

  @NotNull
  String getReturnType();

  @Nullable
  PsiElement getReturnExpr();

  @NotNull
  LinkedHashMap<String, String> getParameters();

}
