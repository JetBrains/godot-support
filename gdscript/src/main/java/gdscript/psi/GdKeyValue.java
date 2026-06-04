package gdscript.psi;

import java.util.List;
import com.intellij.psi.tree.IElementType;
import org.jetbrains.annotations.NotNull;
import com.intellij.psi.PsiElement;
import org.jetbrains.annotations.Nullable;

public interface GdKeyValue extends PsiElement {

  @NotNull
  List<GdExpr> getExprList();

  @Nullable
  default GdExpr getKey() {
    List<GdExpr> list = getExprList();
    return !list.isEmpty() ? list.getFirst() : null;
  }

  @Nullable
  default PsiElement getValue() {
    List<GdExpr> list = getExprList();
    if (list.size() > 1) return list.get(1);

    if (getFirstChild() == null) return null;
    IElementType firstChildType = getFirstChild().getNode().getElementType();
    if (firstChildType == GdTypes.IDENTIFIER || firstChildType == GdTypes.STRING) return getLastChild();
    return null;
  }

}
