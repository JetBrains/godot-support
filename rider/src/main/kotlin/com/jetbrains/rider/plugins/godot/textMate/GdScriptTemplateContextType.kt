package com.jetbrains.rider.plugins.godot.textMate

import com.intellij.codeInsight.template.TemplateActionContext
import com.intellij.codeInsight.template.TemplateContextType

class GdScriptTemplateContextType : TemplateContextType("GdScript") {

    override fun isInContext(context: TemplateActionContext): Boolean =
        context.editor?.virtualFile?.extension.equals("gd", true);

}
