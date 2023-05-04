<?php

$files = [];
$classes = scandir("./godot-master/doc/classes");
foreach ($classes as $filename) {
    $files[] = sprintf("./godot-master/doc/classes/%s", $filename);
}

// Search modules
$modules = scandir("./godot-master/modules");

foreach ($modules as $module) {
    //$files[] = sprintf("./classes/%s", $filename);
    $modulePath = sprintf("./godot-master/modules/%s", $module);
    if (substr($module, 0, 1) == ".") continue;

    if (is_dir($modulePath)) {
        $modulePath = sprintf("%s/doc_classes", $modulePath);
        if (is_dir($modulePath)) {
            $classes = scandir($modulePath);
            foreach ($classes as $filename) {
                $files[] = sprintf("%s/%s", $modulePath, $filename);
            }
        }
    }
}

$target = "./classesGd/%s.gd";
$moduleTarget = "./classesGd/modules/%s.gd";

$formatDesc = function($desc, $key) {
    $desc = trim($desc);
    $desc = explode("\n", $desc);
    $desc = array_map(function ($it) { return trim($it); }, $desc);
    return sprintf("#%s %s\n", $key, implode(sprintf("\n#%s ", $key), $desc));
};

$formatType = function($type) {
    if (substr($type, -2) == "[]") {
        $type = substr($type, 0, strlen($type) - 2);
        return sprintf("Array[%s]", $type);
    }

    return $type;
};

$parseParams = function($value) {
    $params = [];
    $parsed = [];
    $list = $value['param'] ?? [];
    if (!is_array($list)) {
        $list = [$list];
    }

    foreach ($list as $param) {
        $param = (array) $param;
        $p_att = $param['@attributes'];
        $p_name = $p_att['name'];
        if ($p_name == "var") $p_name = "variable";
        $parsed[$p_att['index']] = [$p_name, $p_att['type'], $p_att['default'] ?? null];
    }
    foreach ($parsed as $param) {
        $p = sprintf("%s: %s", $param[0], $param[1]);
        if (($param[2] ?? null) !== null) {
            $p = sprintf("%s = %s", $p, $param[2]);
        }
        $params[] = $p;
    }

    return $params;
};

