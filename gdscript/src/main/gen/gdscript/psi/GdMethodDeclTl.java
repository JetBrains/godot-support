// This is a generated file. Not intended for manual editing.
package gdscript.psi;

import java.util.List;
import org.jetbrains.annotations.*;
import com.intellij.psi.PsiElement;
import com.intellij.psi.StubBasedPsiElement;
import gdscript.index.stub.GdMethodDeclStub;
import java.util.HashMap;

public interface GdMethodDeclTl extends GdTopLevelDecl, StubBasedPsiElement<GdMethodDeclStub> {

  @Nullable
  GdMethodIdNmi getMethodIdNmi();

  @Nullable
  GdParamList getParamList();

  @Nullable
  GdReturnHint getReturnHint();

  @Nullable
  GdStmtOrSuite getStmtOrSuite();

  @Nullable
  String getMethodName();

  @Nullable
  String getReturnType();

  @NotNull
  HashMap<String, String> getParameters();

}
