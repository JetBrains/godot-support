package com.jetbrains.godot.gdscript.parser

import com.intellij.platform.testFramework.junit5.codeInsight.fixture.parsingFixture
import com.intellij.testFramework.junit5.TestApplication
import com.intellij.testFramework.junit5.fixture.projectFixture
import com.jetbrains.godot.getBaseTestDataPath
import com.jetbrains.rider.test.annotations.Subsystem
import com.jetbrains.rider.test.annotations.report.ChecklistItems
import com.jetbrains.rider.test.reporting.SubsystemConstants
import gdscript.GdParserDefinition
import org.junit.jupiter.api.Disabled
import org.junit.jupiter.api.Test
import kotlin.io.path.pathString

@Subsystem(SubsystemConstants.GODOT)
@ChecklistItems(["GDScript/Lexer and parser"])
@TestApplication
class GdGodotTest {
    private val projectFixture = projectFixture()
    private val parser by parsingFixture(
        projectFixture,
        getBaseTestDataPath(GdGodotTest::class.java).resolve("testData/gdscript/parser/godotTestCases").pathString,
        "gd",
        false,
        GdParserDefinition(),
    )

    @Disabled("RIDER-126458") @Test fun testadvanced_expression_matching() = parser.doTest(true, true)
    @Test fun testallowed_keywords_as_identifiers() = parser.doTest(true, true)
    @Test fun testallow_id_similar_to_keyword_in_ascii() = parser.doTest(true, true)
    @Test fun testallow_strings_as_comments() = parser.doTest(true, true)
    @Disabled("RIDER-126458") @Test fun testannotations() = parser.doTest(true, true)
    @Test fun testarray() = parser.doTest(true, true)
    @Test fun testarrays_dictionaries_nested_const() = parser.doTest(true, true)
    @Test fun testbasic_expression_matching() = parser.doTest(true, true)
    @Test fun testbitwise_operators() = parser.doTest(true, true)
    @Test fun testclass() = parser.doTest(true, true)
    @Test fun testclass_inheritance() = parser.doTest(true, true)
    @Test fun testclass_inheritance_access() = parser.doTest(true, true)
    @Test fun testclass_name() = parser.doTest(true, true)
    @Test fun testconcatenation() = parser.doTest(true, true)
    @Test fun testconstants() = parser.doTest(true, true)
    @Test fun testcontinuation_lines_comments() = parser.doTest(true, true)
    @Test fun testdictionary() = parser.doTest(true, true)
    @Test fun testdictionary_lua_style() = parser.doTest(true, true)
    @Test fun testdictionary_mixed_syntax() = parser.doTest(true, true)
    @Disabled("RIDER-126458") @Test fun testdollar_and_percent_get_node() = parser.doTest(true, true)
    @Test fun testdollar_node_paths() = parser.doTest(true, true)
    @Test fun testenum() = parser.doTest(true, true)
    @Test fun testexport_arrays() = parser.doTest(true, true)
    @Test fun testexport_enum() = parser.doTest(true, true)
    @Test fun testexport_variable() = parser.doTest(true, true)
    // RIDER-127009
    @Test fun testexport_variable_negative() = parser.doTest(true, true)
    @Test fun testexport_variable_global() = parser.doTest(true, true)
    @Test fun testexport_variable_unnamed() = parser.doTest(true, true)
    @Test fun testfloat_notation() = parser.doTest(true, true)
    @Test fun testfor_range() = parser.doTest(true, true)
    @Test fun testfunction_default_parameter_type_inference() = parser.doTest(true, true)
    @Test fun testfunction_many_parameters() = parser.doTest(true, true)
    @Test fun testgood_continue_in_lambda() = parser.doTest(true, true)
    @Test fun testif_after_lambda() = parser.doTest(true, true)
    @Test fun testin() = parser.doTest(true, true)
    @Test fun testis_not_operator() = parser.doTest(true, true)
    @Test fun testlambda_callable() = parser.doTest(true, true)
    @Test fun testlambda_callable_multiline() = parser.doTest(true, true)
    @Test fun testcallableInCtor() = parser.doTest(true, true)
    @Test fun testlambda_capture_callable() = parser.doTest(true, true)
    @Test fun testlambda_default_parameter_capture() = parser.doTest(true, true)
    @Test fun testlambda_ends_with_new_line() = parser.doTest(true, true)
    @Test fun testlambda_named_callable() = parser.doTest(true, true)
    @Test fun testlambdaAsArgument() = parser.doTest(true, true)
    @Test fun testlambdaAsArgumentReturnLastArg() = parser.doTest(true, true)
    @Test fun testlambdaAsArgumentReturnCommaNextLine() = parser.doTest(true, true)
    @Test fun testlambdaAsArgumentReturnMultiStmt() = parser.doTest(true, true)
    @Test fun testlambdaAsArgumentReturnNestedIf() = parser.doTest(true, true)
    @Test fun testlambdaAsArgumentNestedIfElse() = parser.doTest(true, true)
    @Disabled("RIDER-126458") @Test fun testmatch() = parser.doTest(true, true)
    @Disabled("RIDER-126458") @Test fun testmatch_array() = parser.doTest(true, true)
    @Test fun testmatch_bind_unused() = parser.doTest(true, true)
    @Disabled("RIDER-126458") @Test fun testmatch_dictionary() = parser.doTest(true, true)
    @Test fun testmatch_multiple_patterns_with_array() = parser.doTest(true, true)
    @Test fun testmatch_multiple_variable_binds_in_pattern() = parser.doTest(true, true)
    @Test fun testmatch_with_variables() = parser.doTest(true, true)
    @Test fun testmixed_indentation_on_blank_lines() = parser.doTest(true, true)
    @Test fun testmultiline_arrays() = parser.doTest(true, true)
    @Test fun testmultiline_assert() = parser.doTest(true, true)
    @Test fun testmultiline_dictionaries() = parser.doTest(true, true)
    @Test fun testmultiline_if() = parser.doTest(true, true)
    @Test fun testmultiline_strings() = parser.doTest(true, true)
    @Test fun testmultiline_vector() = parser.doTest(true, true)
    @Disabled("RIDER-126458") @Test fun testnested_arithmetic() = parser.doTest(true, true)
    @Test fun testnested_array() = parser.doTest(true, true)
    @Test fun testnested_dictionary() = parser.doTest(true, true)
    @Test fun testnested_function_calls() = parser.doTest(true, true)
    @Test fun testnested_if() = parser.doTest(true, true)
    @Test fun testnested_match() = parser.doTest(true, true)
    @Test fun testnested_parentheses() = parser.doTest(true, true)
    @Disabled("RIDER-126458") @Test fun testnumber_literals_with_sign() = parser.doTest(true, true)
    @Test fun testnumber_separators() = parser.doTest(true, true)
    @Test fun testoperator_assign() = parser.doTest(true, true)
    @Test fun testproperty_setter_getter() = parser.doTest(true, true)
    @Disabled("RIDER-126458") @Test fun testreserved_keywords_as_attribute() = parser.doTest(true, true)
    @Disabled("RIDER-126458") @Test fun testr_strings() = parser.doTest(true, true)
    @Test fun testsemicolon_as_end_statement() = parser.doTest(true, true)
    @Test fun testsemicolon_as_terminator() = parser.doTest(true, true)
    @Test fun testsemicolon_separated_statements_one_line() = parser.doTest(true, true)
    @Test fun testsemicolon_multiple_after_statement_suite() = parser.doTest(true, true)
    @Test fun testsemicolon_multiple_alone_suite() = parser.doTest(true, true)
    @Test fun testsemicolon_multiple_on_one_line() = parser.doTest(true, true)
    @Test fun testsignal_declaration() = parser.doTest(true, true)
    @Disabled("RIDER-126458") @Test fun testsingle_line_declaration() = parser.doTest(true, true)
    @Test fun testspace_indentation() = parser.doTest(true, true)
    @Disabled("RIDER-126458") @Test fun teststatic_typing() = parser.doTest(true, true)
    @Test fun teststring_formatting() = parser.doTest(true, true)
    @Test fun teststr_preserves_case() = parser.doTest(true, true)
    @Test fun testsuper() = parser.doTest(true, true)
    @Test fun testtrailing_comma_in_function_args() = parser.doTest(true, true)
    @Test fun testtruthiness() = parser.doTest(true, true)
    @Test fun testnot_in_array() = parser.doTest(true, true)
    @Test fun testtyped_arrays() = parser.doTest(true, true)
    @Test fun testunicode_identifiers() = parser.doTest(true, true)
    @Test fun testvariable_declaration() = parser.doTest(true, true)
    @Disabled("RIDER-126458") @Test fun testvector_inf() = parser.doTest(true, true)
    @Test fun testwhile() = parser.doTest(true, true)

    @Test fun testsignal_connect_func() = parser.doTest(true, false)
    @Test fun testlambdaCallExpr() = parser.doTest(true, true)

    @Test fun testdocumentation_comments() = parser.doTest(true, true)
    @Test fun teststrings() = parser.doTest(true, true)
}
