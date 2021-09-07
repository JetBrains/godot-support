package gdscript;

import com.intellij.openapi.util.IconLoader;
import com.intellij.ui.RowIcon;
import com.intellij.util.IconUtil;

import javax.swing.*;
import java.util.HashMap;

public class GdIcon {

    public static final Icon FILE = IconLoader.getIcon("icons/file.png", GdIcon.class);

    // Godot editor icons
    @Deprecated
    public static final Icon OBJECT = IconLoader.getIcon("icons/godot_editor/Object.svg", GdIcon.class);
    public static final String METHOD_MARKER = "Forward-sq";
    public static final String VAR_MARKER = "KeyBezierSelected";
    public static final String CONST_MARKER = "KeyXform";

    public static HashMap<String, Icon> editorIcons = new HashMap<>();

    public static Icon getEditorIcon(String className) {
        Icon icon = editorIcons.get(className);
        if (icon == null) {
            try {
                Icon loaded = IconLoader.getIcon(String.format("icons/godot_editor/%s.svg", className), GdIcon.class);
                if (loaded.getIconHeight() > 1) {
                    loaded = IconUtil.toSize(loaded, 16, 16);
                    editorIcons.put(className, loaded);
                } else {
                    editorIcons.put(className, OBJECT);
                }
            } catch (Exception e) {
                editorIcons.put(className, OBJECT);
            }
        }

        return editorIcons.get(className);
    }

}
