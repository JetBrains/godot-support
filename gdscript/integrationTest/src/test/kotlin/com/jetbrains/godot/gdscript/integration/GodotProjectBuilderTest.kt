package com.jetbrains.godot.gdscript.integration

import com.jetbrains.godot.test.project.GodotSourceBreakpoint
import com.jetbrains.godot.test.project.godotProject
import com.jetbrains.godot.test.project.godotProjectFromTestData
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.api.io.TempDir
import java.nio.file.Path
import kotlin.io.path.createDirectories
import kotlin.io.path.writeText

class GodotProjectBuilderTest {
    @Test
    fun `single script project generates the existing project settings`() {
        val project = godotProject(name = "DapCompletion") {
            script("main.gd", "extends Node")
            scene("main.tscn", main = true) {
                root("Main", "Node", "main.gd")
            }
            runArgs("--display-driver", "headless", "--audio-driver", "Dummy")
        }

        assertEquals(
            """
                config_version=5
    
                [application]
    
                config/name="DapCompletion"
                run/main_scene="res://main.tscn"
    
                [editor]
    
                run/main_run_args="--display-driver headless --audio-driver Dummy"
            """.trimIndent(), project.files.getValue("project.godot"))
    }

    @Test
    fun `single script project generates the existing main scene`() {
        val project = godotProject(name = "DapCompletion") {
            script("main.gd", "extends Node")
            scene("main.tscn", main = true) {
                root("Main", "Node", "main.gd")
            }
            runArgs("--display-driver", "headless", "--audio-driver", "Dummy")
        }

        assertEquals(
            """
                [gd_scene load_steps=2 format=3]
    
                [ext_resource type="Script" path="res://main.gd" id="1_main"]
    
                [node name="Main" type="Node"]
                script = ExtResource("1_main")
            """.trimIndent(), project.files.getValue("main.tscn"))
    }

    @Test
    fun `multi-scene project generates the complete scene text`() {
        val project = godotProject(name = "Nested") {
            script("scripts/world.gd", "extends Node\n# <bp:ready>\nfunc ready(): pass")
            script("scripts/player.gd", "extends Node2D")
            script("scripts/weapon.gd", "extends Node2D")
            script("scripts/global.gd", "extends Node")
            scene("world.tscn", main = true) {
                root("World", "Node", "scripts/world.gd") {
                    node("Player", "Node2D", "scripts/player.gd") {
                        node("Weapon", "Node2D", "scripts/weapon.gd")
                    }
                    node("Camera", "Camera2D")
                }
            }
            scene("menu.tscn") {
                root("Menu", "Control")
            }
            autoload("Global", "scripts/global.gd")
            runArgs("--headless")
        }

        assertEquals(
            """
                [gd_scene load_steps=4 format=3]
    
                [ext_resource type="Script" path="res://scripts/world.gd" id="1_world"]
    
                [ext_resource type="Script" path="res://scripts/player.gd" id="2_player"]
    
                [ext_resource type="Script" path="res://scripts/weapon.gd" id="3_weapon"]
    
                [node name="World" type="Node"]
                script = ExtResource("1_world")
    
                [node name="Player" type="Node2D" parent="."]
                script = ExtResource("2_player")
    
                [node name="Weapon" type="Node2D" parent="Player"]
                script = ExtResource("3_weapon")
    
                [node name="Camera" type="Camera2D" parent="."]
            """.trimIndent(), project.files.getValue("world.tscn"))
    }

    @Test
    fun `multi-scene project generates the complete project settings`() {
        val project = godotProject(name = "Nested") {
            script("scripts/world.gd", "extends Node\n# <bp:ready>\nfunc ready(): pass")
            script("scripts/player.gd", "extends Node2D")
            script("scripts/weapon.gd", "extends Node2D")
            script("scripts/global.gd", "extends Node")
            scene("world.tscn", main = true) {
                root("World", "Node", "scripts/world.gd") {
                    node("Player", "Node2D", "scripts/player.gd") {
                        node("Weapon", "Node2D", "scripts/weapon.gd")
                    }
                    node("Camera", "Camera2D")
                }
            }
            scene("menu.tscn") {
                root("Menu", "Control")
            }
            autoload("Global", "scripts/global.gd")
            runArgs("--headless")
        }

        assertEquals(
            """
                config_version=5
    
                [application]
    
                config/name="Nested"
                run/main_scene="res://world.tscn"
    
                [autoload]
    
                Global="*res://scripts/global.gd"
    
                [editor]
    
                run/main_run_args="--headless"
            """.trimIndent(), project.files.getValue("project.godot"))
    }

    @Test
    fun `deeply nested nodes use the complete parent path`() {
        val project = godotProject(name = "Deep") {
            scene("deep.tscn") {
                root("One", "Node") {
                    node("Two", "Node") {
                        node("Three", "Node") {
                            node("Four", "Node") {
                                node("Five", "Node")
                            }
                        }
                    }
                }
            }
        }

        assertEquals(
            """
                [gd_scene load_steps=1 format=3]
    
                [node name="One" type="Node"]
    
                [node name="Two" type="Node" parent="."]
    
                [node name="Three" type="Node" parent="Two"]
    
                [node name="Four" type="Node" parent="Two/Three"]
    
                [node name="Five" type="Node" parent="Two/Three/Four"]
            """.trimIndent(), project.files.getValue("deep.tscn"))
    }

    @Test
    fun `project builder extracts a breakpoint from a nested script`() {
        val project = godotProject(name = "Markers") {
            script("scripts/main.gd", "extends Node\n# <bp:ready>\nfunc ready(): pass")
        }

        assertEquals(GodotSourceBreakpoint("scripts/main.gd", 2), project.breakpoints.getValue("ready"))
        assertEquals("extends Node\n\nfunc ready(): pass", project.files.getValue("scripts/main.gd"))
    }

    @Test
    fun `test data reader uses the directory name when project name is absent`(@TempDir directory: Path) {
        directory.resolve("project.godot").writeText("config_version=5\n")

        assertEquals(directory.fileName.toString(), godotProjectFromTestData(directory).name)
    }

    @Test
    fun `test data reader creates the same model`(@TempDir directory: Path) {
        val inline = godotProject(name = "Copied") {
            script("main.gd", "extends Node")
            scene("main.tscn", main = true) {
                root("Main", "Node", "main.gd")
            }
        }
        inline.files.forEach { (path, text) ->
            val file = directory.resolve(path)
            file.parent?.createDirectories()
            file.writeText(text)
        }

        assertEquals(inline, godotProjectFromTestData(directory))
    }

    @Test
    fun `duplicate breakpoint markers fail`() {
        assertThrows<IllegalArgumentException> {
            godotProject(name = "Duplicate") {
                script("first.gd", "# <bp:stop>")
                script("second.gd", "# <bp:stop>")
            }
        }
    }

    @Test
    fun `two main scenes fail`() {
        assertThrows<IllegalArgumentException> {
            godotProject(name = "TwoMains") {
                scene("first.tscn", main = true) { root("First", "Node") }
                scene("second.tscn", main = true) { root("Second", "Node") }
            }
        }
    }

}
