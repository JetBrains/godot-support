package com.jetbrains.godot.project

import GdProjectService
import com.intellij.openapi.application.EDT
import com.intellij.openapi.application.runReadActionBlocking
import com.intellij.openapi.command.WriteCommandAction
import com.intellij.openapi.extensions.ExtensionPointName
import com.intellij.openapi.project.Project
import com.intellij.psi.PsiDocumentManager
import com.intellij.psi.PsiFile
import com.intellij.testFramework.ExtensionTestUtil
import com.intellij.testFramework.common.timeoutRunBlocking
import com.intellij.testFramework.junit5.TestApplication
import com.jetbrains.godot.gdscript.GdFixture
import com.jetbrains.godot.gdscript.gdFixture
import com.jetbrains.rider.godot.community.GodotProjectProvider
import com.jetbrains.rider.godot.community.GodotProjectSettingsProvider
import com.jetbrains.rider.godot.community.utils.GodotCommunityUtil
import com.jetbrains.rider.test.annotations.Subsystem
import com.jetbrains.rider.test.annotations.report.ChecklistItems
import com.jetbrains.rider.test.reporting.SubsystemConstants
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNull
import org.junit.jupiter.api.Test

@Subsystem(SubsystemConstants.GODOT)
@ChecklistItems(["GDScript/Project settings"])
@TestApplication
class GodotProjectSettingsProviderTest {
    private val gd: GdFixture by gdFixture("gdscript/project")

    @Test
    fun testEnabledEditorPlugins() {
        configureProject(
            """
            [editor_plugins]
            enabled=PackedStringArray(
                "res://addons/rider-plugin/plugin.cfg", "res://addons/other/plugin.cfg", "res://addons/rider-plugin/plugin.cfg"
            )
        """.trimIndent()
        )

        assertEquals(setOf("res://addons/rider-plugin/plugin.cfg", "res://addons/other/plugin.cfg"), enabledPlugins())
    }

    @Test
    fun testNoEditorPluginsSection() {
        configureProject(
            """
            [application]
            enabled=PackedStringArray("res://addons/other/plugin.cfg")
        """.trimIndent()
        )

        assertEquals(emptySet<String>(), enabledPlugins())
    }

    @Test
    fun testNoEnabledSetting() {
        configureProject(
            """
            [editor_plugins]
            other=true
        """.trimIndent()
        )

        assertEquals(emptySet<String>(), enabledPlugins())
    }

    @Test
    fun testEmptyArray() {
        configureProject("[editor_plugins]\nenabled=PackedStringArray()")

        assertEquals(emptySet<String>(), enabledPlugins())
    }

    @Test
    fun testMultilineArrayWithComments() {
        configureProject(
            """
            [editor_plugins]
            enabled=PackedStringArray(
                "res://addons/rider-plugin/plugin.cfg", ; The first plugin.
                "res://addons/other/plugin.cfg"
            ) ; The enabled plugins.
        """.trimIndent()
        )

        assertEquals(setOf("res://addons/rider-plugin/plugin.cfg", "res://addons/other/plugin.cfg"), enabledPlugins())
    }

    @Test
    fun testEscapedPaths() {
        configureProject(
            """
            [editor_plugins]
            enabled=PackedStringArray(
                "res://addons/a,b/plugin.cfg", "res://addons/a\"b/plugin.cfg",
                "res://addons/a\\b/plugin.cfg", "res://addons/a/plugin.cfg"
            )
        """.trimIndent()
        )

        assertEquals(
            setOf(
                "res://addons/a,b/plugin.cfg", "res://addons/a\"b/plugin.cfg",
                "res://addons/a\\b/plugin.cfg", "res://addons/a/plugin.cfg"
            ), enabledPlugins()
        )
    }

