package gdscript.highlighter;

import com.intellij.openapi.editor.colors.TextAttributesKey;
import com.intellij.openapi.fileTypes.SyntaxHighlighter;
import com.intellij.openapi.options.colors.AttributesDescriptor;
import com.intellij.openapi.options.colors.ColorDescriptor;
import com.intellij.openapi.options.colors.ColorSettingsPage;
import gdscript.GdIcon;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.util.Map;

public class GdColorSettingsPage implements ColorSettingsPage {
    private static final AttributesDescriptor[] DESCRIPTORS = new AttributesDescriptor[]{
            new AttributesDescriptor("Keywords", GdHighlighterColors.KEYWORD),
            new AttributesDescriptor("Method declaration", GdHighlighterColors.METHOD_DECLARATION),
            new AttributesDescriptor("Method call", GdHighlighterColors.METHOD_CALL),
            new AttributesDescriptor("Class types", GdHighlighterColors.CLASS_TYPE),
            new AttributesDescriptor("Flow control", GdHighlighterColors.FLOW_KEYWORDS),
            new AttributesDescriptor("Numbers", GdHighlighterColors.NUMBER),
            new AttributesDescriptor("Variables", GdHighlighterColors.IDENTIFIER),
            new AttributesDescriptor("Comments", GdHighlighterColors.COMMENT),
            new AttributesDescriptor("Member reference", GdHighlighterColors.MEMBER),
            new AttributesDescriptor("Strings", GdHighlighterColors.STRING),
            new AttributesDescriptor("Annotations", GdHighlighterColors.ANNOTATION),
            new AttributesDescriptor("NodePath", GdHighlighterColors.NODE_PATH),
            new AttributesDescriptor("Errors", GdHighlighterColors.BAD_CHARACTER),
    };

    @Override
    public @Nullable Icon getIcon() {
        return GdIcon.FILE;
    }

    @Override
    public @NotNull SyntaxHighlighter getHighlighter() {
        return new GdSyntaxHighlighter();
    }

    @Override
    public @NonNls
    @NotNull String getDemoText() {
        return
                "extends BaseClass\n" +
                "class_name MyClass, \"res://path/to/optional/icon.svg\"\n" +
                "\n" +
                "\n" +
                "var a = $NodePath\n" +
                "var s = \"Hello\"\n" +
                "var arr = [1, 2, 3]\n" +
                "var dict = {\"key\": \"value\", 2: 3}\n" +
                "@export var typed_var: int\n" +
                "@export var inferred_type := \"String\"\n" +
                "\n" +
                "const ANSWER = 42\n" +
                "const THE_NAME = \"Charly\"\n" +
                "\n" +
                "enum Named {THING_1, THING_2, ANOTHER_THING = -1}\n" +
                "\n" +
                "var v2 = Vector2(1, 2)\n" +
                "\n" +
                "\n" +
                "func _init():\n" +
                "    print(\"Constructed!\")\n" +
                "    var local_var = 5\n" +
                "\n" +
                "    if param1 < local_var:\n" +
                "        print(param1)\n" +
                "    elif param2 > 5:\n" +
                "        print(param2)\n" +
                "    else:\n" +
                "        pass\n" +
                "\n" +
                "    for i in range(20):\n" +
                "        print(i)\n" +
                "\n" +
                "    while param2 != 0:\n" +
                "        param2 -= 1\n" +
                "\n" +
                "    var local_var2 = param1 + 3\n" +
                "    return local_var2\n" +
                "\n" +
                "\n" +
                "class Something:\n" +
                "    var a = 10\n" +
                "\n"
        ;
    }

    @Override
    public @Nullable Map<String, TextAttributesKey> getAdditionalHighlightingTagToDescriptorMap() {
        return null;
    }

    @NotNull
    @Override
    public AttributesDescriptor[] getAttributeDescriptors() {
        return DESCRIPTORS;
    }

    @NotNull
    @Override
    public ColorDescriptor[] getColorDescriptors() {
        return ColorDescriptor.EMPTY_ARRAY;
    }

    @NotNull
    @Override
    public String getDisplayName() {
        return "GdScript";
    }
}
