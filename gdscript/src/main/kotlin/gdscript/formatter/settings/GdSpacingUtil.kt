package gdscript.formatter.settings

import com.intellij.formatting.SpacingBuilder
import com.intellij.formatting.SpacingBuilder.RuleBuilder

object GdSpacingUtil {

    fun RuleBuilder.forcedLines(count: Int, spaces: Int = 0): SpacingBuilder {
        return this.spacing(spaces, spaces, count, false, 0)
    }

}
