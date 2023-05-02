<?php

$sdkFolder = "../sdk";
$sdkPrefix = "GdSdk ";

// Fetch all existing tags
exec("git ls-remote --tags https://github.com/godotengine/godot 2>&1", $tags);
$existingTags = [];
$tagRegex = "/^.+?refs\/tags\/(.+?)-stable$/";
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

// Download and build sdks for newly released tags
foreach ($toFetch as $tag) {
    $sdkFile = "$sdkPrefix$tag.7z";
    exec("rm -R godot-master || true");
    exec("rm -R classesGd || true");
    exec("wget -O - https://github.com/godotengine/godot/archive/$tag-stable.tar.gz | tar -xz");
    exec("mv godot-$tag-stable godot-master");
    exec("rm -R classesGd || true");
    exec("mkdir classesGd || true");
    exec("php classParser.php");
    exec("zip -r '$sdkFile' classesGd");
    exec("mv '$sdkFile' '../sdk/$sdkFile'");
}

exit(count($toFetch) > 0 ? 0 : 1);
