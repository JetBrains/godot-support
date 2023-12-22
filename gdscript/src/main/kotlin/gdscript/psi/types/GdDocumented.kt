package gdscript.psi.types

import gdscript.model.GdTutorial

interface GdDocumented {

    fun description(): String
    fun brief(): String
    fun tutorials(): List<GdTutorial>
    fun isDeprecated(): Boolean
    fun isExperimental(): Boolean

}
