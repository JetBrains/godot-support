package gdscript.psi;

import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElement;
import com.intellij.psi.tree.IElementType;
import gdscript.psi.impl.GdAnnotationParamsImpl;
import gdscript.psi.impl.GdAnnotationStmtImpl;
import gdscript.psi.impl.GdAnnotationTlImpl;
import gdscript.psi.impl.GdAnnotationTypeImpl;
import gdscript.psi.impl.GdArgExprImpl;
import gdscript.psi.impl.GdArgListImpl;
import gdscript.psi.impl.GdArrExImpl;
import gdscript.psi.impl.GdArrayDeclImpl;
import gdscript.psi.impl.GdArrayPatternImpl;
import gdscript.psi.impl.GdAssignSignImpl;
import gdscript.psi.impl.GdAssignStImpl;
import gdscript.psi.impl.GdAssignTypedImpl;
import gdscript.psi.impl.GdAttributeExImpl;
import gdscript.psi.impl.GdAwaitExImpl;
import gdscript.psi.impl.GdBindingPatternImpl;
import gdscript.psi.impl.GdBitAndExImpl;
import gdscript.psi.impl.GdBitAndSignImpl;
import gdscript.psi.impl.GdBitNotExImpl;
import gdscript.psi.impl.GdCallExImpl;
import gdscript.psi.impl.GdCastExImpl;
import gdscript.psi.impl.GdClassDeclElementType;
import gdscript.psi.impl.GdClassDeclTlImpl;
import gdscript.psi.impl.GdClassIdElementType;
import gdscript.psi.impl.GdClassNameNmiImpl;
import gdscript.psi.impl.GdClassNamingElementType;
import gdscript.psi.impl.GdClassNamingImpl;
import gdscript.psi.impl.GdClassVarDeclElementType;
import gdscript.psi.impl.GdClassVarDeclTlImpl;
import gdscript.psi.impl.GdComparisonExImpl;
import gdscript.psi.impl.GdConstDeclElementType;
import gdscript.psi.impl.GdConstDeclStImpl;
import gdscript.psi.impl.GdConstDeclTlImpl;
import gdscript.psi.impl.GdDictDeclImpl;
import gdscript.psi.impl.GdDictPatternImpl;
import gdscript.psi.impl.GdElifStImpl;
import gdscript.psi.impl.GdElseStImpl;
import gdscript.psi.impl.GdEmptyStmtImpl;
import gdscript.psi.impl.GdEndStmtImpl;
import gdscript.psi.impl.GdEnumDeclElementType;
import gdscript.psi.impl.GdEnumDeclNmiImpl;
import gdscript.psi.impl.GdEnumDeclTlImpl;
import gdscript.psi.impl.GdEnumValueImpl;
import gdscript.psi.impl.GdEnumValueNmiImpl;
import gdscript.psi.impl.GdExprStImpl;
import gdscript.psi.impl.GdFactorExImpl;
import gdscript.psi.impl.GdFactorSignImpl;
import gdscript.psi.impl.GdFlowStImpl;
import gdscript.psi.impl.GdForStImpl;
import gdscript.psi.impl.GdFuncDeclExImpl;
import gdscript.psi.impl.GdFuncDeclIdNmiImpl;
import gdscript.psi.impl.GdGetDeclImpl;
import gdscript.psi.impl.GdGetMethodIdRefImpl;
import gdscript.psi.impl.GdIfStImpl;
import gdscript.psi.impl.GdInExImpl;
import gdscript.psi.impl.GdInheritanceElementType;
import gdscript.psi.impl.GdInheritanceIdImpl;
import gdscript.psi.impl.GdInheritanceIdRefImpl;
import gdscript.psi.impl.GdInheritanceImpl;
import gdscript.psi.impl.GdInheritanceSubIdRefImpl;
import gdscript.psi.impl.GdIsExImpl;
import gdscript.psi.impl.GdKeyNmiImpl;
import gdscript.psi.impl.GdKeyValueImpl;
import gdscript.psi.impl.GdKeyValuePatternImpl;
import gdscript.psi.impl.GdLiteralExImpl;
import gdscript.psi.impl.GdLogicExImpl;
import gdscript.psi.impl.GdMatchBlockImpl;
import gdscript.psi.impl.GdMatchStImpl;
import gdscript.psi.impl.GdMethodDeclElementType;
import gdscript.psi.impl.GdMethodDeclTlImpl;
import gdscript.psi.impl.GdMethodIdNmiImpl;
import gdscript.psi.impl.GdMethodSpecifierImpl;
import gdscript.psi.impl.GdNegateExImpl;
import gdscript.psi.impl.GdNewLineEndImpl;
import gdscript.psi.impl.GdNodePathImpl;
import gdscript.psi.impl.GdOperatorImpl;
import gdscript.psi.impl.GdParamImpl;
import gdscript.psi.impl.GdParamListImpl;
import gdscript.psi.impl.GdParenthesizedExImpl;
import gdscript.psi.impl.GdPatternImpl;
import gdscript.psi.impl.GdPatternListImpl;
import gdscript.psi.impl.GdPlusExImpl;
import gdscript.psi.impl.GdPlusMinusExImpl;
import gdscript.psi.impl.GdPlusMinusPreExImpl;
import gdscript.psi.impl.GdPrimaryExImpl;
import gdscript.psi.impl.GdRefIdRefImpl;
import gdscript.psi.impl.GdReturnHintImpl;
import gdscript.psi.impl.GdReturnHintValImpl;
import gdscript.psi.impl.GdSetDeclImpl;
import gdscript.psi.impl.GdSetMethodIdRefImpl;
import gdscript.psi.impl.GdSetgetDeclImpl;
import gdscript.psi.impl.GdShiftExImpl;
import gdscript.psi.impl.GdSignExImpl;
import gdscript.psi.impl.GdSignImpl;
import gdscript.psi.impl.GdSignalDeclElementType;
import gdscript.psi.impl.GdSignalDeclTlImpl;
import gdscript.psi.impl.GdSignalIdNmiImpl;
import gdscript.psi.impl.GdStmtOrSuiteImpl;
import gdscript.psi.impl.GdStringValRefImpl;
import gdscript.psi.impl.GdSuiteImpl;
import gdscript.psi.impl.GdTernaryExImpl;
import gdscript.psi.impl.GdTopLevelDeclImpl;
import gdscript.psi.impl.GdTypeHintImpl;
import gdscript.psi.impl.GdTypeHintRefImpl;
import gdscript.psi.impl.GdTypedImpl;
import gdscript.psi.impl.GdTypedValImpl;
import gdscript.psi.impl.GdVarDeclStImpl;
import gdscript.psi.impl.GdVarNmiImpl;
import gdscript.psi.impl.GdWhileStImpl;
import org.jetbrains.annotations.NotNull;

