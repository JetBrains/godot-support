// This is a generated file. Not intended for manual editing.
package gdscript.psi;

import com.intellij.psi.tree.IElementType;
import com.intellij.psi.PsiElement;
import com.intellij.lang.ASTNode;
import gdscript.psi.impl.GdClassNamingElementType;
import gdscript.psi.impl.GdConstDeclElementType;
import gdscript.psi.impl.GdInheritanceElementType;
import gdscript.psi.impl.*;

public interface GdTypes {

  IElementType BUILT_IN_TYPE = new GdElementType("BUILT_IN_TYPE");
  IElementType CLASS_NAME_NM = new GdElementType("CLASS_NAME_NM");
  IElementType CLASS_NAMING = GdClassNamingElementType.getInstance("CLASS_NAMING");
  IElementType CONST_DECL_TL = GdConstDeclElementType.getInstance("CONST_DECL_TL");
  IElementType CONST_ID_NMI = new GdElementType("CONST_ID_NMI");
  IElementType END_STMT = new GdElementType("END_STMT");
  IElementType EXPR = new GdElementType("EXPR");
  IElementType INHERITANCE = GdInheritanceElementType.getInstance("INHERITANCE");
  IElementType INHERITANCE_ID_NMI = new GdElementType("INHERITANCE_ID_NMI");
  IElementType LITERAL = new GdElementType("LITERAL");
  IElementType STMT = new GdElementType("STMT");
  IElementType STMT_OR_SUITE = new GdElementType("STMT_OR_SUITE");
  IElementType SUITE = new GdElementType("SUITE");
  IElementType TOOLLINE = new GdElementType("TOOLLINE");
  IElementType TOP_LEVEL_DECL = new GdElementType("TOP_LEVEL_DECL");
  IElementType TYPED = new GdElementType("TYPED");
  IElementType TYPE_HINT_NM = new GdElementType("TYPE_HINT_NM");

  IElementType BAD_CHARACTER = new GdTokenType("bad_character");
  IElementType CLASS_NAME = new GdTokenType("CLASS_NAME");
  IElementType COLON = new GdTokenType("COLON");
  IElementType COMMA = new GdTokenType("COMMA");
  IElementType COMMENT = new GdTokenType("comment");
  IElementType CONST = new GdTokenType("CONST");
  IElementType DEDENT = new GdTokenType("DEDENT");
  IElementType DOT = new GdTokenType("DOT");
  IElementType EQ = new GdTokenType("EQ");
  IElementType EXTENDS = new GdTokenType("EXTENDS");
  IElementType FALSE = new GdTokenType("FALSE");
  IElementType FUNC = new GdTokenType("FUNC");
  IElementType IDENTIFIER = new GdTokenType("IDENTIFIER");
  IElementType INDENT = new GdTokenType("INDENT");
  IElementType INT = new GdTokenType("INT");
  IElementType NEW_LINE = new GdTokenType("NEW_LINE");
  IElementType NULL = new GdTokenType("NULL");
  IElementType NUMBER = new GdTokenType("NUMBER");
  IElementType PASS = new GdTokenType("PASS");
  IElementType SEMICON = new GdTokenType("SEMICON");
  IElementType STRING = new GdTokenType("STRING");
  IElementType TOOL = new GdTokenType("TOOL");
  IElementType TRUE = new GdTokenType("TRUE");

  class Factory {
    public static PsiElement createElement(ASTNode node) {
      IElementType type = node.getElementType();
      if (type == BUILT_IN_TYPE) {
        return new GdBuiltInTypeImpl(node);
      }
      else if (type == CLASS_NAME_NM) {
        return new GdClassNameNmImpl(node);
      }
      else if (type == CLASS_NAMING) {
        return new GdClassNamingImpl(node);
      }
      else if (type == CONST_DECL_TL) {
        return new GdConstDeclTlImpl(node);
      }
      else if (type == CONST_ID_NMI) {
        return new GdConstIdNmiImpl(node);
      }
      else if (type == END_STMT) {
        return new GdEndStmtImpl(node);
      }
      else if (type == EXPR) {
        return new GdExprImpl(node);
      }
      else if (type == INHERITANCE) {
        return new GdInheritanceImpl(node);
      }
      else if (type == INHERITANCE_ID_NMI) {
        return new GdInheritanceIdNmiImpl(node);
      }
      else if (type == LITERAL) {
        return new GdLiteralImpl(node);
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
      else if (type == TOOLLINE) {
        return new GdToollineImpl(node);
      }
      else if (type == TYPED) {
        return new GdTypedImpl(node);
      }
      else if (type == TYPE_HINT_NM) {
        return new GdTypeHintNmImpl(node);
      }
      throw new AssertionError("Unknown element type: " + type);
    }
  }
}
