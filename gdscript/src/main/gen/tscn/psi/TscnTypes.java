// This is a generated file. Not intended for manual editing.
package tscn.psi;

import com.intellij.psi.tree.IElementType;
import com.intellij.psi.PsiElement;
import com.intellij.lang.ASTNode;
import tscn.psi.impl.*;

public interface TscnTypes {

  IElementType HEADER = new TscnElementType("HEADER");
  IElementType HEADER_VALUE = new TscnElementType("HEADER_VALUE");
  IElementType TYPE = new TscnElementType("TYPE");

  IElementType BAD_CHARACTER = new TscnTokenType("bad_character");
  IElementType COMMENT = new TscnTokenType("comment");
  IElementType CONNECTION = new TscnTokenType("CONNECTION");
  IElementType DATA_LINE = new TscnTokenType("DATA_LINE");
  IElementType EQ = new TscnTokenType("EQ");
  IElementType EXT_RESOURCE = new TscnTokenType("EXT_RESOURCE");
  IElementType GD_SCENE = new TscnTokenType("GD_SCENE");
  IElementType IDENTIFIER = new TscnTokenType("IDENTIFIER");
  IElementType LSBR = new TscnTokenType("LSBR");
  IElementType NODE = new TscnTokenType("NODE");
  IElementType RSBR = new TscnTokenType("RSBR");
  IElementType SUB_RESOURCE = new TscnTokenType("SUB_RESOURCE");
  IElementType VALUE = new TscnTokenType("VALUE");

  class Factory {
    public static PsiElement createElement(ASTNode node) {
      IElementType type = node.getElementType();
      if (type == HEADER) {
        return new TscnHeaderImpl(node);
      }
      else if (type == HEADER_VALUE) {
        return new TscnHeaderValueImpl(node);
      }
      else if (type == TYPE) {
        return new TscnTypeImpl(node);
      }
      throw new AssertionError("Unknown element type: " + type);
    }
  }
}
