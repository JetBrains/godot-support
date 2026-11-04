package com.jetbrains.godot.gdscript.resolve

import com.intellij.openapi.application.EDT
import com.intellij.testFramework.common.timeoutRunBlocking
import com.intellij.testFramework.junit5.TestApplication
import com.jetbrains.godot.gdscript.GdResolveFixture
import com.jetbrains.godot.gdscript.gdResolveFixture
import com.jetbrains.rider.test.annotations.Subsystem
import com.jetbrains.rider.test.annotations.report.ChecklistItems
import com.jetbrains.rider.test.reporting.SubsystemConstants
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.junit.jupiter.api.Test

@Subsystem(SubsystemConstants.GODOT)
@ChecklistItems(["GDScript/Resolve and navigation"])
@TestApplication
class ResolveMultiFileTest {
    private val gd: GdResolveFixture by gdResolveFixture("gdscript/resolve")

    @Test
    fun testMultiFileReference() = timeoutRunBlocking {
        withContext(Dispatchers.EDT) {
            val files = gd.loadFilesByTestName()
            val annotated = gd.dumpResolvesForFiles(files)
            gd.assertGold(annotated)
        }
    }

    @Test
    fun testOuterStaticAccess() = timeoutRunBlocking {
        withContext(Dispatchers.EDT) {
            val files = gd.loadFilesByTestName()
            val annotated = gd.dumpResolvesForFiles(files)
            gd.assertGold(annotated)
        }
    }

    @Test
    fun testGlobals() = timeoutRunBlocking {
        withContext(Dispatchers.EDT) {
            val files = gd.loadFilesFromSubdirAsProjectRoot()
            val annotated = gd.dumpResolvesForFiles(files)
            gd.assertGold(annotated)
        }
    }

    @Test
    fun testTscnGlobals() = timeoutRunBlocking {
        withContext(Dispatchers.EDT) {
            val files = gd.loadFilesFromSubdirAsProjectRoot()
            val useFile = files.find { it.name == "use.gd" }!!
            val annotated = gd.dumpResolvesWithInlineMarkers(useFile)
            gd.assertGold(annotated)
        }
    }

    @Test
    fun testEnums() = timeoutRunBlocking {
        withContext(Dispatchers.EDT) {
            val files = gd.loadFilesFromSubdirAsProjectRoot()
            val annotated = gd.dumpResolvesForFiles(files)
            gd.assertGold(annotated)
        }
    }

    @Test
    fun testGdAutoloadUid() = timeoutRunBlocking {
        withContext(Dispatchers.EDT) {
            val files = gd.loadFilesFromSubdirAsProjectRoot()
            val useFile = files.find { it.name == "use.gd" }!!
            val annotated = gd.dumpResolvesWithInlineMarkers(useFile)
            gd.assertGold(annotated)
        }
    }

    @Test
    fun testTscnAutoloadUid() = timeoutRunBlocking {
        withContext(Dispatchers.EDT) {
            val files = gd.loadFilesFromSubdirAsProjectRoot()
            val useFile = files.find { it.name == "new_gd_autoload.gd" }!!
            val annotated = gd.dumpResolvesWithInlineMarkers(useFile)
            gd.assertGold(annotated)
        }
    }
}
