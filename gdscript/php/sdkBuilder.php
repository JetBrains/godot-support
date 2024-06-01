<?php

$sdkFolder = "../sdk";
$sdkPrefix = "GdSdk ";

if (!file_exists($sdkFolder)) {
    mkdir($sdkFolder, 0777, true);
}

// Fetch all existing tags
exec("git ls-remote --tags https://github.com/godotengine/godot 2>&1", $tags);
$existingTags = [];
$tagRegex = "/^.*?refs\/tags\/(.+?)-stable$/";
foreach ($tags as $tag) {
    preg_match($tagRegex, $tag, $matched);
    $tag = $matched[1] ?? '';
    if (str_starts_with($tag, "4.")) {
        $existingTags[] = $tag;
    }
}

// Fetch all existing SDKs
$existingSdks = [];
$sdkRegex = "/^$sdkPrefix(.+?).7z$/";
foreach (scandir($sdkFolder) as $sdk) {
    if (is_file("$sdkFolder/$sdk") && preg_match($sdkRegex, $sdk, $matched)) {
        $existingSdks[] = $matched[1];
    }
}

$toFetch = [];
foreach ($existingTags as $tag) {
    if (!in_array($tag, $existingSdks)) {
        $toFetch[] = $tag;
    }
}

$processSdk = function($tag) {
    $sdkPrefix = "GdSdk ";
    $sdkFile = "$sdkPrefix$tag.7z";
    $downloadTag = strtolower($tag);
    if ($downloadTag != "master") {
        $downloadTag = "$downloadTag-stable";
    }

    exec("rm -R godot-master || true");
    exec("rm -R classesGd || true");
    exec("wget -O - https://github.com/godotengine/godot/archive/$downloadTag.tar.gz | tar -xz");
    exec("mv godot-$downloadTag godot-master || true");
    exec("rm -R classesGd || true");
    exec("mkdir classesGd || true");
    exec("php classParser.php");
    exec("php operandParser.php");
    exec("php annotationParser.php");
    exec("zip -r -j '$sdkFile' classesGd/*");
    exec("mv '$sdkFile' '../sdk/$sdkFile'");
};

// Download and build sdks for newly released tags
foreach ($toFetch as $tag) {
    $processSdk($tag);
}
$processSdk("Master");

echo 1; // With master there's always an update
// echo count($toFetch) > 0 ? 0 : 1;
