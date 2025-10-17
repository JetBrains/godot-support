package com.jetbrains.rider.plugins.godot.test.cases.integrationTests

import com.jetbrains.rd.platform.diagnostics.LogTraceScenario
import com.jetbrains.rider.debugger.settings.DotNetDebuggerSettings
import com.jetbrains.rider.diagnostics.LogTraceScenarios
import com.jetbrains.rider.plugins.godot.test.cases.attachDebuggerToGodotEditor
import com.jetbrains.rider.plugins.godot.test.cases.startGodotWithProject
import com.jetbrains.rider.test.annotations.Solution
import com.jetbrains.rider.test.annotations.Subsystem
import com.jetbrains.rider.test.annotations.TestSettings
import com.jetbrains.rider.test.annotations.report.ChecklistItems
import com.jetbrains.rider.test.annotations.report.Feature
import com.jetbrains.rider.test.base.PerTestSolutionTestBase
import com.jetbrains.rider.test.debugger.disableTargetInvokeWithWatches
import com.jetbrains.rider.test.debugger.enableTargetInvokeWithWatches
import com.jetbrains.rider.test.enums.BuildTool
import com.jetbrains.rider.test.enums.sdk.SdkVersion
import com.jetbrains.rider.test.facades.solution.RiderExistingSolutionApiFacade
import com.jetbrains.rider.test.facades.solution.SolutionApiFacade
import com.jetbrains.rider.test.reporting.SubsystemConstants
import com.jetbrains.rider.test.scriptingApi.dumpFullCurrentData
import com.jetbrains.rider.test.scriptingApi.evaluateExpression
import com.jetbrains.rider.test.scriptingApi.resumeSession
import com.jetbrains.rider.test.scriptingApi.stepInto
import com.jetbrains.rider.test.scriptingApi.stepOver
import com.jetbrains.rider.test.scriptingApi.toggleBreakpoint
import com.jetbrains.rider.test.scriptingApi.waitForPause
import com.jetbrains.rider.test.tooling.testTools
import org.testng.annotations.AfterClass
import org.testng.annotations.BeforeClass
import org.testng.annotations.BeforeMethod
import org.testng.annotations.Test
import kotlin.collections.plus

@Subsystem(SubsystemConstants.GODOT)
@Feature("Debug C# godot player")
@Solution("mixed-language-project")
@TestSettings(sdkVersion = SdkVersion.LATEST_STABLE, buildTool = BuildTool.SDK)
class DebugGodotCSharpPlayer : PerTestSolutionTestBase() {
    private var godotProcess: Process? = null

    override val solutionApiFacade: SolutionApiFacade by lazy { RiderExistingSolutionApiFacade() }

    override val traceScenarios: Set<LogTraceScenario>
        get() = super.traceScenarios + LogTraceScenarios.Debugger

    @BeforeMethod(dependsOnMethods = ["startGodot"])
    override fun setUpTestCaseSolution() {
        super.setUpTestCaseSolution()
    }

    @BeforeClass(alwaysRun = true)
    fun setupDotnetDebuggerSettings() {
        DotNetDebuggerSettings.Companion.instance.enableTargetInvokeWithWatches()
    }

    @AfterClass(alwaysRun = true)
    fun disableDotnetDebuggerSettings() {
        DotNetDebuggerSettings.Companion.instance.disableTargetInvokeWithWatches()
    }

    @BeforeMethod(alwaysRun = true)
    fun startGodot() {
        godotProcess = startGodotWithProject(
          projectName = testMethod.solution!!.name,
          testWorkDirectory = testWorkDirectory,
          solutionSourceRootDirectory = solutionSourceRootDirectory,
          logPath = testMethod.logDirectory,
          dotnetSdk = testTools(testEnvironment).build.dotNetSdk[testMethod.settings.sdk].root.absolutePath,
        )
    }

    @Test(description = "Debug C# godot player")
    @ChecklistItems(["Debug/Debug C# godot player"])
    fun testDebug() {
      attachDebuggerToGodotEditor(project,
                                  {
                                    buildApiFacade.buildWholeSolution()
                                    toggleBreakpoint("PlayerLogger.cs", 14) //  GD.Print($"Binary notation: {binaryNotation}");
                                  }, {
                                    waitForPause()
                                    dumpFullCurrentData()
                                    resumeSession()
                                  }, testGoldFile)
    }

    @Test(description = "Check simple stepping")
    @ChecklistItems(["Debug/Stepping while debug C# godot player"])
    fun checkSimpleStepping() {
      attachDebuggerToGodotEditor(project,
                                  {
                                    buildApiFacade.buildWholeSolution()
                                    toggleBreakpoint("PlayerLogger.cs", 15)  //_lastLoggedPosition = GlobalPosition;
                                  },
                                  {
                                    waitForPause()
                                    stepInto()
                                    dumpFullCurrentData()
                                    stepOver()
                                    dumpFullCurrentData()
                                    resumeSession()
                                  }, testGoldFile)
    }

    @Test(description = "Check simple evaluation")
    @ChecklistItems(["Debug/Evaluation while debug C# godot player"])
    fun checkSimpleEvaluation() {
      attachDebuggerToGodotEditor(project,
                                  {
                                    buildApiFacade.buildWholeSolution()
                                    toggleBreakpoint("PlayerLogger.cs", 14) //  GD.Print($"Binary notation: {binaryNotation}");
                                  },
                                  {
                                    val toEvaluate = "binaryNotation / 25"
                                    waitForPause()
                                    printlnIndented("$toEvaluate = ${evaluateExpression(toEvaluate).result}")
                                    dumpFullCurrentData()
                                    resumeSession()
                                  }, testGoldFile)
    }
}