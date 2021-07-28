package gdscript.completion.util

import com.intellij.codeInsight.completion.CompletionResultSet
import gdscript.GdIcon
import gdscript.competion.staticLoader.StaticClassLoader
import gdscript.completion.GdLookup

object GdClassCompletionUtil {

    fun statics(result: CompletionResultSet) {
        StaticClassLoader.getClasses().forEach {
            val className = it.value.name;
            result.addElement(
                GdLookup.create(className, priority = GdLookup.USER_DEFINED, icon = GdIcon.getEditorIcon(className))
            );
        };
    }

}
