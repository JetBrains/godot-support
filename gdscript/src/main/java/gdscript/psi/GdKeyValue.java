package gdscript.psi;

import java.util.List;
import com.intellij.psi.util.PsiTreeUtil;
import org.jetbrains.annotations.NotNull;
import com.intellij.psi.PsiElement;
import org.jetbrains.annotations.Nullable;

public interface GdKeyValue extends PsiElement {

  @NotNull
  List<GdExpr> getExprList();

  @Nullable
  default GdKeyNmi getKeyNmi() {
    return PsiTreeUtil.getChildOfType(this, GdKeyNmi.class);
  }

  /**
   * @return GdKeyNmi or GdExpr
   */
  @Nullable
  default PsiElement getKey() {
      // Identifier and string-literal keys are both wrapped in a GdKeyNmi by the parser,
      // any other key (an arbitrary expression) is the first expr
      GdKeyNmi keyNmi = PsiTreeUtil.getChildOfType(this, GdKeyNmi.class);
      if (keyNmi != null) return keyNmi;

      List<GdExpr> list = getExprList();
      return !list.isEmpty() ? list.getFirst() : null;
  }

  @Nullable
  default PsiElement getValue() {
    List<GdExpr> list = getExprList();
    if (!list.isEmpty()) return list.getLast();
    return null;
  }

}
