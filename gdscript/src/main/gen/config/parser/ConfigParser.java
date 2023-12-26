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
  // VARIADIC | REQUIRED
  public static boolean an_prefix(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "an_prefix")) return false;
    if (!nextTokenIs(b, "<an prefix>", REQUIRED, VARIADIC)) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NONE_, AN_PREFIX, "<an prefix>");
    r = consumeToken(b, VARIADIC);
    if (!r) r = consumeToken(b, REQUIRED);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  /* ********************************************************** */
  // AN an_prefix* annotation_nm param*
  public static boolean annotation(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "annotation")) return false;
    if (!nextTokenIs(b, AN)) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, AN);
    r = r && annotation_1(b, l + 1);
    r = r && annotation_nm(b, l + 1);
    r = r && annotation_3(b, l + 1);
    exit_section_(b, m, ANNOTATION, r);
    return r;
  }

  // an_prefix*
  private static boolean annotation_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "annotation_1")) return false;
    while (true) {
      int c = current_position_(b);
      if (!an_prefix(b, l + 1)) break;
      if (!empty_element_parsed_guard_(b, "annotation_1", c)) break;
    }
    return true;
  }

  // param*
  private static boolean annotation_3(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "annotation_3")) return false;
    while (true) {
      int c = current_position_(b);
      if (!param(b, l + 1)) break;
      if (!empty_element_parsed_guard_(b, "annotation_3", c)) break;
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
  // annotation* operator*
  static boolean file(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "file")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = file_0(b, l + 1);
    r = r && file_1(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  // annotation*
  private static boolean file_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "file_0")) return false;
    while (true) {
      int c = current_position_(b);
      if (!annotation(b, l + 1)) break;
      if (!empty_element_parsed_guard_(b, "file_0", c)) break;
    }
    return true;
  }

  // operator*
  private static boolean file_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "file_1")) return false;
    while (true) {
      int c = current_position_(b);
      if (!operator(b, l + 1)) break;
      if (!empty_element_parsed_guard_(b, "file_1", c)) break;
    }
    return true;
  }

  /* ********************************************************** */
  // OPERAND TYPE COLON TYPE
  public static boolean operation(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "operation")) return false;
    if (!nextTokenIs(b, OPERAND)) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeTokens(b, 0, OPERAND, TYPE, COLON, TYPE);
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
  // IDENTIFIER COLON TYPE
  public static boolean param(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "param")) return false;
    if (!nextTokenIs(b, IDENTIFIER)) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeTokens(b, 0, IDENTIFIER, COLON, TYPE);
    exit_section_(b, m, PARAM, r);
    return r;
  }

}
