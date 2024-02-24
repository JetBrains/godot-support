package gdscript.psi.utils

import com.intellij.openapi.vfs.VirtualFile
import gdscript.utils.VirtualFileUtil.localPath
import java.io.File

object PsiGdResourceUtil {

    val SEPARATOR = "/";

    @Deprecated("VirtualFileUtil")
    fun resourcePath(file: VirtualFile, withPrefix: Boolean = true): String {
        return "${if (withPrefix) "res://" else ""}${file.localPath().trimStart(File.separatorChar).split(File.separatorChar).joinToString(SEPARATOR)}"
    }

}
