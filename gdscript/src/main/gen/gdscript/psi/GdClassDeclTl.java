// This is a generated file. Not intended for manual editing.
package gdscript.psi;

import java.util.List;
import org.jetbrains.annotations.*;
import com.intellij.psi.PsiElement;
import com.intellij.psi.StubBasedPsiElement;
import gdscript.index.stub.GdClassDeclStub;

public interface GdClassDeclTl extends GdTopLevelDecl, StubBasedPsiElement<GdClassDeclStub> {

  @Nullable
  GdClassNameNmi getClassNameNmi();

  @NotNull
  List<GdInheritance> getInheritanceList();

  @NotNull
  List<GdTopLevelDecl> getTopLevelDeclList();

  @NotNull
  String getName();

  @NotNull
  String getParentName();

}
