# References Between TSCN and GDScript

This document summarizes every reference type between Godot text scenes (`.tscn`) and GDScript files (`.gd`).

## Sources of Truth

- **Godot engine source code**: `../godot`
  - Scene parsing and serialization: `../godot/scene/resources/resource_format_text.cpp`
  - Scene instantiation and packing: `../godot/scene/resources/packed_scene.cpp`
  - Node hierarchy and unique names: `../godot/scene/main/node.cpp`
  - Animation tracks: `../godot/scene/resources/animation.h` and `../godot/scene/resources/animation.cpp`
  - GDScript compiler and parser: `../godot/modules/gdscript/gdscript_parser.cpp` and `../godot/modules/gdscript/gdscript_compiler.cpp`
- **Godot documentation**: `../godot-docs`
  - Exported properties: `../godot-docs/tutorials/scripting/gdscript/gdscript_exports.rst`
  - Scene unique nodes: `../godot-docs/tutorials/scripting/scene_unique_nodes.rst`
  - Nodes and scene instances: `../godot-docs/tutorials/scripting/nodes_and_scene_instances.rst`
  - Signals: `../godot-docs/getting_started/step_by_step/signals.rst`
  - Animation tracks: `../godot-docs/tutorials/animation/animation_track_types.rst`

---

## 1. References from TSCN to GDScript

In this direction, a `.tscn` file references a script file, a script class, an exported member, or a script method.

### 1.1 Script Attachment (`ext_resource` and `sub_resource`)

- **External script file reference**:
  - The scene declares an external resource header:
    ```text
    [ext_resource type="Script" path="res://player.gd" id="1_abc"]
    ```
    (or `type="GDScript"`).
  - A node attaches the script with a property assignment:
    ```text
    [node name="Player" type="CharacterBody2D"]
    script = ExtResource("1_abc")
    ```
  - Engine origin: `ResourceLoaderText::load()` in `resource_format_text.cpp` loads the script. `SceneState::instantiate()` in `packed_scene.cpp` executes `node->set_script(script)`.
- **Built-in script (`sub_resource`)**:
  - The scene embeds GDScript code directly:
    ```text
    [sub_resource type="GDScript" id="GDScript_123"]
    script/source = "extends Node\nfunc _ready():\n    pass\n"
    ```
  - A node assigns `script = SubResource("GDScript_123")`.
  - Engine origin: Parsed as an internal resource in `resource_format_text.cpp`.
- **Global script class (`script_class`)**:
  - Resource definitions reference the global name declared by `class_name` in GDScript:
    ```text
    [ext_resource type="Resource" script_class="ItemData" path="res://item.tres" id="2_def"]
    ```
  - Engine origin: `ResourceLoaderText::recognize_script_class()` in `resource_format_text.cpp`.

### 1.2 Exported Property Assignments (`@export`)

- A node block assigns values to variables declared with `@export` in the attached script:
  ```text
  [node name="Player" type="CharacterBody2D"]
  script = ExtResource("1_abc")
  speed = 400.0
  character_name = "Hero"
  ```
- **Exported node assignments**:
  - When GDScript exports a node (`@export var target: Node`), the scene stores the node path in the node header:
    ```text
    [node name="Turret" type="Node2D" node_paths=PackedStringArray("target")]
    target = NodePath("Player")
    ```
  - Engine origin: `DeferredNodePathProperties` in `packed_scene.cpp` resolves `base->get_node_or_null(dnp.value)` and assigns the node.
- **Exported resource assignments**:
  - When GDScript exports a custom resource, the scene stores an `ExtResource` or `SubResource` reference:
    ```text
    inventory = ExtResource("2_def")
    ```
- Documentation: `gdscript_exports.rst`.

### 1.3 Signal Connections (`[connection]`)

- The scene links an emitter node to a method on a target node:
  ```text
  [connection signal="timeout" from="Timer" to="." method="_on_timer_timeout"]
  ```
