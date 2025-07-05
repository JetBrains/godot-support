// This is a generated file. Not intended for manual editing.
package gdscript.psi;

import com.intellij.psi.tree.IElementType;
import com.intellij.psi.PsiElement;
import com.intellij.lang.ASTNode;
import gdscript.psi.impl.GdClassDeclElementType;
import gdscript.psi.impl.GdClassIdElementType;
import gdscript.psi.impl.GdClassNamingElementType;
import gdscript.psi.impl.GdClassVarDeclElementType;
import gdscript.psi.impl.GdConstDeclElementType;
import gdscript.psi.impl.GdEnumDeclElementType;
import gdscript.psi.impl.GdInheritanceElementType;
import gdscript.psi.impl.GdMethodDeclElementType;
import gdscript.psi.impl.GdSignalDeclElementType;
import gdscript.psi.impl.*;

public interface GdTypes {

  IElementType FILE = new GdElementType("FILE");
  IElementType ANNOTATION_PARAMS = new GdElementType("ANNOTATION_PARAMS");
  IElementType ANNOTATION_TL = new GdElementType("ANNOTATION_TL");
  IElementType ANNOTATION_STMT = new GdElementType("ANNOTATION_STMT");
  IElementType ANNOTATION_TYPE = new GdElementType("ANNOTATION_TYPE");
  IElementType ARG_EXPR = new GdElementType("ARG_EXPR");
  IElementType ARG_LIST = new GdElementType("ARG_LIST");
  IElementType ARRAY_DECL = new GdElementType("ARRAY_DECL");
  IElementType ARRAY_PATTERN = new GdElementType("ARRAY_PATTERN");
  IElementType ARR_EX = new GdElementType("ARR_EX");
  IElementType ASSIGN_SIGN = new GdElementType("ASSIGN_SIGN");
  IElementType ASSIGN_ST = new GdElementType("ASSIGN_ST");
  IElementType ASSIGN_TYPED = new GdElementType("ASSIGN_TYPED");
  IElementType ATTRIBUTE_EX = new GdElementType("ATTRIBUTE_EX");
  IElementType AWAIT_EX = new GdElementType("AWAIT_EX");
  IElementType BINDING_PATTERN = new GdElementType("BINDING_PATTERN");
  IElementType BIT_AND_EX = new GdElementType("BIT_AND_EX");
  IElementType BIT_AND_SIGN = new GdElementType("BIT_AND_SIGN");
  IElementType BIT_NOT_EX = new GdElementType("BIT_NOT_EX");
  IElementType CALL_EX = new GdElementType("CALL_EX");
  IElementType CAST_EX = new GdElementType("CAST_EX");
  IElementType CLASS_DECL_TL = GdClassDeclElementType.getInstance("CLASS_DECL_TL");
  IElementType CLASS_NAME_NMI = GdClassIdElementType.getInstance("CLASS_NAME_NMI");
  IElementType CLASS_NAMING = GdClassNamingElementType.getInstance("CLASS_NAMING");
  IElementType CLASS_VAR_DECL_TL = GdClassVarDeclElementType.getInstance("CLASS_VAR_DECL_TL");
  IElementType COMPARISON_EX = new GdElementType("COMPARISON_EX");
  IElementType CONST_DECL_ST = new GdElementType("CONST_DECL_ST");
  IElementType CONST_DECL_TL = GdConstDeclElementType.getInstance("CONST_DECL_TL");
  IElementType DICT_DECL = new GdElementType("DICT_DECL");
  IElementType DICT_PATTERN = new GdElementType("DICT_PATTERN");
  IElementType ELIF_ST = new GdElementType("ELIF_ST");
  IElementType ELSE_ST = new GdElementType("ELSE_ST");
  IElementType END_STMT = new GdElementType("END_STMT");
  IElementType ENUM_DECL_NMI = new GdElementType("ENUM_DECL_NMI");
  IElementType ENUM_DECL_TL = GdEnumDeclElementType.getInstance("ENUM_DECL_TL");
  IElementType ENUM_VALUE = new GdElementType("ENUM_VALUE");
  IElementType ENUM_VALUE_NMI = new GdElementType("ENUM_VALUE_NMI");
  IElementType EXPR = new GdElementType("EXPR");
  IElementType EXPR_ST = new GdElementType("EXPR_ST");
  IElementType FACTOR_EX = new GdElementType("FACTOR_EX");
  IElementType FACTOR_SIGN = new GdElementType("FACTOR_SIGN");
  IElementType FLOW_ST = new GdElementType("FLOW_ST");
  IElementType FOR_ST = new GdElementType("FOR_ST");
  IElementType FUNC_DECL_EX = new GdElementType("FUNC_DECL_EX");
  IElementType FUNC_DECL_ID_NMI = new GdElementType("FUNC_DECL_ID_NMI");
  IElementType GET_DECL = new GdElementType("GET_DECL");
  IElementType GET_METHOD_ID_NM = new GdElementType("GET_METHOD_ID_NM");
  IElementType IF_ST = new GdElementType("IF_ST");
  IElementType INHERITANCE = GdInheritanceElementType.getInstance("INHERITANCE");
  IElementType INHERITANCE_ID = new GdElementType("INHERITANCE_ID");
  IElementType INHERITANCE_ID_NM = new GdElementType("INHERITANCE_ID_NM");
  IElementType INHERITANCE_SUB_ID_NM = new GdElementType("INHERITANCE_SUB_ID_NM");
  IElementType IN_EX = new GdElementType("IN_EX");
  IElementType IS_EX = new GdElementType("IS_EX");
  IElementType KEY_VALUE = new GdElementType("KEY_VALUE");
  IElementType KEY_VALUE_PATTERN = new GdElementType("KEY_VALUE_PATTERN");
  IElementType LITERAL_EX = new GdElementType("LITERAL_EX");
  IElementType LOGIC_EX = new GdElementType("LOGIC_EX");
  IElementType MATCH_BLOCK = new GdElementType("MATCH_BLOCK");
  IElementType MATCH_ST = new GdElementType("MATCH_ST");
  IElementType METHOD_DECL_TL = GdMethodDeclElementType.getInstance("METHOD_DECL_TL");
  IElementType METHOD_ID_NMI = new GdElementType("METHOD_ID_NMI");
  IElementType METHOD_SPECIFIER = new GdElementType("METHOD_SPECIFIER");
  IElementType NEGATE_EX = new GdElementType("NEGATE_EX");
  IElementType NEW_LINE_END = new GdElementType("NEW_LINE_END");
  IElementType NODE_PATH = new GdElementType("NODE_PATH");
  IElementType OPERATOR = new GdElementType("OPERATOR");
  IElementType PARAM = new GdElementType("PARAM");
  IElementType PARAM_LIST = new GdElementType("PARAM_LIST");
  IElementType PATTERN = new GdElementType("PATTERN");
  IElementType PATTERN_LIST = new GdElementType("PATTERN_LIST");
  IElementType PLUS_EX = new GdElementType("PLUS_EX");
  IElementType PLUS_MINUS_EX = new GdElementType("PLUS_MINUS_EX");
  IElementType PLUS_MINUS_PRE_EX = new GdElementType("PLUS_MINUS_PRE_EX");
  IElementType PRIMARY_EX = new GdElementType("PRIMARY_EX");
  IElementType REF_ID_NM = new GdElementType("REF_ID_NM");
  IElementType RETURN_HINT = new GdElementType("RETURN_HINT");
  IElementType RETURN_HINT_VAL = new GdElementType("RETURN_HINT_VAL");
  IElementType SETGET_DECL = new GdElementType("SETGET_DECL");
  IElementType SET_DECL = new GdElementType("SET_DECL");
  IElementType SET_METHOD_ID_NM = new GdElementType("SET_METHOD_ID_NM");
  IElementType SHIFT_EX = new GdElementType("SHIFT_EX");
  IElementType SIGN = new GdElementType("SIGN");
  IElementType SIGNAL_DECL_TL = GdSignalDeclElementType.getInstance("SIGNAL_DECL_TL");
  IElementType SIGNAL_ID_NMI = new GdElementType("SIGNAL_ID_NMI");
  IElementType SIGN_EX = new GdElementType("SIGN_EX");
  IElementType STMT = new GdElementType("STMT");
  IElementType STMT_OR_SUITE = new GdElementType("STMT_OR_SUITE");
  IElementType STRING_VAL_NM = new GdElementType("STRING_VAL_NM");
  IElementType SUITE = new GdElementType("SUITE");
  IElementType TERNARY_EX = new GdElementType("TERNARY_EX");
  IElementType TOP_LEVEL_DECL = new GdElementType("TOP_LEVEL_DECL");
  IElementType TYPED = new GdElementType("TYPED");
  IElementType TYPED_VAL = new GdElementType("TYPED_VAL");
  IElementType TYPE_HINT = new GdElementType("TYPE_HINT");
  IElementType TYPE_HINT_REF = new GdElementType("TYPE_HINT_NM");
  IElementType VAR_DECL_ST = new GdElementType("VAR_DECL_ST");
  IElementType VAR_NMI = new GdElementType("VAR_NMI");
  IElementType WHILE_ST = new GdElementType("WHILE_ST");

