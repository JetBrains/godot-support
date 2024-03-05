// This is a generated file. Not intended for manual editing.
package gdscript.psi;

import java.util.List;

import gdscript.psi.types.GdDocumented;
import org.jetbrains.annotations.*;
import com.intellij.psi.PsiElement;
import com.intellij.psi.StubBasedPsiElement;
import gdscript.index.stub.GdClassVarDeclStub;
import com.intellij.navigation.ItemPresentation;

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
