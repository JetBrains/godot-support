package com.jetbrains.godot.tscn.toolWindow.model

import org.junit.jupiter.api.Assertions.assertArrayEquals
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertNotSame
import org.junit.jupiter.api.Assertions.assertSame
import org.junit.jupiter.api.Assertions.assertThrows
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import tscn.toolWindow.model.SceneDragPayload
import tscn.toolWindow.model.SceneNodeDrag
import tscn.toolWindow.model.SceneNodeTransferable
import tscn.toolWindow.model.SceneNodeTransferable.Companion.SCENE_NODE_FLAVOR
import java.awt.datatransfer.DataFlavor
import java.awt.datatransfer.SystemFlavorMap
import java.awt.datatransfer.UnsupportedFlavorException
import java.io.InputStream

class SceneNodeTransferableTest {
    private val nativeMimeType = "application/x-jetbrains-godot-scene-nodes+json"
    private val nativeFlavor = DataFlavor("$nativeMimeType;class=java.io.InputStream")

    private fun node(name: String, parent: String = ".", isUnique: Boolean = false) =
        SceneNodeDrag("Node2D", parent, name, isUnique)

    private fun nativeData(transferable: SceneNodeTransferable): String =
        (transferable.getTransferData(nativeFlavor) as InputStream).use { it.readBytes().toString(Charsets.UTF_8) }

    @Test
    fun advertisesLocalPayloadAndNativeSceneData() {
        val transferable = SceneNodeTransferable(SceneDragPayload(hashMapOf(), listOf(node("Child"))))

        assertArrayEquals(arrayOf(SCENE_NODE_FLAVOR, nativeFlavor), transferable.transferDataFlavors)
        assertTrue(transferable.isDataFlavorSupported(nativeFlavor))
        assertEquals(InputStream::class.java, transferable.transferDataFlavors[1].representationClass)
        val flavorMap = SystemFlavorMap.getDefaultFlavorMap() as SystemFlavorMap
        assertTrue(flavorMap.getNativesForFlavor(nativeFlavor).contains(nativeMimeType))
        assertTrue(flavorMap.getFlavorsForNative(nativeMimeType).contains(nativeFlavor))
    }

    @Test
    fun keepsLocalPayloadAndNodeMapping() {
        val root = node("Root", parent = "")
        val child = node("Child")
        val payload = SceneDragPayload(hashMapOf("res://root.gd" to mutableListOf(root)), listOf(child))
        val transferable = SceneNodeTransferable(payload)

        assertTrue(transferable.isDataFlavorSupported(SCENE_NODE_FLAVOR))
        assertTrue(transferable.transferDataFlavors.any(SceneNodeTransferable::isSceneTreeFlavor))
        assertSame(payload, transferable.getTransferData(SCENE_NODE_FLAVOR))
    }

    @Test
    fun nativeDataContainsPayloadInSelectionOrder() {
        val nodes = listOf(
            node("Root", parent = ""),
            node("Child"),
            node("\u96EA \"node\"\n", parent = "Parent/Nested", isUnique = true),
        )
        val payload = SceneDragPayload(hashMapOf("res://\u96EA.gd" to nodes.toMutableList()), nodes)
        val transferable = SceneNodeTransferable(payload)
        val expectedNodes = """
            [
                {"nodeType":"Node2D","nodeParentPath":"","nodeName":"Root","isUnique":false},
                {"nodeType":"Node2D","nodeParentPath":".","nodeName":"Child","isUnique":false},
                {"nodeType":"Node2D","nodeParentPath":"Parent/Nested","nodeName":"${'\u96EA'} \"node\"\n","isUnique":true}
            ]
        """.lines().joinToString("") { it.trim() }

        assertEquals(
            """{"nodeMapping":{"res://${'\u96EA'}.gd":$expectedNodes},"nodes":$expectedNodes}""",
            nativeData(transferable),
        )
    }

    @Test
    fun emptySelectionProducesEmptyPayload() {
        val transferable = SceneNodeTransferable(SceneDragPayload(hashMapOf(), emptyList()))

        assertEquals("""{"nodeMapping":{},"nodes":[]}""", nativeData(transferable))
    }

    @Test
    fun eachNativeRequestReturnsFreshStream() {
        val transferable = SceneNodeTransferable(SceneDragPayload(hashMapOf(), listOf(node("Child"))))
        val first = transferable.getTransferData(nativeFlavor) as InputStream
        val firstBytes = first.use { it.readBytes() }
        val second = transferable.getTransferData(nativeFlavor) as InputStream

        assertNotSame(first, second)
        assertArrayEquals(firstBytes, second.use { it.readBytes() })
    }

    @Test
    fun nativeSceneDataIsNotLocalPayload() {
        assertFalse(SceneNodeTransferable.isSceneTreeFlavor(nativeFlavor))
    }

    @Test
    fun plainTextIsNotScenePayload() {
        assertFalse(SceneNodeTransferable.isSceneTreeFlavor(DataFlavor.stringFlavor))
        val unrelatedLocalFlavor = DataFlavor(DataFlavor.javaJVMLocalObjectMimeType + ";class=java.lang.String")
        assertFalse(SceneNodeTransferable.isSceneTreeFlavor(unrelatedLocalFlavor))
    }

    @Test
    fun rejectsUnsupportedFlavor() {
        val transferable = SceneNodeTransferable(SceneDragPayload(hashMapOf(), listOf(node("Child"))))

        for (flavor in arrayOf(DataFlavor.stringFlavor, DataFlavor.imageFlavor, DataFlavor("$nativeMimeType;class=java.lang.String"))) {
            assertFalse(transferable.isDataFlavorSupported(flavor))
            assertThrows(UnsupportedFlavorException::class.java) {
                transferable.getTransferData(flavor)
            }
        }
    }
}