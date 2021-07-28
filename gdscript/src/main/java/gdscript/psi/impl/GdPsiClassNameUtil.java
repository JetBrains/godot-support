package gdscript.psi.impl;

import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElement;
import gdscript.psi.*;

public class GdPsiClassNameUtil {

    public static PsiElement setName(GdClassNameNm element, String newName) {
        ASTNode keyNode = element.getNode().findChildByType(GdTypes.IDENTIFIER);
        if (keyNode != null) {
            PsiElement id = GdElementFactory.INSTANCE.createClassName(element.getProject(), newName);
            element.getNode().replaceChild(keyNode, id.getNode());
        }
        return element;
    }

    public static String getName(GdClassNameNm element) {
        ASTNode valueNode = element.getNode().findChildByType(GdTypes.IDENTIFIER);
        if (valueNode != null) {
            return valueNode.getText();
        } else {
            return "";
        }
    }

    public static PsiElement getNameIdentifier(GdClassNameNm element) {
        ASTNode keyNode = element.getNode().findChildByType(GdTypes.IDENTIFIER);
        if (keyNode != null) {
            return keyNode.getPsi();
        } else {
            return null;
        }
    }

    public static String getInheritanceName(GdInheritance element) {
        if (element == null) {
            return "";
        }

        ASTNode valueNode = element.getNode().findChildByType(GdTypes.INHERITANCE_ID_NMI);
        if (valueNode != null) {
            return valueNode.getText();
        } else {
            return "";
        }
    }

}
