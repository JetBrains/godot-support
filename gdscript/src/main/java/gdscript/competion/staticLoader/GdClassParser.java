package gdscript.competion.staticLoader;

import gdscript.competion.staticLoader.model.GdClass;
import gdscript.competion.staticLoader.model.GdMethod;
import org.jetbrains.annotations.NotNull;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;

import java.io.IOException;
import java.io.InputStream;
import java.util.Comparator;
import java.util.stream.Collectors;

@Deprecated
public class GdClassParser {

    public GdClass parseXmlClass(@NotNull InputStream input) throws IOException {
        return parseClass(Jsoup.parse(input, null, ""));
    }

    private GdClass parseClass(Element doc) {
        return new GdClass(
                doc.select("class").attr("name"),
                doc.select("class").attr("inherits"),
                doc.select("method")
                        .stream()
                        .map(this::parseMethod)
                        .collect(Collectors.toList())
        );
    }

    private GdMethod parseMethod(Element doc) {
        return new GdMethod(
                doc.select("method").attr("name"),
                doc.select("return").attr("type"),
                doc.select("argument")
                        .stream()
                        .map(this::parseArgument)
                        .sorted(Comparator.comparingInt(a -> a.index))
                        .collect(Collectors.toList())
        );
    }

    private GdMethod.Argument parseArgument(Element doc) {
        return new GdMethod.Argument(
                doc.select("argument").attr("name"),
                doc.select("argument").attr("type"),
                Integer.parseInt(doc.select("argument").attr("index"))
        );
    }

}
