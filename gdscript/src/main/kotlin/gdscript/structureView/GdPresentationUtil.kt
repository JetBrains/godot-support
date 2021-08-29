package gdscript.structureView

import com.intellij.navigation.ItemPresentation
import gdscript.GdIcon
import gdscript.psi.*
import javax.swing.Icon

object GdPresentationUtil {

    fun presentation(classVar: GdClassVarDeclTl): ItemPresentation {
        return object : ItemPresentation {
            override fun getPresentableText(): String? = classVar.name;
            override fun getLocationString(): String? = classVar.returnType;
            override fun getIcon(unused: Boolean): Icon? = GdIcon.getEditorIcon(GdIcon.VAR_MARKER);
        }
    }

    fun presentation(constVar: GdConstDeclTl): ItemPresentation {
        return object : ItemPresentation {
            override fun getPresentableText(): String? = constVar.constName;
            override fun getLocationString(): String? = constVar.returnType;
            override fun getIcon(unused: Boolean): Icon? = GdIcon.getEditorIcon(GdIcon.CONST_MARKER);
        }
    }

    fun presentation(method: GdMethodDeclTl): ItemPresentation {
        return object : ItemPresentation {
            override fun getPresentableText(): String? = method.name;
            override fun getLocationString(): String? = method.returnType;
            override fun getIcon(unused: Boolean): Icon? = GdIcon.getEditorIcon(GdIcon.METHOD_MARKER);
        }
    }

}