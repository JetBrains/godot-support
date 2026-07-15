package gdscript.psi;

import com.intellij.navigation.ItemPresentation;
import com.intellij.psi.StubBasedPsiElement;
import gdscript.index.stub.GdEnumDeclStub;
import gdscript.psi.types.GdDocumented;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.LinkedHashMap;
import java.util.List;

public interface GdEnumDeclTl extends GdTopLevelDecl, StubBasedPsiElement<GdEnumDeclStub>, GdDocumented {

  @Nullable
  GdEndStmt getEndStmt();

  @Nullable
  GdEnumDeclNmi getEnumDeclNmi();

  @NotNull
  List<GdEnumValue> getEnumValueList();

  @NotNull
  String getName();

  @NotNull
  LinkedHashMap<String, Long> getValues();

  @NotNull
  ItemPresentation getPresentation();

}
