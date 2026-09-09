package com.jetbrains.rider.plugins.godot.test.cases.integrationTests

import com.intellij.xdebugger.frame.XNamedValue
import com.intellij.xdebugger.frame.XValueContainer
import com.intellij.xdebugger.frame.XValueGroup
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
import com.jetbrains.rider.test.asserts.shouldBe
import com.jetbrains.rider.test.asserts.shouldNotBeNull
import com.jetbrains.rider.test.debugger.XDebuggerTestHelper
import com.jetbrains.rider.test.debugger.disableTargetInvokeWithWatches
import com.jetbrains.rider.test.debugger.enableTargetInvokeWithWatches
import com.jetbrains.rider.test.enums.BuildTool
import com.jetbrains.rider.test.enums.sdk.SdkVersion
import com.jetbrains.rider.test.facades.solution.RiderExistingSolutionApiFacade
import com.jetbrains.rider.test.facades.solution.SolutionApiFacade
import com.jetbrains.rider.test.junit5.base.PerTestSolutionTestBase
import com.jetbrains.rider.test.reporting.SubsystemConstants
import com.jetbrains.rider.test.scriptingApi.DebugTestExecutionContext
import com.jetbrains.rider.test.scriptingApi.dumpFullCurrentData
import com.jetbrains.rider.test.scriptingApi.evaluateExpression
import com.jetbrains.rider.test.scriptingApi.resumeSession
import com.jetbrains.rider.test.scriptingApi.stepInto
import com.jetbrains.rider.test.scriptingApi.stepOver
import com.jetbrains.rider.test.scriptingApi.toggleBreakpoint
import com.jetbrains.rider.test.scriptingApi.waitForPause
import com.jetbrains.rider.test.scriptingApi.waiter
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
@Tag(TeamCityTags.Plugins.Godot.Integration)
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
        debugAtCardReady {
            dumpFullCurrentData()
        }
    }

    @Test // Check simple stepping
    @ChecklistItems(["Debug/Stepping while debug C# Godot player"])
    fun checkSimpleStepping() {
        debugAtCardReady(breakpointLine = 20) {
            stepInto()
            dumpFullCurrentData()
            stepOver()
            dumpFullCurrentData()
        }
    }

    @Test // Check simple evaluation
    @ChecklistItems(["Debug/Evaluation while debugging C# Godot player"])
    fun checkSimpleEvaluation() {
        debugAtCardReady {
            val toEvaluate = "binaryNotation / 25"
            printlnIndented("$toEvaluate = ${evaluateExpression(toEvaluate).result}")
            dumpFullCurrentData()
        }
    }

    @Test
    fun checkSceneTreeRoot() {
        debugAtCardReady {
            dumpSceneTreeShape(sceneTreeNode(SCENE_TREE_ROOT), depth = 1, caption = "/$SCENE_TREE_ROOT")

            // Inline descendants, several levels deep, each level ending in Properties.
            dumpSceneTreeShape(sceneTreeNode(SCENE_TREE_ROOT, "DuelExample"), depth = 3, caption = "current scene")
        }
    }

    /**
     * A node with more children than fit in one view is split into `[a..b]` chunk groups.
     * `SceneTreeFixture/Chunked` holds 150 children, so it must produce `[0..99]` and `[100..149]`.
     */
    @Test
    fun checkSceneTreeChunksLargeChildLists() {
        debugAtCardReady {
            val chunked = sceneTreeNode(SCENE_TREE_ROOT, "SceneTreeFixture", "Chunked")

            // Depth 1 shows the chunk labels; the 150 children inside them are asserted by the unit tests.
            dumpSceneTreeShape(chunked, depth = 1, caption = "Chunked (150 children)")
        }
    }

    /**
     * The fast and slow child-retrieval paths must be indistinguishable in the tree.
     *
     * `Narrow` has 32 children and `Wide` has 33, straddling `NodeChildrenEnumerator.BulkFetchThreshold`, so one is
     * fetched per-child through the array indexer and the other through the compiled helper lambdas in the debuggee.
     * They hold identically named children, so any difference in the rendered rows is a bug in one of the paths.
     */
    @Test
    fun checkBulkAndPerChildRetrievalAgree() {
        debugAtCardReady {
            val narrowNode = sceneTreeNode(SCENE_TREE_ROOT, "SceneTreeFixture", "Narrow")
            val wideNode = sceneTreeNode(SCENE_TREE_ROOT, "SceneTreeFixture", "Wide")

            val narrow = childRowNames(narrowNode)
            val wide = childRowNames(wideNode)

            // Precondition: the fixture really does straddle the threshold, otherwise this test proves nothing.
            narrow.count { it != groupRow(PROPERTIES_GROUP) }.shouldBe(32, "Narrow must stay at the per-child path")
            wide.count { it != groupRow(PROPERTIES_GROUP) }.shouldBe(33, "Wide must cross into the bulk path")

            // Child32 aside, the two must render the same rows in the same order.
            wide.filter { it != "Child32" }.shouldBe(
                narrow,
                "Bulk-fetched children must render exactly like per-child fetched ones"
            )

            dumpSceneTreeShape(narrowNode, depth = 1, caption = "Narrow (32 children, per-child path)")
            dumpSceneTreeShape(wideNode, depth = 1, caption = "Wide (33 children, bulk path)")
        }
    }

    /**
     * A `Godot.Node` reached through code rather than through the scene tree is an ordinary object with a `Children`
     * category and a `Parent` row added on top - it must *not* render its children inline.
     */
    @Test
    fun checkNodeReachedThroughCode() {
        debugAtCardReady {
            val thisValue = localNamed("this")

            // Only the rows the plugin contributes; Card's own members are the platform's job and are unstable.
            val rows = childRowNames(thisValue)
            rows.shouldContainInOrder(listOf(groupRow(CHILDREN_GROUP), PARENT_ROW))

            dumpSceneTreeShape(childNamed(thisValue, CHILDREN_GROUP), depth = 1, caption = "this.Children")
            dumpSceneTreeShape(childNamed(thisValue, PARENT_ROW), depth = 0, caption = "this.Parent")

            // The child must sit *inside* the parent's Children group, never among the parent's own rows.
            val parent = childNamed(thisValue, PARENT_ROW)
            val parentRows = childRowNames(parent)
            parentRows.contains(CARD_NODE).shouldBe(
                false,
                "A code-reached node must not render children inline, found rows: $parentRows"
            )
            dumpSceneTreeShape(childNamed(parent, CHILDREN_GROUP), depth = 1, caption = "this.Parent.Children")
        }
    }

    /**
     * Node names leave the debuggee two different ways: the per-child path reads `Name` on every child, while the bulk
     * path packs them all into one length-prefixed manifest string (`ChildNamesManifest`). Unicode is where the two
     * can disagree, because the manifest counts UTF-16 units, so the fixture holds the same awkward names on both
     * sides of `NodeChildrenEnumerator.BulkFetchThreshold` and they must come back identical.
     */
    @Test
    fun checkSpecialChildNames() {
        debugAtCardReady {
            val perChildNode = sceneTreeNode(SCENE_TREE_ROOT, "SceneTreeFixture", "SpecialNames")
            val bulkNode = sceneTreeNode(SCENE_TREE_ROOT, "SceneTreeFixture", "SpecialNamesWide")

            val perChild = nodeRowNames(perChildNode)
            val bulk = nodeRowNames(bulkNode)

            // Preconditions: the two nodes really do straddle the threshold, otherwise this test proves nothing.
            perChild.size.shouldBe(6, "SpecialNames must stay at the per-child path")
            bulk.size.shouldBe(33, "SpecialNamesWide must cross into the bulk path")

            // The special names are declared first in both nodes, so the manifest-decoded names must match the
            // ones resolved through the Name property, one for one and in order.
            bulk.take(perChild.size).shouldBe(
                perChild,
                "Manifest-decoded names must match per-child resolved names"
            )

            dumpSceneTreeShape(perChildNode, depth = 1, caption = "SpecialNames (per-child path)")
            dumpSceneTreeShape(bulkNode, depth = 1, caption = "SpecialNamesWide (bulk path)")
        }
    }

    /**
     * Without target invocation the scene tree cannot be offered at all: reaching `SceneTree.Root` means calling
     * `Engine.GetMainLoop()` in the debuggee, so `GodotAdditionalValuesProvider.GetAdditionalLocals` contributes the
     * `root` local only while `EvaluationOptions.AllowTargetInvoke` is set. Its absence is the contract here.
     */
    @Test
    fun checkSceneTreeIsAbsentWithoutTargetInvoke() {
        withImplicitTargetInvokeDisabled {
            debugAtCardReady {
                val localNames = getLocals().map { rowName(it) }
                localNames.contains(SCENE_TREE_ROOT).shouldBe(
                    false,
                    "The scene tree must not be offered without target invoke, found locals: $localNames"
                )
                printlnIndented("locals without target invoke: $localNames")

                val thisValue = localNamed("this")
                val rows = childRowNames(thisValue)
                rows.shouldContainInOrder(listOf(groupRow(CHILDREN_GROUP), PARENT_ROW))

                dumpSceneTreeShape(childNamed(thisValue, CHILDREN_GROUP), depth = 0, caption = "this.Children")
                dumpSceneTreeShape(childNamed(thisValue, "Parent"), depth = 0, caption = "this.Parent")
            }
        }
    }

    /**
     * Runs [body] paused at `Card._Ready()`, with the standard node-instance mask and gold comparison.
     *
     * `DeckDrawer.InitialCards` is 1 in `example-layout.tscn`, so exactly one card is ever drawn: `HandLayout` always
     * has exactly one child while paused and nothing re-enters the breakpoint. Raising it would make every dump of
     * the `HUD` subtree depend on draw timing.
     */
    private fun debugAtCardReady(breakpointLine: Int = CARD_READY_LINE, body: DebugTestExecutionContext.() -> Unit) {
        var ld: LifetimeDefinition? = null
        DebugGodotPlayer(
            project,
            {
                buildApiFacade.buildWholeSolution()
                // TODO: for some reason DFA can't work in this scenario because of null declared element on rider backend. Need to investigate
                ld = protocolLifetimeDefinition.createNested { disableDFA(it) }
                toggleBreakpoint("Card.cs", breakpointLine)
            },
            {
                dumpProfile.customRegexToMask["<NODE_INSTANCE>"] = Regex(NODE_INSTANCE_MASK)
                var bodyFailure: Throwable? = null
                try {
                    waitForPause()
                    body()
                } catch (e: Throwable) {
                    bodyFailure = e
                    throw e
                } finally {
                    // A debuggee left paused poisons the shared Godot process for the tests after this one, so always
                    // try to resume. resumeSession() asserts the session actually reached a running state, and on the
                    // happy path that is the only thing checking it - so let it fail the test. Only when the body has
                    // already failed is it suppressed, so it cannot replace the assertion that got us here.
                    try {
                        resumeSession()
                    } catch (e: Throwable) {
                        val failure = bodyFailure ?: throw e
                        failure.addSuppressed(e)
                    } finally {
                        ld?.terminate()
                    }
                }
            },
            testGoldFile
        )
    }

    /**
     * Runs [body] with implicit target invocation switched off, restoring it afterwards.
     *
     * Deliberately not [disableTargetInvokeWithWatches]: that is the restore half of a save/restore pair over
     * module-level state, so it would put back whatever `@BeforeAll` saved - very likely `true` - and also clobber
     * what `@AfterAll` relies on.
     */
    private fun withImplicitTargetInvokeDisabled(body: () -> Unit) {
        val settings = DotNetDebuggerSettings.instance
        val previous = settings.allowImplicitTargetInvoke
        settings.allowImplicitTargetInvoke = false
        try {
            body()
        } finally {
            settings.allowImplicitTargetInvoke = previous
        }
    }

    /**
     * Dumps the *shape* of a scene-tree subtree: one line per row, `name = {Type}` for nodes and `[name]` for group
     * rows, and it never descends into a group.
     *
     * Why not a plain [dumpValueItem] with a depth: that expands `Properties`, which is the platform's member
     * enumeration of a Godot type - hundreds of lines of raw pointers, RIDs and `Notification*` constants that differ
     * between runs and across Godot upgrades. The hierarchy, plus "a Properties group exists and comes last", is the
     * plugin's actual contribution.
     */
    private fun DebugTestExecutionContext.dumpSceneTreeShape(
        container: XValueContainer,
        depth: Int,
        caption: String,
    ) {
        printlnIndented("$caption:")
        withIndent {
            // childrenLevel = 0 renders exactly one masked line, in the same format as the other gold files.
            dumpValueItem(container, childrenLevel = 0)
            withIndent { dumpChildRowsRecursively(container, depth) }
        }
    }

    private fun DebugTestExecutionContext.dumpChildRowsRecursively(container: XValueContainer, depth: Int) {
        if (depth <= 0) return

        for (child in children(container)) {
            dumpValueItem(child, childrenLevel = 0)

            // Groups are listed but never opened. Properties would drown the dump in volatile platform output, and
            // the contents of a chunk group are asserted by ChildChunkingTests instead.
            if (child is XValueGroup) continue

            withIndent { dumpChildRowsRecursively(child, depth - 1) }
        }
    }

    private fun DebugTestExecutionContext.localNamed(name: String): XValueContainer {
        // Collected once: the message argument is evaluated eagerly, so reusing getLocals() inside it would cost a
        // second round-trip to the debuggee on every lookup.
        val locals = getLocals()
        return locals.firstOrNull { rowName(it) == name }
            .shouldNotBeNull("No local named '$name', found: ${locals.map { rowName(it) }}")
    }

    /** Names of a node's child *nodes*, with group rows (`Properties`, chunk groups) left out. */
    private fun nodeRowNames(container: XValueContainer): List<String> =
        children(container).filterNot { it is XValueGroup }.map { rowName(it) }

    /** Walks the scene tree by node name, e.g. `sceneTreeNode(SCENE_TREE_ROOT, "SceneTreeFixture", "Wide")`. */
    private fun DebugTestExecutionContext.sceneTreeNode(vararg path: String): XValueContainer {
        var current = localNamed(path.first())
        for (name in path.drop(1))
            current = childNamed(current, name)
        return current
    }

    private fun childNamed(container: XValueContainer, name: String): XValueContainer {
        val rows = children(container)
        return rows.firstOrNull { rowName(it) == name }
            .shouldNotBeNull("No child row '$name', found: ${rows.map { rowName(it) }}")
    }

    /**
     * Children of a row, waiting the way the debugger test framework does.
     *
     * The single-argument [XDebuggerTestHelper.collectChildrenItems] overload defaults to a waiter that blocks on
     * `Semaphore.tryAcquire`. Test bodies run on the EDT, so blocking it stops the protocol callback that delivers the
     * children from ever running and the wait always times out. [waiter] pumps the event queue while polling instead.
     */
    private fun children(container: XValueContainer): List<XValueContainer> =
        XDebuggerTestHelper.collectChildrenItems(container) { semaphore, timeout ->
            waiter(semaphore, timeout, "godotSceneTreeChildren")
        }

    /** Row names of a node's children, with group rows marked so they cannot be confused with node names. */
    private fun childRowNames(container: XValueContainer): List<String> =
        children(container).map { if (it is XValueGroup) groupRow(it.name) else rowName(it) }

    private fun rowName(container: XValueContainer): String = when (container) {
        is XNamedValue -> container.name
        is XValueGroup -> container.name
        else -> container.toString()
    }

    private fun List<String>.shouldContainInOrder(expected: List<String>, message: String? = null) {
        val indices = expected.map { indexOf(it) }
        indices.forEachIndexed { i, index ->
            (index >= 0).shouldBe(true, message ?: "Row '${expected[i]}' is missing, found: $this")
        }
        indices.shouldBe(indices.sorted(), message ?: "Rows are out of order, found: $this")
    }

    private companion object {
        /** Row name of the scene tree root the plugin contributes as a local - `/root` in the Godot editor. */
        const val SCENE_TREE_ROOT = "root"

        /** `GD.Print(...)` in `Card._Ready()`. `checkSimpleStepping` uses the next line so it has somewhere to step. */
        const val CARD_READY_LINE = 19

        /** Masks the `<Type#id>` a Godot node presents as; the id changes every run. */
        const val NODE_INSTANCE_MASK = "<[A-Za-z0-9]+#-?\\d+>"

        /** The runtime-created card the breakpoint belongs to; the only child of `HandLayout` while it is paused. */
        const val CARD_NODE = "Card"

        // Not localized in the plugin either - see GroupNames.
        const val CHILDREN_GROUP = "Children"
        const val PROPERTIES_GROUP = "Properties"

        /** Row `NodeObjectChildrenRenderer` adds for `Node.GetParent()`. */
        const val PARENT_ROW = "Parent"

        /** Marks a group row so a group named "Children" cannot be mistaken for a node named "Children". */
        private fun groupRow(name: String) = "[$name]"
    }
}
