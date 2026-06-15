package gdscript.polySymbols.sdk.xml

import com.intellij.openapi.diagnostic.Logger
import com.intellij.openapi.vfs.VirtualFile
import gdscript.model.GdTutorial
import org.w3c.dom.Element
import java.io.InputStream
import java.nio.file.Path
import javax.xml.parsers.DocumentBuilderFactory
import kotlin.io.path.inputStream
import kotlin.io.path.pathString

// The format of these XML files can be found in testData/gdscript/xml/class.xsd
object GdSdkXmlParser {
    private val logger = Logger.getInstance(GdSdkXmlParser::class.java)

    private fun getRootFromInputStream(inputStream: InputStream, fileName: String = ""): Element? {
        try {
            val factory = DocumentBuilderFactory.newInstance()
            val builder = factory.newDocumentBuilder()
            val doc = inputStream.use { builder.parse(it) }
            return doc.documentElement
        } catch (e: Exception) {
            logger.warn("Error parsing XML file: $fileName", e)
        }
        return null
    }

    fun parseAnnotations(path: Path): List<GdSdkData.AnnotationData>? {
        val inputStream = path.inputStream()
        val root = getRootFromInputStream(inputStream, path.pathString) ?: return null
        return parseAnnotations(root)
    }

    fun parseAnnotations(file: VirtualFile): List<GdSdkData.AnnotationData>? {
        val inputStream = file.inputStream
        val root = getRootFromInputStream(inputStream, file.name) ?: return null
        return parseAnnotations(root)
    }

    private fun parseAnnotations(root: Element): List<GdSdkData.AnnotationData> {
        val annotations = mutableListOf<GdSdkData.AnnotationData>()
        val annotationsNode = root.getElementsByTagName("annotations").item(0) as? Element ?: return annotations
        val childAnnotationsNode = annotationsNode.getElementsByTagName("annotation")

        for (i in 0 until childAnnotationsNode.length) {
            val node = childAnnotationsNode.item(i) as? Element ?: continue
            if (node.nodeName != "annotation") continue

            annotations.add(
                GdSdkData.AnnotationData(
                    name = getName(node).substringAfter("@"),
                    description = getDescriptionFromTag(node),
                    isVariadic = getQualifiers(node).isVariadic,
                    parameters = parseParameters(node)
                )
            )
        }
        return annotations
    }

    fun parseOperations(path: Path): GdSdkData.OperationsData? {
        val inputStream = path.inputStream()
        val root = getRootFromInputStream(inputStream, path.pathString) ?: return null
        return parseOperations(root)
    }

    fun parseOperations(file: VirtualFile): GdSdkData.OperationsData? {
        val inputStream = file.inputStream
        val root = getRootFromInputStream(inputStream, file.name) ?: return null
        return parseOperations(root)
    }

    private fun parseOperations(root: Element): GdSdkData.OperationsData? {
        val operatorsNode = root.getElementsByTagName("operators").item(0) as? Element ?: return null
        return GdSdkData.OperationsData(
            left = formatType(getName(root)), // the class name
            operators = parseOperators(operatorsNode),
        )
    }

    // apparently can have a qualifiers attribute per the DTD, but it's not used anywhere
    private fun parseOperators(operatorsNode: Element): List<GdSdkData.OperatorData> {
        val operators = mutableListOf<GdSdkData.OperatorData>()
        val childrenOperatorsNode = operatorsNode.getElementsByTagName("operator")

        for (i in 0 until childrenOperatorsNode.length) {
            val node = childrenOperatorsNode.item(i) as? Element ?: continue
            val operatorPrefix = "operator "
            val unaryPrefix = "unary"
            val name = getName(node)
            operators.add(
                GdSdkData.OperatorData(
                    right = getRightOperatorType(node),
                    operator = name.substringAfter(operatorPrefix).substringAfter(unaryPrefix),
                    returnType = getReturnType(node),
                    isUnary = name.contains(unaryPrefix),
                    description = getDescriptionFromTag(node),
                )
            )
        }
        return operators
    }

    fun parseClass(path: Path): GdSdkData.ClassData? {
        val inputStream = path.inputStream()
        val root = getRootFromInputStream(inputStream, path.pathString) ?: return null
        return parseClass(root)
    }

