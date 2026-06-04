package gdscript.psi.impl;

import com.intellij.lang.ASTNode;
import gdscript.psi.GdExpr;
import gdscript.psi.GdParenthesizedEx;
import gdscript.psi.GdVisitor;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class GdParenthesizedExImpl extends GdPrimaryExImpl implements GdParenthesizedEx {

    public GdParenthesizedExImpl(@NotNull ASTNode node) {
        super(node);
    }

    @Override
    public void accept(@NotNull GdVisitor visitor) {
        visitor.visitParenthesizedEx(this);
    }

    @Override
    public @Nullable GdExpr getContainedExpression() {
        return getExpr();
    }
}