package com.jetbrains.godot.tscn.toolWindow.model

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertAll
import org.junitpioneer.jupiter.cartesian.CartesianTest
import tscn.toolWindow.model.SceneTreeEditorDropHandler
import tscn.toolWindow.model.SceneTreeEditorDropHandler.SceneTreeEditorDropDependencies
import tscn.toolWindow.model.SceneTreeEditorDropHandler.ScriptPathAndName

class SceneTreeEditorDropHandlerTest {
    private fun dependencies(
        isCsFile: Boolean = false,
        ctrlDown: Boolean = false,
        altDown: Boolean = false,
        isUnique: Boolean = false,
        nodeName: String = "NodeName",
    ) = SceneTreeEditorDropDependencies(
        nodeParent = "Nested",
        nodeName = nodeName,
        nodeType = "Node2D",
        isUnique = isUnique,
        isCsFile = isCsFile,
        ctrlDown = ctrlDown,
        altDown = altDown,
        scriptInfo = ScriptPathAndName(
            scriptParentPath = "Nested/Nested1.1",
            scriptNodeName = "Nested1.1.1",
        ),
    )

    @CartesianTest(name = CartesianTest.ARGUMENTS_PLACEHOLDER)
    fun csharpDropProducesGetNodeWithoutAltDown(
        @CartesianTest.Values(booleans = [true, false]) ctrlDown: Boolean,
        @CartesianTest.Values(booleans = [true, false]) altDown: Boolean,
    ) {
        if (altDown && !ctrlDown) {
            assertEquals(
                "[Export] private Node2D nodeName;",
                SceneTreeEditorDropHandler.assembleFinalText(dependencies(isCsFile = true, ctrlDown = false, altDown = true)),
                "ctrlDown=false, altDown=true",
            )
        } else {
            assertEquals(
                "GetNode<Node2D>(\"../../NodeName\");",
                SceneTreeEditorDropHandler.assembleFinalText(dependencies(isCsFile = true, ctrlDown = ctrlDown, altDown = altDown)),
                "ctrlDown=$ctrlDown, altDown=$altDown",
            )
        }
    }

    @Test
    fun nonStandardNodeNameProducesRightVariableName() {
        val uncanonicalName = "123Foo_barBaz567"
        val csPath = "GetNode<Node2D>(\"../../$uncanonicalName\");"
        val gdPath = "$\"../../$uncanonicalName\""
        assertAll(
            {
                assertEquals(
                    "[Export] private Node2D _123FooBarBaz567;",
                    SceneTreeEditorDropHandler.assembleFinalText(
                        dependencies(
                            nodeName = uncanonicalName,
                            isCsFile = true,
                            ctrlDown = false,
                            altDown = true
                        )
                    ),
                )
            },
            {
                assertEquals(
                    csPath,
                    SceneTreeEditorDropHandler.assembleFinalText(
                        dependencies(
                            nodeName = uncanonicalName,
                            isCsFile = true,
                            ctrlDown = false,
                            altDown = false,
                        )
                    ),
                )
            },
            {
                assertEquals(
                    gdPath,
                    SceneTreeEditorDropHandler.assembleFinalText(
                        dependencies(
                            nodeName = uncanonicalName,
                            isCsFile = false,
                            ctrlDown = false,
                            altDown = false,
                        )
                    ),
                )
            },
            {
                assertEquals(
                    "@export var _123_foo_bar_baz_567: Node2D",
                    SceneTreeEditorDropHandler.assembleFinalText(
                        dependencies(
                            nodeName = uncanonicalName,
                            isCsFile = false,
                            ctrlDown = false,
                            altDown = true,
                        )
                    ),
                )
            },
            {
                assertEquals(
                    "@onready var _123_foo_bar_baz_567: Node2D = $gdPath",
                    SceneTreeEditorDropHandler.assembleFinalText(
                        dependencies(
                            nodeName = uncanonicalName,
                            isCsFile = false,
                            ctrlDown = true,
                            altDown = false,
                        )
                    ),
                )
            },
        )
    }

    @Test
    fun gdscriptDropProducesSnippetForModifier() {
        assertEquals(
            "$\"../../NodeName\"",
            SceneTreeEditorDropHandler.assembleFinalText(dependencies()),
        )
        assertEquals(
            "@onready var node_name: Node2D = $\"../../NodeName\"",
            SceneTreeEditorDropHandler.assembleFinalText(dependencies(ctrlDown = true)),
        )
        assertEquals(
            "@export var node_name: Node2D",
            SceneTreeEditorDropHandler.assembleFinalText(dependencies(altDown = true)),
        )
        assertEquals(
            "@onready var node_name: Node2D = $\"../../NodeName\"",
            SceneTreeEditorDropHandler.assembleFinalText(dependencies(ctrlDown = true, altDown = true)),
        )
    }

    @CartesianTest(name = CartesianTest.ARGUMENTS_PLACEHOLDER)
    fun csharpUniqueNodeDrop(
        @CartesianTest.Values(booleans = [true, false]) ctrlDown: Boolean,
        @CartesianTest.Values(booleans = [true, false]) altDown: Boolean,
    ) {
        if (altDown && !ctrlDown) {
            assertEquals(
                "[Export] private Node2D nodeName;",
                SceneTreeEditorDropHandler.assembleFinalText(
                    dependencies(
                        isCsFile = true,
                        isUnique = true,
                        ctrlDown = false,
                        altDown = true
                    )
                ),
            )
        } else {
            assertEquals(
                "GetNode<Node2D>(\"%NodeName\");",
                SceneTreeEditorDropHandler.assembleFinalText(
                    dependencies(
                        isCsFile = true,
                        isUnique = true,
                        ctrlDown = ctrlDown,
                        altDown = altDown
                    )
                ),
            )
        }

    }

    @Test
    fun uniqueNodeInGdscript() {
        assertEquals(
            "%NodeName",
            SceneTreeEditorDropHandler.assembleFinalText(dependencies(isUnique = true)),
        )
        assertEquals(
            "@onready var node_name: Node2D = %NodeName",
            SceneTreeEditorDropHandler.assembleFinalText(dependencies(isUnique = true, ctrlDown = true)),
        )
        assertEquals(
            "@export var node_name: Node2D",
            SceneTreeEditorDropHandler.assembleFinalText(dependencies(isUnique = true, altDown = true)),
        )
        assertEquals(
            "@onready var node_name: Node2D = %NodeName",
            SceneTreeEditorDropHandler.assembleFinalText(dependencies(isUnique = true, ctrlDown = true, altDown = true)),
        )
    }
}
