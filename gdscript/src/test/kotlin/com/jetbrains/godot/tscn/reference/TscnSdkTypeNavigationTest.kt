package com.jetbrains.godot.tscn.reference

import com.intellij.polySymbols.testFramework.checkGotoDeclaration
import com.intellij.testFramework.junit5.TestApplication
import com.jetbrains.godot.gdscript.GdSdkFixture
import com.jetbrains.godot.gdscript.gdSdkFixture
import org.junit.jupiter.api.Test

@TestApplication
class TscnSdkTypeNavigationTest {
    private val gd: GdSdkFixture by gdSdkFixture("gdscript/resolve")

    // RIDER-145320 Missing navigation inside .tscn for type="..."
    @Test
    fun testNavigateFromNodeTypeToSdkClass() = gd.doConfiguredTest(
        dir = false,
        configureFileName = "scene.tscn",
        fileContents = """
            [gd_scene format=3]

            [node name="Root" type="Node"]

            [node name="foobar" type="Node2D" parent="." unique_id=1158141441]
            """.trimIndent(),
    ) {
        checkGotoDeclaration(
            fromSignature = "type=\"<caret>Node2D\"",
            declarationSignature = "class_name <caret>Node2D",
            expectedFileName = "Node2D.generated.gd",
        )
    }

    // RIDER-137323 Add navigation to signals and handlers from Godot scenes
    @Test
    fun testNavigateFromConnectionToSdkSignal() = gd.doConfiguredTest(
        dir = false,
        configureFileName = "scene.tscn",
        fileContents = """
            [gd_scene format=3]

            [node name="Root" type="Node"]

            [connection signal="tree_entered" from="." to="." method="_on_tree_entered"]
            """.trimIndent(),
    ) {
        checkGotoDeclaration(
            fromSignature = "signal=\"<caret>tree_entered\"",
            declarationSignature = "signal <caret>tree_entered(",
            expectedFileName = "Node.generated.gd",
        )
    }

    @Test
    fun testNavigateFromChildToDeclaredSdkSignal() = gd.doConfiguredTest(
        dir = false,
        configureFileName = "scene.tscn",
        fileContents = """
            [gd_scene format=3]

            [node name="Root" type="Node"]

            [node name="Emitter" type="Node" parent="."]

            [connection signal="ready" from="Emitter" to="." method="_on_emitter_ready"]
            """.trimIndent(),
    ) {
        checkGotoDeclaration(
            fromSignature = "signal=\"<caret>ready\"",
            declarationSignature = "signal <caret>ready(",
            expectedFileName = "Node.generated.gd",
        )
    }

    // RIDER-137323 Add navigation to signals and handlers from Godot scenes
    @Test
    fun testNavigateFromChildToInheritedSdkSignal() = gd.doConfiguredTest(
        dir = false,
        configureFileName = "scene.tscn",
        fileContents = """
            [gd_scene format=3]

            [node name="Root" type="Node"]

            [node name="Emitter" type="Node2D" parent="."]

            [connection signal="draw" from="Emitter" to="." method="_on_draw"]
            """.trimIndent(),
    ) {
        checkGotoDeclaration(
            fromSignature = "signal=\"<caret>draw\"",
            declarationSignature = "signal <caret>draw(",
            expectedFileName = "CanvasItem.generated.gd",
        )
    }

    // RIDER-137323 Add navigation to signals and handlers from Godot scenes
    @Test
    fun testNavigateFromSceneInstanceToSdkSignal() {
        // The instance has no local type. Its scene defines the SDK class.
        gd.fixture.addFileToProject(
            "emitter.tscn",
            """
                [gd_scene format=3]

                [node name="Emitter" type="Node2D"]
                """.trimIndent(),
        )
        gd.doConfiguredTest(
            dir = false,
            configureFileName = "scene.tscn",
            fileContents = """
                [gd_scene load_steps=2 format=3]

                [ext_resource type="PackedScene" path="res://emitter.tscn" id="1"]

                [node name="Root" type="Node"]

                [node name="Emitter" parent="." instance=ExtResource("1")]

                [connection signal="draw" from="Emitter" to="." method="_on_draw"]
                """.trimIndent(),
        ) {
            checkGotoDeclaration(
                fromSignature = "signal=\"<caret>draw\"",
                declarationSignature = "signal <caret>draw(",
                expectedFileName = "CanvasItem.generated.gd",
            )
        }
    }

