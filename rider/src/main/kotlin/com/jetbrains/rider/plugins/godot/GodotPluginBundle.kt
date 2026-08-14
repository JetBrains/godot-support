package com.jetbrains.rider.plugins.godot

import com.intellij.DynamicBundle
import org.jetbrains.annotations.Nls
import org.jetbrains.annotations.NonNls
import org.jetbrains.annotations.PropertyKey

object GodotPluginBundle {
  @NonNls
  private const val BUNDLE = "messages.GodotPluginBundle"
  private val instance = DynamicBundle(GodotPluginBundle::class.java, BUNDLE)

  @Nls
  fun message(
    @PropertyKey(resourceBundle = BUNDLE) key: String,
    vararg params: Any
  ): String {
    return instance.getMessage(key, *params)
  }
}