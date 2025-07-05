// This is a generated file. Not intended for manual editing.
package config.psi;

import com.intellij.psi.tree.IElementType;
import com.intellij.psi.PsiElement;
import com.intellij.lang.ASTNode;
import config.psi.impl.GdConfigAnnotationElementType;
import config.psi.impl.GdConfigOperationElementType;
import config.psi.impl.GdConfigOperatorElementType;
import config.psi.impl.*;

public interface GdConfigTypes {

  IElementType ANNOTATION = GdConfigAnnotationElementType.getInstance("ANNOTATION");
  IElementType ANNOTATION_NM = new GdConfigElementType("ANNOTATION_NM");
  IElementType LEFT_TYPE = new GdConfigElementType("LEFT_TYPE");
  IElementType OPERATION = GdConfigOperationElementType.getInstance("OPERATION");
  IElementType OPERATOR = GdConfigOperatorElementType.getInstance("OPERATOR");
  IElementType OPERATOR_NM = new GdConfigElementType("OPERATOR_NM");
  IElementType OP_TYPE = new GdConfigElementType("OP_TYPE");
  IElementType PARAM = new GdConfigElementType("PARAM");
  IElementType PARAM_NAME = new GdConfigElementType("PARAM_NAME");
  IElementType REQUIRED = new GdConfigElementType("REQUIRED");
  IElementType RIGHT_TYPE = new GdConfigElementType("RIGHT_TYPE");
  IElementType TYPE = new GdConfigElementType("TYPE");
  IElementType VARIADIC_MARK = new GdConfigElementType("VARIADIC_MARK");

  IElementType AN = new GdConfigTokenType("AN");
  IElementType COLON = new GdConfigTokenType("COLON");
  IElementType IDENTIFIER = new GdConfigTokenType("IDENTIFIER");
  IElementType NUMBER = new GdConfigTokenType("NUMBER");
  IElementType OP = new GdConfigTokenType("OP");
  IElementType OPERAND = new GdConfigTokenType("OPERAND");
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
      else if (type == LEFT_TYPE) {
        return new ConfigLeftTypeImpl(node);
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
      else if (type == OP_TYPE) {
        return new ConfigOpTypeImpl(node);
      }
      else if (type == PARAM) {
        return new ConfigParamImpl(node);
      }
      else if (type == PARAM_NAME) {
        return new ConfigParamNameImpl(node);
      }
      else if (type == REQUIRED) {
        return new ConfigRequiredImpl(node);
      }
      else if (type == RIGHT_TYPE) {
        return new ConfigRightTypeImpl(node);
      }
      else if (type == TYPE) {
        return new ConfigTypeImpl(node);
      }
      else if (type == VARIADIC_MARK) {
        return new ConfigVariadicMarkImpl(node);
      }
      throw new AssertionError("Unknown element type: " + type);
    }
  }
}
