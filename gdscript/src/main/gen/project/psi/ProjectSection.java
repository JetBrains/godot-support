// This is a generated file. Not intended for manual editing.
package project.psi;

import java.util.List;
import org.jetbrains.annotations.*;
import com.intellij.psi.PsiElement;
import com.intellij.psi.StubBasedPsiElement;
import project.index.stub.ProjectSectionStub;

public interface ProjectSection extends PsiElement, StubBasedPsiElement<ProjectSectionStub> {

  @NotNull
  List<ProjectData> getDataList();

  @Nullable
  ProjectSectionNm getSectionNm();

  @NotNull
  String getName();

}
