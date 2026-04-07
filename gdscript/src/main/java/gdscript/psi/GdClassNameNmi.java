package gdscript.psi;

import com.intellij.psi.PsiElement;
import com.intellij.psi.StubBasedPsiElement;
import gdscript.index.stub.GdClassIdStub;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface GdClassNameNmi extends GdNamedIdElement, StubBasedPsiElement<GdClassIdStub> {

  @NotNull
  String getClassId();

  @Nullable
  String getParentName();

  boolean isInner();

  @NotNull
  PsiElement setName(@NotNull String newName);

  @NotNull
  String getName();

  @NotNull
  PsiElement getNameIdentifier();

}
