## Features

List of implemented features  
Please report any issues you encounter - this is still yet to be battle tested

### Auto-completion

<details>
    <summary>Standard completions of Variables, Constants, Methods, Signals, Enums, Classes, Annotations, ...</summary>
    <img src="../../screens/features/autocompletion.png" />
</details>

<details>
    <summary>Methods directly insert whole method</summary>
    <img src="../../screens/features/autocompletion_functions.png" />  
    <div>&nbsp;</div>
    <strong>Resulting in:</strong>  
    <img src="../../screens/features/autocompletion_functions_res.png" />
</details>

<details>
    <summary>Nodes</summary>
    <img src="../../screens/features/autocompletion_resources.png" />
</details>

<details>
    <summary>Inputs, Groups, Meta fields, Resources</summary>
    <img src="../../screens/features/string_completion.png" />
</details>

### Usages

<details>
    <summary>Go to declaration / usages</summary>
    <img src="../../screens/features/usages/usages.png" />
</details>

<details>
    <summary>Refactoring</summary>
    <img src="../../screens/features/usages/refactor.png" />
</details>

<details>
    <summary>Go to file resource (Ctrl+Click)</summary>
    <strong>Currently not working as required API is not allowed publicly yet</strong>
    <div><a href="https://gitlab.com/IceExplosive/gdscript/-/issues/165">Issue</a></div>
    <img src="../../screens/features/usages/goto_resource.png" />
</details>

### File templates

<details>
    <summary>Predefined from Godot's source</summary>
    <img src="../../screens/features/file_template.png" />
</details>

<details>
    <summary>Custom templates</summary>
    <img src="../../screens/features/custom_file_template.png" />
</details>

### Hide private fields

<details>
    <summary>Hides _underscore prefixed variables and methods from completions</summary>
    <img src="../../screens/features/private_fields/hide.png" />
    <div>&nbsp;</div>
    <strong>While showing them within the class itself</strong><br />
    <img src="../../screens/features/private_fields/show_in_self.png" />
</details>

<details>
    <summary>Setting for turning it it off</summary>
    <img src="../../screens/features/setting/private_setting.png" />
</details>

### Built-in documentation (Ctrl+Q)

<details>
    <summary>Generated documentation from comments</summary>
    <img src="../../screens/features/documentation.png" />
</details>

### Line markers

<details>
    <summary>Parent(super) methods</summary>
    <img src="../../screens/features/line_marker/super_method.png" />
    <div>On click redirects to given method</div>
</details>

<details>
    <summary>Run current scene</summary>
    <img src="../../screens/features/line_marker/run_marker.png" />
</details>

<details>
    <summary>Resource usages</summary>
    <img src="../../screens/features/line_marker/resource_usage.png" />
</details>

<details>
    <summary>Connected signals</summary>
    <img src="../../screens/features/line_marker/connected_signal.png" />
</details>

<details>
    <summary>Inherited scene</summary>
    <img src="../../screens/features/line_marker/inherited_scene.png" />
</details>

<details>
    <summary>Color picker</summary>
    <img src="../../screens/features/line_marker/color_picker.png" />
</details>

### Parameter hints

<details>
    <summary>Param hints (Ctrl+P)</summary>
    <img src="../../screens/features/hint/param_hint.png" />
</details>

<details>
    <summary>Inlay hints</summary>
    <img src="../../screens/features/hint/inlay.png" />
</details>

### Run configuration - start game from Editor

<details>
    <summary>Add Godot's path</summary>
    <img src="../../screens/features/run_configuration.png" />
</details>

### Formatter

<details>
    <summary>Code formatter</summary>
    <img src="../../screens/features/formatter.png" />
</details>

<details>
    <summary>Code style settings</summary>
    <img src="../../screens/features/setting/code_style.png" />
</details>

### Validations

#### Type checks

<details>
    <summary>is/has conditioned type</summary>
    <img src="../../screens/features/validations/is_has.png" />
</details>
<details>
    <summary>is/has correct return</summary>
    <img src="../../screens/features/validations/correct_return.png" />
</details>

#### Method correctness

<details>
    <summary>unreachable code</summary>
    <img src="../../screens/features/validations/unreachable_code.png" />
</details>

<details>
    <summary>all paths return</summary>
    <img src="../../screens/features/validations/all_paths_return.png" />
</details>


### Actions

<details>
    <summary>Add/change return Type</summary>
    <img src="../../screens/features/action/specify_variable.png" />
</details>

<details>
    <summary>Generate get_set methods</summary>
    <img src="../../screens/features/action/create_set_method.png" />
</details>

<details>
    <summary>Remove annotation</summary>
    <img src="../../screens/features/action/remove_annotation.png" />
</details>

<details>
    <summary>Change class_name to match filename</summary>
    <img src="../../screens/features/action/match_classname.png" />
</details>

<details>
    <summary>Remove getter & setter</summary>
    <img src="../../screens/features/action/remove_get_set.png" />
</details>

<details>
    <summary>Too many arguments</summary>
    <img src="../../screens/features/action/too_many_arguments.png" />
</details>

<details>
    <summary>Change function type</summary>
    <img src="../../screens/features/action/change_param.png" />
</details>


### ["Trait" like feature](trait.md)

### Known limitations

<details>
    <summary>get_node, get_parent</summary>
    <div>
        <strong>get_node</strong>, <strong>get_parent</strong> and so on atm do not parse actual Node, but only as a generic Node type (will be supported later on)
    </div>
</details>

<details>
    <summary>get_window method</summary>
    <div>
        <strong>get_window</strong> (and maybe few other methods) return different class based on context (SubViewport, Window, ...)
        <br />
        Plugin specify it as base Viewport class, so to get completion/check for inherited ones available you have to manually specify the type
    </div>
</details>

<details>
    <summary>Dynamic nodes</summary>
    <div>
        Dynamic nodes and such added at runtime cannot be predicted and thus no autocompletion is available
    </div>
</details>

