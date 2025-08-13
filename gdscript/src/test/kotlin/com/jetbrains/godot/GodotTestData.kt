package com.jetbrains.godot

import com.intellij.openapi.application.PathManager
import com.intellij.testFramework.ParsingTestCase
import java.nio.file.Path

fun ParsingTestCase.getBaseTestDataPath(): Path {

    val home = PathManager.getHomeDirFor(javaClass)
    if (home != null) {
        return home.resolve("dotnet/Plugins/godot-support/gdscript/src/test")
    }
    return PathManager.getPluginsDir().parent.parent.parent.parent.resolve("src/test")
}