package com.jetbrains.rider.plugins.godot.test.cases

import com.intellij.codeInsight.CodeInsightSettings
import com.intellij.codeInsight.editorActions.CompletionAutoPopupHandler
import com.intellij.testFramework.TestModeFlags
import com.jetbrains.rider.completion.RiderCodeCompletionExtraSettings
import com.jetbrains.rider.test.annotations.RiderTestTimeout
import com.jetbrains.rider.test.annotations.Solution
import com.jetbrains.rider.test.annotations.TestEnvironment
import com.jetbrains.rider.test.base.PerClassSolutionTestBase
import com.jetbrains.rider.test.env.enums.BuildTool
import com.jetbrains.rider.test.env.enums.SdkVersion
import com.jetbrains.rider.test.facades.TestApiScopes
import com.jetbrains.rider.test.facades.editor.EditorApiFacade
import com.jetbrains.rider.test.facades.editor.RiderEditorApiFacade
import com.jetbrains.rider.test.framework.persistAllFilesOnDisk
import com.jetbrains.rider.test.scriptingApi.assertLookupContains
import com.jetbrains.rider.test.scriptingApi.callBasicCompletion
import com.jetbrains.rider.test.scriptingApi.typeWithLatency
import com.jetbrains.rider.test.scriptingApi.withOpenedEditor
import org.testng.annotations.AfterMethod
import org.testng.annotations.BeforeMethod
import org.testng.annotations.Test
import java.io.File
import java.util.concurrent.TimeUnit

@Solution("ResCompletionTest")
@TestEnvironment(sdkVersion = SdkVersion.LATEST_STABLE, buildTool = BuildTool.SDK)
class ResCompletionTest : PerClassSolutionTestBase(), TestApiScopes.Editor {
    override val editorApiFacade: EditorApiFacade by lazy { RiderEditorApiFacade(solutionApiFacade, testDataStorage) }

    override val traceCategories: List<String>
        get() = listOf(
            "#com.jetbrains.rdclient.completion",
            "#com.jetbrains.rdclient.document",
            "#com.jetbrains.rider.document",
            "#com.jetbrains.rider.editors",
            "#com.jetbrains.rider.completion",
            "#com.jetbrains.rdclient.editorActions",
            "JetBrains.ReSharper.Host.Features.Completion",
            "JetBrains.Rider.Test.Framework.Core.Documents",
            "JetBrains.ReSharper.Feature.Services.CodeCompletion",
            "JetBrains.ReSharper.Host.Features.Completion.Strategies.CSharp",
            "JetBrains.ReSharper.Host.Features.Documents",
            "JetBrains.ReSharper.Host.Features.TextControls",
            "JetBrains.ReSharper.Psi.Caches",
            "JetBrains.ReSharper.Psi.Files")

    private val lookupExpectedContents = arrayOf(
        "\"res://resources\"",
        "\"res://scenes\"",
        "\"res://scenes/sample.tscn\"",
        "\"res://scripts\""
        )

    // https://github.com/JetBrains/godot-support/pull/77 // todo: add more cases

    @RiderTestTimeout(5, TimeUnit.MINUTES)
    @Test
    fun test_PrimitiveCompletion() {
        withOpenedEditor(File("scripts").resolve("NewScript.cs").path, "ResCompletionTest1.cs") {
            typeWithLatency("/")
            assertLookupContains(*lookupExpectedContents, checkFocus = false)
        }
    }

    @Test
    fun test_PrimitiveCompletion2() {
        withOpenedEditor(File("scripts").resolve("NewScript.cs").path, "ResCompletionTest2.cs") {
            callBasicCompletion()
            assertLookupContains("res://", "\"res://scenes/sample.tscn\"", checkFocus = false)
        }
    }

    @BeforeMethod
    fun initializeEnvironment() {
        TestModeFlags.set(CompletionAutoPopupHandler.ourTestingAutopopup, true)

        CodeInsightSettings.getInstance().completionCaseSensitive = CodeInsightSettings.NONE
        CodeInsightSettings.getInstance().isSelectAutopopupSuggestionsByChars = true
        CodeInsightSettings.getInstance().AUTO_POPUP_JAVADOC_INFO = false

        //all tests were written with this setting which default was changed only in 18.3
        RiderCodeCompletionExtraSettings.instance.allowToCompleteWithWhitespace = true
  //      prepareAssemblies(project, activeSolutionDirectory)
    }

    // debug only
    @AfterMethod
    fun saveDocuments() {
        persistAllFilesOnDisk()
    }
}