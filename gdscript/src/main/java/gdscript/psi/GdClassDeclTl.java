package gdscript.psi;

import com.intellij.psi.StubBasedPsiElement;
import gdscript.index.stub.GdClassDeclStub;
import gdscript.psi.types.GdDocumented;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public interface GdClassDeclTl extends GdTopLevelDecl, StubBasedPsiElement<GdClassDeclStub>, GdDocumented {

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
