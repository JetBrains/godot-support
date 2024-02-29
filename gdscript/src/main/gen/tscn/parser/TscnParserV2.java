// This is a generated file. Not intended for manual editing.
package tscn.parser;

import com.intellij.lang.ASTNode;
import com.intellij.lang.LightPsiParser;
import com.intellij.lang.PsiBuilder;
import com.intellij.lang.PsiBuilder.Marker;
import com.intellij.lang.PsiParser;
import com.intellij.psi.tree.IElementType;
import com.intellij.psi.tree.TokenSet;

import static com.intellij.lang.parser.GeneratedParserUtilBase.TRUE_CONDITION;
import static com.intellij.lang.parser.GeneratedParserUtilBase._COLLAPSE_;
import static com.intellij.lang.parser.GeneratedParserUtilBase._NONE_;
import static com.intellij.lang.parser.GeneratedParserUtilBase.adapt_builder_;
import static com.intellij.lang.parser.GeneratedParserUtilBase.consumeToken;
import static com.intellij.lang.parser.GeneratedParserUtilBase.consumeTokens;
import static com.intellij.lang.parser.GeneratedParserUtilBase.create_token_set_;
import static com.intellij.lang.parser.GeneratedParserUtilBase.current_position_;
import static com.intellij.lang.parser.GeneratedParserUtilBase.empty_element_parsed_guard_;
import static com.intellij.lang.parser.GeneratedParserUtilBase.enter_section_;
import static com.intellij.lang.parser.GeneratedParserUtilBase.exit_section_;
import static com.intellij.lang.parser.GeneratedParserUtilBase.nextTokenIs;
import static com.intellij.lang.parser.GeneratedParserUtilBase.recursion_guard_;
import static tscn.psi.TscnTypes.AMPERSAND;
import static tscn.psi.TscnTypes.COLON;
import static tscn.psi.TscnTypes.COMMA;
import static tscn.psi.TscnTypes.CONNECTION;
import static tscn.psi.TscnTypes.CONNECTION_HEADER;
import static tscn.psi.TscnTypes.DATA_LINE;
import static tscn.psi.TscnTypes.DATA_LINE_NM;
import static tscn.psi.TscnTypes.DATA_LINE_VALUE;
import static tscn.psi.TscnTypes.EQ;
import static tscn.psi.TscnTypes.EXT_RESOURCE;
import static tscn.psi.TscnTypes.GD_SCENE;
import static tscn.psi.TscnTypes.GODOT_CLASS_REF;
import static tscn.psi.TscnTypes.GODOT_MEMBER_REF;
import static tscn.psi.TscnTypes.GODOT_OBJECT;
import static tscn.psi.TscnTypes.HEADER;
import static tscn.psi.TscnTypes.HEADER_VALUE;
import static tscn.psi.TscnTypes.HEADER_VALUE_NM;
import static tscn.psi.TscnTypes.HEADER_VALUE_VAL;
import static tscn.psi.TscnTypes.JSON_ARRAY;
import static tscn.psi.TscnTypes.JSON_OBJECT;
import static tscn.psi.TscnTypes.JSON_OBJECT_ELEM;
import static tscn.psi.TscnTypes.JSON_VALUE;
import static tscn.psi.TscnTypes.LCBR;
import static tscn.psi.TscnTypes.LP;
import static tscn.psi.TscnTypes.LSBR;
import static tscn.psi.TscnTypes.NODE;
import static tscn.psi.TscnTypes.NODE_HEADER;
import static tscn.psi.TscnTypes.PARAGRAPH;
import static tscn.psi.TscnTypes.RCBR;
import static tscn.psi.TscnTypes.RESOURCE_HEADER;
import static tscn.psi.TscnTypes.RES_STRING_VALUE;
import static tscn.psi.TscnTypes.RP;
import static tscn.psi.TscnTypes.RSBR;
import static tscn.psi.TscnTypes.SCENE_HEADER;
import static tscn.psi.TscnTypes.STRING_VALUE;
import static tscn.psi.TscnTypes.UNKNOWN;
import static tscn.psi.TscnTypes.UNKNOWN_HEADER;
import static tscn.psi.TscnTypes.VALUE;

