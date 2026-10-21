package com.jetbrains.godot.gdscript.model

import com.intellij.polySymbols.PolySymbol
import com.intellij.polySymbols.query.PolySymbolQueryExecutor
import com.intellij.polySymbols.query.PolySymbolQueryExecutorFactory
import com.intellij.testFramework.junit5.TestApplication
import com.jetbrains.godot.gdscript.GdModelFixture
import com.jetbrains.godot.gdscript.gdModelFixture
import gdscript.polySymbols.GdClassSymbol
import gdscript.polySymbols.GdPolySymbolKind
import gdscript.polySymbols.index.GdPolySymbolQueriesUtil
import gdscript.polySymbols.index.GdSdkPolySymbolIndexUtil
import gdscript.polySymbols.sdk.GdSdkEnumSymbol
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test

@TestApplication
class GdCoreSdkModelTest {
    private val gd: GdModelFixture by gdModelFixture()

    companion object{
        const val Node2D = "Node2D"
        const val CanvasItem = "CanvasItem"
        const val Node = "Node"
        const val Vector2 = "Vector2"
        const val GDScript = "@GDScript"
    }

    @Test
    fun testSdkClassesModel() {
        val executor: PolySymbolQueryExecutor = GdSdkPolySymbolIndexUtil.getQueryExecutor(gd.project)

        val classes: List<PolySymbol> = GdPolySymbolQueriesUtil.listSdkClassSymbols(executor)
        assertNotNull(classes, "Classes not found")

        // Find Node2D
        val node2DSymbol = GdPolySymbolQueriesUtil.getSdkClassSymbol(executor, Node2D)
        assertNotNull(node2DSymbol, "$Node2D symbol not found")
        assertEquals(Node2D, node2DSymbol!!.name)

        val canvasItemSymbol = GdPolySymbolQueriesUtil.getSdkClassSymbol(executor, CanvasItem)
        assertNotNull(canvasItemSymbol, "$CanvasItem symbol not found")
        assertEquals(CanvasItem, canvasItemSymbol!!.name)

        // Check inheritance
        val superSymbol: GdClassSymbol? = node2DSymbol.resolveSuperClassSymbol()
        assertNotNull(superSymbol, "$Node2D should have a super class")
        assertEquals(CanvasItem, superSymbol!!.classId)

        val superSuperSymbol: GdClassSymbol? = superSymbol.resolveSuperClassSymbol()
        assertNotNull(superSuperSymbol, "$Node should have a super class")
        assertEquals(Node, superSuperSymbol!!.classId)
    }

