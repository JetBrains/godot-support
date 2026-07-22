package com.jetbrains.rider.plugins.godot.test.cases.integrationTests

import com.jetbrains.rd.platform.diagnostics.LogTraceScenario
import com.jetbrains.rd.util.lifetime.LifetimeDefinition
import com.jetbrains.rdclient.util.idea.pumpMessages
import com.jetbrains.rider.debugger.settings.DotNetDebuggerSettings
import com.jetbrains.rider.diagnostics.LogTraceScenarios
import com.jetbrains.rider.plugins.godot.test.cases.DebugGodotPlayer
import com.jetbrains.rider.plugins.godot.test.cases.disableDFA
import com.jetbrains.rider.plugins.godot.test.cases.startGodotWithProject
import com.jetbrains.rider.test.annotations.Solution
import com.jetbrains.rider.test.annotations.Subsystem
import com.jetbrains.rider.test.annotations.TestSettings
import com.jetbrains.rider.test.annotations.report.ChecklistItems
import com.jetbrains.rider.test.annotations.report.Feature
import com.jetbrains.rider.test.debugger.disableTargetInvokeWithWatches
import com.jetbrains.rider.test.debugger.enableTargetInvokeWithWatches
import com.jetbrains.rider.test.enums.BuildTool
import com.jetbrains.rider.test.enums.sdk.SdkVersion
import com.jetbrains.rider.test.facades.solution.RiderExistingSolutionApiFacade
import com.jetbrains.rider.test.facades.solution.SolutionApiFacade
import com.jetbrains.rider.test.junit5.base.PerTestSolutionTestBase
import com.jetbrains.rider.test.reporting.SubsystemConstants
import com.jetbrains.rider.test.scriptingApi.dumpFullCurrentData
import com.jetbrains.rider.test.scriptingApi.evaluateExpression
import com.jetbrains.rider.test.scriptingApi.resumeSession
import com.jetbrains.rider.test.scriptingApi.stepInto
import com.jetbrains.rider.test.scriptingApi.stepOver
import com.jetbrains.rider.test.scriptingApi.toggleBreakpoint
import com.jetbrains.rider.test.scriptingApi.waitForPause
import com.jetbrains.rider.test.shared.constants.TeamCityTags
import com.jetbrains.rider.test.tooling.testTools
import org.junit.jupiter.api.AfterAll
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Tag
import org.junit.jupiter.api.Test
import java.time.Duration
import kotlin.io.path.absolutePathString

@Subsystem(SubsystemConstants.GODOT)
@Feature("Debug C# godot player")
@Solution("GodotDotNet")
@TestSettings(sdkVersion = SdkVersion.LATEST_STABLE, buildTool = BuildTool.SDK)
@Tag(TeamCityTags.Plugins.GodotIntegration)
class DebugGodotCSharpPlayer : PerTestSolutionTestBase() {
    private var godotProcess: Process? = null

    override val solutionApiFacade: SolutionApiFacade by lazy { RiderExistingSolutionApiFacade() }

    override val traceScenarios: Set<LogTraceScenario>
        get() = super.traceScenarios + LogTraceScenarios.Debugger

    @BeforeEach
    override fun setUpTestCaseSolution() {
        // JUnit5 has no @BeforeMethod(dependsOnMethods=...): start Godot first, then open the solution
        startGodot()
        pumpMessages(Duration.ofSeconds(20))
        super.setUpTestCaseSolution()
    }

    @BeforeAll
    fun setupDotnetDebuggerSettings() {
        DotNetDebuggerSettings.instance.enableTargetInvokeWithWatches()
    }

    @AfterAll
    fun disableDotnetDebuggerSettings() {
        DotNetDebuggerSettings.instance.disableTargetInvokeWithWatches()
    }

    private fun startGodot() {
        godotProcess = startGodotWithProject(
            projectName = testMethod.solution!!.name,
            testWorkDirectory = testWorkDirectory,
            solutionSourceRootDirectory = solutionSourceRootDirectory,
            logPath = testMethod.logDirectory,
            dotnetSdk = testTools(executionTarget).build.dotNetSdk[testMethod.settings.sdk].root.absolutePathString(),
        )
    }

    @Test // Debug C# Godot player
    @ChecklistItems(["Debug/Debug C# Godot player"])
    fun testDebug() {
        var ld: LifetimeDefinition? = null
        DebugGodotPlayer(project,
            {
                buildApiFacade.buildWholeSolution()
                // TODO: for some reason DFA can't work in this scenario because of null declared element on rider backend. Need to investigate
                ld = protocolLifetimeDefinition.createNested { disableDFA(it) }
                toggleBreakpoint("Card.cs", 19)
            },
            {
                dumpProfile.customRegexToMask["<NODE_INSTANCE>"] = Regex("<[A-Za-z0-9]+#\\d+>")
                waitForPause()
                dumpFullCurrentData()
                resumeSession()
                ld?.terminate()
            },
            testGoldFile
        )
    }

    @Test // Check simple stepping
    @ChecklistItems(["Debug/Stepping while debug C# Godot player"])
    fun checkSimpleStepping() {
        var ld: LifetimeDefinition? = null
        DebugGodotPlayer(project,
            {
                buildApiFacade.buildWholeSolution()
                // TODO: for some reason DFA can't work in this scenario because of null declared element on rider backend. Need to investigate
                ld = protocolLifetimeDefinition.createNested { disableDFA(it) }
                toggleBreakpoint("Card.cs", 20)
            },
            {
                dumpProfile.customRegexToMask["<NODE_INSTANCE>"] = Regex("<[A-Za-z0-9]+#\\d+>")
                waitForPause()
                stepInto()
                dumpFullCurrentData()
                stepOver()
                dumpFullCurrentData()
                resumeSession()
                ld?.terminate()
            },
            testGoldFile
        )
    }

    @Test // Check simple evaluation
    @ChecklistItems(["Debug/Evaluation while debugging C# Godot player"])
    fun checkSimpleEvaluation() {
        var ld: LifetimeDefinition? = null
        DebugGodotPlayer(project,
            {
                buildApiFacade.buildWholeSolution()
                // TODO: for some reason DFA can't work in this scenario because of null declared element on rider backend. Need to investigate
                ld = protocolLifetimeDefinition.createNested { disableDFA(it) }
                toggleBreakpoint("Card.cs", 19)
            },
            {
                dumpProfile.customRegexToMask["<NODE_INSTANCE>"] = Regex("<[A-Za-z0-9]+#\\d+>")
                val toEvaluate = "binaryNotation / 25"
                waitForPause()
                printlnIndented("$toEvaluate = ${evaluateExpression(toEvaluate).result}")
                dumpFullCurrentData()
                resumeSession()
                ld?.terminate()
            },
            testGoldFile
        )
    }
}