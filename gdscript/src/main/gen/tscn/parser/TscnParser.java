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
    b = adapt_builder_(t, b, this, null);
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

  /* ********************************************************** */
  // LSBR type headerValue* RSBR
  public static boolean header(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "header")) return false;
    if (!nextTokenIs(b, LSBR)) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, LSBR);
    r = r && type(b, l + 1);
    r = r && header_2(b, l + 1);
    r = r && consumeToken(b, RSBR);
    exit_section_(b, m, HEADER, r);
    return r;
  }

  // headerValue*
  private static boolean header_2(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "header_2")) return false;
    while (true) {
      int c = current_position_(b);
      if (!headerValue(b, l + 1)) break;
      if (!empty_element_parsed_guard_(b, "header_2", c)) break;
    }
    return true;
  }

  /* ********************************************************** */
  // IDENTIFIER EQ VALUE
  public static boolean headerValue(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "headerValue")) return false;
    if (!nextTokenIs(b, IDENTIFIER)) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeTokens(b, 0, IDENTIFIER, EQ, VALUE);
    exit_section_(b, m, HEADER_VALUE, r);
    return r;
  }

  /* ********************************************************** */
  // (header DATA_LINE*)+
  static boolean tscnfile(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "tscnfile")) return false;
    if (!nextTokenIs(b, LSBR)) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = tscnfile_0(b, l + 1);
    while (r) {
      int c = current_position_(b);
      if (!tscnfile_0(b, l + 1)) break;
      if (!empty_element_parsed_guard_(b, "tscnfile", c)) break;
    }
    exit_section_(b, m, null, r);
    return r;
  }

  // header DATA_LINE*
  private static boolean tscnfile_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "tscnfile_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = header(b, l + 1);
    r = r && tscnfile_0_1(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  // DATA_LINE*
  private static boolean tscnfile_0_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "tscnfile_0_1")) return false;
    while (true) {
      int c = current_position_(b);
      if (!consumeToken(b, DATA_LINE)) break;
      if (!empty_element_parsed_guard_(b, "tscnfile_0_1", c)) break;
    }
    return true;
  }

  /* ********************************************************** */
  // GD_SCENE | EXT_RESOURCE | SUB_RESOURCE | NODE | CONNECTION
  public static boolean type(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "type")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NONE_, TYPE, "<type>");
    r = consumeToken(b, GD_SCENE);
    if (!r) r = consumeToken(b, EXT_RESOURCE);
    if (!r) r = consumeToken(b, SUB_RESOURCE);
    if (!r) r = consumeToken(b, NODE);
    if (!r) r = consumeToken(b, CONNECTION);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

}
