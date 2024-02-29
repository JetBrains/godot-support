// This is a generated file. Not intended for manual editing.
package tscn.psi;

import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElement;
import com.intellij.psi.tree.IElementType;
import tscn.psi.impl.TscnConnectionHeaderElementType;
import tscn.psi.impl.TscnConnectionHeaderImpl;
import tscn.psi.impl.TscnDataLineImpl;
import tscn.psi.impl.TscnDataLineNmImpl;
import tscn.psi.impl.TscnDataLineValueImpl;
import tscn.psi.impl.TscnGodotObjectImpl;
import tscn.psi.impl.TscnHeaderValueImpl;
import tscn.psi.impl.TscnHeaderValueNmImpl;
import tscn.psi.impl.TscnHeaderValueValImpl;
import tscn.psi.impl.TscnJsonArrayImpl;
import tscn.psi.impl.TscnJsonObjectElemImpl;
import tscn.psi.impl.TscnJsonObjectImpl;
import tscn.psi.impl.TscnJsonValueImpl;
import tscn.psi.impl.TscnNodeHeaderElementType;
import tscn.psi.impl.TscnNodeHeaderImpl;
import tscn.psi.impl.TscnParagraphElementType;
import tscn.psi.impl.TscnParagraphImpl;
import tscn.psi.impl.TscnResourceHeaderElementType;
import tscn.psi.impl.TscnResourceHeaderImpl;
import tscn.psi.impl.TscnSceneHeaderImpl;
import tscn.psi.impl.TscnUnknownHeaderImpl;

public interface TscnTypes {

  IElementType CONNECTION_HEADER = TscnConnectionHeaderElementType.getInstance("CONNECTION_HEADER");
  IElementType DATA_LINE = new TscnElementType("DATA_LINE");
  IElementType DATA_LINE_NM = new TscnElementType("DATA_LINE_NM");
  IElementType DATA_LINE_VALUE = new TscnElementType("DATA_LINE_VALUE");
  IElementType GODOT_OBJECT = new TscnElementType("GODOT_OBJECT");
  IElementType HEADER = new TscnElementType("HEADER");
  IElementType HEADER_VALUE = new TscnElementType("HEADER_VALUE");
  IElementType HEADER_VALUE_NM = new TscnElementType("HEADER_VALUE_NM");
  IElementType HEADER_VALUE_VAL = new TscnElementType("HEADER_VALUE_VAL");
  IElementType JSON_ARRAY = new TscnElementType("JSON_ARRAY");
  IElementType JSON_OBJECT = new TscnElementType("JSON_OBJECT");
  IElementType JSON_OBJECT_ELEM = new TscnElementType("JSON_OBJECT_ELEM");
  IElementType JSON_VALUE = new TscnElementType("JSON_VALUE");
  IElementType NODE_HEADER = TscnNodeHeaderElementType.getInstance("NODE_HEADER");
  IElementType PARAGRAPH = TscnParagraphElementType.getInstance("PARAGRAPH");
  IElementType RESOURCE_HEADER = TscnResourceHeaderElementType.getInstance("RESOURCE_HEADER");
  IElementType SCENE_HEADER = new TscnElementType("SCENE_HEADER");
  IElementType UNKNOWN_HEADER = new TscnElementType("UNKNOWN_HEADER");

  IElementType AMPERSAND = new TscnTokenType("AMPERSAND");
  IElementType BAD_CHARACTER = new TscnTokenType("bad_character");
  IElementType COLON = new TscnTokenType("COLON");
  IElementType COMMA = new TscnTokenType("COMMA");
  IElementType COMMENT = new TscnTokenType("comment");
  IElementType CONNECTION = new TscnTokenType("CONNECTION");
  IElementType EQ = new TscnTokenType("EQ");
  IElementType EXT_RESOURCE = new TscnTokenType("EXT_RESOURCE");
  IElementType GD_SCENE = new TscnTokenType("GD_SCENE");
  IElementType GODOT_CLASS_REF = new TscnTokenType("GODOT_CLASS_REF");
  IElementType GODOT_MEMBER_REF = new TscnTokenType("GODOT_MEMBER_REF");
  IElementType IDENTIFIER = new TscnTokenType("IDENTIFIER");
  IElementType LCBR = new TscnTokenType("LCBR");
  IElementType LP = new TscnTokenType("LP");
  IElementType LSBR = new TscnTokenType("LSBR");
  IElementType NODE = new TscnTokenType("NODE");
  IElementType RCBR = new TscnTokenType("RCBR");
  IElementType RES_STRING_VALUE = new TscnTokenType("RES_STRING_VALUE");
  IElementType RP = new TscnTokenType("RP");
  IElementType RSBR = new TscnTokenType("RSBR");
  IElementType STRING_VALUE = new TscnTokenType("STRING_VALUE");
  IElementType UNKNOWN = new TscnTokenType("UNKNOWN");
  IElementType VALUE = new TscnTokenType("VALUE");

  class Factory {
    public static PsiElement createElement(ASTNode node) {
      IElementType type = node.getElementType();
      if (type == CONNECTION_HEADER) {
        return new TscnConnectionHeaderImpl(node);
      }
      else if (type == DATA_LINE) {
        return new TscnDataLineImpl(node);
      }
      else if (type == DATA_LINE_NM) {
        return new TscnDataLineNmImpl(node);
      }
      else if (type == DATA_LINE_VALUE) {
        return new TscnDataLineValueImpl(node);
      }
      else if (type == GODOT_OBJECT) {
        return new TscnGodotObjectImpl(node);
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
      else if (type == JSON_ARRAY) {
        return new TscnJsonArrayImpl(node);
      }
      else if (type == JSON_OBJECT) {
        return new TscnJsonObjectImpl(node);
      }
      else if (type == JSON_OBJECT_ELEM) {
        return new TscnJsonObjectElemImpl(node);
      }
      else if (type == JSON_VALUE) {
        return new TscnJsonValueImpl(node);
      }
      else if (type == NODE_HEADER) {
        return new TscnNodeHeaderImpl(node);
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
      throw new AssertionError("Unknown element type: " + type);
    }
  }
}
