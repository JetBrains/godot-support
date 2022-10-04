<?php

$files = scandir("./classes");
$target = "./classesGd/%s.gd";

$formatDesc = function($desc, $key) {
    $desc = trim($desc);
    $desc = explode("\n", $desc);
    $desc = array_map(function ($it) { return trim($it); }, $desc);
    return sprintf("#%s %s\n", $key, implode($desc, sprintf("\n#%s ", $key)));
};

foreach ($files as $filename) {
    if ($filename == "." || $filename == "..") continue;
    $data = "";
    $class_name = substr($filename, 0, strlen($filename) - 4);
    $content = file_get_contents(sprintf("./classes/%s", $filename));

    $xml = (array) simplexml_load_string($content);

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

    $desc = (array) ($xml['description'] ?? []);
    if ($desc) {
        $data .= $formatDesc($desc['0'], "desc");
    }

    $data .= sprintf("class_name %s\n\n", $class_name);

    foreach ($xml['constants'] as $value) {
        $value = (array) $value;
        $att = (array) $value['@attributes'];
        if ($value['0'] ?? null) {
            $data .= $formatDesc($value['0'], "desc");
        }
        $data .= sprintf("const %s = %s;\n\n", $att['name'], $att['value']);
    }
    $data .= "\n";

    foreach ($xml['members'] as $value) {
        $value = (array) $value;
        $att = (array) $value['@attributes'];
        if ($value['0'] ?? null) {
            $data .= $formatDesc($value['0'], "desc");
        }
        $data .= sprintf("var %s: %s;\n\n", $att['name'], $att['type']);
    }
    $data .= "\n";

    foreach ($xml['constructors'] as $value) {
        $value = (array) $value;
        $att = (array) $value['@attributes'];
        $ret = (array) $value['return'];

        $params = [];
        $parsed = [];
        foreach ($value['param'] ?? [] as $param) {
            $param = (array) $param;
            $p_att = $param['@attributes'];
            $parsed[$p_att['index']] = [$p_att['name'], $p_att['type']];
        }
        foreach ($parsed as $param) {
            $params[] = sprintf("%s: %s", $param[0], $param[1]);
        }
        $desc = (array) ($value['description'] ?? []);
        if ($desc) {
            $data .= $formatDesc($desc['0'], "desc");
        }

        $data .= sprintf("func %s(%s) -> %s:\n", $att['name'], implode($params, ', '), $ret['@attributes']['type']);
        $data .= sprintf("\tpass;\n\n");
    }
    $data .= "\n";

    foreach ($xml['methods'] as $value) {
        $value = (array) $value;
        $att = (array) $value['@attributes'];
        $ret = (array) $value['return'];

        $quali = $att['qualifiers'];
        // TODO Tohle pak smazat, až bude const func implementována
        if ($quali == "const") {
            $quali = "";
        }

        if ($quali) {
            $quali .= " ";
        }

        $params = [];
        $parsed = [];
        foreach ($value['param'] ?? [] as $param) {
            $param = (array) $param;
            $p_att = $param['@attributes'];
            $parsed[$p_att['index']] = [$p_att['name'], $p_att['type']];
        }
        foreach ($parsed as $param) {
            $params[] = sprintf("%s: %s", $param[0], $param[1]);
        }
        $desc = (array) ($value['description'] ?? []);
        if ($desc) {
            $data .= $formatDesc($desc['0'], "desc");
        }

        $data .= sprintf("%sfunc %s(%s) -> %s:\n", $quali, $att['name'], implode($params, ', '), $ret['@attributes']['type']);
        $data .= sprintf("\tpass;\n\n");
    }
    $data .= "\n";

    file_put_contents(sprintf($target, $class_name), $data);
}

