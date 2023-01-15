package gdscript.settings

import com.intellij.util.xmlb.annotations.Tag

class GdState {

    @Tag("hidePrivate")
    var hidePrivate = true

    @Tag("sdkPath")
    var sdkPath: String? = ""

}
