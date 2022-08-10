// This is a generated file. Not intended for manual editing.
package gdscript.psi;

import java.util.List;
import org.jetbrains.annotations.*;
import com.intellij.psi.PsiElement;
import com.intellij.psi.StubBasedPsiElement;
import gdscript.index.stub.GdConstDeclStub;
import com.intellij.navigation.ItemPresentation;

public interface GdConstDeclTl extends GdTopLevelDecl, StubBasedPsiElement<GdConstDeclStub> {

  @Nullable
  GdAssignTyped getAssignTyped();

  @Nullable
  GdConstIdNmi getConstIdNmi();

  @Nullable
  GdEndStmt getEndStmt();

  @Nullable
  GdExpr getExpr();

  @Nullable
  GdTyped getTyped();

  @Nullable
  String getConstName();

  @NotNull
  String getReturnType();

  @NotNull
  ItemPresentation getPresentation();

}
