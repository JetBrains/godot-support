package com.jetbrains.godot.test.project

import java.nio.file.Files
import java.nio.file.Path
import kotlin.io.path.name
import kotlin.io.path.readText
import kotlin.io.path.relativeTo

/** A breakpoint location in a generated project file. */
data class GodotSourceBreakpoint(
    val path: String,
    /** The 1-based source line of the breakpoint marker. */
    val line: Int,
)

/** A Godot project represented only by its file text and breakpoint locations. */
data class GodotProject(
    val name: String,
    val files: Map<String, String>,
    val breakpoints: Map<String, GodotSourceBreakpoint> = emptyMap(),
)

/** Builds a Godot project without writing files or using the IntelliJ Platform. */
fun godotProject(name: String, body: GodotProjectBuilder.() -> Unit): GodotProject =
    GodotProjectBuilder(name).apply(body).build()

/** Reads a Godot project from a test data directory without changing the directory. */
fun godotProjectFromTestData(sourceDirectory: Path): GodotProject {
    val files = linkedMapOf<String, String>()
    val breakpoints = linkedMapOf<String, GodotSourceBreakpoint>()
    Files.walk(sourceDirectory).use { paths ->
        paths.filter(Files::isRegularFile).sorted().forEach { file ->
            val path = file.relativeTo(sourceDirectory).toString().replace('\\', '/')
            val parsed = if (file.name.endsWith(".gd")) parseBreakpointMarkers(file.readText()) else null
            files[path] = parsed?.text ?: file.readText()
            parsed?.lines?.forEach { (name, line) ->
                require(breakpoints.put(name, GodotSourceBreakpoint(path, line)) == null) {
                    "The breakpoint marker '$name' occurs in more than one file"
                }
            }
        }
    }
    val projectText = requireNotNull(files[PROJECT_FILE]) { "The test data directory has no $PROJECT_FILE" }
    val name = PROJECT_NAME.find(projectText)?.groupValues?.get(1) ?: sourceDirectory.name
    return GodotProject(name, files, breakpoints)
}

/** Collects the files for one Godot project. */
class GodotProjectBuilder internal constructor(private val name: String) {
    private val scripts = linkedMapOf<String, String>()
    private val scenes = mutableListOf<SceneBuilder>()
    private val autoloads = linkedMapOf<String, String>()
    private var arguments = emptyList<String>()

    /** Adds a script and extracts its breakpoint markers. */
    fun script(path: String, text: String) {
        require(scripts.put(normalizePath(path), text) == null) { "The script '$path' already exists" }
    }

    /** Adds a scene. One scene can be the main scene. */
    fun scene(path: String, main: Boolean = false, body: SceneBuilder.() -> Unit) {
        scenes += SceneBuilder(normalizePath(path), main).apply(body)
    }

    /** Adds an autoload entry. */
    fun autoload(name: String, path: String) {
        require(autoloads.put(name, normalizePath(path)) == null) { "The autoload '$name' already exists" }
    }

    /** Sets the arguments that Godot forwards to the running project. */
    fun runArgs(vararg arguments: String) {
        this.arguments = arguments.toList()
    }

    internal fun build(): GodotProject {
        val mainScenes = scenes.filter { it.main }
        require(mainScenes.size <= 1) { "Only one scene can be the main scene" }

        val files = linkedMapOf<String, String>()
        val breakpoints = linkedMapOf<String, GodotSourceBreakpoint>()
        scripts.forEach { (path, text) ->
            val parsed = parseBreakpointMarkers(text)
            files[path] = parsed.text
            parsed.lines.forEach { (markerName, line) ->
                require(breakpoints.put(markerName, GodotSourceBreakpoint(path, line)) == null) {
                    "The breakpoint marker '$markerName' occurs in more than one script"
                }
            }
        }
        scenes.forEach { scene ->
            require(!files.containsKey(scene.path)) { "The file '${scene.path}' already exists" }
            files[scene.path] = scene.render()
        }
        files[PROJECT_FILE] = renderProjectFile(mainScenes.singleOrNull()?.path)
        return GodotProject(name, files, breakpoints)
    }

