// This is a generated file. Not intended for manual editing.
package gdscript.psi;

import com.intellij.psi.tree.IElementType;
import com.intellij.psi.PsiElement;
import com.intellij.lang.ASTNode;
import gdscript.psi.impl.GdClassNamingElementType;
import gdscript.psi.impl.*;

public interface GdTypes {

  IElementType CLASS_NAME_NM = new GdElementType("CLASS_NAME_NM");
  IElementType CLASS_NAMING = GdClassNamingElementType.getInstance("CLASS_NAMING");
  IElementType END_STMT = new GdElementType("END_STMT");
  IElementType INHERITANCE = new GdElementType("INHERITANCE");
  IElementType INHERITANCE_ID = new GdElementType("INHERITANCE_ID");
  IElementType METHOD_DECL_TL = new GdElementType("METHOD_DECL_TL");
  IElementType STMT = new GdElementType("STMT");
  IElementType STMT_OR_SUITE = new GdElementType("STMT_OR_SUITE");
  IElementType SUITE = new GdElementType("SUITE");
  IElementType TOP_LEVEL_DECL = new GdElementType("TOP_LEVEL_DECL");

  IElementType BAD_CHARACTER = new GdTokenType("bad_character");
  IElementType CLASS_NAME = new GdTokenType("CLASS_NAME");
  IElementType COLON = new GdTokenType("COLON");
  IElementType COMMA = new GdTokenType("COMMA");
  IElementType COMMENT = new GdTokenType("comment");
  IElementType DEDENT = new GdTokenType("DEDENT");
  IElementType DOT = new GdTokenType("DOT");
  IElementType EXTENDS = new GdTokenType("EXTENDS");
  IElementType FUNC = new GdTokenType("FUNC");
  IElementType IDENTIFIER = new GdTokenType("IDENTIFIER");
  IElementType INDENT = new GdTokenType("INDENT");
  IElementType NEW_LINE = new GdTokenType("NEW_LINE");
  IElementType PASS = new GdTokenType("PASS");
  IElementType SEMICON = new GdTokenType("SEMICON");
  IElementType STRING = new GdTokenType("STRING");
  IElementType TOOL = new GdTokenType("TOOL");

  class Factory {
    public static PsiElement createElement(ASTNode node) {
      IElementType type = node.getElementType();
      if (type == CLASS_NAME_NM) {
        return new GdClassNameNmImpl(node);
      }
      else if (type == CLASS_NAMING) {
        return new GdClassNamingImpl(node);
      }
      else if (type == END_STMT) {
        return new GdEndStmtImpl(node);
      }
      else if (type == INHERITANCE) {
        return new GdInheritanceImpl(node);
      }
      else if (type == INHERITANCE_ID) {
        return new GdInheritanceIdImpl(node);
      }
      else if (type == METHOD_DECL_TL) {
        return new GdMethodDeclTlImpl(node);
      }
      else if (type == STMT) {
        return new GdStmtImpl(node);
      }
      else if (type == STMT_OR_SUITE) {
        return new GdStmtOrSuiteImpl(node);
      }
      else if (type == SUITE) {
        return new GdSuiteImpl(node);
      }
      throw new AssertionError("Unknown element type: " + type);
    }
  }
}
