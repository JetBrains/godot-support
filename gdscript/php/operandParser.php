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

$target = "./classesGd/operators.gdconf";

$operators      = [];
$operatorPrefix = strlen("operator ");
foreach ($files as $filepath) {
    $paths = explode("/", $filepath);
    $filename = $paths[count($paths) - 1];

    if ($filename == "." || $filename == "..") continue;
    if (substr($filename, strlen($filename) - 4) != ".xml") continue;

    $data       = "";
    $class_name = substr($filename, 0, strlen($filename) - 4);
    if (substr($class_name, 0, 1) == "@") continue;

    $content = file_get_contents($filepath);
    $xml     = (array)simplexml_load_string($content);

    foreach ($xml['operators'] ?? [] as $value) {
        $value = (array)$value;
        $att   = (array)$value['@attributes'];

        $operator = substr($att['name'], $operatorPrefix);
        if (strpos($operator, "unary") !== false) continue;
        if ($operator == '~') continue;

        $return     = ((array)((array)($value['return']))['@attributes'])['type'];
        $rightParam = ((array)((array)($value['param']))['@attributes'])['type'];

        $operators[$class_name][$operator][$rightParam] = $return;
    }

}

$parsed = "";
foreach ($operators as $left => $operator) {
    $parsed .= "OP $left\n";
    foreach ($operator as $operand => $operands) {
        foreach ($operands as $right => $result) {
            $parsed .= "$operand $right : $result\n";
        }
    }

    $parsed .= "\n";
}

file_put_contents($target, $parsed);
