// This is a generated file. Not intended for manual editing.
package gdscript.psi;

import java.util.List;
import org.jetbrains.annotations.*;
import com.intellij.psi.PsiElement;
import com.intellij.psi.StubBasedPsiElement;
import gdscript.index.stub.GdClassVarDeclStub;

public interface GdClassVarDeclTl extends GdTopLevelDecl, StubBasedPsiElement<GdClassVarDeclStub> {

  @NotNull
  List<GdAnnotation> getAnnotationList();

  @Nullable
  GdClassVarIdNmi getClassVarIdNmi();

  @Nullable
  GdEndStmt getEndStmt();

  @Nullable
  GdExpr getExpr();

  @Nullable
  GdSetgetDecl getSetgetDecl();

  @Nullable
  GdTyped getTyped();

  @Nullable
  String getVarName();

  @Nullable
  String getReturnType();

}
