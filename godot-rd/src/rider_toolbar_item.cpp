#include "rider_toolbar_item.h"

#include "rider_icon.h"
#include "godot_cpp/classes/control.hpp"
#include "godot_cpp/classes/editor_interface.hpp"
#include "godot_cpp/classes/menu_button.hpp"
#include "godot_cpp/classes/project_settings.hpp"
#include "godot_cpp/classes/scene_tree.hpp"
#include "godot_cpp/classes/scene_tree_timer.hpp"
#include "godot_cpp/classes/script.hpp"
#include "godot_cpp/core/object.hpp"
#include "godot_cpp/variant/utility_functions.hpp"


static SceneTree *get_editor_tree() {
	auto *editor = EditorInterface::get_singleton();
	if (editor == nullptr) {
		return nullptr;
	}
	Control *base_control = editor->get_base_control();
	return base_control == nullptr ? nullptr : base_control->get_tree();
}

void RiderToolbarItem::attach() {
	if (attached) {
		return;
	}
	SceneTree *tree = get_editor_tree();
	if (tree == nullptr) {
		UtilityFunctions::print_verbose("[RIDER RD] cannot add the toolbar item: the editor tree is not available");
		return;
	}
	attached = true;
	// The addon can add its control after us, or again after it was disabled and enabled.
	tree->connect("node_added", callable_mp(this, &RiderToolbarItem::_on_node_added));
	if (Node *menu = find_toolbar_menu(EditorInterface::get_singleton()->get_base_control()); menu != nullptr) {
		add_item(menu);
	} else {
		UtilityFunctions::print_verbose("[RIDER RD] the Rider addon toolbar menu is not found yet");
	}
}

void RiderToolbarItem::detach() {
	if (!attached) {
		return;
	}
	attached = false;
	const Callable on_node_added = callable_mp(this, &RiderToolbarItem::_on_node_added);
	if (SceneTree *tree = get_editor_tree(); tree != nullptr && tree->is_connected("node_added", on_node_added)) {
		tree->disconnect("node_added", on_node_added);
	}
	remove_item();
}

void RiderToolbarItem::set_enabled(bool value) {
	enabled = value;
	PopupMenu *popup = get_popup();
	if (popup == nullptr) {
		return;
	}
	const int32_t index = popup->get_item_index(ITEM_ID);
	if (index >= 0) {
		popup->set_item_disabled(index, !enabled);
	}
}

PopupMenu *RiderToolbarItem::get_popup() const {
	return popup_id.is_valid() ? Object::cast_to<PopupMenu>(ObjectDB::get_instance(popup_id)) : nullptr;
}

bool RiderToolbarItem::is_csharp_project() {
	// On save, the editor adds the "C#" feature if the project has a .csproj file, and removes it otherwise.
	const PackedStringArray features = ProjectSettings::get_singleton()->get_setting("application/config/features");
	return features.has("C#");
}

void RiderToolbarItem::add_item(Node *node) {
	if (get_popup() != nullptr) {
		return;
	}
	auto *menu = Object::cast_to<MenuButton>(node);
	PopupMenu *popup = menu == nullptr ? nullptr : menu->get_popup();
	if (popup == nullptr) {
		return;
	}
	popup->connect("id_pressed", callable_mp(this, &RiderToolbarItem::_on_id_pressed));
	popup->connect("about_to_popup", callable_mp(this, &RiderToolbarItem::_on_about_to_popup));
	popup_id = popup->get_instance_id();
	update_item();
}

