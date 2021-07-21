package gdscript.competion.staticLoader.model;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class GdClass {

    public final String name;
    public final String parent;
    public final List<GdMethod> methods;

    public GdClass(@NotNull String name, @Nullable String parent, @NotNull List<GdMethod> methods) {
        this.name = name;
        this.parent = parent;
        this.methods = methods;
    }

}
