package gdscript.structureView

import GdScriptPluginIcons
import com.intellij.navigation.ItemPresentation
import com.jetbrains.rd.util.firstOrNull
import gdscript.GdKeywords
import gdscript.psi.GdClassVarDeclTl
import gdscript.psi.GdConstDeclTl
import gdscript.psi.GdEnumDeclTl
import gdscript.psi.GdMethodDeclTl
import javax.swing.Icon

internal object GdPresentationUtil {

    fun presentation(classVar: GdClassVarDeclTl): ItemPresentation {
        return object : ItemPresentation {
            override fun getPresentableText(): String = classVar.name
            override fun getLocationString(): String = classVar.returnType
            override fun getIcon(unused: Boolean): Icon? = GdScriptPluginIcons.GDScriptIcons.VAR_MARKER
        }
    }

    fun presentation(constVar: GdConstDeclTl): ItemPresentation {
        return object : ItemPresentation {
            override fun getPresentableText(): String = constVar.name
            override fun getLocationString(): String = constVar.returnType
            override fun getIcon(unused: Boolean): Icon? = GdScriptPluginIcons.GDScriptIcons.CONST_MARKER
        }
    }

    fun presentation(method: GdMethodDeclTl): ItemPresentation {
        return object : ItemPresentation {
            override fun getPresentableText(): String? = method.name
            override fun getLocationString(): String = method.returnType
            override fun getIcon(unused: Boolean): Icon? = GdScriptPluginIcons.GDScriptIcons.METHOD_MARKER
        }
    }

    fun presentation(enum: GdEnumDeclTl): ItemPresentation {
        return object : ItemPresentation {
            override fun getPresentableText(): String = enum.enumDeclNmi?.name ?: "(${enum.values.firstOrNull()?.key}, ...)"
            override fun getLocationString(): String = GdKeywords.INT
            override fun getIcon(unused: Boolean): Icon? = GdScriptPluginIcons.GDScriptIcons.ENUM_MARKER
        }
    }

}
