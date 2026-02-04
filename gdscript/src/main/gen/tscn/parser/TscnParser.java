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
    create_token_set_(CONNECTION_HEADER, HEADER, NODE_HEADER, RESOURCE_HEADER,
      SCENE_HEADER, UNKNOWN_HEADER),
  };

  /* ********************************************************** */
  // value? (COMMA value)*
  public static boolean argList(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "argList")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NONE_, ARG_LIST, "<arg list>");
    r = argList_0(b, l + 1);
    r = r && argList_1(b, l + 1);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  // value?
  private static boolean argList_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "argList_0")) return false;
    value(b, l + 1);
    return true;
  }

  // (COMMA value)*
  private static boolean argList_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "argList_1")) return false;
    while (true) {
      int c = current_position_(b);
      if (!argList_1_0(b, l + 1)) break;
      if (!empty_element_parsed_guard_(b, "argList_1", c)) break;
    }
    return true;
  }

  // COMMA value
  private static boolean argList_1_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "argList_1_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, COMMA);
    r = r && value(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  /* ********************************************************** */
  // LSBR valueList? RSBR
  public static boolean array(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "array")) return false;
    if (!nextTokenIs(b, LSBR)) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, LSBR);
    r = r && array_1(b, l + 1);
    r = r && consumeToken(b, RSBR);
    exit_section_(b, m, ARRAY, r);
    return r;
  }

  // valueList?
  private static boolean array_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "array_1")) return false;
    valueList(b, l + 1);
    return true;
  }

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
  // dataLineHeader EQ dataLine_value
  public static boolean dataLine(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "dataLine")) return false;
    if (!nextTokenIs(b, "<data line>", IDENTIFIER, NUMBER)) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NONE_, DATA_LINE, "<data line>");
    r = dataLineHeader(b, l + 1);
    r = r && consumeToken(b, EQ);
    r = r && dataLine_value(b, l + 1);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  /* ********************************************************** */
  // dataLine_nm ((COLON | SLASH) dataLine_nm)*
  public static boolean dataLineHeader(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "dataLineHeader")) return false;
    if (!nextTokenIs(b, "<data line header>", IDENTIFIER, NUMBER)) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NONE_, DATA_LINE_HEADER, "<data line header>");
    r = dataLine_nm(b, l + 1);
    r = r && dataLineHeader_1(b, l + 1);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  // ((COLON | SLASH) dataLine_nm)*
  private static boolean dataLineHeader_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "dataLineHeader_1")) return false;
    while (true) {
      int c = current_position_(b);
      if (!dataLineHeader_1_0(b, l + 1)) break;
      if (!empty_element_parsed_guard_(b, "dataLineHeader_1", c)) break;
    }
    return true;
  }

  // (COLON | SLASH) dataLine_nm
  private static boolean dataLineHeader_1_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "dataLineHeader_1_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = dataLineHeader_1_0_0(b, l + 1);
    r = r && dataLine_nm(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  // COLON | SLASH
  private static boolean dataLineHeader_1_0_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "dataLineHeader_1_0_0")) return false;
    boolean r;
    r = consumeToken(b, COLON);
    if (!r) r = consumeToken(b, SLASH);
    return r;
  }

  /* ********************************************************** */
  // IDENTIFIER | NUMBER
  public static boolean dataLine_nm(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "dataLine_nm")) return false;
    if (!nextTokenIs(b, "<data line nm>", IDENTIFIER, NUMBER)) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NONE_, DATA_LINE_NM, "<data line nm>");
    r = consumeToken(b, IDENTIFIER);
    if (!r) r = consumeToken(b, NUMBER);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  /* ********************************************************** */
  // value
  public static boolean dataLine_value(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "dataLine_value")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NONE_, DATA_LINE_VALUE, "<data line value>");
    r = value(b, l + 1);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  /* ********************************************************** */
  // identifierEx (LSBR typeList? RSBR)? (LRBR argList? RRBR)?
  public static boolean exprValue(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "exprValue")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NONE_, EXPR_VALUE, "<expr value>");
    r = identifierEx(b, l + 1);
    r = r && exprValue_1(b, l + 1);
    r = r && exprValue_2(b, l + 1);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  // (LSBR typeList? RSBR)?
  private static boolean exprValue_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "exprValue_1")) return false;
    exprValue_1_0(b, l + 1);
    return true;
  }

  // LSBR typeList? RSBR
  private static boolean exprValue_1_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "exprValue_1_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, LSBR);
    r = r && exprValue_1_0_1(b, l + 1);
    r = r && consumeToken(b, RSBR);
    exit_section_(b, m, null, r);
    return r;
  }

  // typeList?
  private static boolean exprValue_1_0_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "exprValue_1_0_1")) return false;
    typeList(b, l + 1);
    return true;
  }

  // (LRBR argList? RRBR)?
  private static boolean exprValue_2(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "exprValue_2")) return false;
    exprValue_2_0(b, l + 1);
    return true;
  }

  // LRBR argList? RRBR
  private static boolean exprValue_2_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "exprValue_2_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, LRBR);
    r = r && exprValue_2_0_1(b, l + 1);
    r = r && consumeToken(b, RRBR);
    exit_section_(b, m, null, r);
    return r;
  }

  // argList?
  private static boolean exprValue_2_0_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "exprValue_2_0_1")) return false;
    argList(b, l + 1);
    return true;
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
  // value
  public static boolean headerValue_val(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "headerValue_val")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NONE_, HEADER_VALUE_VAL, "<header value val>");
    r = value(b, l + 1);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  /* ********************************************************** */
  // !(LSBR | IDENTIFIER | NUMBER)
  static boolean header_r(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "header_r")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NOT_);
    r = !header_r_0(b, l + 1);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  // LSBR | IDENTIFIER | NUMBER
  private static boolean header_r_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "header_r_0")) return false;
    boolean r;
    r = consumeToken(b, LSBR);
    if (!r) r = consumeToken(b, IDENTIFIER);
    if (!r) r = consumeToken(b, NUMBER);
    return r;
  }

  /* ********************************************************** */
  // OBJECT_REF | IDENTIFIER | IDENTIFIER_REF | GD_SCENE | EXT_RESOURCE | NODE
  public static boolean identifierEx(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "identifierEx")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NONE_, IDENTIFIER_EX, "<identifier ex>");
    r = consumeToken(b, OBJECT_REF);
    if (!r) r = consumeToken(b, IDENTIFIER);
    if (!r) r = consumeToken(b, IDENTIFIER_REF);
    if (!r) r = consumeToken(b, GD_SCENE);
    if (!r) r = consumeToken(b, EXT_RESOURCE);
    if (!r) r = consumeToken(b, NODE);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  /* ********************************************************** */
  // object | array
  public static boolean json(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "json")) return false;
    if (!nextTokenIs(b, "<json>", LCBR, LSBR)) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NONE_, JSON, "<json>");
    r = object(b, l + 1);
    if (!r) r = array(b, l + 1);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  /* ********************************************************** */
  // jsonValue COLON jsonValue
  public static boolean jsonPair(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "jsonPair")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NONE_, JSON_PAIR, "<json pair>");
    r = jsonValue(b, l + 1);
    r = r && consumeToken(b, COLON);
    r = r && jsonValue(b, l + 1);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  /* ********************************************************** */
  // jsonPair (COMMA jsonPair)*
  public static boolean jsonPairList(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "jsonPairList")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NONE_, JSON_PAIR_LIST, "<json pair list>");
    r = jsonPair(b, l + 1);
    r = r && jsonPairList_1(b, l + 1);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  // (COMMA jsonPair)*
  private static boolean jsonPairList_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "jsonPairList_1")) return false;
    while (true) {
      int c = current_position_(b);
      if (!jsonPairList_1_0(b, l + 1)) break;
      if (!empty_element_parsed_guard_(b, "jsonPairList_1", c)) break;
    }
    return true;
  }

  // COMMA jsonPair
  private static boolean jsonPairList_1_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "jsonPairList_1_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, COMMA);
    r = r && jsonPair(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  /* ********************************************************** */
  // numberValue | TRUE | FALSE | NULL | STRING | STRING_REF | exprValue | array | object
  public static boolean jsonValue(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "jsonValue")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NONE_, JSON_VALUE, "<json value>");
    r = numberValue(b, l + 1);
    if (!r) r = consumeToken(b, TRUE);
    if (!r) r = consumeToken(b, FALSE);
    if (!r) r = consumeToken(b, NULL);
    if (!r) r = consumeToken(b, STRING);
    if (!r) r = consumeToken(b, STRING_REF);
    if (!r) r = exprValue(b, l + 1);
    if (!r) r = array(b, l + 1);
    if (!r) r = object(b, l + 1);
    exit_section_(b, l, m, r, false, null);
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
  // (PLUS | MINUS)? NUMBER
  public static boolean numberValue(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "numberValue")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NONE_, NUMBER_VALUE, "<number value>");
    r = numberValue_0(b, l + 1);
    r = r && consumeToken(b, NUMBER);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  // (PLUS | MINUS)?
  private static boolean numberValue_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "numberValue_0")) return false;
    numberValue_0_0(b, l + 1);
    return true;
  }

  // PLUS | MINUS
  private static boolean numberValue_0_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "numberValue_0_0")) return false;
    boolean r;
    r = consumeToken(b, PLUS);
    if (!r) r = consumeToken(b, MINUS);
    return r;
  }

  /* ********************************************************** */
  // LCBR jsonPairList? RCBR
  public static boolean object(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "object")) return false;
    if (!nextTokenIs(b, LCBR)) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, LCBR);
    r = r && object_1(b, l + 1);
    r = r && consumeToken(b, RCBR);
    exit_section_(b, m, OBJECT, r);
    return r;
  }

  // jsonPairList?
  private static boolean object_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "object_1")) return false;
    jsonPairList(b, l + 1);
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
  // LSBR GD_SCENE headerValue* RSBR {
  // // TODO would be required to recoverWhile only when pinned
  // //    pin=2
  // //    recoverWhile=header_r
  // }
  public static boolean scene_header(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "scene_header")) return false;
    if (!nextTokenIs(b, LSBR)) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeTokens(b, 0, LSBR, GD_SCENE);
    r = r && scene_header_2(b, l + 1);
    r = r && consumeToken(b, RSBR);
    r = r && scene_header_4(b, l + 1);
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

  // {
  // // TODO would be required to recoverWhile only when pinned
  // //    pin=2
  // //    recoverWhile=header_r
  // }
  private static boolean scene_header_4(PsiBuilder b, int l) {
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
  // exprValue? (COMMA exprValue)*
  public static boolean typeList(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "typeList")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NONE_, TYPE_LIST, "<type list>");
    r = typeList_0(b, l + 1);
    r = r && typeList_1(b, l + 1);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  // exprValue?
  private static boolean typeList_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "typeList_0")) return false;
    exprValue(b, l + 1);
    return true;
  }

  // (COMMA exprValue)*
  private static boolean typeList_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "typeList_1")) return false;
    while (true) {
      int c = current_position_(b);
      if (!typeList_1_0(b, l + 1)) break;
      if (!empty_element_parsed_guard_(b, "typeList_1", c)) break;
    }
    return true;
  }

  // COMMA exprValue
  private static boolean typeList_1_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "typeList_1_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, COMMA);
    r = r && exprValue(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  /* ********************************************************** */
  // LSBR IDENTIFIER headerValue* RSBR {
  // //    pin=1
  // //    recoverWhile=header_r
  // }
  public static boolean unknown_header(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "unknown_header")) return false;
    if (!nextTokenIs(b, LSBR)) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeTokens(b, 0, LSBR, IDENTIFIER);
    r = r && unknown_header_2(b, l + 1);
    r = r && consumeToken(b, RSBR);
    r = r && unknown_header_4(b, l + 1);
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

  // {
  // //    pin=1
  // //    recoverWhile=header_r
  // }
  private static boolean unknown_header_4(PsiBuilder b, int l) {
    return true;
  }

  /* ********************************************************** */
  // numberValue | TRUE | FALSE | NULL | STRING | STRING_REF | exprValue | json
  public static boolean value(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "value")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NONE_, VALUE, "<value>");
    r = numberValue(b, l + 1);
    if (!r) r = consumeToken(b, TRUE);
    if (!r) r = consumeToken(b, FALSE);
    if (!r) r = consumeToken(b, NULL);
    if (!r) r = consumeToken(b, STRING);
    if (!r) r = consumeToken(b, STRING_REF);
    if (!r) r = exprValue(b, l + 1);
    if (!r) r = json(b, l + 1);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  /* ********************************************************** */
  // jsonValue (COMMA jsonValue)*
  public static boolean valueList(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "valueList")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NONE_, VALUE_LIST, "<value list>");
    r = jsonValue(b, l + 1);
    r = r && valueList_1(b, l + 1);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  // (COMMA jsonValue)*
  private static boolean valueList_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "valueList_1")) return false;
    while (true) {
      int c = current_position_(b);
      if (!valueList_1_0(b, l + 1)) break;
      if (!empty_element_parsed_guard_(b, "valueList_1", c)) break;
    }
    return true;
  }

  // COMMA jsonValue
  private static boolean valueList_1_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "valueList_1_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, COMMA);
    r = r && jsonValue(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

}