- Reference points:
  - `signal`: Names a signal on the source node. The signal can be a built-in engine signal or a custom `signal my_signal` declared in GDScript.
  - `from`: A `NodePath` that resolves to the emitting node.
  - `to`: A `NodePath` that resolves to the receiving node.
  - `method`: Names a method defined in the script of the target node.
  - Optional attributes: `flags`, `binds`, `unbinds`.
- Engine origin: `SceneState::instantiate()` in `packed_scene.cpp` connects the signal to `Callable(cto, method_name)`.
- Documentation: `signals.rst`.

### 1.4 Animation Tracks in Embedded Animation Resources

When a `.tscn` contains an `AnimationPlayer` with embedded `Animation` sub-resources:
- **Method call track (`TYPE_METHOD`)**:
  - The track header points to a node path: `path = NodePath("Sprite2D")`.
  - Keyframes specify a method name and arguments: `{"method": &"play_sound", "args": []}`.
  - The method name must match a function in the script attached to that node.
- **Property track (`TYPE_VALUE`, `TYPE_BEZIER`)**:
  - The track path points to a node and a property: `path = NodePath("Sprite2D:speed")`.
  - The property can refer to an `@export` variable defined in GDScript.
- Engine origin: `MethodTrack` and `ValueTrack` in `animation.h` and `animation.cpp`.
- Documentation: `animation_track_types.rst`.

---

## 2. References from GDScript to TSCN

In this direction, a GDScript file references a scene resource, a node path, a scene unique node, or a scene signal.

### 2.1 Scene Resource Loading and Instantiation

- **Direct path loading**:
  - `preload("res://enemy.tscn")` parses at compile time.
  - `load("res://enemy.tscn")` loads at runtime.
  - `ResourceLoader.load("res://enemy.tscn")` or threaded loading APIs.
- **UID loading**:
  - `preload("uid://c8y26rxyj3p5m")` loads by unique resource identifier.
- **Scene switching**:
  - `get_tree().change_scene_to_file("res://level.tscn")`.
  - `get_tree().change_scene_to_packed(level_packed_scene)`.
- **Exported scene fields**:
  - `@export var spawn_scene: PackedScene` allows assigning a `.tscn` file in the inspector.
  - `@export_file("*.tscn") var scene_path: String`.
- **Instantiation**:
  - `var instance = spawn_scene.instantiate()` instantiates the node tree of the scene.
  - `instance.owner = root` assigns node ownership for runtime scene packaging.
- Engine origin: `parse_preload()` in `gdscript_parser.cpp`; `PackedScene` in `packed_scene.cpp`.
- Documentation: `nodes_and_scene_instances.rst` and `class_packedscene.rst`.

### 2.2 Node Navigation and NodePaths

- **Shorthand dollar syntax**:
  - `$Path/To/Node` or `$"Path/With Spaces/Node"`.
  - Compiles into a call to `get_node(NodePath("..."))`.
- **Onready node assignment**:
  - `@onready var weapon = $WeaponSlot/Sword`.
- **Method calls for node lookup**:
  - `get_node("Path/To/Node")` and `get_node_or_null("Path/To/Node")`.
  - `find_child("NodeName")` traverses children by pattern.
  - NodePath literals: `^"Path/To/Node"`.
- **Property path indexing**:
  - `NodePath("Node:property")` addresses a specific property or sub-property on a node.
- Engine origin: `parse_get_node()` in `gdscript_parser.cpp`; `GetNodeNode` in `gdscript_compiler.cpp`.
- Documentation: `nodes_and_scene_instances.rst`.

### 2.3 Scene Unique Nodes (`%`)

- Nodes marked with `unique_name_in_owner = true` can be accessed from any script in the same scene without hierarchical paths.
- **Access syntaxes in GDScript**:
  - `%UniqueNode`
  - `$%UniqueNode`
  - `$"%UniqueNode"`
  - `get_node("%UniqueNode")`
  - Relative prefix: `get_node("SubScene/%SubUniqueNode")`