  IElementType AND = new GdTokenType("AND");
  IElementType ANDAND = new GdTokenType("ANDAND");
  IElementType ANNOTATOR = new GdTokenType("ANNOTATOR");
  IElementType AS = new GdTokenType("AS");
  IElementType ASSET = new GdTokenType("ASSET");
  IElementType ASSIGN = new GdTokenType("ASSIGN");
  IElementType AWAIT = new GdTokenType("AWAIT");
  IElementType BACKSLASH = new GdTokenType("\\");
  IElementType BAD_CHARACTER = new GdTokenType("BAD_CHARACTER");
  IElementType BREAK = new GdTokenType("BREAK");
  IElementType BREAKPOINT = new GdTokenType("BREAKPOINT");
  IElementType CEQ = new GdTokenType("CEQ");
  IElementType CLASS = new GdTokenType("CLASS");
  IElementType CLASS_NAME = new GdTokenType("CLASS_NAME");
  IElementType COLON = new GdTokenType("COLON");
  IElementType COMMA = new GdTokenType("COMMA");
  IElementType COMMENT = new GdTokenType("comment");
  IElementType CONST = new GdTokenType("CONST");
  IElementType CONTINUE = new GdTokenType("CONTINUE");
  IElementType DEDENT = new GdTokenType("DEDENT");
  IElementType DIV = new GdTokenType("DIV");
  IElementType DOT = new GdTokenType("DOT");
  IElementType DOTDOT = new GdTokenType("DOTDOT");
  IElementType ELIF = new GdTokenType("ELIF");
  IElementType ELSE = new GdTokenType("ELSE");
  IElementType ENUM = new GdTokenType("ENUM");
  IElementType EQ = new GdTokenType("EQ");
  IElementType EXTENDS = new GdTokenType("EXTENDS");
  IElementType FALSE = new GdTokenType("FALSE");
  IElementType FOR = new GdTokenType("FOR");
  IElementType FUNC = new GdTokenType("FUNC");
  IElementType GET = new GdTokenType("GET");
  IElementType IDENTIFIER = new GdTokenType("IDENTIFIER");
  IElementType IF = new GdTokenType("IF");
  IElementType IN = new GdTokenType("IN");
  IElementType INDENT = new GdTokenType("INDENT");
  IElementType INF = new GdTokenType("INF");
  IElementType IS = new GdTokenType("IS");
  IElementType LBSHIFT = new GdTokenType("LBSHIFT");
  IElementType LCBR = new GdTokenType("LCBR");
  IElementType LRBR = new GdTokenType("LRBR");
  IElementType LSBR = new GdTokenType("LSBR");
  IElementType MASTER = new GdTokenType("MASTER");
  IElementType MATCH = new GdTokenType("MATCH");
  IElementType MINUS = new GdTokenType("MINUS");
  IElementType MMINUS = new GdTokenType("MMINUS");
  IElementType MOD = new GdTokenType("MOD");
  IElementType MUL = new GdTokenType("MUL");
  IElementType NAN = new GdTokenType("NAN");
  IElementType NEGATE = new GdTokenType("NEGATE");
  IElementType NEW_LINE = new GdTokenType("NEW_LINE");
  IElementType NODE_PATH_LEX = new GdTokenType("NODE_PATH_LEX");
  IElementType NODE_PATH_LIT = new GdTokenType("NODE_PATH_LIT");
  IElementType NOT = new GdTokenType("NOT");
  IElementType NULL = new GdTokenType("NULL");
  IElementType NUMBER = new GdTokenType("NUMBER");
  IElementType OR = new GdTokenType("OR");
  IElementType OROR = new GdTokenType("OROR");
  IElementType PASS = new GdTokenType("PASS");
  IElementType PLUS = new GdTokenType("PLUS");
  IElementType POWER = new GdTokenType("POWER");
  IElementType PPLUS = new GdTokenType("PPLUS");
  IElementType PUPPET = new GdTokenType("PUPPET");
  IElementType RBSHIFT = new GdTokenType("RBSHIFT");
  IElementType RCBR = new GdTokenType("RCBR");
  IElementType REMOET = new GdTokenType("REMOET");
  IElementType REMOTE = new GdTokenType("REMOTE");
  IElementType REMOTESYNC = new GdTokenType("REMOTESYNC");
  IElementType MASTERSYNC = new GdTokenType("MASTERSYNC");
  IElementType PUPPETSYNC = new GdTokenType("PUPPETSYNC");
  IElementType RET = new GdTokenType("RET");
  IElementType RETURN = new GdTokenType("RETURN");
  IElementType RRBR = new GdTokenType("RRBR");
  IElementType RSBR = new GdTokenType("RSBR");
  IElementType SELF = new GdTokenType("SELF");
  IElementType SEMICON = new GdTokenType("SEMICON");
  IElementType SET = new GdTokenType("SET");
  IElementType SIGNAL = new GdTokenType("SIGNAL");
  IElementType STATIC = new GdTokenType("STATIC");
  IElementType STRING = new GdTokenType("STRING");
  IElementType STRING_NAME = new GdTokenType("STRING_NAME");
  IElementType SUPER = new GdTokenType("SUPER");
  IElementType TEST_OPERATOR = new GdTokenType("TEST_OPERATOR");
  IElementType TRUE = new GdTokenType("TRUE");
  IElementType UNDER = new GdTokenType("UNDER");
  IElementType VAR = new GdTokenType("VAR");
  IElementType VARARG = new GdTokenType("VARARG");
  IElementType VOID = new GdTokenType("VOID");
  IElementType WHILE = new GdTokenType("WHILE");
  IElementType XOR = new GdTokenType("XOR");

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
