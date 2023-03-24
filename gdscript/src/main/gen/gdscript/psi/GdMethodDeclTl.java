// This is a generated file. Not intended for manual editing.
package gdscript.psi;

import java.util.List;
import org.jetbrains.annotations.*;
import com.intellij.psi.PsiElement;
import com.intellij.psi.StubBasedPsiElement;
import gdscript.index.stub.GdMethodDeclStub;
import com.intellij.navigation.ItemPresentation;
import java.util.HashMap;

public interface GdMethodDeclTl extends GdTopLevelDecl, StubBasedPsiElement<GdMethodDeclStub> {

  @NotNull
  GdMethodIdNmi getMethodIdNmi();

  @Nullable
  GdParamList getParamList();

  @Nullable
  GdReturnHint getReturnHint();

  @Nullable
  GdStmtOrSuite getStmtOrSuite();

  boolean isStatic();

  boolean isVariadic();

  @NotNull
  String getName();

  @NotNull
  String getReturnType();

  @NotNull
  HashMap<String, String> getParameters();

  @NotNull
  ItemPresentation getPresentation();

  boolean isConstructor();

}
