// This is a generated file. Not intended for manual editing.
package config.psi;

import com.intellij.psi.tree.IElementType;
import com.intellij.psi.PsiElement;
import com.intellij.lang.ASTNode;
import config.psi.impl.*;

public interface GdConfigTypes {

  IElementType ANNOTATION = new GdConfigElementType("ANNOTATION");
  IElementType ANNOTATION_NM = new GdConfigElementType("ANNOTATION_NM");
  IElementType AN_PREFIX = new GdConfigElementType("AN_PREFIX");
  IElementType OPERATION = new GdConfigElementType("OPERATION");
  IElementType OPERATOR = new GdConfigElementType("OPERATOR");
  IElementType OPERATOR_NM = new GdConfigElementType("OPERATOR_NM");
  IElementType PARAM = new GdConfigElementType("PARAM");

  IElementType AN = new GdConfigTokenType("AN");
  IElementType COLON = new GdConfigTokenType("COLON");
  IElementType IDENTIFIER = new GdConfigTokenType("IDENTIFIER");
  IElementType OP = new GdConfigTokenType("OP");
  IElementType OPERAND = new GdConfigTokenType("OPERAND");
  IElementType REQUIRED = new GdConfigTokenType("REQUIRED");
  IElementType TYPE = new GdConfigTokenType("TYPE");
  IElementType VARIADIC = new GdConfigTokenType("VARIADIC");

  class Factory {
    public static PsiElement createElement(ASTNode node) {
      IElementType type = node.getElementType();
      if (type == ANNOTATION) {
        return new ConfigAnnotationImpl(node);
      }
      else if (type == ANNOTATION_NM) {
        return new ConfigAnnotationNmImpl(node);
      }
      else if (type == AN_PREFIX) {
        return new ConfigAnPrefixImpl(node);
      }
      else if (type == OPERATION) {
        return new ConfigOperationImpl(node);
      }
      else if (type == OPERATOR) {
        return new ConfigOperatorImpl(node);
      }
      else if (type == OPERATOR_NM) {
        return new ConfigOperatorNmImpl(node);
      }
      else if (type == PARAM) {
        return new ConfigParamImpl(node);
      }
      throw new AssertionError("Unknown element type: " + type);
    }
  }
}
