package gdscript.competion.staticLoader;

import gdscript.competion.staticLoader.model.GdClass;

import java.io.*;
import java.util.*;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

public class StaticClassLoader {

    private static final HashMap<String, GdClass> classes = new HashMap<>();

    static {
        try {
            String folder = "_classes_test";
            GdClassParser parser = new GdClassParser();
            ClassLoader loader = StaticClassLoader.class.getClassLoader();
            var directory = loader.getResource(folder).getPath();
            String src = directory.substring(6, directory.length() - (folder.length() + 2));

            ZipInputStream zip = new ZipInputStream(new FileInputStream(src));
            while (true) {
                ZipEntry e = zip.getNextEntry();
                if (e == null) break;
                String name = e.getName();
                if (name.startsWith(folder) && name.endsWith(".xml")) {
                    GdClass gdClass = parser.parseXmlClass(loader.getResourceAsStream(name));
                    classes.put(gdClass.name, gdClass);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static HashMap<String, GdClass> getClasses() {
        return classes;
    }

}
