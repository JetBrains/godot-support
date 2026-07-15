package gdscript.psi;

import com.intellij.navigation.ItemPresentation;
import com.intellij.psi.StubBasedPsiElement;
import gdscript.index.stub.GdClassVarDeclStub;
import gdscript.psi.types.GdDocumented;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface GdClassVarDeclTl extends GdTopLevelDecl, StubBasedPsiElement<GdClassVarDeclStub>, GdDocumented {

  @Nullable
  GdAssignTyped getAssignTyped();

  @Nullable
  GdEndStmt getEndStmt();

  @Nullable
  GdExpr getExpr();

  @Nullable
  GdSetgetDecl getSetgetDecl();

  @Nullable
  GdTyped getTyped();

  @Nullable
  GdVarNmi getVarNmi();

  @NotNull
  String getName();

  @NotNull
  Boolean isStatic();

  @NotNull
  String getReturnType();

  @NotNull
  ItemPresentation getPresentation();

  boolean isAnnotated(@NotNull String annotator);

}
