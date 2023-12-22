// This is a generated file. Not intended for manual editing.
package gdscript.psi;

import java.util.List;

import gdscript.psi.types.GdDocumented;
import org.jetbrains.annotations.*;
import com.intellij.psi.PsiElement;
import com.intellij.psi.StubBasedPsiElement;
import gdscript.index.stub.GdMethodDeclStub;
import com.intellij.navigation.ItemPresentation;
import java.util.LinkedHashMap;

public interface GdMethodDeclTl extends GdTopLevelDecl, StubBasedPsiElement<GdMethodDeclStub>, GdDocumented {

  @Nullable
  GdMethodIdNmi getMethodIdNmi();

  @NotNull
  List<GdMethodSpecifier> getMethodSpecifierList();

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
  LinkedHashMap<String, String> getParameters();

  @NotNull
  ItemPresentation getPresentation();

  boolean isConstructor();

}
