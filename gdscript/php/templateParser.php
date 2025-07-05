<?php

$baseDir = './godot-master/modules/gdscript/editor/script_templates';
$dirs    = scandir($baseDir);

foreach ($dirs as $dir) {
    if ($dir == "" || $dir[0] == ".") {
        continue;
    }

    $tDir = "$baseDir/$dir";
    if (!is_dir($tDir)) {
        continue;
    }

    $templates = scandir($tDir);
    foreach ($templates as $template) {
        if ($template == "" || $template[0] == ".") {
            continue;
        }

        $templateName = sprintf("%s $template", $dir);
        $filePath     = "$baseDir/$dir/$template";
        $content      = file_get_contents($filePath);
        $content      = str_replace('_BASE_', '${NAME}', $content);

        file_put_contents("../src/main/resources/fileTemplates/$templateName.ft", $content);
    }

}
