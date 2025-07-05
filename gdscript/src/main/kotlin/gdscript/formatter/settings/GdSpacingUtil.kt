package gdscript.formatter.settings

import com.intellij.formatting.SpacingBuilder
import com.intellij.formatting.SpacingBuilder.RuleBuilder

object GdSpacingUtil {

    fun RuleBuilder.forcedLines(count: Int, spaces: Int = 0): SpacingBuilder {
        return this.spacing(spaces, spaces, count+1, false, 0)
    }

    fun RuleBuilder.minMaxLines(min: Int, max: Int): SpacingBuilder {
        return this.spacing(0, 0, min+1, true, max)
    }

    fun RuleBuilder.emptyLines(count: Int): SpacingBuilder {
        return this.spacing(1, 0, 0, true, count)
    }

}