foreach ($files as $filepath) {
    $paths = explode("/", $filepath);
    $filename = $paths[count($paths) - 1];

//     if ($filename != "CanvasItem.xml") { continue; }

    if ($filename == "." || $filename == "..") continue;
    if (substr($filename, strlen($filename) - 4) != ".xml") continue;

    $data = "";
    $class_name = substr($filename, 0, strlen($filename) - 4);
    $class_name = str_replace("@", "_", $class_name);

    $content = file_get_contents($filepath);

    $xml = (array) simplexml_load_string($content);

    $att = (array) $xml['@attributes'];
    if ($att['inherits'] ?? null) {
        $data .= sprintf("extends %s\n", $att['inherits']);
    }

    /** Description */
    $desc = (array) ($xml['brief_description'] ?? []);
    if ($desc) {
        $data .= $formatDesc($desc['0'], "brief");
    }

    //if (substr($class_name, 0, 1) != '@') {
        $desc = (array) ($xml['description'] ?? []);
        if ($desc) {
            $data .= $formatDesc($desc['0'], "desc");
        }

    // Tutorial links
    $baseUrl = 'https://docs.godotengine.org/en/stable';
    $links = (array) ($xml['tutorials'] ?? []);
    foreach ($xml['tutorials'] as $link) {
        $link = (array) $link;
        $p_att = $link['@attributes'];
        $value = str_replace('$DOCS_URL', $baseUrl, $link[0]);
        $data .= $formatDesc(sprintf("[%s] %s", $p_att['title'], $value), "tutorial");
    }

    $data .= sprintf("class_name %s\n\n", $class_name);
    //}

    /** Signals */
    foreach ($xml['signals'] ?? [] as $value) {
        $value = (array) $value;
        $att = (array) $value['@attributes'];

        $params = $parseParams($value);
        $desc = (array) ($value['description'] ?? []);
        if ($desc) {
            $data .= $formatDesc($desc['0'], "desc");
        }

        $paramsStr = implode(', ', $params);
        if ($paramsStr) {
            $paramsStr = sprintf("(%s)", $paramsStr);
        }

        $data .= sprintf("signal %s%s\n", $att['name'], $paramsStr);
    }

    $enums = [];

    /** Constants */
    foreach ($xml['constants'] ?? [] as $value) {
        $value = (array) $value;
        $att = (array) $value['@attributes'];
        if ($value['0'] ?? null) {
            $data .= $formatDesc($value['0'], "desc");
        }
        $enum = $att['enum'] ?? null;
        if ($enum) {
            $enums[$enum][] = sprintf("%s = %s,\n", $att['name'], $att['value']);
        } else {
            $data .= sprintf("const %s = %s;\n\n", $att['name'], $att['value']);
        }
    }
    $data .= "\n";

    foreach ($enums as $key => $values) {
        $data .= "#enum $key\n";
        $data .= "enum {\n";
        foreach ($values as $value) {
            $data .= "    $value";
        }
        $data .= "}\n";
    }

    /** Variables */
    $getSetMethods = "";
    // TODO tohle by se mělo začlenit do hintů?
    if ($filename != 'ProjectSettings.xml') {
        foreach ($xml['members'] ?? [] as $value) {
            $value = (array) $value;
            $att = (array) $value['@attributes'];
            if ($value['0'] ?? null) {
                $data .= $formatDesc($value['0'], "desc");
            }
            $data .= sprintf("var %s: %s", $att['name'], $formatType($att['type']));

            $getter = $att['getter'] ?? null ? sprintf("get = %s", $att['getter']) : null;
            $setter = $att['setter'] ?? null ? sprintf("set = %s", $att['setter']) : null;
            $getSet = [];
            if ($getter) {
                $getSet[] = $getter;
                $getSetMethods .= sprintf("func %s() -> %s:\n\treturn %s\n\n", $att['getter'], $formatType($att['type']), $att['name']);
            }
            if ($setter) {
                $getSet[] = $setter;
                $getSetMethods .= sprintf("func %s(value: %s) -> void:\n\t%s = value\n\n", $att['setter'], $formatType($att['type']), $att['name']);
            }
            if ($getSet) {
                $data .= ":\n\t";
                $data .= implode(', ', $getSet);
            }
            $data .= "\n\n";
        }

        $data .= "\n";
    }

    /** Constructor methods */
    foreach ($xml['constructors'] ?? [] as $value) {
        $value = (array) $value;
        $att = (array) $value['@attributes'];
        $ret = (array) $value['return'];

        $params = $parseParams($value);
        $desc = (array) ($value['description'] ?? []);
        if ($desc) {
            $data .= $formatDesc($desc['0'], "desc");
        }

        $data .= sprintf("func %s(%s) -> %s:\n", $att['name'], implode(', ', $params), $formatType($ret['@attributes']['type']));
        $data .= sprintf("\tpass;\n\n");
    }
    $data .= "\n";

    /** Methods */
    foreach ($xml['methods'] ?? [] as $value) {
        $value = (array) $value;
        $att = (array) $value['@attributes'];
        $ret = (array) ($value['return'] ?? ['@attributes' => [ 'type' => 'void' ]]);

        $quali = $att['qualifiers'] ?? ""; // TODO multiple?
        // TODO Tohle pak smazat, až bude const func implementována .. piozor existuje ještě "virtual" .. např _init
        $allowed = ["static", "vararg"];
        if (!in_array($quali, $allowed)) {
            $quali = "";
        }

        if ($quali) {
            $quali .= " ";
        }

        $params = $parseParams($value);
        $desc = (array) ($value['description'] ?? []);
        if ($desc) {
            $data .= $formatDesc($desc['0'], "desc");
        }

        $data .= sprintf("%sfunc %s(%s) -> %s:\n", $quali, $att['name'], implode(', ', $params), $formatType($ret['@attributes']['type']));
        $data .= sprintf("\tpass;\n\n");
    }
    $data .= "\n";
    $data .= $getSetMethods;

//    if (strpos($filepath, "modules") > 0) {
//        file_put_contents(sprintf($moduleTarget, $class_name), $data);
//    } else {
        file_put_contents(sprintf($target, $class_name), $data);
//    }

}

