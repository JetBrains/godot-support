package gdscript.psi;

import gdscript.psi.types.GdDocumented;
import org.jetbrains.annotations.*;
import com.intellij.psi.StubBasedPsiElement;
import gdscript.index.stub.GdConstDeclStub;
import com.intellij.navigation.ItemPresentation;

public interface GdConstDeclTl extends GdTopLevelDecl, StubBasedPsiElement<GdConstDeclStub>, GdDocumented {

  @Nullable
  GdAssignTyped getAssignTyped();

  @Nullable
  GdEndStmt getEndStmt();

  @Nullable
  GdExpr getExpr();

  @Nullable
  GdTyped getTyped();

  @Nullable
  GdVarNmi getVarNmi();

  @NotNull
  String getName();

  @NotNull
  String getReturnType();

  @NotNull
  ItemPresentation getPresentation();

}
