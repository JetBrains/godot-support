package com.jetbrains.rider.plugins.godot.textMate

import com.intellij.codeInsight.template.TemplateActionContext
import com.intellij.codeInsight.template.TemplateContextType
import com.jetbrains.rider.plugins.godot.Util

class GdScriptTemplateContextType : TemplateContextType(Util.GDSCRIPT) {

    override fun isInContext(context: TemplateActionContext): Boolean =
        Util.isGdFile(context.editor?.virtualFile)
}
