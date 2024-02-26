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

$addDocumentation = function($data, $desc, $brief = null, $tutorials = null, $deprecated = false, $experimental = false) {
    if (!$brief && !$desc && !$tutorials && !$deprecated && !$experimental) {
        return $data;
    }

    $addSpace = false;
    if ($brief) {
        $addSpace = true;
        $lines = explode("\n", $brief);
        foreach ($lines as $line) {
            $line = trim($line);
            if ($line) {
                $data .= "## $line\n";
            }
        }
    }
    if ($desc) {
        if ($addSpace) {
            $data .= "##\n";
        }
        $addSpace = true;
        $lines = explode("\n", $desc);
        foreach ($lines as $line) {
            $line = trim($line);
            if ($line) {
                $data .= "## $line\n";
            }
        }
    }
    if ($tutorials) {
        if ($addSpace) {
            $data .= "##\n";
        }
        foreach ($tutorials as $tutorial) {
            $name = $tutorial[0];
            if ($name) {
                $name = "($name)";
            }
            $data .= sprintf("## @tutorial%s: %s\n", $name, $tutorial[1]);
        }
    }
    if ($deprecated == "true") {
        $data .= "## @deprecated\n";
    }
    if ($experimental == "true") {
        $data .= "## @experimental\n";
    }

    return $data;
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
        $paramType = $param[1];
        if (str_ends_with($paramType, '[]')) {
            $paramType = substr($paramType, 0, strlen($paramType) - 2);
            $paramType = "Array[$paramType]";
        }

        $p = sprintf("%s: %s", $param[0], $paramType);
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

    if ($filename == "." || $filename == "..") continue;
    if (substr($filename, strlen($filename) - 4) != ".xml") continue;

    $data = "";

    $content = file_get_contents($filepath);

    $xml = (array) simplexml_load_string($content);

    $att = (array) $xml['@attributes'];
    $class_name = str_replace("@", "_", $att['name'] ?? substr($filename, 0, strlen($filename) - 4));

    if ($att['inherits'] ?? null) {
        $data .= sprintf("extends %s\n", $att['inherits']);
    }

    $data .= sprintf("class_name %s\n\n", $class_name);

    /** Description */
    $baseTutorialUrl = 'https://docs.godotengine.org/en/stable';
    $desc = (array) ($xml['brief_description'] ?? []);
    $brief = $desc['0'] ?? "";

    $desc = (array) ($xml['description'] ?? []);
    $description = $desc['0'] ?? "";

    $links = (array) ($xml['tutorials'] ?? []);
    $tutorials = [];
    foreach ($xml['tutorials'] as $link) {
        $link = (array) $link;
        $p_att = $link['@attributes'];
        $value = str_replace('$DOCS_URL', $baseTutorialUrl, $link[0]);

        $tutorials[] = [$p_att['title'] ?? null, $value];
    }

    $data = $addDocumentation($data, $description, $brief, $tutorials, $att['is_deprecated'] ?? false, $att['is_experimental'] ?? false);
    $data .= "\n\n";

    /** Signals */
    foreach ($xml['signals'] ?? [] as $value) {
        $value = (array) $value;
        $att = (array) $value['@attributes'];

        $params = $parseParams($value);
        $desc = (array) ($value['description'] ?? []);
        $data = $addDocumentation($data, $desc['0'] ?? '', null, null, $att['is_deprecated'] ?? false, $att['is_experimental'] ?? false);

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
        $data = $addDocumentation($data, $value['0'] ?? null, null, null, $att['is_deprecated'] ?? false, $att['is_experimental'] ?? false);
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

    // GlobalScope enums can be accesses both ways as named & unnamed
    if ($class_name == "_GlobalScope") {
        foreach ($enums as $key => $values) {
            if (str_contains($key, '.')) {
                continue;
            }
            $data .= "#enum $key\n";
            $data .= "enum $key {\n";
            foreach ($values as $value) {
                $data .= "    $value";
            }
            $data .= "}\n";
        }
    }

    /** Variables */
    $getSetMethods = "";
    // TODO tohle by se mělo začlenit do hintů?
    if ($filename != 'ProjectSettings.xml') {
        foreach ($xml['members'] ?? [] as $value) {
            $value = (array) $value;
            $att = (array) $value['@attributes'];
            $data = $addDocumentation($data, $value['0'] ?? null, null, null, $att['is_deprecated'] ?? false, $att['is_experimental'] ?? false);
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
        $data = $addDocumentation($data, $desc['0'] ?? '', null, null, $att['is_deprecated'] ?? false, $att['is_experimental'] ?? false);

        $data .= sprintf("func %s(%s) -> %s:\n", $att['name'], implode(', ', $params), $formatType($ret['@attributes']['type']));
        $data .= sprintf("\tpass;\n\n");
    }
    $data .= "\n";

    /** Methods */
    foreach ($xml['methods'] ?? [] as $value) {
        $value = (array) $value;
        $att = (array) $value['@attributes'];
        $ret = (array) ($value['return'] ?? ['@attributes' => [ 'type' => 'void' ]]);

        $quali = explode(' ', $att['qualifiers'] ?? '');
        // TODO Tohle pak smazat, až bude const func implementována .. piozor existuje ještě "virtual" .. např _init
        $allowed = ["static", "vararg"];
        $quali = array_filter($quali, fn($qualiItem) => in_array($qualiItem, $allowed));
        $quali = implode(' ', $quali);

        if ($quali) {
            $quali .= " ";
        }

        $params = $parseParams($value);
        $desc = (array) ($value['description'] ?? []);
        $data = $addDocumentation($data, $desc['0'] ?? '', null, null, $att['is_deprecated'] ?? false, $att['is_experimental'] ?? false);

        $data .= sprintf("%sfunc %s(%s) -> %s:\n", $quali, $att['name'], implode(', ', $params), $formatType($ret['@attributes']['type']));
        $data .= sprintf("\tpass;\n\n");
    }
    $data .= "\n";
    $data .= $getSetMethods;

    file_put_contents(sprintf($target, $class_name), $data);

}