    private fun renderProjectFile(mainScene: String?): String = buildString {
        appendLine("config_version=5")
        appendLine()
        appendLine("[application]")
        appendLine()
        appendLine("config/name=\"${escape(name)}\"")
        if (mainScene != null) appendLine("run/main_scene=\"res://${escape(mainScene)}\"")
        if (autoloads.isNotEmpty()) {
            appendLine()
            appendLine("[autoload]")
            appendLine()
            autoloads.forEach { (autoloadName, path) ->
                appendLine("$autoloadName=\"*res://${escape(path)}\"")
            }
        }
        if (arguments.isNotEmpty()) {
            appendLine()
            appendLine("[editor]")
            appendLine()
            append("run/main_run_args=\"")
            append(arguments.joinToString(" ") { escape(it) })
            append('"')
        }
    }.trimEnd()
}

/** Collects the node tree for one Godot scene. */
class SceneBuilder internal constructor(internal val path: String, internal val main: Boolean) {
    private val roots = mutableListOf<GodotNode>()

    /** Adds a node to the scene. A scene must have one root node. */
    fun node(name: String, type: String, script: String? = null, body: NodeBuilder.() -> Unit = {}) {
        roots += NodeBuilder(name, type, script?.let(::normalizePath)).apply(body).build()
    }

    internal fun render(): String {
        require(roots.size == 1) { "The scene '$path' must have one root node" }
        val root = roots.single()
        val scripts = root.flatten().mapNotNull { it.script }.distinct()
        val resourceIds = scripts.mapIndexed { index, script -> script to resourceId(index + 1, script) }.toMap()
        return buildString {
            appendLine("[gd_scene load_steps=${scripts.size + 1} format=3]")
            scripts.forEach { script ->
                appendLine()
                appendLine("[ext_resource type=\"Script\" path=\"res://${escape(script)}\" id=\"${resourceIds.getValue(script)}\"]")
            }
            appendNode(root, null, resourceIds)
        }.trimEnd()
    }
}

/** Collects child nodes for one scene node. */
class NodeBuilder internal constructor(
    private val name: String,
    private val type: String,
    private val script: String?,
) {
    private val children = mutableListOf<GodotNode>()

    /** Adds a child node. */
    fun node(name: String, type: String, script: String? = null, body: NodeBuilder.() -> Unit = {}) {
        children += NodeBuilder(name, type, script?.let(::normalizePath)).apply(body).build()
    }

    internal fun build(): GodotNode = GodotNode(name, type, script, children.toList())
}

internal data class GodotNode(
    val name: String,
    val type: String,
    val script: String?,
    val children: List<GodotNode>,
) {
    fun flatten(): List<GodotNode> = listOf(this) + children.flatMap { it.flatten() }
}

private fun StringBuilder.appendNode(node: GodotNode, parent: String?, resourceIds: Map<String, String>) {
    appendLine()
    append("[node name=\"${escape(node.name)}\" type=\"${escape(node.type)}\"")
    if (parent != null) append(" parent=\"${escape(parent)}\"")
    appendLine("]")
    node.script?.let { appendLine("script = ExtResource(\"${resourceIds.getValue(it)}\")") }
    val childParent = if (parent == null) "." else if (parent == ".") node.name else "$parent/${node.name}"
    node.children.forEach { appendNode(it, childParent, resourceIds) }
}

private fun resourceId(index: Int, path: String): String {
    val stem = path.substringAfterLast('/').substringBeforeLast('.').replace(Regex("[^A-Za-z0-9_]"), "_")
    return "${index}_$stem"
}

private fun normalizePath(path: String): String = path.replace('\\', '/').removePrefix("/")
private fun escape(value: String): String = value.replace("\\", "\\\\").replace("\"", "\\\"")
private val PROJECT_NAME = Regex("(?m)^config/name=\"([^\"]*)\"$")
private const val PROJECT_FILE = "project.godot"
