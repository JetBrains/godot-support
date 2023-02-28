package gdscript.formatter.settings

import com.intellij.formatting.SpacingBuilder
import com.intellij.formatting.SpacingBuilder.RuleBuilder

object GdSpacingUtil {

    fun RuleBuilder.forcedLines(count: Int): SpacingBuilder {
        return this.spacing(0, 0, count, false, 0)
    }

}
