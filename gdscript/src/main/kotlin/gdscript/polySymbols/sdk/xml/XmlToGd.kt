package gdscript.polySymbols.sdk.xml

import com.intellij.openapi.vfs.VirtualFile
import gdscript.sdk.xml.GdNameSanitizer
import java.nio.file.Path

/**
 * Converts Godot XML class definitions to GDScript files
 */
class XmlToGd {

    private val csharpCodeBlock = "(?s)\\[csharp].*?\\[/csharp]\\s*".toRegex()

    /**
     * Converts an XML file to GDScript content
     * Parameters: path — The path to the XML file
     * Return: The GDScript content as a string
     */
    fun convert(path: Path): String {
        val classData = GdSdkXmlParser.parseClass(path) ?: return ""
        return convert(classData)
    }

    fun convert(file: VirtualFile): String {
        val classData = GdSdkXmlParser.parseClass(file) ?: return ""
        return convert(classData)
    }

    /**
     * Converts parsed ClassData to GDScript content
     * Parameters: clazz — The parsed class data
     * Return: The GDScript content as a string
     */
    fun convert(clazz: GdSdkData.ClassData): String {
        val sb = StringBuilder()

        if (clazz.inherits?.isNotEmpty() == true) {
            sb.appendLine("extends ${clazz.inherits}")
        }
        sb.appendLine("class_name ${GdNameSanitizer.sanitizeClassName(clazz.name)}")

        if (hasClassDocumentation(clazz)) {
            addClassDocumentation(sb, clazz)
        }

        addConstructors(sb, clazz.constructors)

        addSignals(sb, clazz.signals)

        addConstants(sb, clazz.constants)
        addEnums(sb, clazz.enums)

        addProperties(sb, clazz.properties)

        addMethods(sb, clazz.methods)

        addAnnotations(sb, clazz.annotations)

        return sb.toString()
    }

    private fun hasClassDocumentation(clazz: GdSdkData.ClassData): Boolean =
        clazz.briefDescription?.isNotEmpty() == true ||
            clazz.description?.isNotEmpty() == true ||
            clazz.tutorials?.isNotEmpty() == true ||
            clazz.isDeprecated ||
            clazz.isExperimental

    private fun addClassDocumentation(sb: StringBuilder, clazz: GdSdkData.ClassData) {
        sb.appendLine()
        if (clazz.briefDescription?.isNotEmpty() == true) {
            addDescription(sb, clazz.briefDescription)
        }
        if (clazz.description?.isNotEmpty() == true) {
            addDescription(sb, clazz.description)
        }
        if (clazz.tutorials?.isNotEmpty() == true) {
            clazz.tutorials.forEach { sb.appendLine("## @tutorial(${it.name}): ${it.url}") }
        }
        if (clazz.isDeprecated) {
            sb.appendLine("## @deprecated")
        }
        if (clazz.isExperimental) {
            sb.appendLine("## @experimental")
        }
        sb.appendLine()
    }

    private fun addMemberDocumentation(sb: StringBuilder, description: String?, isDeprecated: Boolean, isExperimental: Boolean) {
        if (description?.isNotEmpty() == true) {
            addDescription(sb, description)
        }
        if (isDeprecated) {
            sb.appendLine("## @deprecated")
        }
        if (isExperimental) {
            sb.appendLine("## @experimental")
        }
    }

    private fun addConstructors(sb: StringBuilder, constructors: List<GdSdkData.ConstructorData>) {
        if (constructors.isEmpty()) return
        sb.appendLine()
        val constructorsRegionName = "Constructors"
        addStartRegion(sb, constructorsRegionName)
        sb.appendLine()
        constructors.forEach { constructor ->
            addMemberDocumentation(sb, constructor.description, constructor.isDeprecated, constructor.isExperimental)
            val vararg = if (constructor.qualifiers.isVariadic) "...args: Array" else null
            val params = constructor.parameters.joinToString(", ") {
                "${GdNameSanitizer.sanitizeParameterName(it.name)}: ${it.type.name}"
            }
            val paramsList = vararg?.let { if (params.isNotEmpty()) "$params, $vararg" else vararg } ?: params
            sb.appendLine("func _init($paramsList):")
            sb.appendLine("\tpass")
            sb.appendLine()
        }
        addEndRegion(sb, constructorsRegionName)
        sb.appendLine()
    }

