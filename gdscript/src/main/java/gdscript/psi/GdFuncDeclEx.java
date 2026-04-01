package gdscript.psi;

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

  @Nullable
  GdStmtOrSuite getStmtOrSuite();

  @NotNull
  String getInvokedReturnType();

  @Nullable
  PsiElement getReturnExpr();

  @NotNull
  LinkedHashMap<String, String> getParameters();

}
