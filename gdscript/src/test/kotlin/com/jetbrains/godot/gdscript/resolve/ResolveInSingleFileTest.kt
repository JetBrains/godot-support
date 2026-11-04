package com.jetbrains.godot.gdscript.resolve

import com.intellij.openapi.application.EDT
import com.intellij.testFramework.common.timeoutRunBlocking
import com.intellij.testFramework.junit5.TestApplication
import com.jetbrains.godot.gdscript.GdResolveFixture
import com.jetbrains.godot.gdscript.gdResolveFixture
import com.jetbrains.rider.test.annotations.Subsystem
import com.jetbrains.rider.test.annotations.report.ChecklistItems
import com.jetbrains.rider.test.reporting.SubsystemConstants
import gdscript.psi.GdConstDeclTl
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.junit.jupiter.api.Disabled
import org.junit.jupiter.api.Test

@Subsystem(SubsystemConstants.GODOT)
@ChecklistItems(["GDScript/Resolve and navigation"])
@TestApplication
class ResolveInSingleFileTest {
    private val gd: GdResolveFixture by gdResolveFixture("gdscript/resolve")

    @Test
    fun testUnresolvableMembersOnNestedClasses() = timeoutRunBlocking {
        withContext(Dispatchers.EDT) {
            val file = gd.loadByTestName()
            val annotated = gd.dumpResolvesWithInlineMarkers(file)
            gd.assertGold(annotated)
        }
    }

    @Test
    @Disabled("RIDER-131644 Resolve `super` base class, defined in the same file")
    fun testResolveSuperClass() {
        val file = gd.loadByTestName()
        val annotated = gd.dumpResolvesWithInlineMarkers(file)
        gd.assertGold(annotated)
    }

    @Test
    fun testGdDictionaryLuaStyle() = timeoutRunBlocking {
        withContext(Dispatchers.EDT) {
            val file = gd.loadByTestName()
            val annotated = gd.dumpResolvesWithInlineMarkers(file)
            gd.assertGold(annotated)
        }
    }

    @Test
    fun testGdDictionaryStringStyle() = timeoutRunBlocking {
        withContext(Dispatchers.EDT) {
            val file = gd.loadByTestName()
            val annotated = gd.dumpResolvesWithInlineMarkers(file)
            gd.assertGold(annotated)
        }
    }

    @Test
    fun testNestedGdDictionary() = timeoutRunBlocking {
        withContext(Dispatchers.EDT) {
            val file = gd.loadByTestName()
            val annotated = gd.dumpResolvesWithInlineMarkers(file)
            gd.assertGold(annotated)
        }
    }

    @Test
    fun testEnum1() = timeoutRunBlocking {
        withContext(Dispatchers.EDT) {
            val file = gd.loadByTestName()
            val annotated = gd.dumpResolvesWithInlineMarkers(file)
            gd.assertGold(annotated)
        }
    }

    @Test
    fun testUnnamedEnums() = timeoutRunBlocking {
        withContext(Dispatchers.EDT) {
            val file = gd.loadByTestName()
            val annotated = gd.dumpResolvesWithInlineMarkers(file)
            gd.assertGold(annotated)
        }
    }

    @Test
    fun testUnnamedEnumsOuterConflicts() = timeoutRunBlocking {
        withContext(Dispatchers.EDT) {
            val file = gd.loadByTestName()
            val annotated = gd.dumpResolvesWithInlineMarkers(file)
            gd.assertGold(annotated)
        }
    }

    @Test
    @Disabled("RIDER-132087")
    fun testExtendsClass() {
        val file = gd.loadByTestName()
        val annotated = gd.dumpResolvesWithInlineMarkers(file)
        gd.assertGold(annotated)
    }

    @Test
    fun testNewResolvesToConstructorInit() = timeoutRunBlocking {
        withContext(Dispatchers.EDT) {
            val file = gd.loadByTestName()
            val annotated = gd.dumpResolvesWithInlineMarkers(file)
            gd.assertGold(annotated)
        }
    }

    @Test
    fun testSelfReferentialConstNoStackOverflow() = timeoutRunBlocking {
        withContext(Dispatchers.EDT) {
            val file = gd.loadByTestName()
            // RIDER-137850 Calling returnType triggers the infinite type-inference chain without the fix
            file.children.filterIsInstance<GdConstDeclTl>().forEach { it.returnType }
        }
    }

    @Test
    fun testResolveSetGet() = timeoutRunBlocking {
        withContext(Dispatchers.EDT) {
            val file = gd.loadByTestName()
            val annotated = gd.dumpResolvesWithInlineMarkers(file)
            gd.assertGold(annotated)
        }
    }

    @Test
    fun testChainedReferenceBackslash() = timeoutRunBlocking {
        withContext(Dispatchers.EDT) {
            val file = gd.loadByTestName()
            val annotated = gd.dumpResolvesWithInlineMarkers(file)
            gd.assertGold(annotated)
        }
    }

    @Test
    fun testLocalScopeDoesNotLeakAcrossMethods() = timeoutRunBlocking {
        withContext(Dispatchers.EDT) {
            val file = gd.loadByTestName()
            val annotated = gd.dumpResolvesWithInlineMarkers(file)
            gd.assertGold(annotated)
        }
    }

    @Test
    fun testLocalScopeEndsAtBlockBoundary() = timeoutRunBlocking {
        withContext(Dispatchers.EDT) {
            val file = gd.loadByTestName()
            val annotated = gd.dumpResolvesWithInlineMarkers(file)
            gd.assertGold(annotated)
        }
    }

    @Test
    fun testForLoopVariableVisibleInBody() = timeoutRunBlocking {
        withContext(Dispatchers.EDT) {
            val file = gd.loadByTestName()
            val annotated = gd.dumpResolvesWithInlineMarkers(file)
            gd.assertGold(annotated)
        }
    }

    /**
     * Documents an accepted simplification: [gdscript.polySymbols.scope.GdLocalSymbolsStructuredScope]
     * scopes on PSI containment, not statement order, so locals within the same flat suite are
     * mutually visible regardless of textual position — a forward reference resolves here, where
     * the classic PSI-based resolve would have left it unresolved.
     */
    @Test
    fun testLocalVariableVisibleBeforeDeclarationInSameSuite() = timeoutRunBlocking {
        withContext(Dispatchers.EDT) {
            val file = gd.loadByTestName()
            val annotated = gd.dumpResolvesWithInlineMarkers(file)
            gd.assertGold(annotated)
        }
    }
}
