//package tscn.parser
//
//import com.intellij.testFramework.ParsingTestCase
//import tscn.TscnParserDefinition
//
//class TscnRecoveryParserTest : ParsingTestCase {
//
//    constructor(): super("", "tscn", TscnParserDefinition())
//
//    // TODO as noted within bnf - recover while is fired even when rule is not matched nor pinned - required to rollback unless pinned
//    // 2 possible solutions:
//    //  a) custom Parser - preferred
//    //  b) rewrite bnf to single header rule
//    // fun testHeader() = doTest(true)
//
//    override fun getTestDataPath(): String {
//        return "src/test/kotlin/tscn/parser/recoveryData"
//    }
//
//    override fun skipSpaces(): Boolean {
//        return false
//    }
//
//    override fun includeRanges(): Boolean {
//        return true
//    }
//
//}