    @Test
    fun testSdkClassMembersModel() {
        val sdkExecutor: PolySymbolQueryExecutor = GdSdkPolySymbolIndexUtil.getQueryExecutor(gd.project)

        val node2DSymbol = GdPolySymbolQueriesUtil.getSdkClassSymbol(sdkExecutor, Node2D)
        assertNotNull(node2DSymbol, "$Node2D symbol not found")
        assertEquals(Node2D, node2DSymbol!!.name)

        // Check direct members (Node2D has 'position', doesn't have 'visible')
        val node2DDirectMemberExecutor = PolySymbolQueryExecutorFactory.createCustom{
            addRootScope(node2DSymbol.directMemberScope)
        }
        val directMembers = GdPolySymbolQueriesUtil.listSdkMemberSymbols(node2DDirectMemberExecutor, GdPolySymbolKind.PROPERTY)

        assertTrue(directMembers.isNotEmpty(), "$Node2D should have direct members")
        assertTrue(directMembers.any { it.name == "position" }, "Should contain 'position'")
        assertFalse(directMembers.any { it.name == "visible" }, "Should not contain 'visible' in the direct member scope")

        // Check inherited members (Node has 'name', 'visible', but not 'position')
        val inheritedScopes = node2DSymbol.inheritedQueryScopes()
        val node2DInheritedScopeExecutor = PolySymbolQueryExecutorFactory.createCustom{
            addRootScopes(inheritedScopes)
        }
        val allInheritedMembers = GdPolySymbolQueriesUtil.listSdkMemberSymbols(node2DInheritedScopeExecutor, GdPolySymbolKind.PROPERTY)

        assertFalse(allInheritedMembers.any { it.name == "position" }, "Should not contain 'position'")
        assertTrue(allInheritedMembers.any { it.name == "visible" }, "Should contain inherited 'visible' from $CanvasItem")
        assertTrue(allInheritedMembers.any { it.name == "name" }, "Should contain inherited 'name' from $Node")


        // Check all members (Node2D has 'position', 'visible', 'name')
        val node2DExecutor = PolySymbolQueryExecutorFactory.createCustom{
            addRootScopes(node2DSymbol.queryScope)
        }
        val allMembers = GdPolySymbolQueriesUtil.listSdkMemberSymbols(node2DExecutor, GdPolySymbolKind.PROPERTY)

        assertTrue(allMembers.isNotEmpty(), "$Node2D should have members")
        assertTrue(allMembers.any { it.name == "position" }, "Should contain 'position'")
        assertTrue(allMembers.any { it.name == "visible" }, "Should contain 'visible' from $CanvasItem")
        assertTrue(allMembers.any { it.name == "name" }, "Should contain 'name' from $Node")

        val processModeSymbol = GdPolySymbolQueriesUtil.getSymbol(
            node2DExecutor, GdPolySymbolKind.ENUM, "ProcessMode", GdSdkEnumSymbol::class.java
        )
        assertNotNull(processModeSymbol, "'ProcessMode' property should be accessible on $Node2D")
        assertEquals("ProcessMode", processModeSymbol!!.name)

        val processModeExecutor = PolySymbolQueryExecutorFactory.createCustom{
            addRootScopes(processModeSymbol.queryScope)
        }

        val processModeValues = GdPolySymbolQueriesUtil.listSdkMemberSymbols(processModeExecutor, GdPolySymbolKind.ENUM_VALUE)
        assertTrue(processModeValues.any { it.name == "PROCESS_MODE_INHERIT" }, "Should contain 'PROCESS_MODE_INHERIT' from $Node.ProcessMode")

    }

    @Test
    fun testSdkOperationsModel() {
        val executor: PolySymbolQueryExecutor = GdSdkPolySymbolIndexUtil.getOperationsQueryExecutor(gd.project)

        val operators = GdPolySymbolQueriesUtil.listSdkMemberSymbols(executor, GdPolySymbolKind.OPERATOR)

        assertTrue(operators.isNotEmpty(), "Should find operators from $Vector2.xml")
        assertTrue(operators.any { it.name == "Vector2+Vector2" }, "Should contain $Vector2 addition")
        assertTrue(operators.any { it.name == "Vector2*float" }, "Should contain multiplication with floats")
    }

    @Test
    fun testSdkAnnotationsModel() {
        // TODO add parameter check
        val exportAnnotation = GdPolySymbolQueriesUtil.getAnnotationSymbol(gd.project, "export")
        assertNotNull(exportAnnotation, "Should contain @export")
    }

    @Test
    fun testSdkGlobalScopeModel(){
        val executor: PolySymbolQueryExecutor = GdSdkPolySymbolIndexUtil.getQueryExecutor(gd.project)

        val globalConstants = GdPolySymbolQueriesUtil.listSdkMemberSymbols(executor, GdPolySymbolKind.CONSTANT)

        assertTrue(globalConstants.isNotEmpty(), "Should find global constants")
        assertTrue(globalConstants.any { it.name == "PI" }, "Should contain 'PI'")

        val globalMethods = GdPolySymbolQueriesUtil.listSdkMemberSymbols(executor, GdPolySymbolKind.METHOD)
        assertTrue(globalMethods.any { it.name == "print" }, "Should contain 'print'")
    }
}
