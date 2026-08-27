package gdscript.library

import com.intellij.openapi.util.JDOMUtil
import gdscript.GdKeywords
import org.jdom.Document
import org.jdom.Element
import org.jetbrains.annotations.ApiStatus

/**
 * Renders the GDExtension singletons reported by `gdscript/scripts/dump_singletons.gd` into a synthetic SDK doc file
 * shaped exactly like Godot's own `@GlobalScope.xml`.
 *
 * Every member of `@GlobalScope.xml` is self-typed (`<member name="Input" type="Input">`), i.e. that file is nothing but
 * the engine's singleton list expressed as global variables. Generating the same shape for GDExtension singletons makes
 * them travel the very same path (global property -> its type -> the class symbol), so nothing in the symbol or return
 * type inference layers has to know about singletons at all.
 *
 * Engine singletons are deliberately not emitted: they are already in `@GlobalScope.xml`, and a second declaration would
 * only produce duplicate symbols.
 */
@ApiStatus.Internal
object GdGlobalSingletonsDocWriter {

    const val FILE_NAME: String = "${GdKeywords.GDEXTENSION_SCOPE}.xml"

    private const val MEMBER_DESCRIPTION = "Singleton registered by a GDExtension."

    fun buildGdExtensionScopeXml(singletons: List<GdSingletonInfo>): String {
        val members = singletons.filter { it.isFromGdExtension }.sortedBy { it.name }

        val classElement = Element("class").apply {
            setAttribute("name", GdKeywords.GDEXTENSION_SCOPE)
            addContent(Element("members").apply {
                members.forEach { singleton ->
                    addContent(Element("member").apply {
                        setAttribute("name", singleton.name)
                        setAttribute("type", singleton.className)
                        setAttribute("setter", "")
                        setAttribute("getter", "")
                        text = MEMBER_DESCRIPTION
                    })
                }
            })
        }

        // Outputting a Document rather than an Element also emits the XML declaration. The dump is external input, but it
        // needs no manual escaping either: the outputter escapes attribute and text content.
        return JDOMUtil.writeDocument(Document(classElement))
    }
}
