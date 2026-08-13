package com.jetbrains.godot.tscn.toolWindow.model

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
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
    ) = SceneTreeEditorDropDependencies(
        nodeParent = "Nested",
        nodeName = "NodeName",
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

    @CartesianTest
    fun csharpDropProducesGetNodeRegardlessOfModifiers(
        @CartesianTest.Values(booleans = [true, false]) ctrlDown: Boolean,
        @CartesianTest.Values(booleans = [true, false]) altDown: Boolean,
        ) {
        assertEquals(
            "GetNode<Node2D>(\"../../NodeName\");",
            SceneTreeEditorDropHandler.assembleFinalText(dependencies(isCsFile = true, ctrlDown = ctrlDown, altDown = altDown)),
            "ctrlDown=$ctrlDown, altDown=$altDown",
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

    @CartesianTest
    fun csharpUniqueNodeDropProducesGetNodeRegardlessOfModifiers(
        @CartesianTest.Values(booleans = [true, false]) ctrlDown: Boolean,
        @CartesianTest.Values(booleans = [true, false]) altDown: Boolean,
    ) {
        assertEquals(
            "GetNode<Node2D>(\"%NodeName\");",
            SceneTreeEditorDropHandler.assembleFinalText(
                dependencies(
                    isCsFile = true,
                    isUnique = true,
                    ctrlDown = ctrlDown,
                    altDown = altDown)
            ),
        )
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
