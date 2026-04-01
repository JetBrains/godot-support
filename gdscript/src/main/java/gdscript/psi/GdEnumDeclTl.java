package gdscript.psi;

import java.util.LinkedHashMap;
import java.util.List;

import gdscript.psi.types.GdDocumented;
import org.jetbrains.annotations.*;
import com.intellij.psi.StubBasedPsiElement;
import gdscript.index.stub.GdEnumDeclStub;
import com.intellij.navigation.ItemPresentation;

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