public interface GdTypes {

  @NotNull IElementType FILE = new GdElementType("FILE");
  @NotNull IElementType ANNOTATION_PARAMS = new GdElementType("ANNOTATION_PARAMS");
  @NotNull IElementType ANNOTATION_TL = new GdElementType("ANNOTATION_TL");
  @NotNull IElementType ANNOTATION_STMT = new GdElementType("ANNOTATION_STMT");
  @NotNull IElementType ANNOTATION_TYPE = new GdElementType("ANNOTATION_TYPE");
  @NotNull IElementType ARG_EXPR = new GdElementType("ARG_EXPR");
  @NotNull IElementType ARG_LIST = new GdElementType("ARG_LIST");
  @NotNull IElementType ARRAY_DECL = new GdElementType("ARRAY_DECL");
  @NotNull IElementType ARRAY_PATTERN = new GdElementType("ARRAY_PATTERN");
  @NotNull IElementType ARR_EX = new GdElementType("ARR_EX");
  @NotNull IElementType ASSIGN_SIGN = new GdElementType("ASSIGN_SIGN");
  @NotNull IElementType ASSIGN_ST = new GdElementType("ASSIGN_ST");
  @NotNull IElementType ASSIGN_TYPED = new GdElementType("ASSIGN_TYPED");
  @NotNull IElementType ATTRIBUTE_EX = new GdElementType("ATTRIBUTE_EX");
  @NotNull IElementType AWAIT_EX = new GdElementType("AWAIT_EX");
  @NotNull IElementType BINDING_PATTERN = new GdElementType("BINDING_PATTERN");
  @NotNull IElementType BIT_AND_EX = new GdElementType("BIT_AND_EX");
  @NotNull IElementType BIT_AND_SIGN = new GdElementType("BIT_AND_SIGN");
  @NotNull IElementType BIT_NOT_EX = new GdElementType("BIT_NOT_EX");
  @NotNull IElementType CALL_EX = new GdElementType("CALL_EX");
  @NotNull IElementType CAST_EX = new GdElementType("CAST_EX");
  @NotNull IElementType CLASS_DECL_TL = GdClassDeclElementType.getInstance("CLASS_DECL_TL");
  @NotNull IElementType CLASS_NAME_NMI = GdClassIdElementType.getInstance("CLASS_NAME_NMI");
  @NotNull IElementType CLASS_NAMING = GdClassNamingElementType.getInstance("CLASS_NAMING");
  @NotNull IElementType CLASS_VAR_DECL_TL = GdClassVarDeclElementType.getInstance("CLASS_VAR_DECL_TL");
  @NotNull IElementType COMPARISON_EX = new GdElementType("COMPARISON_EX");
  @NotNull IElementType CONST_DECL_ST = new GdElementType("CONST_DECL_ST");
  @NotNull IElementType CONST_DECL_TL = GdConstDeclElementType.getInstance("CONST_DECL_TL");
  @NotNull IElementType DICT_DECL = new GdElementType("DICT_DECL");
  @NotNull IElementType DICT_PATTERN = new GdElementType("DICT_PATTERN");
  @NotNull IElementType ELIF_ST = new GdElementType("ELIF_ST");
  @NotNull IElementType ELSE_ST = new GdElementType("ELSE_ST");
  @NotNull IElementType EMPTY_STMT = new GdElementType("EMPTY_STMT");
  @NotNull IElementType END_STMT = new GdElementType("END_STMT");
  @NotNull IElementType ENUM_DECL_NMI = new GdElementType("ENUM_DECL_NMI");
  @NotNull IElementType ENUM_DECL_TL = GdEnumDeclElementType.getInstance("ENUM_DECL_TL");
  @NotNull IElementType ENUM_VALUE = new GdElementType("ENUM_VALUE");
  @NotNull IElementType ENUM_VALUE_NMI = new GdElementType("ENUM_VALUE_NMI");
  @NotNull IElementType EXPR = new GdElementType("EXPR");
  @NotNull IElementType EXPR_ST = new GdElementType("EXPR_ST");
  @NotNull IElementType FACTOR_EX = new GdElementType("FACTOR_EX");
  @NotNull IElementType FACTOR_SIGN = new GdElementType("FACTOR_SIGN");
  @NotNull IElementType FLOW_ST = new GdElementType("FLOW_ST");
  @NotNull IElementType FOR_ST = new GdElementType("FOR_ST");
  @NotNull IElementType FUNC_DECL_EX = new GdElementType("FUNC_DECL_EX");
  @NotNull IElementType FUNC_DECL_ID_NMI = new GdElementType("FUNC_DECL_ID_NMI");
  @NotNull IElementType GET_DECL = new GdElementType("GET_DECL");
  @NotNull IElementType GET_METHOD_ID_NM = new GdElementType("GET_METHOD_ID_NM");
  @NotNull IElementType IF_ST = new GdElementType("IF_ST");
  @NotNull IElementType INHERITANCE = GdInheritanceElementType.getInstance("INHERITANCE");
  @NotNull IElementType INHERITANCE_ID = new GdElementType("INHERITANCE_ID");
  @NotNull IElementType INHERITANCE_ID_NM = new GdElementType("INHERITANCE_ID_NM");
  @NotNull IElementType INHERITANCE_SUB_ID_NM = new GdElementType("INHERITANCE_SUB_ID_NM");
  @NotNull IElementType IN_EX = new GdElementType("IN_EX");
  @NotNull IElementType IS_EX = new GdElementType("IS_EX");
  @NotNull IElementType KEY_NMI = new GdElementType("KEY");
  @NotNull IElementType KEY_VALUE = new GdElementType("KEY_VALUE");
  @NotNull IElementType KEY_VALUE_PATTERN = new GdElementType("KEY_VALUE_PATTERN");
  @NotNull IElementType LITERAL_EX = new GdElementType("LITERAL_EX");
  @NotNull IElementType LOGIC_EX = new GdElementType("LOGIC_EX");
  @NotNull IElementType MATCH_BLOCK = new GdElementType("MATCH_BLOCK");
  @NotNull IElementType MATCH_ST = new GdElementType("MATCH_ST");
  @NotNull IElementType METHOD_DECL_TL = GdMethodDeclElementType.getInstance("METHOD_DECL_TL");
  @NotNull IElementType METHOD_ID_NMI = new GdElementType("METHOD_ID_NMI");
  @NotNull IElementType METHOD_SPECIFIER = new GdElementType("METHOD_SPECIFIER");
  @NotNull IElementType NEGATE_EX = new GdElementType("NEGATE_EX");
  @NotNull IElementType NEW_LINE_END = new GdElementType("NEW_LINE_END");
  @NotNull IElementType NODE_PATH = new GdElementType("NODE_PATH");
  @NotNull IElementType OPERATOR = new GdElementType("OPERATOR");
  @NotNull IElementType PARAM = new GdElementType("PARAM");
  @NotNull IElementType PARAM_LIST = new GdElementType("PARAM_LIST");
  @NotNull IElementType PARENTHESIZED_EX = new GdElementType("PARENTHESIZED_EX");
  @NotNull IElementType PATTERN = new GdElementType("PATTERN");
  @NotNull IElementType PATTERN_LIST = new GdElementType("PATTERN_LIST");
  @NotNull IElementType PLUS_EX = new GdElementType("PLUS_EX");
  @NotNull IElementType PLUS_MINUS_EX = new GdElementType("PLUS_MINUS_EX");
  @NotNull IElementType PLUS_MINUS_PRE_EX = new GdElementType("PLUS_MINUS_PRE_EX");
  @NotNull IElementType PRIMARY_EX = new GdElementType("PRIMARY_EX");
  @NotNull IElementType REF_ID_NM = new GdElementType("REF_ID_NM");
  @NotNull IElementType RETURN_HINT = new GdElementType("RETURN_HINT");
  @NotNull IElementType RETURN_HINT_VAL = new GdElementType("RETURN_HINT_VAL");
  @NotNull IElementType SETGET_DECL = new GdElementType("SETGET_DECL");
  @NotNull IElementType SET_DECL = new GdElementType("SET_DECL");
  @NotNull IElementType SET_METHOD_ID_NM = new GdElementType("SET_METHOD_ID_NM");
  @NotNull IElementType SHIFT_EX = new GdElementType("SHIFT_EX");
  @NotNull IElementType SIGN = new GdElementType("SIGN");
  @NotNull IElementType SIGNAL_DECL_TL = GdSignalDeclElementType.getInstance("SIGNAL_DECL_TL");
  @NotNull IElementType SIGNAL_ID_NMI = new GdElementType("SIGNAL_ID_NMI");
  @NotNull IElementType SIGN_EX = new GdElementType("SIGN_EX");
  @NotNull IElementType STMT = new GdElementType("STMT");
  @NotNull IElementType STMT_OR_SUITE = new GdElementType("STMT_OR_SUITE");
  @NotNull IElementType STRING_VAL_NM = new GdElementType("STRING_VAL_NM");
  @NotNull IElementType SUITE = new GdElementType("SUITE");
  @NotNull IElementType TERNARY_EX = new GdElementType("TERNARY_EX");
  @NotNull IElementType TOP_LEVEL_DECL = new GdElementType("TOP_LEVEL_DECL");
  @NotNull IElementType TYPED = new GdElementType("TYPED");
  @NotNull IElementType TYPED_VAL = new GdElementType("TYPED_VAL");
  @NotNull IElementType TYPE_HINT = new GdElementType("TYPE_HINT");
  @NotNull IElementType TYPE_HINT_REF = new GdElementType("TYPE_HINT_NM");
  @NotNull IElementType VAR_DECL_ST = new GdElementType("VAR_DECL_ST");
  @NotNull IElementType VAR_NMI = new GdElementType("VAR_NMI");
  @NotNull IElementType WHILE_ST = new GdElementType("WHILE_ST");