    private fun addMethods(sb: StringBuilder, methods: List<GdSdkData.MethodData>) {
        if (methods.isEmpty()) return
        var firstGetterOrSetter = true

        val getterSettersRegionName = "Getters and Setters"
        val methodsRegionName = "Methods"
        sb.appendLine()
        addStartRegion(sb, methodsRegionName)
        sb.appendLine()
        methods.forEachIndexed { i, method ->
            if ((method.isGetter || method.isSetter) && firstGetterOrSetter) {
                sb.appendLine()
                addStartRegion(sb, getterSettersRegionName)
                sb.appendLine()
                firstGetterOrSetter = false
            }
            // TODO add annotations/comments to show the rest of the qualifiers
            addMemberDocumentation(sb, method.description, method.isDeprecated, method.isExperimental)
            val static = if (method.qualifiers.isStatic) "static " else ""
            val vararg = if (method.qualifiers.isVariadic) "...args: Array" else null
            val params = method.parameters.joinToString(", ") {
                "${GdNameSanitizer.sanitizeParameterName(it.name)}: ${it.type.name}"
            }
            val paramsList = vararg?.let { if (params.isNotEmpty()) "$params, $vararg" else vararg } ?: params
            sb.appendLine("${static}func ${method.name}($paramsList) -> ${method.returnType.name}:")
            if (!method.isGetter && !method.isSetter) {
                sb.appendLine("\tpass")
            } else if (method.isGetter) {
                sb.appendLine("\treturn ${method.associatedPropertyName}")
            } else { // isSetter
                sb.appendLine("\t${method.associatedPropertyName} = value")
            }
            sb.appendLine()
            if (methods.size - 1 == i && !firstGetterOrSetter) {
                addEndRegion(sb, getterSettersRegionName)
                sb.appendLine()
            }
        }
        addEndRegion(sb, methodsRegionName)
        sb.appendLine()

    }

    private fun addProperties(sb: StringBuilder, properties: List<GdSdkData.PropertyData>) {
        if (properties.isEmpty()) return
        sb.appendLine()
        val propertiesRegionName = "Properties"
        addStartRegion(sb, propertiesRegionName)
        sb.appendLine()
        properties.forEach { property ->
            addMemberDocumentation(sb, property.description, property.isDeprecated, property.isExperimental)
            val getterSetter = mutableListOf<String>()
            if (property.getter?.isNotEmpty() == true) {
                getterSetter.add("get = ${property.getter}")
            }
            if (property.setter?.isNotEmpty() == true) {
                getterSetter.add("set = ${property.setter}")
            }
            sb.appendLine(
                "var ${property.name}: ${property.type.name}"
                    + if (getterSetter.isNotEmpty()) ": ${getterSetter.joinToString(", ")}" else ""
            )
            sb.appendLine()
        }
        addEndRegion(sb, propertiesRegionName)
        sb.appendLine()
    }

    private fun addSignals(sb: StringBuilder, signals: List<GdSdkData.SignalData>) {
        if (signals.isEmpty()) return
        sb.appendLine()
        val signalsRegionName = "Signals"
        addStartRegion(sb, signalsRegionName)
        sb.appendLine()
        signals.forEach { signal ->
            addMemberDocumentation(sb, signal.description, signal.isDeprecated, signal.isExperimental)
            val params = signal.parameters.joinToString(", ") {
                "${GdNameSanitizer.sanitizeParameterName(it.name)}: ${it.type.name}"
            }
            sb.appendLine("signal ${signal.name}($params)")
            sb.appendLine()
        }
        addEndRegion(sb, signalsRegionName)
        sb.appendLine()
    }

