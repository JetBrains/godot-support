// This is a generated file. Not intended for manual editing.
package tscn.psi;

import com.intellij.psi.tree.IElementType;
import com.intellij.psi.PsiElement;
import com.intellij.lang.ASTNode;
import tscn.psi.impl.TscnConnectionHeaderElementType;
import tscn.psi.impl.TscnNodeHeaderElementType;
import tscn.psi.impl.TscnParagraphElementType;
import tscn.psi.impl.TscnResourceHeaderElementType;
import tscn.psi.impl.*;

public interface TscnTypes {

  IElementType ARG_LIST = new TscnElementType("ARG_LIST");
  IElementType ARRAY = new TscnElementType("ARRAY");
  IElementType CONNECTION_HEADER = TscnConnectionHeaderElementType.getInstance("CONNECTION_HEADER");
  IElementType DATA_LINE = new TscnElementType("DATA_LINE");
  IElementType DATA_LINE_HEADER = new TscnElementType("DATA_LINE_HEADER");
  IElementType DATA_LINE_NM = new TscnElementType("DATA_LINE_NM");
  IElementType DATA_LINE_VALUE = new TscnElementType("DATA_LINE_VALUE");
  IElementType EXPR_VALUE = new TscnElementType("EXPR_VALUE");
  IElementType HEADER = new TscnElementType("HEADER");
  IElementType HEADER_VALUE = new TscnElementType("HEADER_VALUE");
  IElementType HEADER_VALUE_NM = new TscnElementType("HEADER_VALUE_NM");
  IElementType HEADER_VALUE_VAL = new TscnElementType("HEADER_VALUE_VAL");
  IElementType IDENTIFIER_EX = new TscnElementType("IDENTIFIER_EX");
  IElementType JSON = new TscnElementType("JSON");
  IElementType JSON_PAIR = new TscnElementType("JSON_PAIR");
  IElementType JSON_PAIR_LIST = new TscnElementType("JSON_PAIR_LIST");
  IElementType JSON_VALUE = new TscnElementType("JSON_VALUE");
  IElementType NODE_HEADER = TscnNodeHeaderElementType.getInstance("NODE_HEADER");
  IElementType NUMBER_VALUE = new TscnElementType("NUMBER_VALUE");
  IElementType OBJECT = new TscnElementType("OBJECT");
  IElementType PARAGRAPH = TscnParagraphElementType.getInstance("PARAGRAPH");
  IElementType RESOURCE_HEADER = TscnResourceHeaderElementType.getInstance("RESOURCE_HEADER");
  IElementType SCENE_HEADER = new TscnElementType("SCENE_HEADER");
  IElementType UNKNOWN_HEADER = new TscnElementType("UNKNOWN_HEADER");
  IElementType VALUE = new TscnElementType("VALUE");
  IElementType VALUE_LIST = new TscnElementType("VALUE_LIST");

  IElementType BAD_CHARACTER = new TscnTokenType("bad_character");
  IElementType COLON = new TscnTokenType("COLON");
  IElementType COMMA = new TscnTokenType("COMMA");
  IElementType COMMENT = new TscnTokenType("comment");
  IElementType CONNECTION = new TscnTokenType("CONNECTION");
  IElementType EQ = new TscnTokenType("EQ");
  IElementType EXT_RESOURCE = new TscnTokenType("EXT_RESOURCE");
  IElementType FALSE = new TscnTokenType("FALSE");
  IElementType GD_SCENE = new TscnTokenType("GD_SCENE");
  IElementType IDENTIFIER = new TscnTokenType("IDENTIFIER");
  IElementType IDENTIFIER_REF = new TscnTokenType("IDENTIFIER_REF");
  IElementType LCBR = new TscnTokenType("LCBR");
  IElementType LRBR = new TscnTokenType("LRBR");
  IElementType LSBR = new TscnTokenType("LSBR");
  IElementType MINUS = new TscnTokenType("MINUS");
  IElementType NODE = new TscnTokenType("NODE");
  IElementType NULL = new TscnTokenType("NULL");
  IElementType NUMBER = new TscnTokenType("NUMBER");
  IElementType OBJECT_REF = new TscnTokenType("OBJECT_REF");
  IElementType PLUS = new TscnTokenType("PLUS");
  IElementType RCBR = new TscnTokenType("RCBR");
  IElementType RRBR = new TscnTokenType("RRBR");
  IElementType RSBR = new TscnTokenType("RSBR");
  IElementType SLASH = new TscnTokenType("SLASH");
  IElementType STRING = new TscnTokenType("STRING");
  IElementType STRING_REF = new TscnTokenType("STRING_REF");
  IElementType TRUE = new TscnTokenType("TRUE");

  class Factory {
    public static PsiElement createElement(ASTNode node) {
      IElementType type = node.getElementType();
      if (type == ARG_LIST) {
        return new TscnArgListImpl(node);
      }
      else if (type == ARRAY) {
        return new TscnArrayImpl(node);
      }
      else if (type == CONNECTION_HEADER) {
        return new TscnConnectionHeaderImpl(node);
      }
      else if (type == DATA_LINE) {
        return new TscnDataLineImpl(node);
      }
      else if (type == DATA_LINE_HEADER) {
        return new TscnDataLineHeaderImpl(node);
      }
      else if (type == DATA_LINE_NM) {
        return new TscnDataLineNmImpl(node);
      }
      else if (type == DATA_LINE_VALUE) {
        return new TscnDataLineValueImpl(node);
      }
      else if (type == EXPR_VALUE) {
        return new TscnExprValueImpl(node);
      }
      else if (type == HEADER) {
        return new TscnHeaderImpl(node);
      }
      else if (type == HEADER_VALUE) {
        return new TscnHeaderValueImpl(node);
      }
      else if (type == HEADER_VALUE_NM) {
        return new TscnHeaderValueNmImpl(node);
      }
      else if (type == HEADER_VALUE_VAL) {
        return new TscnHeaderValueValImpl(node);
      }
      else if (type == IDENTIFIER_EX) {
        return new TscnIdentifierExImpl(node);
      }
      else if (type == JSON) {
        return new TscnJsonImpl(node);
      }
      else if (type == JSON_PAIR) {
        return new TscnJsonPairImpl(node);
      }
      else if (type == JSON_PAIR_LIST) {
        return new TscnJsonPairListImpl(node);
      }
      else if (type == JSON_VALUE) {
        return new TscnJsonValueImpl(node);
      }
      else if (type == NODE_HEADER) {
        return new TscnNodeHeaderImpl(node);
      }
      else if (type == NUMBER_VALUE) {
        return new TscnNumberValueImpl(node);
      }
      else if (type == OBJECT) {
        return new TscnObjectImpl(node);
      }
      else if (type == PARAGRAPH) {
        return new TscnParagraphImpl(node);
      }
      else if (type == RESOURCE_HEADER) {
        return new TscnResourceHeaderImpl(node);
      }
      else if (type == SCENE_HEADER) {
        return new TscnSceneHeaderImpl(node);
      }
      else if (type == UNKNOWN_HEADER) {
        return new TscnUnknownHeaderImpl(node);
      }
      else if (type == VALUE) {
        return new TscnValueImpl(node);
      }
      else if (type == VALUE_LIST) {
        return new TscnValueListImpl(node);
      }
      throw new AssertionError("Unknown element type: " + type);
    }
  }
}
