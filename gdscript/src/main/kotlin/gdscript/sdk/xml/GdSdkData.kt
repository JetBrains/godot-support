package gdscript.sdk.xml

import gdscript.model.GdTutorial

sealed class GdSdkData {
    data class ClassData(
        val name: String,
        val inherits: String?,
        val briefDescription: String?,
        val description: String?,
        val constructors: List<ConstructorData>,
        val methods: List<MethodData>,
        val properties: List<PropertyData>,
        val signals: List<SignalData>,
        val constants: List<ConstantData>,
        val enums: List<EnumData>,
        val themeItems: List<ThemeItemData>,
        val tutorials: List<GdTutorial>?,
        val isDeprecated: Boolean,
        val isExperimental: Boolean,
    ) : GdSdkData()

    data class ConstructorData(
        val parameters: List<ParameterData>,
        val qualifiers: QualifierData,
        val description: String?,
        val isDeprecated: Boolean,
        val isExperimental: Boolean,
    ) : GdSdkData()

    data class MethodData(
        val name: String,
        val returnType: TypeData,
        val parameters: List<ParameterData>,
        val qualifiers: QualifierData,
        val description: String?,
        val isDeprecated: Boolean,
        val isExperimental: Boolean,
        val isGetter: Boolean = false,
        val isSetter: Boolean = false,
        val associatedPropertyName: String? = null, // name of the property for which this is a getter/setter
    ) : GdSdkData()

    data class PropertyData(
        val name: String,
        val type: TypeData,
        val default: String?,
        val setter: String?,
        val getter: String?,
        val overrides: String?,
        val description: String?,
        val isDeprecated: Boolean,
        val isExperimental: Boolean,
    ) : GdSdkData()

    data class SignalData(
        val name: String,
        val parameters: List<ParameterData>,
        val description: String?,
        val isDeprecated: Boolean,
        val isExperimental: Boolean,
    ) : GdSdkData()

    data class ConstantData(
        val name: String,
        val value: String,
        val description: String?,
        val isDeprecated: Boolean,
        val isExperimental: Boolean,
    ) : GdSdkData()

    data class EnumData(
        val name: String,
        val values: List<EnumValueData>,
        val isBitField: Boolean,
    ) : GdSdkData()

    data class EnumValueData(
        val name: String,
        val value: String,
    ) : GdSdkData()

    // has return type that is always void
    data class AnnotationData(
        val name: String,
        val description: String?,
        val isVariadic: Boolean,
        val parameters: List<ParameterData>,
    ) : GdSdkData()

    data class ThemeItemData(
        val name: String,
        val dataType: String,
        val type: String,
        val default: String?,
        val isDeprecated: Boolean,
        val isExperimental: Boolean,
    ) : GdSdkData()

    data class OperationsData(
        val left: String, // the left operand type, also the class where they are defined
        val operators: List<OperatorData>,
    ) : GdSdkData()

    data class OperatorData(
        val right: String,
        val operator: String,
        val returnType: TypeData,
        val isUnary: Boolean,
        val description: String?,
    ) : GdSdkData()

    data class ParameterData(
        val name: String,
        val type: TypeData,
        val default: String? = null,
    ) : GdSdkData()

    data class TypeData(
        val name: String = "void",
        val enumName: String? = null,
        val isBitField: Boolean = false,
    ) : GdSdkData()

    // these are all the possible qualifiers as of godot 4.5.0.
    // To find all possible qualifiers you can run the following command on the doctool output dir
    // grep -RohE 'qualifiers="[^\"]*"' <doctool output dir> 2>/dev/null | sed -E 's/^qualifiers="(.*)"$/\1/' | tr ' ' '\n' | sed '/^$/d' | sort -u
    data class QualifierData(
        val isConst: Boolean = false,
        val isStatic: Boolean = false,
        val isVirtual: Boolean = false,
        val isVariadic: Boolean = false,
        val isRequired: Boolean = false,
    ) : GdSdkData()
}