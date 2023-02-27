package gdscript.formatter.settings

import com.intellij.formatting.SpacingBuilder
import com.intellij.formatting.SpacingBuilder.RuleBuilder

// TODO remove
object GdSpacingUtil {

    fun RuleBuilder.lines(count: Int): SpacingBuilder {
        return this.spacing(0, 0, count, false, count)
    }

}
