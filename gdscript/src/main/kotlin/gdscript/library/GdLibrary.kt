package gdscript.library

import com.intellij.util.xmlb.annotations.Tag

class GdLibrary {
    @Tag("path")
    var path: String = ""

    @Tag("version")
    var version: String = ""
}