    fun parseClass(file: VirtualFile): GdSdkData.ClassData? {
        val inputStream = file.inputStream
        val root = getRootFromInputStream(inputStream, file.name) ?: return null
        return parseClass(root)
    }

    fun parseClass(root: Element): GdSdkData.ClassData {
        val properties = parseProperties(root)
        return GdSdkData.ClassData(
                name = getName(root),
                inherits = getInherits(root),
                briefDescription = getBriefDescription(root),
                description = getDescriptionFromTag(root),
                constructors = parseConstructors(root),
                methods = parseMethods(root).plus(parseGettersAndSetters(properties)),
                properties = properties,
                signals = parseSignals(root),
                constants = parseConstants(root),
                enums = parseEnums(root),
                themeItems = parseThemeItems(root),
                tutorials = parseTutorials(root),
                isDeprecated = getIsDeprecated(root),
                isExperimental = getIsExperimental(root),
            )
    }

    private fun parseConstructors(root: Element): List<GdSdkData.ConstructorData> {
        val constructors = mutableListOf<GdSdkData.ConstructorData>()
        val constructorsNode = root.getElementsByTagName("constructors").item(0) as? Element ?: return constructors
        val childrenConstructorsNode = constructorsNode.getElementsByTagName("constructor")

        for (i in 0 until childrenConstructorsNode.length) {
            val node = childrenConstructorsNode.item(i) as? Element ?: continue
            constructors.add(
                GdSdkData.ConstructorData(
                    parameters = parseParameters(node),
                    qualifiers = getQualifiers(node),
                    description = getDescriptionFromTag(node),
                    isDeprecated = getIsDeprecated(node),
                    isExperimental = getIsExperimental(node),
                )
            )
        }
        return constructors
    }

    // there are <returns_error> tags that we don't parse, only present now in ConfigFile and PackedDataContainer
    private fun parseMethods(root: Element): List<GdSdkData.MethodData> {
        val methods = mutableListOf<GdSdkData.MethodData>()
        val methodsNode = root.getElementsByTagName("methods").item(0) as? Element ?: return methods
        val childrenMethodsNode = methodsNode.getElementsByTagName("method")

        for (i in 0 until childrenMethodsNode.length) {
            val node = childrenMethodsNode.item(i) as? Element ?: continue
            methods.add(
                GdSdkData.MethodData(
                    name = getName(node),
                    returnType = getReturnType(node),
                    parameters = parseParameters(node),
                    qualifiers = getQualifiers(node),
                    description = getDescriptionFromTag(node),
                    isDeprecated = getIsDeprecated(node),
                    isExperimental = getIsExperimental(node),
                )
            )
        }
        return methods
    }

    private fun parseProperties(root: Element): List<GdSdkData.PropertyData> {
        val properties = mutableListOf<GdSdkData.PropertyData>()
        val membersNode = root.getElementsByTagName("members").item(0) as? Element ?: return properties
        val childrenMembersNode = membersNode.getElementsByTagName("member")

        for (i in 0 until childrenMembersNode.length) {
            val node = childrenMembersNode.item(i) as? Element ?: continue
            properties.add(
                GdSdkData.PropertyData(
                    name = getName(node),
                    type = getType(node),
                    default = getDefault(node),
                    setter = getSetter(node),
                    getter = getGetter(node),
                    overrides = getOverrides(node),
                    description = getDescriptionFromContent(node),
                    isDeprecated = getIsDeprecated(node),
                    isExperimental = getIsExperimental(node),
                )
            )
        }
        return properties
    }

    private fun parseGettersAndSetters(properties: List<GdSdkData.PropertyData>): List<GdSdkData.MethodData> {
        val gettersAndSetters = mutableListOf<GdSdkData.MethodData>()
        for (property in properties) {
            if(property.getter != null) {
                gettersAndSetters.add(
                    GdSdkData.MethodData(
                        name = property.getter,
                        returnType = property.type,
                        parameters = emptyList(),
                        qualifiers = GdSdkData.QualifierData(),
                        description = "",
                        isDeprecated = false,
                        isExperimental = false,
                        isGetter = true,
                        associatedPropertyName = property.name,
                    )
                )
            }
            if(property.setter != null) {
                gettersAndSetters.add(
                    GdSdkData.MethodData(
                        name = property.setter,
                        returnType = GdSdkData.TypeData(),
                        parameters = listOf(
                            GdSdkData.ParameterData(
                                name = "value",
                                type = property.type,
                            )
                        ),
                        qualifiers = GdSdkData.QualifierData(),
                        description = "",
                        isDeprecated = false,
                        isExperimental = false,
                        isSetter = true,
                        associatedPropertyName = property.name,
                    )
                )
            }
        }
        return gettersAndSetters
    }