@SuppressWarnings({"SimplifiableIfStatement", "UnusedAssignment"})
public class TscnParserV2 implements PsiParser, LightPsiParser {

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
    create_token_set_(CONNECTION_HEADER, HEADER, NODE_HEADER, RESOURCE_HEADER,
      SCENE_HEADER, UNKNOWN_HEADER),
  };

  /* ********************************************************** */
  // LSBR CONNECTION headerValue* RSBR
  public static boolean connection_header(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "connection_header")) return false;
    if (!nextTokenIs(b, LSBR)) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeTokens(b, 0, LSBR, CONNECTION);
    r = r && connection_header_2(b, l + 1);
    r = r && consumeToken(b, RSBR);
    exit_section_(b, m, CONNECTION_HEADER, r);
    return r;
  }

  // headerValue*
  private static boolean connection_header_2(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "connection_header_2")) return false;
    while (true) {
      int c = current_position_(b);
      if (!headerValue(b, l + 1)) break;
      if (!empty_element_parsed_guard_(b, "connection_header_2", c)) break;
    }
    return true;
  }

  /* ********************************************************** */
  // dataLine_nm EQ dataLine_value
  public static boolean dataLine(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "dataLine")) return false;
    if (!nextTokenIs(b, DATA_LINE_NM)) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = dataLine_nm(b, l + 1);
    r = r && consumeToken(b, EQ);
    r = r && dataLine_value(b, l + 1);
    exit_section_(b, m, DATA_LINE, r);
    return r;
  }

  /* ********************************************************** */
  // DATA_LINE_NM
  public static boolean dataLine_nm(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "dataLine_nm")) return false;
    if (!nextTokenIs(b, DATA_LINE_NM)) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, DATA_LINE_NM);
    exit_section_(b, m, DATA_LINE_NM, r);
    return r;
  }

  /* ********************************************************** */
  // jsonValue
  public static boolean dataLine_value(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "dataLine_value")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NONE_, DATA_LINE_VALUE, "<data line value>");
    r = jsonValue(b, l + 1);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  /* ********************************************************** */
  // GODOT_CLASS_REF LP ((STRING_VALUE | VALUE) (COMMA (STRING_VALUE | VALUE))*)? RP
  public static boolean godotObject(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "godotObject")) return false;
    if (!nextTokenIs(b, GODOT_CLASS_REF)) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeTokens(b, 0, GODOT_CLASS_REF, LP);
    r = r && godotObject_2(b, l + 1);
    r = r && consumeToken(b, RP);
    exit_section_(b, m, GODOT_OBJECT, r);
    return r;
  }

  // ((STRING_VALUE | VALUE) (COMMA (STRING_VALUE | VALUE))*)?
  private static boolean godotObject_2(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "godotObject_2")) return false;
    godotObject_2_0(b, l + 1);
    return true;
  }

  // (STRING_VALUE | VALUE) (COMMA (STRING_VALUE | VALUE))*
  private static boolean godotObject_2_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "godotObject_2_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = godotObject_2_0_0(b, l + 1);
    r = r && godotObject_2_0_1(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  // STRING_VALUE | VALUE
  private static boolean godotObject_2_0_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "godotObject_2_0_0")) return false;
    boolean r;
    r = consumeToken(b, STRING_VALUE);
    if (!r) r = consumeToken(b, VALUE);
    return r;
  }

  // (COMMA (STRING_VALUE | VALUE))*
  private static boolean godotObject_2_0_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "godotObject_2_0_1")) return false;
    while (true) {
      int c = current_position_(b);
      if (!godotObject_2_0_1_0(b, l + 1)) break;
      if (!empty_element_parsed_guard_(b, "godotObject_2_0_1", c)) break;
    }
    return true;
  }

  // COMMA (STRING_VALUE | VALUE)
  private static boolean godotObject_2_0_1_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "godotObject_2_0_1_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, COMMA);
    r = r && godotObject_2_0_1_0_1(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  // STRING_VALUE | VALUE
  private static boolean godotObject_2_0_1_0_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "godotObject_2_0_1_0_1")) return false;
    boolean r;
    r = consumeToken(b, STRING_VALUE);
    if (!r) r = consumeToken(b, VALUE);
    return r;
  }

  /* ********************************************************** */
  // scene_header
  //     | node_header
  //     | resource_header
  //     | connection_header
  //     | unknown_header
  public static boolean header(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "header")) return false;
    if (!nextTokenIs(b, LSBR)) return false;
    boolean r;
    Marker m = enter_section_(b, l, _COLLAPSE_, HEADER, null);
    r = scene_header(b, l + 1);
    if (!r) r = node_header(b, l + 1);
    if (!r) r = resource_header(b, l + 1);
    if (!r) r = connection_header(b, l + 1);
    if (!r) r = unknown_header(b, l + 1);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  /* ********************************************************** */
  // headerValue_nm EQ headerValue_val
  public static boolean headerValue(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "headerValue")) return false;
    if (!nextTokenIs(b, HEADER_VALUE_NM)) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = headerValue_nm(b, l + 1);
    r = r && consumeToken(b, EQ);
    r = r && headerValue_val(b, l + 1);
    exit_section_(b, m, HEADER_VALUE, r);
    return r;
  }

  /* ********************************************************** */
  // HEADER_VALUE_NM
  public static boolean headerValue_nm(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "headerValue_nm")) return false;
    if (!nextTokenIs(b, HEADER_VALUE_NM)) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, HEADER_VALUE_NM);
    exit_section_(b, m, HEADER_VALUE_NM, r);
    return r;
  }

  /* ********************************************************** */
  // RES_STRING_VALUE | STRING_VALUE | VALUE | godotObject
  public static boolean headerValue_val(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "headerValue_val")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NONE_, HEADER_VALUE_VAL, "<header value val>");
    r = consumeToken(b, RES_STRING_VALUE);
    if (!r) r = consumeToken(b, STRING_VALUE);
    if (!r) r = consumeToken(b, VALUE);
    if (!r) r = godotObject(b, l + 1);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  /* ********************************************************** */
  // LSBR (jsonValue (COMMA jsonValue)*)? RSBR
  public static boolean jsonArray(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "jsonArray")) return false;
    if (!nextTokenIs(b, LSBR)) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, LSBR);
    r = r && jsonArray_1(b, l + 1);
    r = r && consumeToken(b, RSBR);
    exit_section_(b, m, JSON_ARRAY, r);
    return r;
  }

  // (jsonValue (COMMA jsonValue)*)?
  private static boolean jsonArray_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "jsonArray_1")) return false;
    jsonArray_1_0(b, l + 1);
    return true;
  }

  // jsonValue (COMMA jsonValue)*
  private static boolean jsonArray_1_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "jsonArray_1_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = jsonValue(b, l + 1);
    r = r && jsonArray_1_0_1(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  // (COMMA jsonValue)*
  private static boolean jsonArray_1_0_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "jsonArray_1_0_1")) return false;
    while (true) {
      int c = current_position_(b);
      if (!jsonArray_1_0_1_0(b, l + 1)) break;
      if (!empty_element_parsed_guard_(b, "jsonArray_1_0_1", c)) break;
    }
    return true;
  }

  // COMMA jsonValue
  private static boolean jsonArray_1_0_1_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "jsonArray_1_0_1_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, COMMA);
    r = r && jsonValue(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  /* ********************************************************** */
  // LCBR (jsonObjectElem (COMMA jsonObjectElem)*)? RCBR
  public static boolean jsonObject(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "jsonObject")) return false;
    if (!nextTokenIs(b, LCBR)) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, LCBR);
    r = r && jsonObject_1(b, l + 1);
    r = r && consumeToken(b, RCBR);
    exit_section_(b, m, JSON_OBJECT, r);
    return r;
  }

  // (jsonObjectElem (COMMA jsonObjectElem)*)?
  private static boolean jsonObject_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "jsonObject_1")) return false;
    jsonObject_1_0(b, l + 1);
    return true;
  }

  // jsonObjectElem (COMMA jsonObjectElem)*
  private static boolean jsonObject_1_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "jsonObject_1_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = jsonObjectElem(b, l + 1);
    r = r && jsonObject_1_0_1(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  // (COMMA jsonObjectElem)*
  private static boolean jsonObject_1_0_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "jsonObject_1_0_1")) return false;
    while (true) {
      int c = current_position_(b);
      if (!jsonObject_1_0_1_0(b, l + 1)) break;
      if (!empty_element_parsed_guard_(b, "jsonObject_1_0_1", c)) break;
    }
    return true;
  }

  // COMMA jsonObjectElem
  private static boolean jsonObject_1_0_1_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "jsonObject_1_0_1_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, COMMA);
    r = r && jsonObjectElem(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  /* ********************************************************** */
  // STRING_VALUE COLON jsonValue
  public static boolean jsonObjectElem(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "jsonObjectElem")) return false;
    if (!nextTokenIs(b, STRING_VALUE)) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeTokens(b, 0, STRING_VALUE, COLON);
    r = r && jsonValue(b, l + 1);
    exit_section_(b, m, JSON_OBJECT_ELEM, r);
    return r;
  }

  /* ********************************************************** */
  // RES_STRING_VALUE | STRING_VALUE | VALUE | (AMPERSAND GODOT_MEMBER_REF) | godotObject | jsonObject | jsonArray
  public static boolean jsonValue(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "jsonValue")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NONE_, JSON_VALUE, "<json value>");
    r = consumeToken(b, RES_STRING_VALUE);
    if (!r) r = consumeToken(b, STRING_VALUE);
    if (!r) r = consumeToken(b, VALUE);
    if (!r) r = jsonValue_3(b, l + 1);
    if (!r) r = godotObject(b, l + 1);
    if (!r) r = jsonObject(b, l + 1);
    if (!r) r = jsonArray(b, l + 1);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  // AMPERSAND GODOT_MEMBER_REF
  private static boolean jsonValue_3(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "jsonValue_3")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeTokens(b, 0, AMPERSAND, GODOT_MEMBER_REF);
    exit_section_(b, m, null, r);
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
  // header dataLine*
  public static boolean paragraph(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "paragraph")) return false;
    if (!nextTokenIs(b, LSBR)) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = header(b, l + 1);
    r = r && paragraph_1(b, l + 1);
    exit_section_(b, m, PARAGRAPH, r);
    return r;
  }

  // dataLine*
  private static boolean paragraph_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "paragraph_1")) return false;
    while (true) {
      int c = current_position_(b);
      if (!dataLine(b, l + 1)) break;
      if (!empty_element_parsed_guard_(b, "paragraph_1", c)) break;
    }
    return true;
  }

  /* ********************************************************** */
  // LSBR EXT_RESOURCE headerValue* RSBR
  public static boolean resource_header(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "resource_header")) return false;
    if (!nextTokenIs(b, LSBR)) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeTokens(b, 0, LSBR, EXT_RESOURCE);
    r = r && resource_header_2(b, l + 1);
    r = r && consumeToken(b, RSBR);
    exit_section_(b, m, RESOURCE_HEADER, r);
    return r;
  }

  // headerValue*
  private static boolean resource_header_2(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "resource_header_2")) return false;
    while (true) {
      int c = current_position_(b);
      if (!headerValue(b, l + 1)) break;
      if (!empty_element_parsed_guard_(b, "resource_header_2", c)) break;
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

  /* ********************************************************** */
  // LSBR UNKNOWN headerValue* RSBR
  public static boolean unknown_header(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "unknown_header")) return false;
    if (!nextTokenIs(b, LSBR)) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeTokens(b, 0, LSBR, UNKNOWN);
    r = r && unknown_header_2(b, l + 1);
    r = r && consumeToken(b, RSBR);
    exit_section_(b, m, UNKNOWN_HEADER, r);
    return r;
  }

  // headerValue*
  private static boolean unknown_header_2(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "unknown_header_2")) return false;
    while (true) {
      int c = current_position_(b);
      if (!headerValue(b, l + 1)) break;
      if (!empty_element_parsed_guard_(b, "unknown_header_2", c)) break;
    }
    return true;
  }

}
