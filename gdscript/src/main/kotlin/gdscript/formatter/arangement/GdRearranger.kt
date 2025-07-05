package gdscript.formatter.arangement

import com.intellij.openapi.editor.Document
import com.intellij.openapi.util.Pair
import com.intellij.openapi.util.TextRange
import com.intellij.psi.PsiElement
import com.intellij.psi.codeStyle.CodeStyleSettings
import com.intellij.psi.codeStyle.arrangement.ArrangementSettings
import com.intellij.psi.codeStyle.arrangement.ArrangementSettingsSerializer
import com.intellij.psi.codeStyle.arrangement.Rearranger

class GdRearranger : Rearranger<GdArrangementEntry> {
    /**
     * Tries to wrap given element into arrangement entry at the target context.
     *
     *
     * This is useful in a situation when new element is generated and we're deciding where to insert it (e.g. new field is
     * generated and we want to insert it according to the arrangement rules like 'fields before methods').
     *
     *
     * @param element   element to wrap into format eligible for further processing by arrangement engine
     * @param settings  arrangement settings to use. The primary idea is to make the rearranger aware about
     * [grouping rules][StdArrangementTokens.Grouping] (if any). E.g. it's not worth to process java method bodies
     * in order to build method dependency graph if no such grouping rule is defined
     * @return          arrangement entry for the given element if it's possible to perform the mapping and list of arrangement entries
     * available at the given context plus newly created entry for the given element;
     * `null` otherwise
     */
    override fun parseWithNew(
        root: PsiElement,
        document: Document?,
        ranges: MutableCollection<out TextRange>,
        element: PsiElement,
        settings: ArrangementSettings,
    ): Pair<GdArrangementEntry, MutableList<GdArrangementEntry>>? {
        TODO("Not yet implemented")
    }

    /**
     * Allows to build rearranger-interested data for the given element.
     *
     * @param root      root element which children should be parsed for the rearrangement
     * @param document  document which corresponds to the target PSI tree
     * @param ranges    target offsets ranges to use for filtering given root's children
     * @param settings  arrangement settings to use. The primary idea is to make the rearranger aware about
     * [grouping rules][StdArrangementTokens.Grouping] (if any). E.g. it's not worth to process java method bodies
     * in order to build method dependency graph if no such grouping rule is defined
     * @return          given root's children which are subject for further rearrangement
     */
    override fun parse(
        root: PsiElement,
        document: Document?,
        ranges: MutableCollection<out TextRange>,
        settings: ArrangementSettings,
    ): MutableList<GdArrangementEntry> {
        TODO("Not yet implemented")
    }

    /**
     * @return serializer to save [arrangement settings][ArrangementSettings].
     * Serializer is expected to be lazy and don't save
     * [default settings][com.intellij.psi.codeStyle.arrangement.std.ArrangementStandardSettingsAware.getDefaultSettings].
     *
     *
     * @see DefaultArrangementSettingsSerializer
     */
    override fun getSerializer(): ArrangementSettingsSerializer {
        TODO("Not yet implemented")
    }

    /**
     * Allows to answer how many blank lines should be inserted before the target arrangement entry which position is changed.
     *
     * @param settings  code style settings to use (it's assumed that returned result is derived from 'blank lines' code style settings)
     * @param parent    target entry's parent (if available)
     * @param previous  previous entry (if available)
     * @param target    target entry which blank lines number the caller is interested in
     * @return          number of blank lines to insert before the target entry;
     * negative as an indication that no blank lines adjustment is necessary
     */
    override fun getBlankLines(
        settings: CodeStyleSettings,
        parent: GdArrangementEntry?,
        previous: GdArrangementEntry?,
        target: GdArrangementEntry,
    ): Int {
        TODO("Not yet implemented")
    }
}