<?php

$sdkFolder = "sdk";
$sdkPrefix = "GdSdk ";

// Function to execute command and check for errors
$execAndCheck = function($cmd) {
    $output = [];
    $returnVar = 0;
    exec($cmd, $output, $returnVar);
    if ($returnVar !== 0) {
        echo "Error executing command: $cmd\n";
        echo "Return code: $returnVar\n";
        echo "Output: " . implode("\n", $output) . "\n";
        exit(1); // Exit with error code
    }
    return $output;
};

if (!file_exists($sdkFolder)) {
    mkdir($sdkFolder, 0777, true);
}

// Fetch all existing tags
$tags = $execAndCheck("git ls-remote --tags https://github.com/godotengine/godot 2>&1");
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

$processSdk = function($tag) use ($execAndCheck) {
    $downloadTag = strtolower($tag);
    if ($downloadTag != "master") {
        $downloadTag = "$downloadTag-stable";
    }

    $execAndCheck("rm -R godot-master || true");
    $execAndCheck("wget https://github.com/godotengine/godot/archive/$downloadTag.tar.gz");
    #exec("wget https://github.com/godotengine/godot/archive/refs/tags/$downloadTag.tar.gz"); # is it similar to the previous line?
    $execAndCheck("tar -xf $downloadTag.tar.gz");
    if ($downloadTag != "master") {
        $execAndCheck("mv godot-$downloadTag godot-master || true");
    }
    $execAndCheck("rm -R classesGd || true");
    $execAndCheck("mkdir classesGd || true");
    $execAndCheck("php classParser.php");
    $execAndCheck("php operandParser.php");
    $execAndCheck("php annotationParser.php");
    $execAndCheck("mv 'classesGd' 'sdk/$tag'");
};

$execAndCheck("rm -R $sdkFolder || true");
$execAndCheck("mkdir $sdkFolder || true");

// Download and build sdks for newly released tags
foreach ($toFetch as $tag) {
    $processSdk($tag);
}
$processSdk("Master");

$execAndCheck("tar -caf gdscriptsdk.tar.xz -C $sdkFolder ."); # good compression
# upload to https://jetbrains.team/p/net/packages/files/gdscriptsdk
