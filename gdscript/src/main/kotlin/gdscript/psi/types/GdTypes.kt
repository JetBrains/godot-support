package gdscript.psi.types

import com.intellij.psi.tree.IElementType
import gdscript.psi.GdTokenType
import gdscript.psi.impl.GdClassIdElementType
import gdscript.psi.impl.GdClassNamingElementType

val FILE: IElementType = GdTokenType("FILE")
val CLASS_NAME: IElementType = GdTokenType("CLASS_NAME")
val CLASS_NAME_NMI: IElementType = GdClassIdElementType.getInstance("CLASS_NAME_NMI")
val CLASS_NAMING: IElementType = GdClassNamingElementType.getInstance("CLASS_NAMING")

val SEMICON: IElementType = GdTokenType("SEMICON")
val NEW_LINE: IElementType = GdTokenType("NEW_LINE")
val END_STMT: IElementType = GdTokenType("END_STMT")
