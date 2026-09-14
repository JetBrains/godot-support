package gdscript.embeddedDocs

import org.jetbrains.annotations.ApiStatus
import javax.xml.XMLConstants
import javax.xml.parsers.DocumentBuilderFactory

/**
 * The two limits match the values that the IDE test JVM sets.
 *
 * The IDE test JVM sets `jdk.xml.entityExpansionLimit` to 2500 and `jdk.xml.maxElementDepth` to 100.
 * A higher number here is dead weight in the IDE, because the ambient property is already stricter.
 */
private const val ENTITY_EXPANSION_LIMIT = "2500"
private const val MAX_ELEMENT_DEPTH = "100"

/**
 * Creates an XML parser factory that blocks external data access and limits resource use.
 *
 * The factory permits a DOCTYPE because Godot XML files and Apple property lists use safe declarations.
 */
@ApiStatus.Internal
fun newHardenedDocumentBuilderFactory(): DocumentBuilderFactory =
    DocumentBuilderFactory.newDefaultInstance().apply {
        setFeature(XMLConstants.FEATURE_SECURE_PROCESSING, true)
        setFeature("http://xml.org/sax/features/external-general-entities", false)
        setFeature("http://xml.org/sax/features/external-parameter-entities", false)
        setFeature("http://apache.org/xml/features/nonvalidating/load-external-dtd", false)
        isXIncludeAware = false
        isExpandEntityReferences = false
        setAttribute(XMLConstants.ACCESS_EXTERNAL_DTD, "")
        setAttribute(XMLConstants.ACCESS_EXTERNAL_SCHEMA, "")
        setAttribute("http://www.oracle.com/xml/jaxp/properties/entityExpansionLimit", ENTITY_EXPANSION_LIMIT)
        setAttribute("http://www.oracle.com/xml/jaxp/properties/maxElementDepth", MAX_ELEMENT_DEPTH)
    }
