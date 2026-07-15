package gdscript.psi.impl;

import com.intellij.extapi.psi.ASTWrapperPsiElement;
import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElementVisitor;
import com.intellij.psi.util.PsiTreeUtil;
import gdscript.psi.GdStmt;
import gdscript.psi.GdStmtOrSuite;
import gdscript.psi.GdSuite;
import gdscript.psi.GdVisitor;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class GdStmtOrSuiteImpl extends ASTWrapperPsiElement implements GdStmtOrSuite {

  public GdStmtOrSuiteImpl(@NotNull ASTNode node) {
    super(node);
  }

  public void accept(@NotNull GdVisitor visitor) {
    visitor.visitStmtOrSuite(this);
  }

  @Override
  public void accept(@NotNull PsiElementVisitor visitor) {
    if (visitor instanceof GdVisitor) accept((GdVisitor)visitor);
    else super.accept(visitor);
  }

  @Override
  @Nullable
  public GdStmt getStmt() {
    return PsiTreeUtil.getChildOfType(this, GdStmt.class);
  }

  @Override
  @NotNull
  public List<GdSuite> getSuiteList() {
    return PsiTreeUtil.getChildrenOfTypeAsList(this, GdSuite.class);
  }

}
