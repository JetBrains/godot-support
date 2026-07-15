package gdscript.psi;

import com.intellij.navigation.ItemPresentation;
import com.intellij.psi.StubBasedPsiElement;
import gdscript.index.stub.GdConstDeclStub;
import gdscript.psi.types.GdDocumented;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

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
