package gdscript.psi;

import java.util.List;
import com.intellij.psi.PsiElement;
import org.jetbrains.annotations.NotNull;

public interface GdAnnotationParams extends PsiElement {

  @NotNull
  List<GdExpr> getExprList();

}
