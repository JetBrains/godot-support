package com.jetbrains.godot.project

import GdProjectService
import com.intellij.openapi.command.WriteCommandAction
import com.intellij.openapi.extensions.ExtensionPointName
import com.intellij.openapi.project.Project
import com.intellij.psi.PsiDocumentManager
import com.intellij.psi.PsiFile
import com.intellij.testFramework.ExtensionTestUtil
import com.intellij.testFramework.fixtures.BasePlatformTestCase
import com.intellij.testFramework.fixtures.TempDirTestFixture
import com.intellij.testFramework.fixtures.impl.TempDirTestFixtureImpl
import com.jetbrains.rider.godot.community.GodotProjectProvider
import com.jetbrains.rider.godot.community.GodotProjectSettingsProvider
import com.jetbrains.rider.godot.community.utils.GodotCommunityUtil

class GodotProjectSettingsProviderTest : BasePlatformTestCase() {
    override fun createTempDirTestFixture(): TempDirTestFixture = TempDirTestFixtureImpl()

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

    fun testNoEditorPluginsSection() {
        configureProject(
            """
            [application]
            enabled=PackedStringArray("res://addons/other/plugin.cfg")
        """.trimIndent()
        )

        assertEquals(emptySet<String>(), enabledPlugins())
    }

    fun testNoEnabledSetting() {
        configureProject(
            """
            [editor_plugins]
            other=true
        """.trimIndent()
        )

        assertEquals(emptySet<String>(), enabledPlugins())
    }

    fun testEmptyArray() {
        configureProject("[editor_plugins]\nenabled=PackedStringArray()")

        assertEquals(emptySet<String>(), enabledPlugins())
    }

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

    fun testEscapedPaths() {
        configureProject(
            """
            [editor_plugins]
            enabled=PackedStringArray(
                "res://addons/a,b/plugin.cfg", "res://addons/a\"b/plugin.cfg",
                "res://addons/a\\b/plugin.cfg", "res://addons/\u0061/plugin.cfg"
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
            assertNull(value, enabledPlugins())
        }
    }

    fun testUsesGodotProjectRoot() {
        myFixture.addFileToProject("project.godot", "[editor_plugins]\nenabled=PackedStringArray(\"wrong\")")
        configureProject("[editor_plugins]\nenabled=PackedStringArray(\"res://addons/rider-plugin/plugin.cfg\")", "nested/project.godot")

        assertEquals(setOf("res://addons/rider-plugin/plugin.cfg"), enabledPlugins())
    }

    fun testMissingProjectFileIsUnavailable() {
        val directory = myFixture.tempDirFixture.findOrCreateDir("godot")
        GdProjectService.getInstance(project).discoverProject(directory)

        assertNull(enabledPlugins())
    }

    fun testMissingProjectRootIsUnavailable() {
        ExtensionTestUtil.maskExtensions(
            ExtensionPointName.create<GodotProjectProvider>("com.intellij.rider.godot.community.godotProjectProvider"),
            emptyList(), testRootDisposable,
        )

        assertNull(enabledPlugins())
    }

    fun testMissingSettingsProviderIsUnavailable() {
        ExtensionTestUtil.maskExtensions(settingsProviderEp, emptyList(), testRootDisposable)

        assertNull(enabledPlugins())
    }

    fun testSkipsUnavailableProvider() {
        ExtensionTestUtil.maskExtensions(
            settingsProviderEp, listOf(provider(null), provider(setOf("res://addons/other/plugin.cfg"))),
            testRootDisposable
        )

        assertEquals(setOf("res://addons/other/plugin.cfg"), enabledPlugins())
    }

    fun testEmptySetDoesNotFallThrough() {
        ExtensionTestUtil.maskExtensions(
            settingsProviderEp, listOf(provider(emptySet()), provider(setOf("res://addons/other/plugin.cfg"))),
            testRootDisposable
        )

        assertEquals(emptySet<String>(), enabledPlugins())
    }

    fun testReadsCommittedChanges() {
        val file = configureProject("[editor_plugins]\nenabled=PackedStringArray(\"res://addons/rider-plugin/plugin.cfg\")")
        assertEquals(setOf("res://addons/rider-plugin/plugin.cfg"), enabledPlugins())

        updateText(file, "[editor_plugins]\nenabled=PackedStringArray()")

        assertEquals(emptySet<String>(), enabledPlugins())
    }

    private fun configureProject(text: String, path: String = "project.godot"): PsiFile {
        val file = myFixture.addFileToProject(path, text)
        GdProjectService.getInstance(project).discoverProject(file.virtualFile.parent)
        return file
    }

    private fun updateText(file: PsiFile, text: String) {
        val manager = PsiDocumentManager.getInstance(project)
        val document = manager.getDocument(file)!!
        WriteCommandAction.runWriteCommandAction(project) {
            document.setText(text)
            manager.commitDocument(document)
        }
    }

    private fun enabledPlugins(): Set<String>? = GodotCommunityUtil.getEnabledEditorPlugins(project)

    private fun provider(paths: Set<String>?): GodotProjectSettingsProvider = object : GodotProjectSettingsProvider {
        override fun getEnabledEditorPlugins(project: Project): Set<String>? = paths
    }

    private val settingsProviderEp =
        ExtensionPointName.create<GodotProjectSettingsProvider>("com.intellij.rider.godot.community.godotProjectSettingsProvider")
}