    private fun parseSignals(root: Element): List<GdSdkData.SignalData> {
        val signals = mutableListOf<GdSdkData.SignalData>()
        val signalsNode = root.getElementsByTagName("signals").item(0) as? Element ?: return signals
        val childrenSignalsNode = signalsNode.getElementsByTagName("signal")

        for (i in 0 until childrenSignalsNode.length) {
            val node = childrenSignalsNode.item(i) as? Element ?: continue
            signals.add(
                GdSdkData.SignalData(
                    name = getName(node),
                    parameters = parseParameters(node),
                    description = getDescriptionFromTag(node),
                    isDeprecated = getIsDeprecated(node),
                    isExperimental = getIsExperimental(node),
                )
            )
        }
        return signals
    }

    private fun parseConstants(root: Element): List<GdSdkData.ConstantData> {
        val constants = mutableListOf<GdSdkData.ConstantData>()
        val constantsNode = root.getElementsByTagName("constants").item(0) as? Element ?: return constants
        val childrenConstantsNode = constantsNode.getElementsByTagName("constant")

        // also add enum values into constants because they can be referenced from the enums or as normal constants through unnamed enums
        for (i in 0 until childrenConstantsNode.length) {
            val node = childrenConstantsNode.item(i) as? Element ?: continue
            constants.add(
                GdSdkData.ConstantData(
                    name = getName(node),
                    value = getValue(node),
                    description = getDescriptionFromContent(node),
                    isDeprecated = getIsDeprecated(node),
                    isExperimental = getIsExperimental(node),
                )
            )
        }
        return constants
    }

    private fun parseEnums(root: Element): List<GdSdkData.EnumData> {
        val enums = mutableListOf<GdSdkData.EnumData>()
        val enumValues = mutableMapOf<String, Pair<MutableList<GdSdkData.EnumValueData>, Boolean>>()
        val constantsNode = root.getElementsByTagName("constants").item(0) as? Element ?: return enums
        val childrenConstantsNode = constantsNode.getElementsByTagName("constant")

        for (i in 0 until childrenConstantsNode.length) {
            val node = childrenConstantsNode.item(i) as? Element ?: continue
            val enumName = getEnumName(node) ?: continue
            if (enumName.isEmpty()) continue

            val enumValue = GdSdkData.EnumValueData(
                name = getName(node),
                value = getValue(node),
            )
            val isBitField = getIsBitField(node)

            val existing = enumValues.getOrPut(enumName) { Pair(mutableListOf(), isBitField) }
            existing.first.add(enumValue)
        }

        for (enumName in enumValues.keys) {
            enums.add(
                GdSdkData.EnumData(
                name = enumName,
                values = enumValues[enumName]!!.first,
                isBitField = enumValues[enumName]!!.second
                )
            )
        }
        return enums
    }

    private fun parseThemeItems(root: Element): List<GdSdkData.ThemeItemData> {
        val themeItems = mutableListOf<GdSdkData.ThemeItemData>()
        val themeItemsNode = root.getElementsByTagName("theme_items").item(0) as? Element ?: return themeItems
        val childrenThemeItemsNode = themeItemsNode.getElementsByTagName("theme_item")

        for (i in 0 until childrenThemeItemsNode.length){
            val node = childrenThemeItemsNode.item(i) as? Element ?: continue
            themeItems.add(
                GdSdkData.ThemeItemData(
                    name = getName(node),
                    dataType = getDataTypeAttribute(node),
                    type = getTypeAttribute(node),
                    default = getDefault(node),
                    isDeprecated = getIsDeprecated(node),
                    isExperimental = getIsExperimental(node),
                )
            )

        }
        return themeItems
    }

