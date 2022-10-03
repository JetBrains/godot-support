<?php

$files = scandir("./classes");
$target = "./classesGd/%s.gd";

foreach ($files as $filename) {
    if ($filename == "." || $filename == "..") continue;
    $data = "";
    $class_name = substr($filename, 0, strlen($filename) - 4);
    $content = file_get_contents(sprintf("./classes/%s", $filename));

    $xml = (array) simplexml_load_string($content);
    $data .= sprintf("class_name %s\n\n", $class_name);

    foreach ($xml['constants'] as $value) {
        $att = (array) ((array) $value)['@attributes'];
        $data .= sprintf("const %s = %s;\n", $att['name'], $att['value']);
    }
    $data .= "\n";

    foreach ($xml['members'] as $value) {
        $att = (array) ((array) $value)['@attributes'];
        $data .= sprintf("var %s: %s;\n", $att['name'], $att['type']);
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

        $data .= sprintf("func %s(%s) -> %s:\n", $att['name'], implode($params, ', '), $ret['@attributes']['type']);
        $data .= sprintf("\tpass;\n");
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

        $data .= sprintf("%sfunc %s(%s) -> %s:\n", $quali, $att['name'], implode($params, ', '), $ret['@attributes']['type']);
        $data .= sprintf("\tpass;\n");
    }
    $data .= "\n";

    file_put_contents(sprintf($target, $class_name), $data);
}