  @NotNull IElementType AND = new GdTokenType("AND");
  @NotNull IElementType ANDAND = new GdTokenType("ANDAND");
  @NotNull IElementType ANNOTATOR = new GdTokenType("ANNOTATOR");
  @NotNull IElementType AS = new GdTokenType("AS");
  @NotNull IElementType ASSET = new GdTokenType("ASSET");
  @NotNull IElementType ASSIGN = new GdTokenType("ASSIGN");
  @NotNull IElementType AWAIT = new GdTokenType("AWAIT");
  @NotNull IElementType BACKSLASH = new GdTokenType("\\");
  @NotNull IElementType BAD_CHARACTER = new GdTokenType("BAD_CHARACTER");
  @NotNull IElementType BREAK = new GdTokenType("BREAK");
  @NotNull IElementType BREAKPOINT = new GdTokenType("BREAKPOINT");
  @NotNull IElementType CEQ = new GdTokenType("CEQ");
  @NotNull IElementType CLASS = new GdTokenType("CLASS");
  @NotNull IElementType CLASS_NAME = new GdTokenType("CLASS_NAME");
  @NotNull IElementType COLON = new GdTokenType("COLON");
  @NotNull IElementType COMMA = new GdTokenType("COMMA");
  @NotNull IElementType COMMENT = new GdTokenType("comment");
  @NotNull IElementType CONST = new GdTokenType("CONST");
  @NotNull IElementType CONTINUE = new GdTokenType("CONTINUE");
  @NotNull IElementType DEDENT = new GdTokenType("DEDENT");
  @NotNull IElementType DIV = new GdTokenType("DIV");
  @NotNull IElementType DOT = new GdTokenType("DOT");
  @NotNull IElementType DOTDOT = new GdTokenType("DOTDOT");
  @NotNull IElementType ELIF = new GdTokenType("ELIF");
  @NotNull IElementType ELSE = new GdTokenType("ELSE");
  @NotNull IElementType ENUM = new GdTokenType("ENUM");
  @NotNull IElementType EQ = new GdTokenType("EQ");
  @NotNull IElementType EXTENDS = new GdTokenType("EXTENDS");
  @NotNull IElementType FALSE = new GdTokenType("FALSE");
  @NotNull IElementType FOR = new GdTokenType("FOR");
  @NotNull IElementType FUNC = new GdTokenType("FUNC");
  @NotNull IElementType GET = new GdTokenType("GET");
  @NotNull IElementType IDENTIFIER = new GdTokenType("IDENTIFIER");
  @NotNull IElementType IF = new GdTokenType("IF");
  @NotNull IElementType IN = new GdTokenType("IN");
  @NotNull IElementType INDENT = new GdTokenType("INDENT");
  @NotNull IElementType INF = new GdTokenType("INF");
  @NotNull IElementType IS = new GdTokenType("IS");
  @NotNull IElementType LBSHIFT = new GdTokenType("LBSHIFT");
  @NotNull IElementType LCBR = new GdTokenType("LCBR");
  @NotNull IElementType LRBR = new GdTokenType("LRBR");
  @NotNull IElementType LSBR = new GdTokenType("LSBR");
  @NotNull IElementType MASTER = new GdTokenType("MASTER");
  @NotNull IElementType MATCH = new GdTokenType("MATCH");
  @NotNull IElementType MINUS = new GdTokenType("MINUS");
  @NotNull IElementType MMINUS = new GdTokenType("MMINUS");
  @NotNull IElementType MOD = new GdTokenType("MOD");
  @NotNull IElementType MUL = new GdTokenType("MUL");
  @NotNull IElementType NAN = new GdTokenType("NAN");
  @NotNull IElementType NEGATE = new GdTokenType("NEGATE");
  @NotNull IElementType NEW_LINE = new GdTokenType("NEW_LINE");
  @NotNull IElementType NODE_PATH_LEX = new GdTokenType("NODE_PATH_LEX");
  @NotNull IElementType NODE_PATH_LIT = new GdTokenType("NODE_PATH_LIT");
  @NotNull IElementType NOT = new GdTokenType("NOT");
  @NotNull IElementType NULL = new GdTokenType("NULL");
  @NotNull IElementType NUMBER = new GdTokenType("NUMBER");
  @NotNull IElementType OR = new GdTokenType("OR");
  @NotNull IElementType OROR = new GdTokenType("OROR");
  @NotNull IElementType PASS = new GdTokenType("PASS");
  @NotNull IElementType PLUS = new GdTokenType("PLUS");
  @NotNull IElementType POWER = new GdTokenType("POWER");
  @NotNull IElementType PPLUS = new GdTokenType("PPLUS");
  @NotNull IElementType PUPPET = new GdTokenType("PUPPET");
  @NotNull IElementType RBSHIFT = new GdTokenType("RBSHIFT");
  @NotNull IElementType RCBR = new GdTokenType("RCBR");
  @NotNull IElementType REMOET = new GdTokenType("REMOET");
  @NotNull IElementType REMOTE = new GdTokenType("REMOTE");
  @NotNull IElementType REMOTESYNC = new GdTokenType("REMOTESYNC");
  @NotNull IElementType MASTERSYNC = new GdTokenType("MASTERSYNC");
  @NotNull IElementType PUPPETSYNC = new GdTokenType("PUPPETSYNC");
  @NotNull IElementType RET = new GdTokenType("RET");
  @NotNull IElementType RETURN = new GdTokenType("RETURN");
  @NotNull IElementType RRBR = new GdTokenType("RRBR");
  @NotNull IElementType RSBR = new GdTokenType("RSBR");
  @NotNull IElementType SELF = new GdTokenType("SELF");
  @NotNull IElementType SEMICON = new GdTokenType("SEMICON");
  @NotNull IElementType SET = new GdTokenType("SET");
  @NotNull IElementType SIGNAL = new GdTokenType("SIGNAL");
  @NotNull IElementType STATIC = new GdTokenType("STATIC");
  @NotNull IElementType STRING = new GdTokenType("STRING");
  @NotNull IElementType STRING_NAME = new GdTokenType("STRING_NAME");
  @NotNull IElementType SUPER = new GdTokenType("SUPER");
  @NotNull IElementType TEST_OPERATOR = new GdTokenType("TEST_OPERATOR");
  @NotNull IElementType TRUE = new GdTokenType("TRUE");
  @NotNull IElementType UNDER = new GdTokenType("UNDER");
  @NotNull IElementType VAR = new GdTokenType("VAR");
  @NotNull IElementType VARARG = new GdTokenType("VARARG");
  @NotNull IElementType VOID = new GdTokenType("VOID");
  @NotNull IElementType WHEN = new GdTokenType("WHEN");
  @NotNull IElementType WHILE = new GdTokenType("WHILE");
  @NotNull IElementType XOR = new GdTokenType("XOR");
    @NotNull IElementType DOLLAR = new GdTokenType("DOLLAR");
    @NotNull IElementType QUESTION_MARK = new GdTokenType("QUESTION_MARK");
    @NotNull IElementType BACKTICK = new GdTokenType("BACKTICK");
    @NotNull IElementType DOTDOTDOT = new GdTokenType("DOTDOTDOT");