- Engine origin: `Node::is_unique_name_in_owner()` and `Node::get_node()` in `node.cpp`; `parse_get_node()` in `gdscript_parser.cpp`.
- Documentation: `scene_unique_nodes.rst`.

### 2.4 Programmatic Signal Connections to Scene Nodes

- A script connects to signals of nodes in the scene tree:
  ```gdscript
  $Button.pressed.connect(_on_button_pressed)
  ```
- A script accesses properties or calls methods on child nodes defined in `.tscn`:
  ```gdscript
  $AnimationPlayer.play("walk")
  ```
- Documentation: `signals.rst`.

### 2.5 Groups Defined in Scenes

- Nodes specify groups in `.tscn` using the `groups` array in the node header:
  ```text
  [node name="Mob" type="CharacterBody2D" groups=["enemies"]]
  ```
- GDScript queries or notifies those nodes:
  - `get_tree().get_nodes_in_group("enemies")`
  - `get_tree().call_group("enemies", "take_damage", 10)`
  - `node.is_in_group("enemies")`
- Documentation: `tutorials/scripting/groups.rst`.

### 2.6 Autoload Singletons

- An autoload entry in `project.godot` can point to a scene:
  ```text
  [autoload]
  GameManager="*res://game_manager.tscn"
  ```
- GDScript accesses `GameManager` as a global identifier pointing to the root node of that scene.
- Documentation: `tutorials/scripting/singletons_autoload.rst`.

---

## What Was Omitted

- Binary scene files (`.scn`) were omitted because this document focuses on text scenes (`.tscn`). The internal node structure in `SceneState` is identical for both.
- C# bindings (`.cs`) were omitted because the scope is GDScript (`.gd`).

---

## 3. Known Gaps

The inventory lists every reference type the engine supports. Not all of them resolve as a
`PsiReference`/PolySymbol own-reference yet, so Find Usages, rename, and go-to-declaration miss
these:

- **Node navigation (`$Node`, `%UniqueNode`)** - `gdscript.psi.impl.GdNodePathImpl` declares no
  reference. `gdscript.psi.utils.GdNodeUtil.findNode` already resolves a `GdNodePath` to its
  `.tscn` node, but only completion, type inference, and the "unresolved path" annotator call it.
- **Exported node-path values (`NodePath("Player")`)** - a `.tscn` property value of this shape
  resolves to a node only inside the animation method-track walk in
  `tscn.psi.impl.TscnNamedElementImpl`. An `@export var target: Node` assignment, and an animation
  property track (`TYPE_VALUE`/`TYPE_BEZIER`), carry no own-reference to the target node.
  Best paired example in godot-demo-projects:
    • misc/large_world_coordinates/controls.gd:8-10 declares:
    @export var node_to_move: Node3D
    • misc/large_world_coordinates/test.tscn:251 assigns them on the Controls node:
    [node name="Controls" type="VBoxContainer" parent="." node_paths=PackedStringArray("camera", "camera_holder", "rotation_x", "node_to_move")]
- **Groups (`groups=[...]`)** - `tscn.psi.utils.TscnNodeUtil.listAllGroups` indexes every group
  name, but no reference links a group string literal in `.tscn` to a `get_nodes_in_group()`,
  `call_group()`, or `is_in_group()` argument in GDScript, in either direction.
  Best paired example in godot-demo-projects:
    • 3d/lights_and_shadows/test.tscn:139 (and repeated at lines 164, 185, 213, 242, 321, 347, 377, 407) — [node name="DirectionalLight3D" type="DirectionalLight3D" parent="." groups=["animatable"]]
    • 3d/lights_and_shadows/tester.gd:78 — for animatable_node in get_tree().get_nodes_in_group(&"animatable"):
    This is a direct round trip: the group string "animatable" is declared in the .tscn header and consumed by get_nodes_in_group(&"animatable") in the script, matching the "no reference links a group string literal in .tscn to a get_nodes_in_group() ... argument" gap.

