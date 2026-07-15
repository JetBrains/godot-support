package gdscript.psi;

import com.intellij.psi.StubBasedPsiElement;
import gdscript.index.stub.GdSignalDeclStub;
import gdscript.psi.types.GdDocumented;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

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
