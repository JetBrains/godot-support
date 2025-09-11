package com.jetbrains.godot

import com.intellij.openapi.application.PathManager
import junit.framework.TestCase
import java.nio.file.Path

fun TestCase.getBaseTestDataPath(): Path {

    val home = PathManager.getHomeDirFor(javaClass)
    if (home != null) {
        return home.resolve("dotnet/Plugins/godot-support/gdscript/src/test")
    }
    return PathManager.getPluginsDir().parent.parent.parent.parent.resolve("src/test")
}