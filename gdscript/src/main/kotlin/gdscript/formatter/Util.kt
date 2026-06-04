package gdscript.formatter

import com.intellij.formatting.SpacingBuilder
import com.intellij.lang.ASTNode
import com.intellij.psi.TokenType
import com.intellij.psi.codeStyle.CodeStyleSettings
import com.intellij.psi.codeStyle.CommonCodeStyleSettings
import com.intellij.psi.tree.IElementType
import com.jetbrains.rider.godot.community.gdscript.GdLanguage
import gdscript.formatter.block.GdBlocks
import gdscript.psi.GdTypes

val CodeStyleSettings.gdCommonSettings: CommonCodeStyleSettings
    get() = getCommonSettings(GdLanguage)

val CodeStyleSettings.gdCustomSettings: GdCodeStyleSettings
    get() = getCustomSettings(GdCodeStyleSettings::class.java)

/** Exactly empty lines */
fun SpacingBuilder.RuleBuilder.forcedLines(count: Int, spaces: Int = 0): SpacingBuilder {
    return this.spacing(spaces, spaces, count + 1, false, 0)
}

/** Minimum and maximum empty lines */
fun SpacingBuilder.RuleBuilder.minMaxLines(min: Int, max: Int): SpacingBuilder {
    return this.spacing(0, 0, min + 1, true, max)
}

/** Maximum of empty lines  */
fun SpacingBuilder.RuleBuilder.emptyLines(count: Int): SpacingBuilder {
    return this.spacing(1, 0, 0, true, count)
}

fun IElementType.isWhiteSpace(): Boolean =
    this == TokenType.WHITE_SPACE
        || this in GdBlocks.WHITE_SPACE
        || this == GdTypes.BACKSLASH


fun printAST(node: ASTNode?, indent: Int) {
    var node = node
    while (node != null) {
        for (i in 0..<indent) {
            print(" ")
        }
        println(node.toString() + " " + node.getTextRange())
        printAST(node.getFirstChildNode(), indent + 2)
        node = node.getTreeNext()
    }
}
