package gdscript.run

import com.intellij.execution.configurations.LocatableRunConfigurationOptions
import com.intellij.util.xmlb.annotations.Property

class GdRunConfigurationOptions : LocatableRunConfigurationOptions {

    constructor() : super() {
        isNameGenerated = true
    }

    @get:Property
    var godotExe by string()

    @get:Property
    var tscn by string()

    @get:Property
    var debugShapes by property(false)

    @get:Property
    var debugPaths by property(false)

    @get:Property
    var arguments by string()

}