    @Test
    fun testNavigateFromInheritedChildToSdkSignal() {
        gd.fixture.addFileToProject(
            "base_scene.tscn",
            """
                [gd_scene format=3]

                [node name="Root" type="Node"]

                [node name="Emitter" type="Node2D" parent="."]
                """.trimIndent(),
        )
        gd.doConfiguredTest(
            dir = false,
            configureFileName = "derived_scene.tscn",
            fileContents = """
                [gd_scene load_steps=2 format=3]

                [ext_resource type="PackedScene" path="res://base_scene.tscn" id="1"]

                [node name="Root" instance=ExtResource("1")]

                [connection signal="draw" from="Emitter" to="." method="_on_draw"]
                """.trimIndent(),
        ) {
            checkGotoDeclaration(
                fromSignature = "signal=\"<caret>draw\"",
                declarationSignature = "signal <caret>draw(",
                expectedFileName = "CanvasItem.generated.gd",
            )
        }
    }

    // RIDER-137323 Add navigation to signals and handlers from Godot scenes
    @Test
    fun testNavigateFromConnectionToScriptSignal() {
        gd.fixture.addFileToProject(
            "emitter.gd",
            """
                extends Node2D

                signal custom_signal()
                """.trimIndent(),
        )
        gd.doConfiguredTest(
            dir = false,
            configureFileName = "scene.tscn",
            fileContents = """
                [gd_scene load_steps=2 format=3]

                [ext_resource type="Script" path="res://emitter.gd" id="1"]

                [node name="Root" type="Node"]

                [node name="Emitter" type="Node2D" parent="."]
                script = ExtResource("1")

                [connection signal="custom_signal" from="Emitter" to="." method="_on_custom_signal"]
                """.trimIndent(),
        ) {
            checkGotoDeclaration(
                fromSignature = "signal=\"<caret>custom_signal\"",
                declarationSignature = "signal <caret>custom_signal(",
                expectedFileName = "emitter.gd",
            )
        }
    }

    @Test
    fun testNavigateFromInheritedChildToScriptSignal() {
        gd.fixture.addFileToProject("emitter.gd", "extends Node2D\n\nsignal custom_signal()\n")
        gd.fixture.addFileToProject(
            "base_scene.tscn",
            """
                [gd_scene load_steps=2 format=3]

                [ext_resource type="Script" path="res://emitter.gd" id="1"]

                [node name="Root" type="Node"]

                [node name="Emitter" type="Node2D" parent="."]
                script = ExtResource("1")
                """.trimIndent(),
        )
        gd.doConfiguredTest(
            dir = false,
            configureFileName = "derived_scene.tscn",
            fileContents = """
                [gd_scene load_steps=2 format=3]

                [ext_resource type="PackedScene" path="res://base_scene.tscn" id="1"]

                [node name="Root" instance=ExtResource("1")]

                [connection signal="custom_signal" from="Emitter" to="." method="_on_custom_signal"]
                """.trimIndent(),
        ) {
            checkGotoDeclaration(
                fromSignature = "signal=\"<caret>custom_signal\"",
                declarationSignature = "signal <caret>custom_signal(",
                expectedFileName = "emitter.gd",
            )
        }
    }

    @Test
    fun testNavigateFromConnectionToSdkSignalWithScript() {
        gd.fixture.addFileToProject("emitter.gd", "extends Node2D\n")
        gd.doConfiguredTest(
            dir = false,
            configureFileName = "scene.tscn",
            fileContents = """
                [gd_scene load_steps=2 format=3]

                [ext_resource type="Script" path="res://emitter.gd" id="1"]

                [node name="Root" type="Node"]

                [node name="Emitter" type="Node2D" parent="."]
                script = ExtResource("1")

                [connection signal="draw" from="Emitter" to="." method="_on_draw"]
                """.trimIndent(),
        ) {
            checkGotoDeclaration(
                fromSignature = "signal=\"<caret>draw\"",
                declarationSignature = "signal <caret>draw(",
                expectedFileName = "CanvasItem.generated.gd",
            )
        }
    }
}
