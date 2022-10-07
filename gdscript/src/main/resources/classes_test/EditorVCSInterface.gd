extends Object
#brief Version Control System (VCS) interface, which reads and writes to the local VCS in use.
#desc Defines the API that the editor uses to extract information from the underlying VCS. The implementation of this API is included in VCS plugins, which are GDExtension plugins that inherit [EditorVCSInterface] and are attached (on demand) to the singleton instance of [EditorVCSInterface]. Instead of performing the task themselves, all the virtual functions listed below are calling the internally overridden functions in the VCS plugins to provide a plug-n-play experience. A custom VCS plugin is supposed to inherit from [EditorVCSInterface] and override each of these virtual functions.
class_name EditorVCSInterface

#desc A new file has been added.
const CHANGE_TYPE_NEW = 0;

#desc An earlier added file has been modified.
const CHANGE_TYPE_MODIFIED = 1;

#desc An earlier added file has been renamed.
const CHANGE_TYPE_RENAMED = 2;

#desc An earlier added file has been deleted.
const CHANGE_TYPE_DELETED = 3;

#desc An earlier added file has been typechanged.
const CHANGE_TYPE_TYPECHANGE = 4;

#desc A file is left unmerged.
const CHANGE_TYPE_UNMERGED = 5;

#desc A commit is encountered from the commit area.
const TREE_AREA_COMMIT = 0;

#desc A file is encountered from the staged area.
const TREE_AREA_STAGED = 1;

#desc A file is encountered from the unstaged area.
const TREE_AREA_UNSTAGED = 2;




#desc Checks out a [code]branch_name[/code] in the VCS.
func _checkout_branch(branch_name: String) -> bool:
	pass;

#desc Commits the currently staged changes and applies the commit [code]msg[/code] to the resulting commit.
func _commit(msg: String) -> void:
	pass;

#desc Creates a new branch named [code]branch_name[/code] in the VCS.
func _create_branch(branch_name: String) -> void:
	pass;

#desc Creates a new remote destination with name [code]remote_name[/code] and points it to [code]remote_url[/code]. This can be an HTTPS remote or an SSH remote.
func _create_remote(remote_name: String, remote_url: String) -> void:
	pass;

#desc Discards the changes made in a file present at [code]file_path[/code].
func _discard_file(file_path: String) -> void:
	pass;

#desc Fetches new changes from the remote, but doesn't write changes to the current working directory. Equivalent to [code]git fetch[/code].
func _fetch(remote: String) -> void:
	pass;

#desc Gets an instance of an [Array] of [String]s containing available branch names in the VCS.
func _get_branch_list() -> Array[Dictionary]:
	pass;

#desc Gets the current branch name defined in the VCS.
func _get_current_branch_name() -> String:
	pass;

#desc Returns an array of [Dictionary] items (see [method create_diff_file], [method create_diff_hunk], [method create_diff_line], [method add_line_diffs_into_diff_hunk] and [method add_diff_hunks_into_diff_file]), each containing information about a diff. If [code]identifier[/code] is a file path, returns a file diff, and if it is a commit identifier, then returns a commit diff.
func _get_diff(identifier: String, area: int) -> Array[Dictionary]:
	pass;

#desc Returns an [Array] of [Dictionary] items (see [method create_diff_hunk]), each containing a line diff between a file at [code]file_path[/code] and the [code]text[/code] which is passed in.
func _get_line_diff(file_path: String, text: String) -> Array[Dictionary]:
	pass;

#desc Returns an [Array] of [Dictionary] items (see [method create_status_file]), each containing the status data of every modified file in the project folder.
func _get_modified_files_data() -> Array[Dictionary]:
	pass;

#desc Returns an [Array] of [Dictionary] items (see [method create_commit]), each containing the data for a past commit.
func _get_previous_commits(max_commits: int) -> Array[Dictionary]:
	pass;

#desc Returns an [Array] of [String]s, each containing the name of a remote configured in the VCS.
func _get_remotes() -> Array[Dictionary]:
	pass;

#desc Returns the name of the underlying VCS provider.
func _get_vcs_name() -> String:
	pass;

