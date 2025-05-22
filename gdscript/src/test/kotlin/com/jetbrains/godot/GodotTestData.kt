package com.jetbrains.godot

import com.intellij.openapi.application.PathManager
import com.intellij.testFramework.ParsingTestCase
import java.nio.file.Path

fun ParsingTestCase.getBaseTestDataPath(): Path =
    PathManager.getHomeDirFor(javaClass)!!.resolve("dotnet/Plugins/godot-support/gdscript/src/test")