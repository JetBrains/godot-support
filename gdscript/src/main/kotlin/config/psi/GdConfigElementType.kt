package config.psi

import com.intellij.psi.tree.IElementType
import config.GdConfigLanguage
import org.jetbrains.annotations.NonNls

class GdConfigElementType(debugName: @NonNls String) : IElementType(debugName, GdConfigLanguage) {
}
