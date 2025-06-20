<?php

$sdkFolder = "sdk";
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
    $downloadTag = strtolower($tag);
    if ($downloadTag != "master") {
        $downloadTag = "$downloadTag-stable";
    }

    exec("rm -R godot-master || true");
    exec("wget https://github.com/godotengine/godot/archive/$downloadTag.tar.gz");
    #exec("wget https://github.com/godotengine/godot/archive/refs/tags/$downloadTag.tar.gz"); # is it similar to the previous line?
    exec("tar -xf $downloadTag.tar.gz");
    if ($downloadTag != "master") {
        exec("mv godot-$downloadTag godot-master || true");
    }
    exec("rm -R classesGd || true");
    exec("mkdir classesGd || true");
    exec("php classParser.php");
    exec("php operandParser.php");
    exec("php annotationParser.php");
    exec("mv 'classesGd' 'sdk/$tag'");
};

exec("rm -R $sdkFolder || true");
exec("mkdir $sdkFolder || true");

// Download and build sdks for newly released tags
foreach ($toFetch as $tag) {
    $processSdk($tag);
}
$processSdk("Master");

exec("tar -caf gdscriptsdk-1.0.0-SNAPSHOT.tar.xz -C $sdkFolder ."); # good compression
# upload to https://jetbrains.team/p/net/packages/files/gdscriptsdk