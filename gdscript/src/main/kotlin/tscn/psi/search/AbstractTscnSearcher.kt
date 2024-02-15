package tscn.psi.search

import com.intellij.openapi.project.Project
import com.intellij.psi.PsiFile
import com.intellij.psi.search.PsiSearchHelper
import com.intellij.psi.search.PsiSearchScopeUtil
import com.intellij.psi.search.UsageSearchContext.IN_CODE
import com.intellij.psi.util.PsiTreeUtil
import com.intellij.psi.util.descendantsOfType
import com.intellij.usageView.UsageInfo
import gdscript.utils.ProjectUtil.contentScope
import gdscript.utils.VirtualFileUtil.resourcePath
import tscn.TscnFileType
import tscn.psi.TscnConnectionHeader
import tscn.psi.TscnDataLine
import tscn.psi.TscnNodeHeader
import tscn.psi.TscnParagraph
import tscn.psi.search.processor.TscnConnectionHeaderCollectingProcessor
import tscn.psi.search.processor.TscnParagraphCollectingProcessor
import tscn.psi.utils.TscnNodeUtil.findNode
import tscn.psi.utils.TscnResourceUtil

abstract class AbstractTscnSearcher(val project: Project, scriptFile: PsiFile) {

    private val searchHelper = PsiSearchHelper.getInstance(project)
    // restrict searchScope to Tscn files
    private val searchScope = PsiSearchScopeUtil.restrictScopeTo(project.contentScope(), TscnFileType)
    private val linkedScenes = TscnResourceUtil.findTscnByResources(scriptFile).map { it.containingFile }
    private val sceneResources = linkedScenes.map { it.virtualFile.resourcePath() }
    private val scriptResource = scriptFile.virtualFile.resourcePath()

    protected fun listConnectionReference(searchTerm: String, earlyStop: Boolean, conSourceFn : (TscnConnectionHeader) -> String) : List<UsageInfo> {
        val result = mutableListOf<UsageInfo>()
        val processor = TscnConnectionHeaderCollectingProcessor()
        searchHelper.processElementsWithWord(processor, searchScope, searchTerm, IN_CODE, true)

        // for each connection check if the header points to a scene with the attached script
        for (con in processor.result) {

            // find the referenced node from the connection
            val node = findNode(con.containingFile.descendantsOfType<TscnNodeHeader>(), conSourceFn(con)) ?: continue

            // check if node resource is one of the ones we are looking for
            if (sceneResources.contains(node.instanceResource) || scriptResource == node.scriptResource) {
                result.add(UsageInfo(con))
                // check for early stop
                if (earlyStop) return result
            }
        }

        return result
    }

    protected fun listAnimationReference(searchTerm: String, earlyStop: Boolean, type: String) : List<UsageInfo> {
        val result = mutableListOf<UsageInfo>()
        val processor = TscnParagraphCollectingProcessor("Animation")
        searchHelper.processElementsWithWord(processor, searchScope, searchTerm, IN_CODE, true)
        val typeSuffix = "/type = \"${type}\""

        for (par in processor.result) {
            // first check if there is a node which maybe matches the criteria
            val nodePaths = extractNodePaths(par.containingFile)
            if (nodePaths.isEmpty()) continue
            // check if the paragraph is a match
            if (matchInParagraph(par, searchTerm, typeSuffix, nodePaths)) {
                result.add(UsageInfo(par))
                // check for early stop
                if (earlyStop) return result
            }
        }

        return result
    }

    private fun extractNodePaths(sceneFile: PsiFile): MutableList<String> {
        val nodePaths = mutableListOf<String>()

        for (node in sceneFile.descendantsOfType<TscnNodeHeader>()) {

            // if it is an eligible node add to the result
            if (sceneResources.contains(node.instanceResource) || scriptResource == node.scriptResource) {
                if (node.parentPath.isEmpty()) {
                    nodePaths.add("/path = NodePath(\".\")")
                } else if(node.parentPath == ".") {
                    nodePaths.add("/path = NodePath(\"${node.name}\")")
                } else {
                    nodePaths.add("/path = NodePath(\"${node.parentPath}/${node.name}\")")
                }
            }
        }
        return nodePaths
    }

    private fun matchInParagraph(par: TscnParagraph, searchTerm: String, typeSuffix: String, nodePaths: List<String>) : Boolean {
        var activeTrack: String? = null
        var possibleMatch = false

        for (line in PsiTreeUtil.getStubChildrenOfTypeAsList(par, TscnDataLine::class.java)) {
            val lineText = line.text ?: continue

            // check if we are still in the same track
            if (activeTrack != null && lineText.startsWith(activeTrack)) {

                // if we have a possible match and the keys contains the searchTerm we have a match
                if (possibleMatch && lineText.startsWith("${activeTrack}/keys") && lineText.contains(searchTerm)) {
                    return true
                }
                // check if the path leads to a node which has the script attached
                else if (lineText.startsWith("${activeTrack}/path")) {
                    possibleMatch = nodePaths.any { suffix -> lineText.contains(suffix) }
                }
            } else {
                // reset
                activeTrack = null
            }

            // check if we found a track of the matching type
            var typeIdx = lineText.indexOf(typeSuffix)
            if (typeIdx != -1) {
                activeTrack = lineText.substring(0, typeIdx)
                possibleMatch = false
            }
        }

        return false
    }
}