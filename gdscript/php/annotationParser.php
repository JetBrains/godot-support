<?php

$filename = "@GDScript.xml";
$target = "./classesGd/annotation.gdconf";

$data    = "";
$content = file_get_contents(sprintf("./godot-master/modules/gdscript/doc_classes/%s", $filename));
$xml     = (array)simplexml_load_string($content);

foreach ($xml['annotations'] ?? [] as $value) {
    $required = 0;
    $value    = (array)$value;
    $att      = (array)$value['@attributes'];

    $name       = substr($att['name'], 1);
    $qualifiers = $att['qualifiers'] ?? '' === 'vararg';
    $vararg     = $qualifiers ? '_variadic' : str_repeat(" ", strlen("_variadic"));
    $paramLine       = "";

    $any = false;
    $params = $value['param'] ?? [];
    if (!is_array($params)) {
        $params = [(array)$params];
    }

    foreach ($params as $index => $param) {
        $any = true;
        $param = (array)$param;
        $p_att = $param['@attributes'] ?? [];
        $pName = $p_att['name'] ?? '';
        $type  = $p_att['type'] ?? 'Variant';
        $paramLine  .= " $pName:$type";

        if (!array_key_exists('default', $p_att)) {
            $required++;
        }
    }

    $paramSpace = str_repeat(" ", 30 - strlen($name));

    $data .= "AN $vararg $required $name$paramSpace$paramLine\n";
}

file_put_contents($target, $data);
