// This is a generated file. Not intended for manual editing.
package config.parser;

import com.intellij.lang.PsiBuilder;
import com.intellij.lang.PsiBuilder.Marker;
import static config.psi.GdConfigTypes.*;
import static com.intellij.lang.parser.GeneratedParserUtilBase.*;
import com.intellij.psi.tree.IElementType;
import com.intellij.lang.ASTNode;
import com.intellij.psi.tree.TokenSet;
import com.intellij.lang.PsiParser;
import com.intellij.lang.LightPsiParser;

@SuppressWarnings({"SimplifiableIfStatement", "UnusedAssignment"})
public class ConfigParser implements PsiParser, LightPsiParser {

  public ASTNode parse(IElementType t, PsiBuilder b) {
    parseLight(t, b);
    return b.getTreeBuilt();
  }

  public void parseLight(IElementType t, PsiBuilder b) {
    boolean r;
    b = adapt_builder_(t, b, this, null);
    Marker m = enter_section_(b, 0, _COLLAPSE_, null);
    r = parse_root_(t, b);
    exit_section_(b, 0, m, t, r, true, TRUE_CONDITION);
  }

  protected boolean parse_root_(IElementType t, PsiBuilder b) {
    return parse_root_(t, b, 0);
  }

  static boolean parse_root_(IElementType t, PsiBuilder b, int l) {
    return file(b, l + 1);
  }

  /* ********************************************************** */
  // AN variadicMark? required annotation_nm param*
  public static boolean annotation(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "annotation")) return false;
    if (!nextTokenIs(b, AN)) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, AN);
    r = r && annotation_1(b, l + 1);
    r = r && required(b, l + 1);
    r = r && annotation_nm(b, l + 1);
    r = r && annotation_4(b, l + 1);
    exit_section_(b, m, ANNOTATION, r);
    return r;
  }

  // variadicMark?
  private static boolean annotation_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "annotation_1")) return false;
    variadicMark(b, l + 1);
    return true;
  }

  // param*
  private static boolean annotation_4(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "annotation_4")) return false;
    while (true) {
      int c = current_position_(b);
      if (!param(b, l + 1)) break;
      if (!empty_element_parsed_guard_(b, "annotation_4", c)) break;
    }
    return true;
  }

  /* ********************************************************** */
  // IDENTIFIER
  public static boolean annotation_nm(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "annotation_nm")) return false;
    if (!nextTokenIs(b, IDENTIFIER)) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, IDENTIFIER);
    exit_section_(b, m, ANNOTATION_NM, r);
    return r;
  }

  /* ********************************************************** */
  // (annotation | operator)*
  static boolean file(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "file")) return false;
    while (true) {
      int c = current_position_(b);
      if (!file_0(b, l + 1)) break;
      if (!empty_element_parsed_guard_(b, "file", c)) break;
    }
    return true;
  }

  // annotation | operator
  private static boolean file_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "file_0")) return false;
    boolean r;
    r = annotation(b, l + 1);
    if (!r) r = operator(b, l + 1);
    return r;
  }

  /* ********************************************************** */
  // IDENTIFIER
  public static boolean leftType(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "leftType")) return false;
    if (!nextTokenIs(b, IDENTIFIER)) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, IDENTIFIER);
    exit_section_(b, m, LEFT_TYPE, r);
    return r;
  }

  /* ********************************************************** */
  // OPERAND
  public static boolean opType(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "opType")) return false;
    if (!nextTokenIs(b, OPERAND)) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, OPERAND);
    exit_section_(b, m, OP_TYPE, r);
    return r;
  }

  /* ********************************************************** */
  // opType leftType COLON rightType
  public static boolean operation(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "operation")) return false;
    if (!nextTokenIs(b, OPERAND)) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = opType(b, l + 1);
    r = r && leftType(b, l + 1);
    r = r && consumeToken(b, COLON);
    r = r && rightType(b, l + 1);
    exit_section_(b, m, OPERATION, r);
    return r;
  }

  /* ********************************************************** */
  // OP operator_nm operation*
  public static boolean operator(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "operator")) return false;
    if (!nextTokenIs(b, OP)) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, OP);
    r = r && operator_nm(b, l + 1);
    r = r && operator_2(b, l + 1);
    exit_section_(b, m, OPERATOR, r);
    return r;
  }

  // operation*
  private static boolean operator_2(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "operator_2")) return false;
    while (true) {
      int c = current_position_(b);
      if (!operation(b, l + 1)) break;
      if (!empty_element_parsed_guard_(b, "operator_2", c)) break;
    }
    return true;
  }

  /* ********************************************************** */
  // IDENTIFIER
  public static boolean operator_nm(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "operator_nm")) return false;
    if (!nextTokenIs(b, IDENTIFIER)) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, IDENTIFIER);
    exit_section_(b, m, OPERATOR_NM, r);
    return r;
  }

  /* ********************************************************** */
  // paramName COLON type
  public static boolean param(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "param")) return false;
    if (!nextTokenIs(b, IDENTIFIER)) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = paramName(b, l + 1);
    r = r && consumeToken(b, COLON);
    r = r && type(b, l + 1);
    exit_section_(b, m, PARAM, r);
    return r;
  }

  /* ********************************************************** */
  // IDENTIFIER
  public static boolean paramName(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "paramName")) return false;
    if (!nextTokenIs(b, IDENTIFIER)) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, IDENTIFIER);
    exit_section_(b, m, PARAM_NAME, r);
    return r;
  }

  /* ********************************************************** */
  // NUMBER
  public static boolean required(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "required")) return false;
    if (!nextTokenIs(b, NUMBER)) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, NUMBER);
    exit_section_(b, m, REQUIRED, r);
    return r;
  }

  /* ********************************************************** */
  // IDENTIFIER
  public static boolean rightType(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "rightType")) return false;
    if (!nextTokenIs(b, IDENTIFIER)) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, IDENTIFIER);
    exit_section_(b, m, RIGHT_TYPE, r);
    return r;
  }

  /* ********************************************************** */
  // IDENTIFIER
  public static boolean type(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "type")) return false;
    if (!nextTokenIs(b, IDENTIFIER)) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, IDENTIFIER);
    exit_section_(b, m, TYPE, r);
    return r;
  }

  /* ********************************************************** */
  // VARIADIC
  public static boolean variadicMark(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "variadicMark")) return false;
    if (!nextTokenIs(b, VARIADIC)) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, VARIADIC);
    exit_section_(b, m, VARIADIC_MARK, r);
    return r;
  }

}