void RiderToolbarItem::update_item() {
	PopupMenu *popup = get_popup();
	if (popup == nullptr) {
		return;
	}
	const bool has_item = popup->get_item_index(ITEM_ID) >= 0;
	if (!is_csharp_project()) {
		if (has_item) {
			remove_popup_items(popup);
		}
		showing_stop_action = false;
		return;
	}
	if (!has_item) {
		popup->add_separator("", SEPARATOR_ID);
		popup->add_item(DEBUG_CURRENT_SCENE, ITEM_ID);
	}
	showing_stop_action = is_debugging_callback && is_debugging_callback();
	const String action_name = showing_stop_action ? STOP_DEBUGGING : DEBUG_CURRENT_SCENE;
	const String action_tooltip = showing_stop_action ? STOP_DEBUGGING_TOOLTIP : DEBUG_CURRENT_SCENE_TOOLTIP;
	Ref<Texture2D> action_icon;
	if (showing_stop_action) {
		Control *base_control = EditorInterface::get_singleton()->get_base_control();
		if (base_control != nullptr) {
			action_icon = base_control->get_theme_icon("Stop", "EditorIcons");
		}
	} else {
		if (debug_icon.is_null()) {
			debug_icon = create_dotnet_debug_icon(EditorInterface::get_singleton()->get_editor_scale());
		}
		if (debug_icon.is_valid()) {
			action_icon = debug_icon;
		}
	}
	const int32_t index = popup->get_item_index(ITEM_ID);
	popup->set_item_text(index, action_name);
	popup->set_item_icon(index, action_icon);
	// Keep the icon within the regular editor icon width.
	popup->set_item_icon_max_width(index, static_cast<int32_t>(16 * EditorInterface::get_singleton()->get_editor_scale()));
	popup->set_item_tooltip(index, action_tooltip);
	popup->set_item_disabled(index, !enabled);
	if (!has_item) {
		UtilityFunctions::print_verbose("[RIDER RD] added the toolbar item");
	}
}

void RiderToolbarItem::remove_popup_items(PopupMenu *popup) {
	for (const int id : { ITEM_ID, SEPARATOR_ID }) {
		if (const int32_t index = popup->get_item_index(id); index >= 0) {
			popup->remove_item(index);
		}
	}
}

void RiderToolbarItem::remove_item() {
	PopupMenu *popup = get_popup();
	popup_id = ObjectID();
	if (popup == nullptr) {
		return;
	}
	const Callable on_id_pressed = callable_mp(this, &RiderToolbarItem::_on_id_pressed);
	if (popup->is_connected("id_pressed", on_id_pressed)) {
		popup->disconnect("id_pressed", on_id_pressed);
	}
	const Callable on_about_to_popup = callable_mp(this, &RiderToolbarItem::_on_about_to_popup);
	if (popup->is_connected("about_to_popup", on_about_to_popup)) {
		popup->disconnect("about_to_popup", on_about_to_popup);
	}
	remove_popup_items(popup);
}

bool RiderToolbarItem::is_toolbar_menu(Node *node) {
	if (Object::cast_to<MenuButton>(node) == nullptr) {
		return false;
	}
	const Ref<Script> script = node->get_script();
	return script.is_valid() && script->get_global_name() == StringName(TOOLBAR_MENU_CLASS);
}

Node *RiderToolbarItem::find_toolbar_menu(Node *node) {
	if (node == nullptr) {
		return nullptr;
	}
	if (is_toolbar_menu(node)) {
		return node;
	}
	for (int i = 0; i < node->get_child_count(); ++i) {
		if (Node *result = find_toolbar_menu(node->get_child(i)); result != nullptr) {
			return result;
		}
	}
	return nullptr;
}

void RiderToolbarItem::_on_node_added(Node *node) {
	if (get_popup() == nullptr && is_toolbar_menu(node)) {
		add_item(node);
	}
}

void RiderToolbarItem::_on_about_to_popup() {
	// The project can become a C# project while the editor runs, so check it each time the popup opens.
	update_item();
}

void RiderToolbarItem::_on_id_pressed(int64_t id) {
	if (id != ITEM_ID) {
		return;
	}
	pressed_stop_action = showing_stop_action;
	SceneTree *tree = get_editor_tree();
	if (tree == nullptr) {
		_on_press_delay_timeout();
		return;
	}
	tree->create_timer(PRESS_DELAY_SEC)->connect("timeout", callable_mp(this, &RiderToolbarItem::_on_press_delay_timeout));
}

void RiderToolbarItem::_on_press_delay_timeout() {
	if (!attached) {
		return;
	}
	const auto &callback = pressed_stop_action ? stop_callback : pressed_callback;
	if (!callback) {
		ERR_PRINT("[RIDER RD] internal error: toolbar item called without setting callback");
		return;
	}
	callback();
}

void RiderToolbarItem::_bind_methods() {
}