    private fun addConstants(sb: StringBuilder, constants: List<GdSdkData.ConstantData>) {
        if (constants.isEmpty()) return
        sb.appendLine()
        val constantsRegionName = "Constants"
        addStartRegion(sb, constantsRegionName)
        sb.appendLine()
        constants.forEach { constant ->
            addMemberDocumentation(sb, constant.description, constant.isDeprecated, constant.isExperimental)
            sb.appendLine("const ${constant.name} = ${constant.value}")
            sb.appendLine()
        }
        addEndRegion(sb, constantsRegionName)
        sb.appendLine()
    }

    private fun addEnums(sb: StringBuilder, enums: List<GdSdkData.EnumData>) {
        if (enums.isEmpty()) return
        sb.appendLine()
        val enumsRegionName = "Enums"
        addStartRegion(sb, enumsRegionName)
        sb.appendLine()
        enums.forEach { enum ->
            // Splitting at `.`, because sometimes e.g. `Variant.Type` is used in `@GlobalScope`, which does not yield a valid GDScript
            sb.appendLine("enum ${enum.name.split(".").last()} {")
            enum.values.forEach { enumValue ->
                if (enumValue.description?.isNotEmpty() == true) {
                    addDescription(sb, enumValue.description, "\t")
                }
                sb.appendLine("\t${enumValue.name} = ${enumValue.value},")
            }
            sb.appendLine("}")
            sb.appendLine()
        }
        addEndRegion(sb, enumsRegionName)
        sb.appendLine()
    }

    /**
     * GDScript has no syntax to declare an annotation.
     * Each annotation gets its `##` description and a plain `# @name(params)` comment as the anchor.
     */
    private fun addAnnotations(sb: StringBuilder, annotations: List<GdSdkData.AnnotationData>) {
        if (annotations.isEmpty()) return
        sb.appendLine()
        val annotationsRegionName = "Annotations"
        addStartRegion(sb, annotationsRegionName)
        sb.appendLine()
        annotations.forEach { annotation ->
            addMemberDocumentation(sb, annotation.description, isDeprecated = false, isExperimental = false)
            val params = annotation.parameters.mapIndexed { i, param ->
                val name = GdNameSanitizer.sanitizeParameterName(param.name)
                if (annotation.isVariadic && i == annotation.parameters.lastIndex) "...$name: ${param.type.name}"
                else "$name: ${param.type.name}" + (param.default?.takeIf { it.isNotEmpty() }?.let { " = $it" } ?: "")
            }
            val paramsList = if (annotation.isVariadic && params.isEmpty()) "...args: Array" else params.joinToString(", ")
            sb.appendLine("# @${annotation.name}" + if (paramsList.isEmpty()) "" else "($paramsList)")
            sb.appendLine()
        }
        addEndRegion(sb, annotationsRegionName)
        sb.appendLine()
    }

    private fun addDescription(sb: StringBuilder, description: String?, prefix: String = "") {
        val lines = description?.replace(csharpCodeBlock, "")?.lines() ?: return
        // Keep relative indentation inside docs (code samples), but drop the shared leading indent.
        val commonIndent = lines.drop(1)
            .filterNot { it.isBlank() }
            .minOfOrNull { leadingWhitespaceLength(it) }
            ?: 0

        lines.forEachIndexed { index, line ->
            val leadingLength = leadingWhitespaceLength(line)
            val content = line.drop(leadingLength).trimEnd()
            val relativeLeading = if (index == 0) {
                line.take(leadingLength)
            }
            else {
                line.take(leadingLength).drop(commonIndent.coerceAtMost(leadingLength))
            }
            if (content.isEmpty()) {
                sb.appendLine("##")
            } else {
                sb.appendLine("$prefix## $relativeLeading$content")
            }
        }
    }

    private fun leadingWhitespaceLength(line: String): Int =
        line.indexOfFirst { it != ' ' && it != '\t' }.let { if (it < 0) line.length else it }

    private fun addStartRegion(sb: StringBuilder, name: String) {
        sb.appendLine("#region $name")
    }

    private fun addEndRegion(sb: StringBuilder, name: String) {
        sb.appendLine("#endregion $name")
    }

}