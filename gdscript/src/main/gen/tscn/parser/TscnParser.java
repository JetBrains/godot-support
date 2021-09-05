// This is a generated file. Not intended for manual editing.
package tscn.parser;

import com.intellij.lang.PsiBuilder;
import com.intellij.lang.PsiBuilder.Marker;
import static tscn.psi.TscnTypes.*;
import static com.intellij.lang.parser.GeneratedParserUtilBase.*;
import com.intellij.psi.tree.IElementType;
import com.intellij.lang.ASTNode;
import com.intellij.psi.tree.TokenSet;
import com.intellij.lang.PsiParser;
import com.intellij.lang.LightPsiParser;

@SuppressWarnings({"SimplifiableIfStatement", "UnusedAssignment"})
public class TscnParser implements PsiParser, LightPsiParser {

  public ASTNode parse(IElementType t, PsiBuilder b) {
    parseLight(t, b);
    return b.getTreeBuilt();
  }

  public void parseLight(IElementType t, PsiBuilder b) {
    boolean r;
    b = adapt_builder_(t, b, this, EXTENDS_SETS_);
    Marker m = enter_section_(b, 0, _COLLAPSE_, null);
    r = parse_root_(t, b);
    exit_section_(b, 0, m, t, r, true, TRUE_CONDITION);
  }

  protected boolean parse_root_(IElementType t, PsiBuilder b) {
    return parse_root_(t, b, 0);
  }

  static boolean parse_root_(IElementType t, PsiBuilder b, int l) {
    return tscnfile(b, l + 1);
  }

  public static final TokenSet[] EXTENDS_SETS_ = new TokenSet[] {
    create_token_set_(EXT_HEADER, HEADER, NODE_HEADER, SCENE_HEADER,
      SUB_HEADER),
  };

  /* ********************************************************** */
  // LSBR EXT_RESOURCE headerValue* RSBR
  public static boolean ext_header(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "ext_header")) return false;
    if (!nextTokenIs(b, LSBR)) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeTokens(b, 0, LSBR, EXT_RESOURCE);
    r = r && ext_header_2(b, l + 1);
    r = r && consumeToken(b, RSBR);
    exit_section_(b, m, EXT_HEADER, r);
    return r;
  }

  // headerValue*
  private static boolean ext_header_2(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "ext_header_2")) return false;
    while (true) {
      int c = current_position_(b);
      if (!headerValue(b, l + 1)) break;
      if (!empty_element_parsed_guard_(b, "ext_header_2", c)) break;
    }
    return true;
  }

  /* ********************************************************** */
  // scene_header
  //     | node_header
  //     | ext_header
  //     | sub_header
  public static boolean header(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "header")) return false;
    if (!nextTokenIs(b, LSBR)) return false;
    boolean r;
    Marker m = enter_section_(b, l, _COLLAPSE_, HEADER, null);
    r = scene_header(b, l + 1);
    if (!r) r = node_header(b, l + 1);
    if (!r) r = ext_header(b, l + 1);
    if (!r) r = sub_header(b, l + 1);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  /* ********************************************************** */
  // headerValue_nm EQ headerValue_val
  public static boolean headerValue(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "headerValue")) return false;
    if (!nextTokenIs(b, IDENTIFIER)) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = headerValue_nm(b, l + 1);
    r = r && consumeToken(b, EQ);
    r = r && headerValue_val(b, l + 1);
    exit_section_(b, m, HEADER_VALUE, r);
    return r;
  }

  /* ********************************************************** */
  // IDENTIFIER
  public static boolean headerValue_nm(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "headerValue_nm")) return false;
    if (!nextTokenIs(b, IDENTIFIER)) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, IDENTIFIER);
    exit_section_(b, m, HEADER_VALUE_NM, r);
    return r;
  }

  /* ********************************************************** */
  // VALUE
  public static boolean headerValue_val(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "headerValue_val")) return false;
    if (!nextTokenIs(b, VALUE)) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, VALUE);
    exit_section_(b, m, HEADER_VALUE_VAL, r);
    return r;
  }

  /* ********************************************************** */
  // LSBR NODE headerValue* RSBR
  public static boolean node_header(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "node_header")) return false;
    if (!nextTokenIs(b, LSBR)) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeTokens(b, 0, LSBR, NODE);
    r = r && node_header_2(b, l + 1);
    r = r && consumeToken(b, RSBR);
    exit_section_(b, m, NODE_HEADER, r);
    return r;
  }

  // headerValue*
  private static boolean node_header_2(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "node_header_2")) return false;
    while (true) {
      int c = current_position_(b);
      if (!headerValue(b, l + 1)) break;
      if (!empty_element_parsed_guard_(b, "node_header_2", c)) break;
    }
    return true;
  }

  /* ********************************************************** */
  // header DATA_LINE*
  static boolean paragraph(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "paragraph")) return false;
    if (!nextTokenIs(b, LSBR)) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = header(b, l + 1);
    r = r && paragraph_1(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  // DATA_LINE*
  private static boolean paragraph_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "paragraph_1")) return false;
    while (true) {
      int c = current_position_(b);
      if (!consumeToken(b, DATA_LINE)) break;
      if (!empty_element_parsed_guard_(b, "paragraph_1", c)) break;
    }
    return true;
  }

  /* ********************************************************** */
  // LSBR GD_SCENE headerValue* RSBR
  public static boolean scene_header(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "scene_header")) return false;
    if (!nextTokenIs(b, LSBR)) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeTokens(b, 0, LSBR, GD_SCENE);
    r = r && scene_header_2(b, l + 1);
    r = r && consumeToken(b, RSBR);
    exit_section_(b, m, SCENE_HEADER, r);
    return r;
  }

  // headerValue*
  private static boolean scene_header_2(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "scene_header_2")) return false;
    while (true) {
      int c = current_position_(b);
      if (!headerValue(b, l + 1)) break;
      if (!empty_element_parsed_guard_(b, "scene_header_2", c)) break;
    }
    return true;
  }

  /* ********************************************************** */
  // LSBR (SUB_RESOURCE | CONNECTION) headerValue* RSBR
  public static boolean sub_header(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "sub_header")) return false;
    if (!nextTokenIs(b, LSBR)) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, LSBR);
    r = r && sub_header_1(b, l + 1);
    r = r && sub_header_2(b, l + 1);
    r = r && consumeToken(b, RSBR);
    exit_section_(b, m, SUB_HEADER, r);
    return r;
  }

  // SUB_RESOURCE | CONNECTION
  private static boolean sub_header_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "sub_header_1")) return false;
    boolean r;
    r = consumeToken(b, SUB_RESOURCE);
    if (!r) r = consumeToken(b, CONNECTION);
    return r;
  }

  // headerValue*
  private static boolean sub_header_2(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "sub_header_2")) return false;
    while (true) {
      int c = current_position_(b);
      if (!headerValue(b, l + 1)) break;
      if (!empty_element_parsed_guard_(b, "sub_header_2", c)) break;
    }
    return true;
  }

  /* ********************************************************** */
  // paragraph+
  static boolean tscnfile(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "tscnfile")) return false;
    if (!nextTokenIs(b, LSBR)) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = paragraph(b, l + 1);
    while (r) {
      int c = current_position_(b);
      if (!paragraph(b, l + 1)) break;
      if (!empty_element_parsed_guard_(b, "tscnfile", c)) break;
    }
    exit_section_(b, m, null, r);
    return r;
  }

}
