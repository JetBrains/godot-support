package gdscript.settings

import com.intellij.util.xmlb.annotations.Tag

// TODO REMOVE
class GdApplicationState {

    @Tag("sdkPaths")
    var sdkPaths: MutableList<String> = mutableListOf()

}
