package tscn.inspection

import com.intellij.codeInspection.LocalInspectionTool
import com.intellij.codeInspection.ProblemHighlightType
import com.intellij.codeInspection.ProblemsHolder
import com.intellij.psi.PsiElementVisitor
import com.intellij.psi.PsiFile
import com.intellij.psi.util.childrenOfType
import gdscript.GdScriptBundle
import tscn.TresFileType
import tscn.TscnFileType
import tscn.psi.TscnConnectionHeader
import tscn.psi.TscnHeader
import tscn.psi.TscnNodeHeader
import tscn.psi.TscnParagraph
import tscn.psi.TscnResourceHeader
import tscn.psi.TscnSceneHeader
import tscn.psi.TscnTypes
import tscn.psi.TscnUnknownHeader

/**
 * Inspection that checks for incorrect section order in TSCN/TRES files.
 *
 * Godot requires sections in the following order:
 * 1. \[gd_scene\] or \[gd_resource\] - File descriptor
 * 2. \[ext_resource\] - External resources
 * 3. \[sub_resource\] - Internal resources
 * 4. \[node\] - Nodes
 * 5. \[connection\] - Connections
 */
class TscnSectionOrderInspection : LocalInspectionTool() {

    override fun buildVisitor(holder: ProblemsHolder, isOnTheFly: Boolean): PsiElementVisitor {
        return SectionOrderVisitor(holder)
    }

    private class SectionOrderVisitor(
        private val holder: ProblemsHolder
    ) : PsiElementVisitor() {

        override fun visitFile(file: PsiFile) {
            super.visitFile(file)

            if (file.fileType !is TscnFileType && file.fileType !is TresFileType) {
                return
            }

            val paragraphs = file.childrenOfType<TscnParagraph>()

            var maxOrderSeen = -1
            var maxOrderSectionType: TscnSectionType? = null

            for (paragraph in paragraphs) {
                val header = paragraph.header
                val sectionType = TscnSectionType.fromHeader(header)

                if (sectionType.order < maxOrderSeen) {
                    holder.registerProblem(
                        header,
                        GdScriptBundle.message(
                            "inspection.tscn.section.order.wrong",
                            sectionType.displayName,
                            maxOrderSectionType!!.displayName
                        ),
                        ProblemHighlightType.GENERIC_ERROR
                    )
                } else if (sectionType.order > maxOrderSeen) {
                    maxOrderSeen = sectionType.order
                    maxOrderSectionType = sectionType
                }
            }
        }
    }

    private enum class TscnSectionType(val order: Int, val displayName: String) {
        GD_SCENE(0, "gd_scene"),
        GD_RESOURCE(0, "gd_resource"),
        EXT_RESOURCE(1, "ext_resource"),
        SUB_RESOURCE(2, "sub_resource"),
        NODE(3, "node"),
        CONNECTION(4, "connection"),
        UNKNOWN(Int.MAX_VALUE, "unknown");

        companion object {
            fun fromHeader(header: TscnHeader): TscnSectionType = when (header) {
                is TscnSceneHeader -> GD_SCENE
                is TscnResourceHeader -> EXT_RESOURCE
                // Work-around, since gd_resource and sub_resource don't have their own token
                is TscnUnknownHeader -> when (header.node.findChildByType(TscnTypes.IDENTIFIER)?.text) {
                    GD_RESOURCE.displayName -> GD_RESOURCE
                    SUB_RESOURCE.displayName -> SUB_RESOURCE
                    else -> UNKNOWN
                }

                is TscnNodeHeader -> NODE
                is TscnConnectionHeader -> CONNECTION
                else -> UNKNOWN
            }
        }
    }

}
