package gdscript.competion.staticLoader.model;

import org.jetbrains.annotations.NotNull;

import java.util.List;

@Deprecated
public class GdMethod {

    public final String name;
    public final String returnType;
    public final List<Argument> arguments;

    public GdMethod(@NotNull String name, @NotNull String returnType, @NotNull List<Argument> arguments) {
        this.name = name;
        this.returnType = returnType;
        this.arguments = arguments;
    }

    public String getArgSignature() {
        StringBuilder b = new StringBuilder();
        b.append("(");

        if (!arguments.isEmpty()) {
            Argument arg0 = arguments.get(0);
            b.append(arg0.name);
            b.append(": ");
            b.append(arg0.returnType);

            arguments
                    .stream()
                    .skip(1)
                    .forEach(arg -> {
                        b.append(", ");
                        b.append(arg.name);
                        b.append(": ");
                        b.append(arg.returnType);
                    });
        }
        b.append(")");

        return b.toString();
    }

    public static class Argument {
        public String name;
        public String returnType;
        public int index;

        public Argument(@NotNull String name, @NotNull String returnType, int index) {
            // Xml doc has invalid type as typeHint
            if (returnType.equals("StringName")) {
                returnType = "String";
            }

            this.name = name;
            this.returnType = returnType;
            this.index = index;
        }
    }

}