    private fun parseTutorials(root: Element): List<GdTutorial> {
        val tutorials = mutableListOf<GdTutorial>()
        val tutorialsNode = root.getElementsByTagName("tutorials").item(0) as? Element ?: return tutorials
        val childrenTutorialsNode = tutorialsNode.getElementsByTagName("link")

        for (i in 0 until childrenTutorialsNode.length) {
            val node = childrenTutorialsNode.item(i) as? Element ?: continue
            tutorials.add(
                GdTutorial(
                    name = getTitle(node),
                    url = getTextContent(node).replace("\$DOCS_URL", "https://docs.godotengine.org/en/stable"),
                )
            )
        }
        return tutorials
    }

    private fun parseParameters(element: Element): List<GdSdkData.ParameterData> {
        val params = mutableListOf<GdSdkData.ParameterData>()
        val paramNodes = element.getElementsByTagName("param")

        for (i in 0 until paramNodes.length) {
            val node = paramNodes.item(i) as? Element ?: continue
            params.add(
                GdSdkData.ParameterData(
                    name = getName(node),
                    type = getType(node),
                    default = getDefault(node),
                )
            )
        }
        return params
    }


    private fun getReturnType(element: Element): GdSdkData.TypeData {
        val returnNode = element.getElementsByTagName("return").item(0) as? Element ?: return GdSdkData.TypeData()
        return getType(returnNode)
    }

    private fun getType(element: Element): GdSdkData.TypeData =
        GdSdkData.TypeData(
            name = getTypeAttribute(element).takeIf { it.isNotEmpty() } ?: "void",
            enumName = getEnumName(element),
            isBitField = getIsBitField(element),
        )

    private fun getQualifiers(element: Element): GdSdkData.QualifierData {
        val qualifiers = getQualifiersField(element)
        return GdSdkData.QualifierData(
            isConst = qualifiers.contains("const"),
            isStatic = qualifiers.contains("static"),
            isVirtual = qualifiers.contains("virtual"),
            isVariadic = qualifiers.contains("vararg"),
            isRequired = qualifiers.contains("required"),
        )
    }

    private fun getBriefDescription(element: Element): String =
        getTextContent(element, "brief_description")

    private fun getDescriptionFromContent(element: Element): String =
        getTextContent(element)

    private fun getDescriptionFromTag(element: Element): String =
        getTextContent(element, "description")

    private fun getTextContent(element: Element): String =
        element.textContent.trim()

    private fun getTextContent(element: Element, tagName: String): String {
        val node = element.getElementsByTagName(tagName).item(0)
        return node?.textContent?.trim() ?: ""
    }

    private fun getInherits(element: Element): String =
        element.getAttribute("inherits")

    private fun getName(element: Element): String =
        element.getAttribute("name")

    private fun getTitle(element: Element): String =
        element.getAttribute("title")

    private fun getIsDeprecated(element: Element): Boolean =
        element.getAttribute("is_deprecated") == "true"
            || element.getAttribute("deprecated") == "true"

    private fun getIsExperimental(element: Element): Boolean =
        element.getAttribute("is_experimental") == "true"
            || element.getAttribute("experimental") == "true"

    private fun getEnumName(element: Element): String? =
        element.getAttribute("enum").takeIf { it.isNotEmpty() }

    private fun getIsBitField(element: Element): Boolean =
        element.getAttribute("is_bitfield") == "true"

    private fun getQualifiersField(element: Element): List<String> =
        element.getAttribute("qualifiers").split(" ").filter { it.isNotEmpty() }

    private fun getValue(element: Element): String =
        element.getAttribute("value")

    private fun getDefault(element: Element): String? =
        element.getAttribute("default").takeIf { it.isNotEmpty() }

    private fun getSetter(element: Element): String? =
        element.getAttribute("setter").takeIf { it.isNotEmpty() }

    private fun getGetter(element: Element): String? =
        element.getAttribute("getter").takeIf { it.isNotEmpty() }

    private fun getOverrides(element: Element): String? =
        element.getAttribute("overrides").takeIf { it.isNotEmpty() }

    private fun getDataTypeAttribute(element: Element): String =
        element.getAttribute("data_type")

    private fun getTypeAttribute(element: Element): String =
        formatType(element.getAttribute("type"))

    private fun getRightOperatorType(element: Element): String {
        val paramNodes = element.getElementsByTagName("param")

        val node = paramNodes.item(0) as? Element ?: return ""
        return getTypeAttribute(node)
    }

    private fun formatType(type: String): String{
        if (type.endsWith("[]")) {
            val baseType = type.substring(0, type.length - 2)
            return "Array[$baseType]"
        }
        return type
    }
}