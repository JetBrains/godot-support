package tscn.highlighter

import com.intellij.openapi.editor.colors.TextAttributesKey
import com.intellij.openapi.fileTypes.SyntaxHighlighter
import com.intellij.openapi.options.colors.AttributesDescriptor
import com.intellij.openapi.options.colors.ColorDescriptor
import com.intellij.openapi.options.colors.ColorSettingsPage
import tscn.TscnIcon
import javax.swing.Icon

class TscnColorSettingsPage : ColorSettingsPage {

    override fun getIcon(): Icon {
        return TscnIcon.FILE
    }

    override fun getHighlighter(): SyntaxHighlighter {
        return TscnSyntaxHighlighter()
    }

    override fun getDemoText(): String {
        return """[gd_scene load_steps=6 format=3 uid="uid://chktolfsjje2w"]

[ext_resource type="Script" path="res://example.gd" id="1_88ugo"]
[ext_resource type="PackedScene" uid="uid://5unk7mt3afwe" path="res://example_child.tscn" id="2_xbmbh"]

[sub_resource type="Animation" id="Animation_05djp"]
resource_name = "Example"
tracks/0/type = "value"
tracks/0/imported = false
tracks/0/enabled = true
tracks/0/path = NodePath(".:visible")
tracks/0/interp = 1
tracks/0/loop_wrap = true
tracks/0/keys = {
"times": PackedFloat32Array(1),
"transitions": PackedFloat32Array(1),
"update": 1,
"values": [true]
}

[sub_resource type="AnimationLibrary" id="AnimationLibrary_0qe1n"]
_data = {
"Example": SubResource("Animation_05djp")
}

[sub_resource type="CircleShape2D" id="CircleShape2D_xthut"]
radius = 29.2746

[node name="Example" type="CharacterBody2D"]
script = ExtResource("1_88ugo")

[node name="AnimationPlayer" type="AnimationPlayer" parent="."]
libraries = {
"": SubResource("AnimationLibrary_0qe1n")
}

[node name="CollisionShape2D" type="CollisionShape2D" parent="."]
shape = SubResource("CircleShape2D_xthut")

[node name="ExampleChild" parent="." instance=ExtResource("2_xbmbh")]

[connection signal="example_signal" from="ExampleChild" to="." method="_on_example_child_example_signal"]

"""
    }

    override fun getAdditionalHighlightingTagToDescriptorMap(): Map<String, TextAttributesKey>? {
        return null
    }

    override fun getAttributeDescriptors(): Array<AttributesDescriptor> {
        return DESCRIPTORS
    }

    override fun getColorDescriptors(): Array<ColorDescriptor> {
        return ColorDescriptor.EMPTY_ARRAY
    }

    override fun getDisplayName(): String {
        return "GdScene"
    }

    companion object {
        private val DESCRIPTORS = arrayOf(
                AttributesDescriptor("Syntax tokens", TscnHighlighterColors.SYNTAX_TOKENS),
                AttributesDescriptor("Node resource", TscnHighlighterColors.NODE_RESOURCE),
                AttributesDescriptor("Node type", TscnHighlighterColors.NODE_TYPE),
                AttributesDescriptor("Attributes", TscnHighlighterColors.ATTRIBUTES),
                AttributesDescriptor("String", TscnHighlighterColors.STRING),
                AttributesDescriptor("Resource string", TscnHighlighterColors.RES_STRING),
                AttributesDescriptor("Attribute value", TscnHighlighterColors.ATTRIBUTE_VALUES),
        )
    }
}
