package com.jetbrains.godot.gdscript.parser

import com.intellij.testFramework.ParsingTestCase
import com.jetbrains.godot.getBaseTestDataPath
import gdscript.GdParserDefinition
import org.junit.Ignore
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import kotlin.io.path.pathString

@RunWith(JUnit4::class)
class GdGodotTest : ParsingTestCase("", "gd", GdParserDefinition()) {
    @Ignore("RIDER-126458") @Test fun testadvanced_expression_matching() = doTest(true, true)
    @Test fun testallowed_keywords_as_identifiers() = doTest(true, true)
    @Test fun testallow_id_similar_to_keyword_in_ascii() = doTest(true, true)
    @Ignore("RIDER-126458") @Test fun testallow_strings_as_comments() = doTest(true, true)
    @Ignore("RIDER-126458") @Test fun testannotations() = doTest(true, true)
    @Test fun testarray() = doTest(true, true)
    @Test fun testarrays_dictionaries_nested_const() = doTest(true, true)
    @Test fun testbasic_expression_matching() = doTest(true, true)
    @Ignore("RIDER-126458") @Test fun testbitwise_operators() = doTest(true, true)
    @Test fun testclass() = doTest(true, true)
    @Test fun testclass_inheritance() = doTest(true, true)
    @Test fun testclass_inheritance_access() = doTest(true, true)
    @Test fun testclass_name() = doTest(true, true)
    @Test fun testconcatenation() = doTest(true, true)
    @Test fun testconstants() = doTest(true, true)
    @Test fun testcontinuation_lines_comments() = doTest(true, true)
    @Test fun testdictionary() = doTest(true, true)
    @Test fun testdictionary_lua_style() = doTest(true, true)
    @Test fun testdictionary_mixed_syntax() = doTest(true, true)
    @Ignore("RIDER-126458") @Test fun testdollar_and_percent_get_node() = doTest(true, true)
    @Test fun testdollar_node_paths() = doTest(true, true)
    @Test fun testenum() = doTest(true, true)
    @Test fun testexport_arrays() = doTest(true, true)
    @Test fun testexport_enum() = doTest(true, true)
    @Test fun testexport_variable() = doTest(true, true)
    // RIDER-127009
    @Test fun testexport_variable_negative() = doTest(true, true)
    @Test fun testexport_variable_global() = doTest(true, true)
    @Test fun testexport_variable_unnamed() = doTest(true, true)
    @Ignore("RIDER-126458") @Test fun testfloat_notation() = doTest(true, true)
    @Test fun testfor_range() = doTest(true, true)
    @Test fun testfunction_default_parameter_type_inference() = doTest(true, true)
    @Test fun testfunction_many_parameters() = doTest(true, true)
    @Ignore("RIDER-126458") @Test fun testgood_continue_in_lambda() = doTest(true, true)
    @Test fun testif_after_lambda() = doTest(true, true)
    @Test fun testin() = doTest(true, true)
    @Ignore("RIDER-126458") @Test fun testis_not_operator() = doTest(true, true)
    @Test fun testlambda_callable() = doTest(true, true)
    @Test fun testlambda_callable_multiline() = doTest(true, true)
    @Test fun testcallableInCtor() = doTest(true, true)
    @Test fun testlambda_capture_callable() = doTest(true, true)
    @Test fun testlambda_default_parameter_capture() = doTest(true, true)
    @Ignore("RIDER-126458") @Test fun testlambda_ends_with_new_line() = doTest(true, true)
    @Ignore("RIDER-126458") @Test fun testlambda_named_callable() = doTest(true, true)
    @Ignore("RIDER-126458") @Test fun testmatch() = doTest(true, true)
    @Ignore("RIDER-126458") @Test fun testmatch_array() = doTest(true, true)
    @Test fun testmatch_bind_unused() = doTest(true, true)
    @Ignore("RIDER-126458") @Test fun testmatch_dictionary() = doTest(true, true)
    @Test fun testmatch_multiple_patterns_with_array() = doTest(true, true)
    @Test fun testmatch_multiple_variable_binds_in_pattern() = doTest(true, true)
    @Test fun testmatch_with_variables() = doTest(true, true)
    @Test fun testmixed_indentation_on_blank_lines() = doTest(true, true)
    @Test fun testmultiline_arrays() = doTest(true, true)
    @Test fun testmultiline_assert() = doTest(true, true)
    @Test fun testmultiline_dictionaries() = doTest(true, true)
    @Test fun testmultiline_if() = doTest(true, true)
    @Ignore("RIDER-126458") @Test fun testmultiline_strings() = doTest(true, true)
    @Test fun testmultiline_vector() = doTest(true, true)
    @Ignore("RIDER-126458") @Test fun testnested_arithmetic() = doTest(true, true)
    @Test fun testnested_array() = doTest(true, true)
    @Test fun testnested_dictionary() = doTest(true, true)
    @Test fun testnested_function_calls() = doTest(true, true)
    @Test fun testnested_if() = doTest(true, true)
    @Test fun testnested_match() = doTest(true, true)
    @Test fun testnested_parentheses() = doTest(true, true)
    @Ignore("RIDER-126458") @Test fun testnumber_literals_with_sign() = doTest(true, true)
    @Ignore("RIDER-126458") @Test fun testnumber_separators() = doTest(true, true)
    @Test fun testoperator_assign() = doTest(true, true)
    @Ignore("RIDER-126458") @Test fun testproperty_setter_getter() = doTest(true, true)
    @Ignore("RIDER-126458") @Test fun testreserved_keywords_as_attribute() = doTest(true, true)
    @Ignore("RIDER-126458") @Test fun testr_strings() = doTest(true, true)
    @Ignore("RIDER-126458") @Test fun testsemicolon_as_end_statement() = doTest(true, true)
    @Ignore("RIDER-126458") @Test fun testsemicolon_as_terminator() = doTest(true, true)
    @Test fun testsignal_declaration() = doTest(true, true)
    @Ignore("RIDER-126458") @Test fun testsingle_line_declaration() = doTest(true, true)
    @Test fun testspace_indentation() = doTest(true, true)
    @Ignore("RIDER-126458") @Test fun teststatic_typing() = doTest(true, true)
    @Test fun teststring_formatting() = doTest(true, true)
    @Test fun teststr_preserves_case() = doTest(true, true)
    @Test fun testsuper() = doTest(true, true)
    @Test fun testtrailing_comma_in_function_args() = doTest(true, true)
    @Test fun testtruthiness() = doTest(true, true)
    @Test fun testnot_in_array() = doTest(true, true)
    @Test fun testtyped_arrays() = doTest(true, true)
    @Ignore("RIDER-126458") @Test fun testunicode_identifiers() = doTest(true, true)
    @Test fun testunnamed_enums_outer_conflicts() = doTest(true, true)
    @Test fun testvariable_declaration() = doTest(true, true)
    @Ignore("RIDER-126458") @Test fun testvector_inf() = doTest(true, true)
    @Test fun testwhile() = doTest(true, true)

    @Test fun testsignal_connect_func() = doTest(true, false)

    override fun getTestDataPath(): String {
        return getBaseTestDataPath().resolve("testData/gdscript/parser/godotTestCases").pathString
    }

    override fun skipSpaces(): Boolean = false

    override fun includeRanges(): Boolean = true

}
