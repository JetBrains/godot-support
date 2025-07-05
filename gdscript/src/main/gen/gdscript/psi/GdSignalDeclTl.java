// This is a generated file. Not intended for manual editing.
package gdscript.psi;

import java.util.List;

import gdscript.psi.types.GdDocumented;
import org.jetbrains.annotations.*;
import com.intellij.psi.PsiElement;
import com.intellij.psi.StubBasedPsiElement;
import gdscript.index.stub.GdSignalDeclStub;
import java.util.LinkedHashMap;

public interface GdSignalDeclTl extends GdTopLevelDecl, StubBasedPsiElement<GdSignalDeclStub>, GdDocumented {

  @Nullable
  GdEndStmt getEndStmt();

  @Nullable
  GdParamList getParamList();

  @Nullable
  GdSignalIdNmi getSignalIdNmi();

  @NotNull
  String getName();

  @NotNull
  LinkedHashMap<String, String> getParameters();

}
