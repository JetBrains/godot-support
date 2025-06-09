package com.jetbrains.rider.godot.community

import com.intellij.DynamicBundle
import org.jetbrains.annotations.Nls
import org.jetbrains.annotations.PropertyKey

object GodotCommunityBundle {
    private const val BUNDLE = "messages.GodotCommunityBundle"

    private val INSTANCE: DynamicBundle = DynamicBundle(GodotCommunityBundle::class.java, BUNDLE)

    @Nls
    fun message(@PropertyKey(resourceBundle = BUNDLE) key: String, vararg params: Any): String {
        return INSTANCE.getMessage(key, *params)
    }
}