#desc Initializes the VCS plugin when called from the editor. Returns whether or not the plugin was successfully initialized. A VCS project is initialized at [code]project_path[/code].
func _initialize(project_path: String) -> bool:
	pass;

#desc Pulls changes from the remote. This can give rise to merge conflicts.
func _pull(remote: String) -> void:
	pass;

#desc Pushes changes to the [code]remote[/code]. Optionally, if [code]force[/code] is set to true, a force push will override the change history already present on the remote.
func _push(remote: String, force: bool) -> void:
	pass;

#desc Remove a branch from the local VCS.
func _remove_branch(branch_name: String) -> void:
	pass;

#desc Remove a remote from the local VCS.
func _remove_remote(remote_name: String) -> void:
	pass;

#desc Set user credentials in the underlying VCS. [code]username[/code] and [code]password[/code] are used only during HTTPS authentication unless not already mentioned in the remote URL. [code]ssh_public_key_path[/code], [code]ssh_private_key_path[/code], and [code]ssh_passphrase[/code] are only used during SSH authentication.
func _set_credentials(username: String, password: String, ssh_public_key_path: String, ssh_private_key_path: String, ssh_passphrase: String) -> void:
	pass;

#desc Shuts down VCS plugin instance. Called when the user either closes the editor or shuts down the VCS plugin through the editor UI.
func _shut_down() -> bool:
	pass;

#desc Stages the file present at [code]file_path[/code] to the staged area.
func _stage_file(file_path: String) -> void:
	pass;

#desc Unstages the file present at [code]file_path[/code] from the staged area to the unstaged area.
func _unstage_file(file_path: String) -> void:
	pass;

#desc Helper function to add an array of [code]diff_hunks[/code] into a [code]diff_file[/code].
func add_diff_hunks_into_diff_file(diff_file: Dictionary, diff_hunks: Dictionary[]) -> Dictionary:
	pass;

#desc Helper function to add an array of [code]line_diffs[/code] into a [code]diff_hunk[/code].
func add_line_diffs_into_diff_hunk(diff_hunk: Dictionary, line_diffs: Dictionary[]) -> Dictionary:
	pass;

#desc Helper function to create a commit [Dictionary] item. [code]msg[/code] is the commit message of the commit. [code]author[/code] is a single human-readable string containing all the author's details, e.g. the email and name configured in the VCS. [code]id[/code] is the identifier of the commit, in whichever format your VCS may provide an identifier to commits. [code]unix_timestamp[/code] is the UTC Unix timestamp of when the commit was created. [code]offset_minutes[/code] is the timezone offset in minutes, recorded from the system timezone where the commit was created.
func create_commit(msg: String, author: String, id: String, unix_timestamp: int, offset_minutes: int) -> Dictionary:
	pass;

#desc Helper function to create a [code]Dictionary[/code] for storing old and new diff file paths.
func create_diff_file(new_file: String, old_file: String) -> Dictionary:
	pass;

#desc Helper function to create a [code]Dictionary[/code] for storing diff hunk data. [code]old_start[/code] is the starting line number in old file. [code]new_start[/code] is the starting line number in new file. [code]old_lines[/code] is the number of lines in the old file. [code]new_lines[/code] is the number of lines in the new file.
func create_diff_hunk(old_start: int, new_start: int, old_lines: int, new_lines: int) -> Dictionary:
	pass;

#desc Helper function to create a [code]Dictionary[/code] for storing a line diff. [code]new_line_no[/code] is the line number in the new file (can be [code]-1[/code] if the line is deleted). [code]old_line_no[/code] is the line number in the old file (can be [code]-1[/code] if the line is added). [code]content[/code] is the diff text. [code]status[/code] is a single character string which stores the line origin.
func create_diff_line(new_line_no: int, old_line_no: int, content: String, status: String) -> Dictionary:
	pass;

#desc Helper function to create a [code]Dictionary[/code] used by editor to read the status of a file.
func create_status_file(file_path: String, change_type: int, area: int) -> Dictionary:
	pass;

#desc Pops up an error message in the edior which is shown as coming from the underlying VCS. Use this to show VCS specific error messages.
func popup_error(msg: String) -> void:
	pass;


