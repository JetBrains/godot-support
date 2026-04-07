package gdscript.psi;

import java.util.List;
import org.jetbrains.annotations.NotNull;
import com.intellij.psi.PsiElement;

public interface GdKeyValue extends PsiElement {

  @NotNull
  List<GdExpr> getExprList();

}
