package gdscript.sdk.xml

import com.intellij.openapi.vfs.VirtualFile
import java.nio.file.Path

/**
 * Converts Godot XML class definitions to GDScript files
 */
class XmlToGd {

    /**
     * Converts an XML file to GDScript content
     * Parameters: path — The path to the XML file
     * Return: The GDScript content as a string
     */
    fun convert(path: Path): String {
        val classData = GdSdkXmlParser.parseClass(path) ?: return ""
        return convert(classData)
    }

    fun convert(file: VirtualFile): String{
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
        sb.appendLine("class_name ${clazz.name}")

        if(hasClassDocumentation(clazz)) {
            addClassDocumentation(sb, clazz)
        }

        addConstructors(sb, clazz.constructors)

        addSignals(sb, clazz.signals)

        addConstants(sb, clazz.constants)
        addEnums(sb, clazz.enums)

        addProperties(sb, clazz.properties)

        addMethods(sb, clazz.methods)

        return sb.toString()
    }

    private fun hasClassDocumentation(clazz: GdSdkData.ClassData): Boolean =
        clazz.briefDescription?.isNotEmpty() == true ||
        clazz.description?.isNotEmpty() == true ||
        clazz.tutorials?.isNotEmpty() == true ||
        clazz.isDeprecated ||
        clazz.isExperimental

    private fun addClassDocumentation(sb: StringBuilder, clazz: GdSdkData.ClassData){
        sb.appendLine()
        if (clazz.briefDescription?.isNotEmpty() == true) {
            clazz.briefDescription.lines().forEach { sb.appendLine("## ${it.trim()}") }
        }
        if (clazz.description?.isNotEmpty() == true) {
            clazz.description.lines().forEach { sb.appendLine("## ${it.trim()}") }
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
            description.lines().forEach { sb.appendLine("## ${it.trim()}") }
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
        sb.appendLine("# Constructors")
        sb.appendLine()
        constructors.forEach { constructor ->
            addMemberDocumentation(sb, constructor.description, constructor.isDeprecated, constructor.isExperimental)
            val vararg = if (constructor.qualifiers.isVariadic) "...args: Array" else null
            val params = constructor.parameters.joinToString(", ") { "${it.name}: ${it.type.name}" }
            val paramsList = vararg?.let { if (params.isNotEmpty()) "$params, $vararg" else vararg } ?: params
            sb.appendLine("func _init($paramsList):")
            sb.appendLine("\tpass")
            sb.appendLine()
        }
    }

    private fun addMethods(sb: StringBuilder, methods: List<GdSdkData.MethodData>) {
        if (methods.isEmpty()) return
        var firstGetterOrSetter = true

        sb.appendLine()
        sb.appendLine("# Methods")
        sb.appendLine()
        methods.forEach { method ->
            if ((method.isGetter || method.isSetter) && firstGetterOrSetter) {
                printGetterSetterMethodsHeader(sb)
                firstGetterOrSetter = false
            }
            // we could add annotations/comments to show the rest of the qualifiers
            addMemberDocumentation(sb, method.description, method.isDeprecated, method.isExperimental)
            val static = if (method.qualifiers.isStatic) "static " else ""
            val vararg = if (method.qualifiers.isVariadic) "...args: Array" else null
            val params = method.parameters.joinToString(", ") { "${it.name}: ${it.type.name}" }
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
        }
    }

    private fun printGetterSetterMethodsHeader(sb: StringBuilder) {
        sb.appendLine()
        sb.appendLine("# Getters and Setters")
        sb.appendLine()
    }

    private fun addProperties(sb: StringBuilder, properties: List<GdSdkData.PropertyData>) {
        if (properties.isEmpty()) return
        sb.appendLine()
        sb.appendLine("# Properties")
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
            sb.appendLine("var ${property.name}: ${property.type.name}"
                + if (getterSetter.isNotEmpty()) ": ${getterSetter.joinToString(", ")}" else "")
            sb.appendLine()
        }
    }

    private fun addSignals(sb: StringBuilder, signals: List<GdSdkData.SignalData>) {
        if (signals.isEmpty()) return
        sb.appendLine()
        sb.appendLine("# Signals")
        sb.appendLine()
        signals.forEach { signal ->
            addMemberDocumentation(sb, signal.description, signal.isDeprecated, signal.isExperimental)
            val params = signal.parameters.joinToString(", ") { "${it.name}: ${it.type.name}" }
            sb.appendLine("signal ${signal.name}($params)")
            sb.appendLine()
        }
    }

    private fun addConstants(sb: StringBuilder, constants: List<GdSdkData.ConstantData>) {
        if (constants.isEmpty()) return
        sb.appendLine()
        sb.appendLine("# Constants")
        sb.appendLine()
        constants.forEach { constant ->
            addMemberDocumentation(sb, constant.description, constant.isDeprecated, constant.isExperimental)
            sb.appendLine("const ${constant.name} = ${constant.value}")
            sb.appendLine()
        }
    }

    private fun addEnums(sb: StringBuilder, enums: List<GdSdkData.EnumData>) {
        if (enums.isEmpty()) return
        sb.appendLine()
        sb.appendLine("# Enums")
        sb.appendLine()
        enums.forEach { enum ->
            sb.appendLine("enum ${enum.name} {")
            enum.values.forEach { enumValue ->
                sb.appendLine("\t${enumValue.name} = ${enumValue.value},")
            }
            sb.appendLine("}")
            sb.appendLine()
        }
    }
}