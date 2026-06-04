package gdscript.psi;

import java.util.List;
import com.intellij.lang.ASTNode;
import com.intellij.psi.tree.TokenSet;
import org.jetbrains.annotations.NotNull;
import com.intellij.psi.PsiElement;
import org.jetbrains.annotations.Nullable;

public interface GdArgList extends PsiElement {

  @NotNull
  List<GdArgExpr> getArgExprList();

  default @Nullable ASTNode getClosingParen() {
    ASTNode node = getNode();
    final ASTNode[] children = node.getChildren(TokenSet.create(GdTypes.RRBR));
    return children.length == 0 ? null : children[children.length - 1];
  }

}
