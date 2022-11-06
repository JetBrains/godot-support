<?php

$files = scandir("./classes");
$target = "./classesGd/%s.gd";

$formatDesc = function($desc, $key) {
    $desc = trim($desc);
    $desc = explode("\n", $desc);
    $desc = array_map(function ($it) { return trim($it); }, $desc);
    return sprintf("#%s %s\n", $key, implode(sprintf("\n#%s ", $key), $desc));
};

$formatType = function($type) {
    $type = $type['@attributes']['type'];
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
        $parsed[$p_att['index']] = [$p_att['name'], $p_att['type'], $p_att['default'] ?? null];
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

foreach ($files as $filename) {
    // if ($filename != "Vector2.xml") { continue; }

    if ($filename == "." || $filename == "..") continue;
    $data = "";
    $class_name = substr($filename, 0, strlen($filename) - 4);
    $class_name = str_replace("@", "_", $class_name);

    $content = file_get_contents(sprintf("./classes/%s", $filename));

    $xml = (array) simplexml_load_string($content);

    $att = (array) $xml['@attributes'];
    if ($att['inherits'] ?? null) {
        $data .= sprintf("extends %s\n", $att['inherits']);
    }

    $desc = (array) ($xml['brief_description'] ?? []);
    if ($desc) {
        $data .= $formatDesc($desc['0'], "brief");
    }

// TODO lze někam nacpat linky? -> resolve $DOCS_URL
//     $desc = (array) ($xml['tutorials'] ?? []);
//     foreach ($desc['link'] ?? [] as $param) {
//         $param = (array) $param;
//         $p_att = $param['@attributes'];
//         $parsed[$p_att['index']] = [$p_att['name'], $p_att['type']];
//     }

    //if (substr($class_name, 0, 1) != '@') {
        $desc = (array) ($xml['description'] ?? []);
        if ($desc) {
            $data .= $formatDesc($desc['0'], "desc");
        }

        $data .= sprintf("class_name %s\n\n", $class_name);
    //}

    foreach ($xml['constants'] ?? [] as $value) {
        $value = (array) $value;
        $att = (array) $value['@attributes'];
        if ($value['0'] ?? null) {
            $data .= $formatDesc($value['0'], "desc");
        }
        $data .= sprintf("const %s = %s;\n\n", $att['name'], $att['value']);
    }
    $data .= "\n";

    foreach ($xml['members'] ?? [] as $value) {
        $value = (array) $value;
        $att = (array) $value['@attributes'];
        if ($value['0'] ?? null) {
            $data .= $formatDesc($value['0'], "desc");
        }
        $data .= sprintf("var %s: %s;\n\n", $att['name'], $att['type']);
    }
    $data .= "\n";

    foreach ($xml['constructors'] ?? [] as $value) {
        $value = (array) $value;
        $att = (array) $value['@attributes'];
        $ret = (array) $value['return'];

        $params = $parseParams($value);
        $desc = (array) ($value['description'] ?? []);
        if ($desc) {
            $data .= $formatDesc($desc['0'], "desc");
        }

        $data .= sprintf("func %s(%s) -> %s:\n", $att['name'], implode(', ', $params), $formatType($ret));
        $data .= sprintf("\tpass;\n\n");
    }
    $data .= "\n";

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

        $data .= sprintf("%sfunc %s(%s) -> %s:\n", $quali, $att['name'], implode(', ', $params), $formatType($ret));
        $data .= sprintf("\tpass;\n\n");
    }
    $data .= "\n";

    file_put_contents(sprintf($target, $class_name), $data);
}

