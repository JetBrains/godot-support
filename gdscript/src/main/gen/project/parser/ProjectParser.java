// This is a generated file. Not intended for manual editing.
package project.parser;

import com.intellij.lang.PsiBuilder;
import com.intellij.lang.PsiBuilder.Marker;
import static project.psi.ProjectTypes.*;
import static com.intellij.lang.parser.GeneratedParserUtilBase.*;
import com.intellij.psi.tree.IElementType;
import com.intellij.lang.ASTNode;
import com.intellij.psi.tree.TokenSet;
import com.intellij.lang.PsiParser;
import com.intellij.lang.LightPsiParser;

@SuppressWarnings({"SimplifiableIfStatement", "UnusedAssignment"})
public class ProjectParser implements PsiParser, LightPsiParser {

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
  // dataKey EQ dataValue
  public static boolean data(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "data")) return false;
    if (!nextTokenIs(b, KEY)) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = dataKey(b, l + 1);
    r = r && consumeToken(b, EQ);
    r = r && dataValue(b, l + 1);
    exit_section_(b, m, DATA, r);
    return r;
  }

  /* ********************************************************** */
  // KEY
  public static boolean dataKey(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "dataKey")) return false;
    if (!nextTokenIs(b, KEY)) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, KEY);
    exit_section_(b, m, DATA_KEY, r);
    return r;
  }

  /* ********************************************************** */
  // VALUE+
  public static boolean dataValue(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "dataValue")) return false;
    if (!nextTokenIs(b, VALUE)) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, VALUE);
    while (r) {
      int c = current_position_(b);
      if (!consumeToken(b, VALUE)) break;
      if (!empty_element_parsed_guard_(b, "dataValue", c)) break;
    }
    exit_section_(b, m, DATA_VALUE, r);
    return r;
  }

  /* ********************************************************** */
  // section+
  static boolean file(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "file")) return false;
    if (!nextTokenIs(b, "", KEY, SECTION_KEY)) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = section(b, l + 1);
    while (r) {
      int c = current_position_(b);
      if (!section(b, l + 1)) break;
      if (!empty_element_parsed_guard_(b, "file", c)) break;
    }
    exit_section_(b, m, null, r);
    return r;
  }

  /* ********************************************************** */
  // section_nm? data+
  public static boolean section(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "section")) return false;
    if (!nextTokenIs(b, "<section>", KEY, SECTION_KEY)) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NONE_, SECTION, "<section>");
    r = section_0(b, l + 1);
    r = r && section_1(b, l + 1);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  // section_nm?
  private static boolean section_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "section_0")) return false;
    section_nm(b, l + 1);
    return true;
  }

  // data+
  private static boolean section_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "section_1")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = data(b, l + 1);
    while (r) {
      int c = current_position_(b);
      if (!data(b, l + 1)) break;
      if (!empty_element_parsed_guard_(b, "section_1", c)) break;
    }
    exit_section_(b, m, null, r);
    return r;
  }

  /* ********************************************************** */
  // SECTION_KEY
  public static boolean section_nm(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "section_nm")) return false;
    if (!nextTokenIs(b, SECTION_KEY)) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, SECTION_KEY);
    exit_section_(b, m, SECTION_NM, r);
    return r;
  }

}
