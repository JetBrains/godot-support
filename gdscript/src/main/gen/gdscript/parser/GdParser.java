// This is a generated file. Not intended for manual editing.
package gdscript.parser;

import com.intellij.lang.PsiBuilder;
import com.intellij.lang.PsiBuilder.Marker;
import static gdscript.psi.GdTypes.*;
import static com.intellij.lang.parser.GeneratedParserUtilBase.*;
import com.intellij.psi.tree.IElementType;
import com.intellij.lang.ASTNode;
import com.intellij.psi.tree.TokenSet;
import com.intellij.lang.PsiParser;
import com.intellij.lang.LightPsiParser;

@SuppressWarnings({"SimplifiableIfStatement", "UnusedAssignment"})
public class GdParser implements PsiParser, LightPsiParser {

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
    return gdfile(b, l + 1);
  }

  public static final TokenSet[] EXTENDS_SETS_ = new TokenSet[] {
    create_token_set_(CLASS_VAR_DECL_TL, CONST_DECL_TL, METHOD_DECL_TL, TOP_LEVEL_DECL),
  };

  /* ********************************************************** */
  // ANNOTATOR
  public static boolean annotation(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "annotation")) return false;
    if (!nextTokenIs(b, ANNOTATOR)) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, ANNOTATOR);
    exit_section_(b, m, ANNOTATION, r);
    return r;
  }

  /* ********************************************************** */
  // INT
  public static boolean builtInType(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "builtInType")) return false;
    if (!nextTokenIs(b, INT)) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, INT);
    exit_section_(b, m, BUILT_IN_TYPE, r);
    return r;
  }

  /* ********************************************************** */
  // IDENTIFIER
  public static boolean className_nm(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "className_nm")) return false;
    if (!nextTokenIs(b, IDENTIFIER)) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, IDENTIFIER);
    exit_section_(b, m, CLASS_NAME_NM, r);
    return r;
  }

  /* ********************************************************** */
  // CLASS_NAME className_nm (COMMA STRING)? newLineEnd
  public static boolean classNaming(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "classNaming")) return false;
    boolean r, p;
    Marker m = enter_section_(b, l, _NONE_, CLASS_NAMING, "<class naming>");
    r = consumeToken(b, CLASS_NAME);
    p = r; // pin = 1
    r = r && report_error_(b, className_nm(b, l + 1));
    r = p && report_error_(b, classNaming_2(b, l + 1)) && r;
    r = p && newLineEnd(b, l + 1) && r;
    exit_section_(b, l, m, r, p, GdParser::classNaming_r);
    return r || p;
  }

  // (COMMA STRING)?
  private static boolean classNaming_2(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "classNaming_2")) return false;
    classNaming_2_0(b, l + 1);
    return true;
  }

  // COMMA STRING
  private static boolean classNaming_2_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "classNaming_2_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeTokens(b, 0, COMMA, STRING);
    exit_section_(b, m, null, r);
    return r;
  }

  /* ********************************************************** */
  // !(TOOL) & topLevelDecl_r
  static boolean classNaming_r(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "classNaming_r")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = classNaming_r_0(b, l + 1);
    r = r && classNaming_r_1(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  // !(TOOL)
  private static boolean classNaming_r_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "classNaming_r_0")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NOT_);
    r = !consumeToken(b, TOOL);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  // & topLevelDecl_r
  private static boolean classNaming_r_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "classNaming_r_1")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _AND_);
    r = topLevelDecl_r(b, l + 1);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  /* ********************************************************** */
  // annotation* VAR classVarId_nmi typed? (EQ expr)? setgetDecl? endStmt
  public static boolean classVarDecl_tl(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "classVarDecl_tl")) return false;
    boolean r, p;
    Marker m = enter_section_(b, l, _NONE_, CLASS_VAR_DECL_TL, "<class var decl tl>");
    r = classVarDecl_tl_0(b, l + 1);
    r = r && consumeToken(b, VAR);
    p = r; // pin = 2
    r = r && report_error_(b, classVarId_nmi(b, l + 1));
    r = p && report_error_(b, classVarDecl_tl_3(b, l + 1)) && r;
    r = p && report_error_(b, classVarDecl_tl_4(b, l + 1)) && r;
    r = p && report_error_(b, classVarDecl_tl_5(b, l + 1)) && r;
    r = p && endStmt(b, l + 1) && r;
    exit_section_(b, l, m, r, p, GdParser::topLevelDecl_r);
    return r || p;
  }

  // annotation*
  private static boolean classVarDecl_tl_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "classVarDecl_tl_0")) return false;
    while (true) {
      int c = current_position_(b);
      if (!annotation(b, l + 1)) break;
      if (!empty_element_parsed_guard_(b, "classVarDecl_tl_0", c)) break;
    }
    return true;
  }

  // typed?
  private static boolean classVarDecl_tl_3(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "classVarDecl_tl_3")) return false;
    typed(b, l + 1);
    return true;
  }

  // (EQ expr)?
  private static boolean classVarDecl_tl_4(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "classVarDecl_tl_4")) return false;
    classVarDecl_tl_4_0(b, l + 1);
    return true;
  }

  // EQ expr
  private static boolean classVarDecl_tl_4_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "classVarDecl_tl_4_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, EQ);
    r = r && expr(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  // setgetDecl?
  private static boolean classVarDecl_tl_5(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "classVarDecl_tl_5")) return false;
    setgetDecl(b, l + 1);
    return true;
  }

  /* ********************************************************** */
  // IDENTIFIER
  public static boolean classVarId_nmi(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "classVarId_nmi")) return false;
    if (!nextTokenIs(b, IDENTIFIER)) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, IDENTIFIER);
    exit_section_(b, m, CLASS_VAR_ID_NMI, r);
    return r;
  }

  /* ********************************************************** */
  // CONST constId_nmi typed? EQ expr endStmt
  public static boolean constDecl_tl(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "constDecl_tl")) return false;
    boolean r, p;
    Marker m = enter_section_(b, l, _NONE_, CONST_DECL_TL, "<const decl tl>");
    r = consumeToken(b, CONST);
    p = r; // pin = 1
    r = r && report_error_(b, constId_nmi(b, l + 1));
    r = p && report_error_(b, constDecl_tl_2(b, l + 1)) && r;
    r = p && report_error_(b, consumeToken(b, EQ)) && r;
    r = p && report_error_(b, expr(b, l + 1)) && r;
    r = p && endStmt(b, l + 1) && r;
    exit_section_(b, l, m, r, p, GdParser::topLevelDecl_r);
    return r || p;
  }

  // typed?
  private static boolean constDecl_tl_2(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "constDecl_tl_2")) return false;
    typed(b, l + 1);
    return true;
  }

  /* ********************************************************** */
  // IDENTIFIER
  public static boolean constId_nmi(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "constId_nmi")) return false;
    if (!nextTokenIs(b, IDENTIFIER)) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, IDENTIFIER);
    exit_section_(b, m, CONST_ID_NMI, r);
    return r;
  }

  /* ********************************************************** */
  // SEMICON | newLineEnd
  public static boolean endStmt(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "endStmt")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NONE_, END_STMT, "<end stmt>");
    r = consumeToken(b, SEMICON);
    if (!r) r = newLineEnd(b, l + 1);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  /* ********************************************************** */
  // literal
  public static boolean expr(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "expr")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NONE_, EXPR, "<expr>");
    r = literal(b, l + 1);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  /* ********************************************************** */
  // inheritance? classNaming? toolline? topLevelDecl*
  static boolean gdfile(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "gdfile")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = gdfile_0(b, l + 1);
    r = r && gdfile_1(b, l + 1);
    r = r && gdfile_2(b, l + 1);
    r = r && gdfile_3(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  // inheritance?
  private static boolean gdfile_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "gdfile_0")) return false;
    inheritance(b, l + 1);
    return true;
  }

  // classNaming?
  private static boolean gdfile_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "gdfile_1")) return false;
    classNaming(b, l + 1);
    return true;
  }

  // toolline?
  private static boolean gdfile_2(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "gdfile_2")) return false;
    toolline(b, l + 1);
    return true;
  }

  // topLevelDecl*
  private static boolean gdfile_3(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "gdfile_3")) return false;
    while (true) {
      int c = current_position_(b);
      if (!topLevelDecl(b, l + 1)) break;
      if (!empty_element_parsed_guard_(b, "gdfile_3", c)) break;
    }
    return true;
  }

  /* ********************************************************** */
  // IDENTIFIER
  public static boolean getMethodId_nm(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "getMethodId_nm")) return false;
    if (!nextTokenIs(b, IDENTIFIER)) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, IDENTIFIER);
    exit_section_(b, m, GET_METHOD_ID_NM, r);
    return r;
  }

  /* ********************************************************** */
  // EXTENDS inheritanceId_nmi (DOT IDENTIFIER)? newLineEnd
  public static boolean inheritance(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "inheritance")) return false;
    boolean r, p;
    Marker m = enter_section_(b, l, _NONE_, INHERITANCE, "<inheritance>");
    r = consumeToken(b, EXTENDS);
    p = r; // pin = 1
    r = r && report_error_(b, inheritanceId_nmi(b, l + 1));
    r = p && report_error_(b, inheritance_2(b, l + 1)) && r;
    r = p && newLineEnd(b, l + 1) && r;
    exit_section_(b, l, m, r, p, GdParser::inheritance_r);
    return r || p;
  }

  // (DOT IDENTIFIER)?
  private static boolean inheritance_2(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "inheritance_2")) return false;
    inheritance_2_0(b, l + 1);
    return true;
  }

  // DOT IDENTIFIER
  private static boolean inheritance_2_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "inheritance_2_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeTokens(b, 0, DOT, IDENTIFIER);
    exit_section_(b, m, null, r);
    return r;
  }

  /* ********************************************************** */
  // IDENTIFIER
  public static boolean inheritanceId_nmi(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "inheritanceId_nmi")) return false;
    if (!nextTokenIs(b, IDENTIFIER)) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, IDENTIFIER);
    exit_section_(b, m, INHERITANCE_ID_NMI, r);
    return r;
  }

  /* ********************************************************** */
  // !(CLASS_NAME | TOOL) & topLevelDecl_r
  static boolean inheritance_r(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "inheritance_r")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = inheritance_r_0(b, l + 1);
    r = r && inheritance_r_1(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  // !(CLASS_NAME | TOOL)
  private static boolean inheritance_r_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "inheritance_r_0")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NOT_);
    r = !inheritance_r_0_0(b, l + 1);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  // CLASS_NAME | TOOL
  private static boolean inheritance_r_0_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "inheritance_r_0_0")) return false;
    boolean r;
    r = consumeToken(b, CLASS_NAME);
    if (!r) r = consumeToken(b, TOOL);
    return r;
  }

  // & topLevelDecl_r
  private static boolean inheritance_r_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "inheritance_r_1")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _AND_);
    r = topLevelDecl_r(b, l + 1);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  /* ********************************************************** */
  // TRUE | FALSE | STRING | NUMBER | NULL
  public static boolean literal(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "literal")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NONE_, LITERAL, "<literal>");
    r = consumeToken(b, TRUE);
    if (!r) r = consumeToken(b, FALSE);
    if (!r) r = consumeToken(b, STRING);
    if (!r) r = consumeToken(b, NUMBER);
    if (!r) r = consumeToken(b, NULL);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  /* ********************************************************** */
  // FUNC methodId_nmi COLON stmtOrSuite
  public static boolean methodDecl_tl(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "methodDecl_tl")) return false;
    boolean r, p;
    Marker m = enter_section_(b, l, _NONE_, METHOD_DECL_TL, "<method decl tl>");
    r = consumeToken(b, FUNC);
    p = r; // pin = 1
    r = r && report_error_(b, methodId_nmi(b, l + 1));
    r = p && report_error_(b, consumeToken(b, COLON)) && r;
    r = p && stmtOrSuite(b, l + 1) && r;
    exit_section_(b, l, m, r, p, GdParser::topLevelDecl_r);
    return r || p;
  }

  /* ********************************************************** */
  // IDENTIFIER
  public static boolean methodId_nmi(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "methodId_nmi")) return false;
    if (!nextTokenIs(b, IDENTIFIER)) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, IDENTIFIER);
    exit_section_(b, m, METHOD_ID_NMI, r);
    return r;
  }

  /* ********************************************************** */
  // NEW_LINE | <<eof>>
  public static boolean newLineEnd(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "newLineEnd")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NONE_, NEW_LINE_END, "<new line end>");
    r = consumeToken(b, NEW_LINE);
    if (!r) r = eof(b, l + 1);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  /* ********************************************************** */
  // IDENTIFIER
  public static boolean setMethodId_nm(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "setMethodId_nm")) return false;
    if (!nextTokenIs(b, IDENTIFIER)) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, IDENTIFIER);
    exit_section_(b, m, SET_METHOD_ID_NM, r);
    return r;
  }

  /* ********************************************************** */
  // SETGET setMethodId_nm (COMMA getMethodId_nm)?
  public static boolean setgetDecl(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "setgetDecl")) return false;
    if (!nextTokenIs(b, SETGET)) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, SETGET);
    r = r && setMethodId_nm(b, l + 1);
    r = r && setgetDecl_2(b, l + 1);
    exit_section_(b, m, SETGET_DECL, r);
    return r;
  }

  // (COMMA getMethodId_nm)?
  private static boolean setgetDecl_2(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "setgetDecl_2")) return false;
    setgetDecl_2_0(b, l + 1);
    return true;
  }

  // COMMA getMethodId_nm
  private static boolean setgetDecl_2_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "setgetDecl_2_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, COMMA);
    r = r && getMethodId_nm(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  /* ********************************************************** */
  // PASS endStmt
  public static boolean stmt(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "stmt")) return false;
    if (!nextTokenIs(b, PASS)) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, PASS);
    r = r && endStmt(b, l + 1);
    exit_section_(b, m, STMT, r);
    return r;
  }

  /* ********************************************************** */
  // stmt | (NEW_LINE INDENT suite DEDENT)
  public static boolean stmtOrSuite(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "stmtOrSuite")) return false;
    if (!nextTokenIs(b, "<stmt or suite>", NEW_LINE, PASS)) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NONE_, STMT_OR_SUITE, "<stmt or suite>");
    r = stmt(b, l + 1);
    if (!r) r = stmtOrSuite_1(b, l + 1);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  // NEW_LINE INDENT suite DEDENT
  private static boolean stmtOrSuite_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "stmtOrSuite_1")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeTokens(b, 0, NEW_LINE, INDENT);
    r = r && suite(b, l + 1);
    r = r && consumeToken(b, DEDENT);
    exit_section_(b, m, null, r);
    return r;
  }

  /* ********************************************************** */
  // stmt*
  public static boolean suite(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "suite")) return false;
    Marker m = enter_section_(b, l, _NONE_, SUITE, "<suite>");
    while (true) {
      int c = current_position_(b);
      if (!stmt(b, l + 1)) break;
      if (!empty_element_parsed_guard_(b, "suite", c)) break;
    }
    exit_section_(b, l, m, true, false, null);
    return true;
  }

  /* ********************************************************** */
  // TOOL newLineEnd
  public static boolean toolline(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "toolline")) return false;
    boolean r, p;
    Marker m = enter_section_(b, l, _NONE_, TOOLLINE, "<toolline>");
    r = consumeToken(b, TOOL);
    p = r; // pin = 1
    r = r && newLineEnd(b, l + 1);
    exit_section_(b, l, m, r, p, GdParser::topLevelDecl_r);
    return r || p;
  }

  /* ********************************************************** */
  // constDecl_tl |
  //     classVarDecl_tl |
  //     methodDecl_tl
  public static boolean topLevelDecl(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "topLevelDecl")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _COLLAPSE_, TOP_LEVEL_DECL, "<top level decl>");
    r = constDecl_tl(b, l + 1);
    if (!r) r = classVarDecl_tl(b, l + 1);
    if (!r) r = methodDecl_tl(b, l + 1);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  /* ********************************************************** */
  // !(FUNC | CONST | IDENTIFIER | VAR | annotation)
  static boolean topLevelDecl_r(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "topLevelDecl_r")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NOT_);
    r = !topLevelDecl_r_0(b, l + 1);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  // FUNC | CONST | IDENTIFIER | VAR | annotation
  private static boolean topLevelDecl_r_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "topLevelDecl_r_0")) return false;
    boolean r;
    r = consumeToken(b, FUNC);
    if (!r) r = consumeToken(b, CONST);
    if (!r) r = consumeToken(b, IDENTIFIER);
    if (!r) r = consumeToken(b, VAR);
    if (!r) r = annotation(b, l + 1);
    return r;
  }

  /* ********************************************************** */
  // builtInType | IDENTIFIER
  public static boolean typeHint_nm(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "typeHint_nm")) return false;
    if (!nextTokenIs(b, "<type hint nm>", IDENTIFIER, INT)) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NONE_, TYPE_HINT_NM, "<type hint nm>");
    r = builtInType(b, l + 1);
    if (!r) r = consumeToken(b, IDENTIFIER);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  /* ********************************************************** */
  // COLON typeHint_nm
  public static boolean typed(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "typed")) return false;
    if (!nextTokenIs(b, COLON)) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, COLON);
    r = r && typeHint_nm(b, l + 1);
    exit_section_(b, m, TYPED, r);
    return r;
  }

}
