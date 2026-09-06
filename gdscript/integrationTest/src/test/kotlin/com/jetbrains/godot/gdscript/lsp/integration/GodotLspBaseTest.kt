package com.jetbrains.godot.gdscript.lsp.integration

import GdProjectService
import com.intellij.codeInsight.lookup.Lookup
import com.intellij.openapi.application.EDT
import com.intellij.platform.lsp.testFramework.awaitFileOpenedByLspServer
import com.intellij.platform.lsp.testFramework.checkHighlightingRetrying
import com.intellij.platform.testFramework.junit5.codeInsight.fixture.codeInsightFixture
import com.intellij.polySymbols.testFramework.usagesAtCaret
import com.intellij.testFramework.PlatformTestUtil
import com.intellij.testFramework.common.timeoutRunBlocking
import com.intellij.testFramework.fixtures.impl.CodeInsightTestFixtureImpl
import com.intellij.testFramework.junit5.TestApplication
import com.intellij.testFramework.junit5.fixture.moduleFixture
import com.intellij.testFramework.junit5.fixture.projectFixture
import com.intellij.testFramework.junit5.fixture.tempPathFixture
import com.intellij.util.NetworkUtils
import com.jetbrains.godot.test.BASIC_PROJECT_GODOT
import com.jetbrains.godot.test.EDITOR_LOADED_FLAG_4_5
import com.jetbrains.godot.test.EDITOR_LOADED_FLAG_PRE_4_5
import com.jetbrains.godot.test.GODOT_NUMBER_VERSION
import com.jetbrains.godot.test.downloadAndExtractGodot
import gdscript.settings.GdProjectSettingsState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import java.nio.file.Files
import kotlin.io.path.readLines
import kotlin.time.Duration.Companion.minutes

val defaultLspTestTimeout = 5.minutes

/**
 * Used for various LSP tests, where the LSP is provided from a headless Godot instance.
 */
@TestApplication
abstract class GodotLspBaseTest {
    private val tempDirFixture = tempPathFixture()
    private val projectFixture = projectFixture(pathFixture = tempDirFixture, openAfterCreation = true)

    @Suppress("unused") // The code insight fixture requires a module in the project.
    private val moduleFixture = projectFixture.moduleFixture(tempDirFixture, addPathToSourceRoot = true)

    protected val myFixture by codeInsightFixture(projectFixture, tempDirFixture)
    protected val project get() = projectFixture.get()

    private var godotProcess: Process? = null

    companion object {
        private val LOG = com.intellij.openapi.diagnostic.logger<GodotLspBaseTest>()
    }

    @BeforeEach
    fun setUp() {
        val godotExecutable = downloadAndExtractGodot(isMono = false)
        val projectFile = myFixture.addFileToProject(
            "project.godot", BASIC_PROJECT_GODOT.trimIndent()
        )
        val projectFolder = checkNotNull(projectFile.virtualFile.parent) { "Failed to get Godot project folder" }
        GdProjectService.getInstance(project).discoverProject(projectFolder)
        // custom port, so it doesn't collide locally with an open Godot instance
        val freePort = NetworkUtils.findFreePort()
        GdProjectSettingsState.getInstance(project).state.lspRemoteHostPort = freePort
        val command = mutableListOf(
            godotExecutable.toString(),
            "--verbose",
            "--headless",
            "--editor",
            "--lsp-port", freePort.toString(),
            "--path", projectFolder.path
        )
        val godotOutputFile = Files.createTempFile("godot-lsp-", ".log")
        godotProcess = ProcessBuilder(command)
            .redirectErrorStream(true)
            .redirectOutput(godotOutputFile.toFile())
            .start()
        LOG.info("Godot process has been started, going to wait for warmup")
        val cmp = com.intellij.openapi.util.text.StringUtil.compareVersionNumbers(GODOT_NUMBER_VERSION, "4.5")
        val needle = if (cmp == 0 || cmp > 0) {
            // Godot started using colored ansi colored sequences from 4.5
            EDITOR_LOADED_FLAG_4_5
        } else {
            EDITOR_LOADED_FLAG_PRE_4_5
        }
        var lastInput: List<String> = listOf()
        timeoutRunBlocking(timeout = defaultLspTestTimeout, context = Dispatchers.EDT) {
            PlatformTestUtil.waitWithEventsDispatching(
                {
                    "Godot didn't initialize before timeout, last input was:\n${lastInput.joinToString("\n")}"
                },
                {
                    lastInput = godotOutputFile.readLines().asReversed()
                    lastInput.any { it == needle }
                },
                (defaultLspTestTimeout / 2).inWholeSeconds.toInt(),
            )
        }
        LOG.info("Godot editor initialized")
        (myFixture as CodeInsightTestFixtureImpl).canChangeDocumentDuringHighlighting(true)
    }

    @AfterEach
    fun tearDown() {
        try {
            godotProcess?.destroy()
        } finally {
            godotProcess = null
        }
    }

    protected suspend fun runHighlightingCheck() {
        waitForGodotLspStart()
        withContext(Dispatchers.EDT) {
            myFixture.checkHighlightingRetrying(true)
        }
    }

    protected suspend fun runCompletionAndCheckItems(expectedCompletionItems: List<String>) {
        waitForGodotLspStart()
        val items = withContext(Dispatchers.EDT) {
            myFixture.completeBasic()
                ?.map { it.lookupString }
                .orEmpty()
        }
        assertEquals(expectedCompletionItems.sorted(), items.sorted())
    }

    protected suspend fun runCompletionAndCheckAgainst(expected: String) {
        waitForGodotLspStart()
        withContext(Dispatchers.EDT) {
            myFixture.completeBasic()
            myFixture.finishLookup(Lookup.NORMAL_SELECT_CHAR)
            myFixture.checkResult(expected)
        }
    }

    protected suspend fun runFindUsagesByMarker(expectedCount: Int) {
        waitForGodotLspStart()
        val usages = withContext(Dispatchers.EDT) {
            myFixture.usagesAtCaret()
        }
        assertEquals(expectedCount, usages.size)
    }

    protected suspend fun waitForGodotLspStart() {
        awaitFileOpenedByLspServer(project, myFixture.file.virtualFile)
    }
}