    class Factory {
    public static PsiElement createElement(ASTNode node) {
      IElementType type = node.getElementType();
      if (type == ANNOTATION_PARAMS) {
        return new GdAnnotationParamsImpl(node);
      }
      else if (type == ANNOTATION_TL) {
        return new GdAnnotationTlImpl(node);
      }
      else if (type == ANNOTATION_STMT) {
        return new GdAnnotationStmtImpl(node);
      }
      else if (type == ANNOTATION_TYPE) {
        return new GdAnnotationTypeImpl(node);
      }
      else if (type == ARG_EXPR) {
        return new GdArgExprImpl(node);
      }
      else if (type == ARG_LIST) {
        return new GdArgListImpl(node);
      }
      else if (type == ARRAY_DECL) {
        return new GdArrayDeclImpl(node);
      }
      else if (type == ARRAY_PATTERN) {
        return new GdArrayPatternImpl(node);
      }
      else if (type == ARR_EX) {
        return new GdArrExImpl(node);
      }
      else if (type == ASSIGN_SIGN) {
        return new GdAssignSignImpl(node);
      }
      else if (type == ASSIGN_ST) {
        return new GdAssignStImpl(node);
      }
      else if (type == ASSIGN_TYPED) {
        return new GdAssignTypedImpl(node);
      }
      else if (type == ATTRIBUTE_EX) {
        return new GdAttributeExImpl(node);
      }
      else if (type == AWAIT_EX) {
        return new GdAwaitExImpl(node);
      }
      else if (type == BINDING_PATTERN) {
        return new GdBindingPatternImpl(node);
      }
      else if (type == BIT_AND_EX) {
        return new GdBitAndExImpl(node);
      }
      else if (type == BIT_AND_SIGN) {
        return new GdBitAndSignImpl(node);
      }
      else if (type == BIT_NOT_EX) {
        return new GdBitNotExImpl(node);
      }
      else if (type == CALL_EX) {
        return new GdCallExImpl(node);
      }
      else if (type == CAST_EX) {
        return new GdCastExImpl(node);
      }
      else if (type == CLASS_DECL_TL) {
        return new GdClassDeclTlImpl(node);
      }
      else if (type == CLASS_NAME_NMI) {
        return new GdClassNameNmiImpl(node);
      }
      else if (type == CLASS_NAMING) {
        return new GdClassNamingImpl(node);
      }
      else if (type == CLASS_VAR_DECL_TL) {
        return new GdClassVarDeclTlImpl(node);
      }
      else if (type == COMPARISON_EX) {
        return new GdComparisonExImpl(node);
      }
      else if (type == CONST_DECL_ST) {
        return new GdConstDeclStImpl(node);
      }
      else if (type == CONST_DECL_TL) {
        return new GdConstDeclTlImpl(node);
      }
      else if (type == DICT_DECL) {
        return new GdDictDeclImpl(node);
      }
      else if (type == DICT_PATTERN) {
        return new GdDictPatternImpl(node);
      }
      else if (type == ELIF_ST) {
        return new GdElifStImpl(node);
      }
      else if (type == ELSE_ST) {
        return new GdElseStImpl(node);
      }
      else if (type == EMPTY_STMT) {
        return new GdEmptyStmtImpl(node);
      }
      else if (type == END_STMT) {
        return new GdEndStmtImpl(node);
      }
      else if (type == ENUM_DECL_NMI) {
        return new GdEnumDeclNmiImpl(node);
      }
      else if (type == ENUM_DECL_TL) {
        return new GdEnumDeclTlImpl(node);
      }
      else if (type == ENUM_VALUE) {
        return new GdEnumValueImpl(node);
      }
      else if (type == ENUM_VALUE_NMI) {
        return new GdEnumValueNmiImpl(node);
      }
      else if (type == EXPR_ST) {
        return new GdExprStImpl(node);
      }
      else if (type == FACTOR_EX) {
        return new GdFactorExImpl(node);
      }
      else if (type == FACTOR_SIGN) {
        return new GdFactorSignImpl(node);
      }
      else if (type == FLOW_ST) {
        return new GdFlowStImpl(node);
      }
      else if (type == FOR_ST) {
        return new GdForStImpl(node);
      }
      else if (type == FUNC_DECL_EX) {
        return new GdFuncDeclExImpl(node);
      }
      else if (type == FUNC_DECL_ID_NMI) {
        return new GdFuncDeclIdNmiImpl(node);
      }
      else if (type == GET_DECL) {
        return new GdGetDeclImpl(node);
      }
      else if (type == GET_METHOD_ID_NM) {
        return new GdGetMethodIdRefImpl(node);
      }
      else if (type == IF_ST) {
        return new GdIfStImpl(node);
      }
      else if (type == INHERITANCE) {
        return new GdInheritanceImpl(node);
      }
      else if (type == INHERITANCE_ID) {
        return new GdInheritanceIdImpl(node);
      }
      else if (type == INHERITANCE_ID_NM) {
        return new GdInheritanceIdRefImpl(node);
      }
      else if (type == INHERITANCE_SUB_ID_NM) {
        return new GdInheritanceSubIdRefImpl(node);
      }
      else if (type == IN_EX) {
        return new GdInExImpl(node);
      }
      else if (type == IS_EX) {
        return new GdIsExImpl(node);
      }
      else if (type == KEY_NMI) {
        return new GdKeyNmiImpl(node);
      }
      else if (type == KEY_VALUE) {
        return new GdKeyValueImpl(node);
      }
      else if (type == KEY_VALUE_PATTERN) {
        return new GdKeyValuePatternImpl(node);
      }
      else if (type == LITERAL_EX) {
        return new GdLiteralExImpl(node);
      }
      else if (type == LOGIC_EX) {
        return new GdLogicExImpl(node);
      }
      else if (type == MATCH_BLOCK) {
        return new GdMatchBlockImpl(node);
      }
      else if (type == MATCH_ST) {
        return new GdMatchStImpl(node);
      }
      else if (type == METHOD_DECL_TL) {
        return new GdMethodDeclTlImpl(node);
      }
      else if (type == METHOD_ID_NMI) {
        return new GdMethodIdNmiImpl(node);
      }
      else if (type == METHOD_SPECIFIER) {
        return new GdMethodSpecifierImpl(node);
      }
      else if (type == NEGATE_EX) {
        return new GdNegateExImpl(node);
      }
      else if (type == NEW_LINE_END) {
        return new GdNewLineEndImpl(node);
      }
      else if (type == NODE_PATH) {
        return new GdNodePathImpl(node);
      }
      else if (type == OPERATOR) {
        return new GdOperatorImpl(node);
      }
      else if (type == PARAM) {
        return new GdParamImpl(node);
      }
      else if (type == PARAM_LIST) {
        return new GdParamListImpl(node);
      }
      else if (type == PARENTHESIZED_EX) {
          return new GdParenthesizedExImpl(node);
      }
      else if (type == PATTERN) {
        return new GdPatternImpl(node);
      }
      else if (type == PATTERN_LIST) {
        return new GdPatternListImpl(node);
      }
      else if (type == PLUS_EX) {
        return new GdPlusExImpl(node);
      }
      else if (type == PLUS_MINUS_EX) {
        return new GdPlusMinusExImpl(node);
      }
      else if (type == PLUS_MINUS_PRE_EX) {
        return new GdPlusMinusPreExImpl(node);
      }
      else if (type == PRIMARY_EX) {
        return new GdPrimaryExImpl(node);
      }
      else if (type == REF_ID_NM) {
        return new GdRefIdRefImpl(node);
      }
      else if (type == RETURN_HINT) {
        return new GdReturnHintImpl(node);
      }
      else if (type == RETURN_HINT_VAL) {
        return new GdReturnHintValImpl(node);
      }
      else if (type == SETGET_DECL) {
        return new GdSetgetDeclImpl(node);
      }
      else if (type == SET_DECL) {
        return new GdSetDeclImpl(node);
      }
      else if (type == SET_METHOD_ID_NM) {
        return new GdSetMethodIdRefImpl(node);
      }
      else if (type == SHIFT_EX) {
        return new GdShiftExImpl(node);
      }
      else if (type == SIGN) {
        return new GdSignImpl(node);
      }
      else if (type == SIGNAL_DECL_TL) {
        return new GdSignalDeclTlImpl(node);
      }
      else if (type == SIGNAL_ID_NMI) {
        return new GdSignalIdNmiImpl(node);
      }
      else if (type == SIGN_EX) {
        return new GdSignExImpl(node);
      }
      else if (type == STMT_OR_SUITE) {
        return new GdStmtOrSuiteImpl(node);
      }
      else if (type == STRING_VAL_NM) {
        return new GdStringValRefImpl(node);
      }
      else if (type == SUITE) {
        return new GdSuiteImpl(node);
      }
      else if (type == TERNARY_EX) {
        return new GdTernaryExImpl(node);
      }
      else if (type == TOP_LEVEL_DECL) {
        return new GdTopLevelDeclImpl(node);
      }
      else if (type == TYPED) {
        return new GdTypedImpl(node);
      }
      else if (type == TYPED_VAL) {
        return new GdTypedValImpl(node);
      }
      else if (type == TYPE_HINT) {
        return new GdTypeHintImpl(node);
      }
      else if (type == TYPE_HINT_REF) {
        return new GdTypeHintRefImpl(node);
      }
      else if (type == VAR_DECL_ST) {
        return new GdVarDeclStImpl(node);
      }
      else if (type == VAR_NMI) {
        return new GdVarNmiImpl(node);
      }
      else if (type == WHILE_ST) {
        return new GdWhileStImpl(node);
      }
      throw new AssertionError("Unknown element type: " + type);
    }
  }
}
