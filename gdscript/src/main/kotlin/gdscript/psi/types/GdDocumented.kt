package gdscript.psi.types

import com.intellij.openapi.util.NlsSafe
import gdscript.model.GdTutorial

interface GdDocumented {

    @NlsSafe
    fun description(): String

    @NlsSafe
    fun brief(): String

    fun tutorials(): List<GdTutorial>
    fun isDeprecated(): Boolean
    fun isExperimental(): Boolean

}
