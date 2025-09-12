// This is a generated file. Not intended for manual editing.
package gdscript.psi;

import java.util.List;

import gdscript.psi.types.GdDocumented;
import org.jetbrains.annotations.*;
import com.intellij.psi.PsiElement;
import java.util.LinkedHashMap;

public interface GdFuncDeclEx extends GdExpr, GdDocumented {

  @Nullable
  GdFuncDeclIdNmi getFuncDeclIdNmi();

  @Nullable
  GdParamList getParamList();

  @Nullable
  GdReturnHint getReturnHint();

  @NotNull
  GdStmtOrSuite getStmtOrSuite();

  @NotNull
  String getInvokedReturnType();

  @Nullable
  PsiElement getReturnExpr();

  @NotNull
  LinkedHashMap<String, String> getParameters();

}
