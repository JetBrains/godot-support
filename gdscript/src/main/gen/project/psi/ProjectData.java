// This is a generated file. Not intended for manual editing.
package project.psi;

import java.util.List;
import org.jetbrains.annotations.*;
import com.intellij.psi.PsiElement;
import com.intellij.psi.StubBasedPsiElement;
import project.index.stub.ProjectDataStub;

public interface ProjectData extends PsiElement, StubBasedPsiElement<ProjectDataStub> {

  @NotNull
  ProjectDataKey getDataKey();

  @NotNull
  ProjectDataValue getDataValue();

  @NotNull
  String getKey();

  @NotNull
  String getValue();

}