    @Test
    fun testMalformedValuesAreUnavailable() {
        val values = listOf(
            "true",
            "Array(\"res://addons/rider-plugin/plugin.cfg\")",
            "PackedStringArray(\"res://addons/rider-plugin/plugin.cfg\"",
            "PackedStringArray(\"res://addons/rider-plugin/plugin.cfg)",
            "PackedStringArray(\"res://addons/rider-plugin/plugin.cfg\", 1)",
            "PackedStringArray(\"first\" \"second\")",
            "PackedStringArray(\"res://addons/rider-plugin/plugin.cfg\") unexpected",
        )
        val file = configureProject("[editor_plugins]\nenabled=true")
        for (value in values) {
            updateText(file, "[editor_plugins]\nenabled=$value")
            assertNull(enabledPlugins(), value)
        }
    }

    @Test
    fun testUsesGodotProjectRoot() {
        gd.fixture.addFileToProject("project.godot", "[editor_plugins]\nenabled=PackedStringArray(\"wrong\")")
        configureProject("[editor_plugins]\nenabled=PackedStringArray(\"res://addons/rider-plugin/plugin.cfg\")", "nested/project.godot")

        assertEquals(setOf("res://addons/rider-plugin/plugin.cfg"), enabledPlugins())
    }

    @Test
    fun testMissingProjectFileIsUnavailable() {
        val directory = gd.fixture.tempDirFixture.findOrCreateDir("godot")
        GdProjectService.getInstance(gd.project).discoverProject(directory)

        assertNull(enabledPlugins())
    }

    @Test
    fun testMissingProjectRootIsUnavailable() {
        ExtensionTestUtil.maskExtensions(
            ExtensionPointName.create<GodotProjectProvider>("com.intellij.rider.godot.community.godotProjectProvider"),
            emptyList(), gd.testRootDisposable,
        )

        assertNull(enabledPlugins())
    }

    @Test
    fun testMissingSettingsProviderIsUnavailable() {
        ExtensionTestUtil.maskExtensions(settingsProviderEp, emptyList(), gd.testRootDisposable)

        assertNull(enabledPlugins())
    }

    @Test
    fun testSkipsUnavailableProvider() {
        ExtensionTestUtil.maskExtensions(
            settingsProviderEp, listOf(provider(null), provider(setOf("res://addons/other/plugin.cfg"))),
            gd.testRootDisposable
        )

        assertEquals(setOf("res://addons/other/plugin.cfg"), enabledPlugins())
    }

    @Test
    fun testEmptySetDoesNotFallThrough() {
        ExtensionTestUtil.maskExtensions(
            settingsProviderEp, listOf(provider(emptySet()), provider(setOf("res://addons/other/plugin.cfg"))),
            gd.testRootDisposable
        )

        assertEquals(emptySet<String>(), enabledPlugins())
    }

    @Test
    fun testReadsCommittedChanges() {
        val file = configureProject("[editor_plugins]\nenabled=PackedStringArray(\"res://addons/rider-plugin/plugin.cfg\")")
        assertEquals(setOf("res://addons/rider-plugin/plugin.cfg"), enabledPlugins())

        updateText(file, "[editor_plugins]\nenabled=PackedStringArray()")

        assertEquals(emptySet<String>(), enabledPlugins())
    }

    private fun configureProject(text: String, path: String = "project.godot"): PsiFile {
        val file = gd.fixture.addFileToProject(path, text)
        GdProjectService.getInstance(gd.project).discoverProject(file.virtualFile.parent)
        return file
    }

    private fun updateText(file: PsiFile, text: String) {
        val manager = PsiDocumentManager.getInstance(gd.project)
        val document = runReadActionBlocking { manager.getDocument(file)!! }
        WriteCommandAction.runWriteCommandAction(gd.project) {
            document.setText(text)
            manager.commitDocument(document)
        }
    }

    private fun enabledPlugins(): Set<String>? = timeoutRunBlocking {
        withContext(Dispatchers.EDT) {
            GodotCommunityUtil.getEnabledEditorPlugins(gd.project)
        }
    }

    private fun provider(paths: Set<String>?): GodotProjectSettingsProvider = object : GodotProjectSettingsProvider {
        override fun getEnabledEditorPlugins(project: Project): Set<String>? = paths
    }

    private val settingsProviderEp =
        ExtensionPointName.create<GodotProjectSettingsProvider>("com.intellij.rider.godot.community.godotProjectSettingsProvider")
}
