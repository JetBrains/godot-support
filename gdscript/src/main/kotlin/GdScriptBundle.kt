import com.intellij.DynamicBundle
import org.jetbrains.annotations.Nls
import org.jetbrains.annotations.PropertyKey

object GdScriptBundle {
    private const val BUNDLE = "messages.gdscript"

    private val INSTANCE: DynamicBundle = DynamicBundle(GdScriptBundle::class.java, BUNDLE)

    @Nls
    fun message(@PropertyKey(resourceBundle = BUNDLE) key: String, vararg params: Any): String {
        return INSTANCE.getMessage(key, *params)
    }
}