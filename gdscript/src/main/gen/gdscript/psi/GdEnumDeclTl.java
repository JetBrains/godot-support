// This is a generated file. Not intended for manual editing.
package gdscript.psi;

import java.util.List;
import org.jetbrains.annotations.*;
import com.intellij.psi.PsiElement;
import com.intellij.psi.StubBasedPsiElement;
import gdscript.index.stub.GdEnumDeclStub;
import com.intellij.navigation.ItemPresentation;
import java.util.HashMap;

public interface GdEnumDeclTl extends GdTopLevelDecl, StubBasedPsiElement<GdEnumDeclStub> {

  @Nullable
  GdEndStmt getEndStmt();

  @Nullable
  GdEnumDeclNmi getEnumDeclNmi();

  @NotNull
  List<GdEnumValue> getEnumValueList();

  @NotNull
  String getName();

  @NotNull
  HashMap<String, Long> getValues();

  @NotNull
  ItemPresentation getPresentation();

}
