package com.jetbrains.godot.gdscript.polySymbols.model

import com.intellij.polySymbols.PolySymbol
import com.intellij.polySymbols.query.PolySymbolQueryExecutor
import com.intellij.polySymbols.query.PolySymbolQueryExecutorFactory
import com.jetbrains.godot.gdscript.polySymbols.GdPolySymbolModelTestBase
import gdscript.polySymbols.GdPolySymbolKind
import gdscript.polySymbols.GdClassSymbol
import gdscript.polySymbols.index.GdPolySymbolQueriesUtil
import gdscript.polySymbols.index.GdSdkPolySymbolIndexUtil
import gdscript.polySymbols.sdk.GdSdkEnumSymbol
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

@RunWith(JUnit4::class)
class GdCoreSdkPolySymbolModelTest : GdPolySymbolModelTestBase() {

    companion object{
        const val Node2D = "Node2D"
        const val CanvasItem = "CanvasItem"
        const val Node = "Node"
        const val Vector2 = "Vector2"
        const val GDScript = "@GDScript"
    }

    @Test
    fun testSdkClassesModel() {
        val executor: PolySymbolQueryExecutor = GdSdkPolySymbolIndexUtil.getQueryExecutor(project)

        val classes: List<PolySymbol> = GdPolySymbolQueriesUtil.listSdkClassSymbols(executor)
        assertNotNull("Classes not found", classes)

        // Find Node2D
        val node2DSymbol = GdPolySymbolQueriesUtil.getSdkClassSymbol(executor, Node2D)
        assertNotNull("$Node2D symbol not found", node2DSymbol)
        assertEquals(Node2D, node2DSymbol!!.name)

        val canvasItemSymbol = GdPolySymbolQueriesUtil.getSdkClassSymbol(executor, CanvasItem)
        assertNotNull("$CanvasItem symbol not found", canvasItemSymbol)
        assertEquals(CanvasItem, canvasItemSymbol!!.name)

        // Check inheritance
        val superSymbol: GdClassSymbol? = node2DSymbol.resolveSuperClassSymbol()
        assertNotNull("$Node2D should have a super class", superSymbol)
        assertEquals(CanvasItem, superSymbol!!.classId)

        val superSuperSymbol: GdClassSymbol? = superSymbol.resolveSuperClassSymbol()
        assertNotNull("$Node should have a super class", superSuperSymbol)
        assertEquals(Node, superSuperSymbol!!.classId)
    }

    @Test
    fun testSdkClassMembersModel() {
        val sdkExecutor: PolySymbolQueryExecutor = GdSdkPolySymbolIndexUtil.getQueryExecutor(project)

        val node2DSymbol = GdPolySymbolQueriesUtil.getSdkClassSymbol(sdkExecutor, Node2D)
        assertNotNull("$Node2D symbol not found", node2DSymbol)
        assertEquals(Node2D, node2DSymbol!!.name)

        // Check direct members (Node2D has 'position', doesn't have 'visible')
        val node2DDirectMemberExecutor = PolySymbolQueryExecutorFactory.createCustom{
            addRootScope(node2DSymbol.directMemberScope)
        }
        val directMembers = GdPolySymbolQueriesUtil.listSdkMemberSymbols(node2DDirectMemberExecutor, GdPolySymbolKind.PROPERTY)

        assertTrue("$Node2D should have direct members", directMembers.isNotEmpty())
        assertTrue("Should contain 'position'", directMembers.any { it.name == "position" })
        assertFalse("Should not contain 'visible' in the direct member scope", directMembers.any { it.name == "visible" })

        // Check inherited members (Node has 'name', 'visible', but not 'position')
        val inheritedScopes = node2DSymbol.inheritedQueryScopes()
        val node2DInheritedScopeExecutor = PolySymbolQueryExecutorFactory.createCustom{
            addRootScopes(inheritedScopes)
        }
        val allInheritedMembers = GdPolySymbolQueriesUtil.listSdkMemberSymbols(node2DInheritedScopeExecutor, GdPolySymbolKind.PROPERTY)

        assertFalse("Should not contain 'position'", allInheritedMembers.any { it.name == "position" })
        assertTrue("Should contain inherited 'visible' from $CanvasItem", allInheritedMembers.any { it.name == "visible" })
        assertTrue("Should contain inherited 'name' from $Node", allInheritedMembers.any { it.name == "name" })


        // Check all members (Node2D has 'position', 'visible', 'name')
        val node2DExecutor = PolySymbolQueryExecutorFactory.createCustom{
            addRootScopes(node2DSymbol.queryScope)
        }
        val allMembers = GdPolySymbolQueriesUtil.listSdkMemberSymbols(node2DExecutor, GdPolySymbolKind.PROPERTY)

        assertTrue("$Node2D should have members", allMembers.isNotEmpty())
        assertTrue("Should contain 'position'", allMembers.any { it.name == "position" })
        assertTrue("Should contain 'visible' from $CanvasItem", allMembers.any { it.name == "visible" })
        assertTrue("Should contain 'name' from $Node", allMembers.any { it.name == "name" })

        val processModeSymbol = GdPolySymbolQueriesUtil.getSymbol(
            node2DExecutor, GdPolySymbolKind.ENUM, "ProcessMode", GdSdkEnumSymbol::class.java
        )
        assertNotNull("'ProcessMode' property should be accessible on $Node2D", processModeSymbol)
        assertEquals("ProcessMode", processModeSymbol!!.name)

        val processModeExecutor = PolySymbolQueryExecutorFactory.createCustom{
            addRootScopes(processModeSymbol.queryScope)
        }

        val processModeValues = GdPolySymbolQueriesUtil.listSdkMemberSymbols(processModeExecutor, GdPolySymbolKind.ENUM_VALUE)
        assertTrue("Should contain 'PROCESS_MODE_INHERIT' from $Node.ProcessMode", processModeValues.any { it.name == "PROCESS_MODE_INHERIT" })

    }

    @Test
    fun testSdkOperationsModel() {
        val executor: PolySymbolQueryExecutor = GdSdkPolySymbolIndexUtil.getOperationsQueryExecutor(project)

        val operators = GdPolySymbolQueriesUtil.listSdkMemberSymbols(executor, GdPolySymbolKind.OPERATOR)

        assertTrue("Should find operators from $Vector2.xml", operators.isNotEmpty())
        assertTrue("Should contain $Vector2 addition", operators.any { it.name == "Vector2+Vector2" })
        assertTrue("Should contain multiplication with floats", operators.any { it.name == "Vector2*float" })
    }

    @Test
    fun testSdkAnnotationsModel() {
        // TODO add parameter check
        val exportAnnotation = GdPolySymbolQueriesUtil.getAnnotationSymbol(project, "export")
        assertNotNull("Should contain @export", exportAnnotation)
    }

    @Test
    fun testSdkGlobalScopeModel(){
        val executor: PolySymbolQueryExecutor = GdSdkPolySymbolIndexUtil.getQueryExecutor(project)

        val globalConstants = GdPolySymbolQueriesUtil.listSdkMemberSymbols(executor, GdPolySymbolKind.CONSTANT)

        assertTrue("Should find global constants", globalConstants.isNotEmpty())
        assertTrue("Should contain 'PI'", globalConstants.any { it.name == "PI" })

        val globalMethods = GdPolySymbolQueriesUtil.listSdkMemberSymbols(executor, GdPolySymbolKind.METHOD)
        assertTrue("Should contain 'print'", globalMethods.any { it.name == "print" })
    }
}
