package gdscript.psi;

import java.util.List;
import org.jetbrains.annotations.NotNull;
import com.intellij.psi.PsiElement;

public interface GdExpr extends PsiElement {

  @NotNull
  String getReturnType();

  @NotNull
  String getReturnTypeOrRes(boolean allowResource);

}
