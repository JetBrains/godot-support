package com.jetbrains.godot.test.process

import com.intellij.execution.configurations.GeneralCommandLine
import com.intellij.execution.process.KillableProcessHandler
import com.intellij.execution.process.ProcessEvent
import com.intellij.execution.process.ProcessListener
import com.intellij.openapi.util.Key
import com.intellij.openapi.util.text.StringUtil
import com.intellij.util.AnsiCsiUtil
import com.intellij.util.ui.EDT
import com.jetbrains.godot.test.EDITOR_LOADED_FLAG_4_5
import com.jetbrains.godot.test.EDITOR_LOADED_FLAG_PRE_4_5
import com.jetbrains.godot.test.GODOT_NUMBER_VERSION
import com.jetbrains.rider.test.framework.frameworkLogger
import java.nio.file.Path
import java.util.concurrent.CountDownLatch
import java.util.concurrent.TimeUnit
import kotlin.io.path.absolutePathString
import kotlin.time.Duration

private val LOG = frameworkLogger

/**
 * Returns the last marker that a verbose Godot editor prints during startup.
 *
 * Godot uses a colored marker from version 4.5 and a different plain marker before version 4.5.
 */
fun godotEditorLoadedMarker(version: String = GODOT_NUMBER_VERSION): String =
    if (StringUtil.compareVersionNumbers(version, "4.5") >= 0) EDITOR_LOADED_FLAG_4_5 else EDITOR_LOADED_FLAG_PRE_4_5

/** Starts a headless Godot editor for [projectPath] and captures its merged process output. */
fun startGodotEditor(
    godotExecutable: Path,
    projectPath: Path,
    lspPort: Int? = null,
    dapPort: Int? = null,
): GodotEditor {
    val command = buildList {
        add(godotExecutable.absolutePathString())
        // The load marker appears only in the verbose output.
        add("--verbose")
        add("--headless")
        add("--editor")
        if (lspPort != null) addAll(listOf("--lsp-port", lspPort.toString()))
        if (dapPort != null) addAll(listOf("--dap-port", dapPort.toString()))
        addAll(listOf("--path", projectPath.absolutePathString()))
    }
    LOG.info("Starting the Godot editor: ${command.joinToString(" ")}")

    val processHandler = KillableProcessHandler(GeneralCommandLine(command).withRedirectErrorStream(true))
    val output = GodotEditorOutput()
    processHandler.addProcessListener(object : ProcessListener {
        override fun onTextAvailable(event: ProcessEvent, outputType: Key<*>) {
            output.append(event.text)
        }

        override fun processTerminated(event: ProcessEvent) {
            output.processTerminated()
        }
    })
    return GodotEditor(processHandler, output)
}

/** Owns the Godot editor process and its captured output. */
class GodotEditor internal constructor(
    private val processHandler: KillableProcessHandler,
    private val outputState: GodotEditorOutput,
) {
    /** Returns the merged process output that the Godot editor has printed so far. */
    val output: String
        get() = outputState.text

    /** Waits until the Godot editor prints [godotEditorLoadedMarker]. Call this from a background thread. */
    fun awaitLoaded(
        timeout: Duration,
        version: String = GODOT_NUMBER_VERSION,
    ) {
        check(!EDT.isCurrentThreadEdt()) {
            "awaitLoaded must run on a background thread"
        }
        outputState.setMarker(godotEditorLoadedMarker(version))
        if (!processHandler.isStartNotified) {
            processHandler.startNotify()
        }

        if (!outputState.await(timeout)) {
            val state = if (outputState.isTerminated) "terminated" else "did not start before the timeout"
            error("The Godot editor $state. Its output was:\n${outputState.text}")
        }
        LOG.info("The Godot editor is initialized")
    }

    /** Stops the Godot editor and waits for its process to exit. */
    fun stop(timeout: Duration) {
        processHandler.killProcess()
        check(processHandler.waitFor(timeout.inWholeMilliseconds)) {
            "The Godot editor did not stop. Its output was:\n${outputState.text}"
        }
    }
}

internal class GodotEditorOutput {
    private val lock = Any()
    private val loaded = CountDownLatch(1)
    private val buffer = StringBuilder()
    private var marker: String? = null
    private var markerFound = false
    private var processTerminated = false

    val text: String
        get() = synchronized(lock) { buffer.toString() }

    val isTerminated: Boolean
        get() = synchronized(lock) { processTerminated }

    fun setMarker(value: String) {
        synchronized(lock) {
            marker = AnsiCsiUtil.stripAnsi(value)
            checkForMarker()
        }
    }

    fun append(text: String) {
        synchronized(lock) {
            buffer.append(text)
            checkForMarker()
        }
    }

    fun processTerminated() {
        synchronized(lock) {
            processTerminated = true
            loaded.countDown()
        }
    }

    fun await(timeout: Duration): Boolean {
        loaded.await(timeout.inWholeMilliseconds, TimeUnit.MILLISECONDS)
        return synchronized(lock) { markerFound }
    }

    private fun checkForMarker() {
        val expected = marker ?: return
        if (!markerFound && AnsiCsiUtil.stripAnsi(buffer).contains(expected)) {
            markerFound = true
            loaded.countDown()
        }
    }
}
