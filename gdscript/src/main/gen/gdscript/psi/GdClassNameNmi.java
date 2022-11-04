// This is a generated file. Not intended for manual editing.
package gdscript.psi;

import java.util.List;
import org.jetbrains.annotations.*;
import com.intellij.psi.PsiElement;
import com.intellij.psi.StubBasedPsiElement;
import gdscript.index.stub.GdClassIdStub;

public interface GdClassNameNmi extends GdNamedIdElement, StubBasedPsiElement<GdClassIdStub> {

  @NotNull
  String getClassId();

  @Nullable
  String getParentName();

  boolean isInner();

  @NotNull
  String getName();

  @NotNull
  PsiElement setName(@NotNull String newName);

  @Nullable
  PsiElement getNameIdentifier();

}
