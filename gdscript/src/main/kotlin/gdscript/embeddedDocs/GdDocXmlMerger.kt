package gdscript.embeddedDocs

import com.intellij.openapi.progress.ProgressManager
import org.w3c.dom.Document
import org.w3c.dom.Element
import org.w3c.dom.Node
import org.w3c.dom.NodeList
import org.xml.sax.InputSource
import java.io.StringReader
import java.io.StringWriter
import javax.xml.transform.OutputKeys
import javax.xml.transform.TransformerFactory
import javax.xml.transform.dom.DOMSource
import javax.xml.transform.stream.StreamResult

object GdDocXmlMerger {
    private val proseAttributes = listOf("is_deprecated", "deprecated", "is_experimental", "experimental", "keywords")
    private val namedMemberSections = mapOf(
        "methods" to "method",
        "signals" to "signal",
        "annotations" to "annotation",
        "members" to "member",
        "constants" to "constant",
    )

    /**
     * Uses the doctool XML as the structural target and copies only prose from the extracted XML.
     * Named members match by name. Theme items match by name and data type.
     * Constructors and operators match by name and their ordered parameter types.
     *
     * [mergeNamedMembers] and [mergeSignatureMembers] match member collections.
     * [copyProseAttributes], [copyTaggedDescription], and [copyDirectText] move prose between matched elements.
     * [replaceTutorials] replaces the tutorials subtree.
     */
    fun merge(
        targetXml: String?,
        sourceXml: String,
        checkCanceled: () -> Unit = { ProgressManager.checkCanceled() },
    ): String {
        if (targetXml == null) return sourceXml

        val target = parse(targetXml)
        val source = parse(sourceXml)
        val targetClass = target.documentElement
        val sourceClass = source.documentElement
        if (targetClass.tagName != "class" || sourceClass.tagName != "class" ||
            targetClass.getAttribute("name") != sourceClass.getAttribute("name")
        ) {
            return targetXml
        }

        copyProseAttributes(targetClass, sourceClass, copyKeywords = true)
        copyTaggedDescription(targetClass, sourceClass, "brief_description")
        copyTaggedDescription(targetClass, sourceClass, "description")
        replaceTutorials(target, targetClass, sourceClass)

        for ((sectionName, elementName) in namedMemberSections) {
            mergeNamedMembers(targetClass, sourceClass, sectionName, elementName, checkCanceled = checkCanceled)
        }
        mergeNamedMembers(targetClass, sourceClass, "theme_items", "theme_item", matchDataType = true, checkCanceled = checkCanceled)
        mergeSignatureMembers(targetClass, sourceClass, "constructors", "constructor", checkCanceled)
        mergeSignatureMembers(targetClass, sourceClass, "operators", "operator", checkCanceled)

        return serialize(target)
    }

    private fun parse(xml: String): Document {
        val builder = newHardenedDocumentBuilderFactory().newDocumentBuilder()
        return builder.parse(InputSource(StringReader(xml)))
    }

    private fun mergeNamedMembers(
        targetClass: Element,
        sourceClass: Element,
        sectionName: String,
        elementName: String,
        matchDataType: Boolean = false,
        checkCanceled: () -> Unit,
    ) {
        val sourceMembers = indexFirst(directChildren(directChild(sourceClass, sectionName), elementName)) {
            it.getAttribute("name") to it.getAttribute("data_type").takeIf { matchDataType }
        }
        for (targetMember in directChildren(directChild(targetClass, sectionName), elementName)) {
            checkCanceled()
            val key = targetMember.getAttribute("name") to targetMember.getAttribute("data_type").takeIf { matchDataType }
            val sourceMember = sourceMembers[key] ?: continue
            copyMemberProse(targetMember, sourceMember, copyKeywords = true)
        }
    }

    private fun mergeSignatureMembers(
        targetClass: Element,
        sourceClass: Element,
        sectionName: String,
        elementName: String,
        checkCanceled: () -> Unit,
    ) {
        val sourceMembers = indexFirst(directChildren(directChild(sourceClass, sectionName), elementName), ::signature)
        for (targetMember in directChildren(directChild(targetClass, sectionName), elementName)) {
            checkCanceled()
            val sourceMember = sourceMembers[signature(targetMember)] ?: continue
            copyMemberProse(targetMember, sourceMember, copyKeywords = false)
        }
    }

    private fun <K> indexFirst(elements: List<Element>, key: (Element) -> K): Map<K, Element> =
        LinkedHashMap<K, Element>().apply {
            for (element in elements) putIfAbsent(key(element), element)
        }

    private fun signature(element: Element): Pair<String, List<String>> =
        element.getAttribute("name") to directChildren(element, "param").map { it.getAttribute("type") }

    private fun copyMemberProse(target: Element, source: Element, copyKeywords: Boolean) {
        copyProseAttributes(target, source, copyKeywords)
        if (target.tagName in setOf("member", "constant", "theme_item")) {
            copyDirectText(target, source)
        } else {
            copyTaggedDescription(target, source, "description")
        }
    }

    private fun copyProseAttributes(target: Element, source: Element, copyKeywords: Boolean) {
        for (attribute in proseAttributes) {
            if (!copyKeywords && attribute == "keywords") continue
            target.removeAttribute(attribute)
            if (source.hasAttribute(attribute)) target.setAttribute(attribute, source.getAttribute(attribute))
        }
    }

    private fun copyTaggedDescription(target: Element, source: Element, tagName: String) {
        val targetDescription = directChild(target, tagName) ?: return
        targetDescription.textContent = directChild(source, tagName)?.textContent.orEmpty()
    }

    private fun copyDirectText(target: Element, source: Element) {
        target.textContent = source.textContent
    }

    private fun replaceTutorials(document: Document, targetClass: Element, sourceClass: Element) {
        val targetTutorials = directChild(targetClass, "tutorials") ?: return
        val sourceTutorials = directChild(sourceClass, "tutorials")
        if (sourceTutorials == null) {
            while (targetTutorials.hasChildNodes()) targetTutorials.removeChild(targetTutorials.firstChild)
            return
        }
        targetClass.replaceChild(document.importNode(sourceTutorials, true), targetTutorials)
    }

    private fun directChild(parent: Element?, tagName: String): Element? =
        directChildren(parent, tagName).firstOrNull()

    private fun directChildren(parent: Element?, tagName: String): List<Element> {
        if (parent == null) return emptyList()
        return parent.childNodes.asList().filterIsInstance<Element>().filter { it.tagName == tagName }
    }

    private fun serialize(document: Document): String {
        val transformer = TransformerFactory.newInstance().newTransformer().apply {
            setOutputProperty(OutputKeys.ENCODING, "UTF-8")
            setOutputProperty(OutputKeys.INDENT, "no")
            setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "no")
        }
        return StringWriter().also { transformer.transform(DOMSource(document), StreamResult(it)) }.toString()
    }

    private fun NodeList.asList(): List<Node> = (0 until length).map(::item)
}
