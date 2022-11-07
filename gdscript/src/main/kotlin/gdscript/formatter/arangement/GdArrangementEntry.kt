package gdscript.formatter.arangement

import com.intellij.psi.codeStyle.arrangement.ArrangementEntry

class GdArrangementEntry : ArrangementEntry {
    /**
     * @return    parent entry, e.g. it would be a class entry for a method entry
     * @see .getChildren
     */
    override fun getParent(): ArrangementEntry? {
        TODO("Not yet implemented")
    }

    /**
     * Entries can be organised into hierarchies, that means that only siblings position can be changed during the rearrangement.
     *
     *
     * Example: there are two classes at file and every class has two methods. Rearranger should not swap positions of methods
     * from the different classes here.
     *
     * @return    current entry's children. Empty collection if there are no children
     * @see .getParent
     */
    override fun getChildren(): MutableList<out ArrangementEntry> {
        TODO("Not yet implemented")
    }

    /**
     * There is a possible case that particular entry position depends on another entries positions. E.g. static initialization
     * block which uses static fields from the same class must be declared after them.
     *
     *
     * This method allows to answer what sibling entries must be located before the current one at the resulting arrangement algorithm.
     *
     *
     * There is also a special case when a list with the single entry which is the current's entry [parent][.getParent]
     * is returned - that means that current entry should be arranged to be the first child.
     *
     * @return    current entry's dependencies (if any)
     */
    override fun getDependencies(): MutableList<out ArrangementEntry>? {
        TODO("Not yet implemented")
    }

    /**
     * @return    start offset of the current entry (inclusive) within the target document. Rearranger engine uses this information
     * to move rearranged entries
     */
    override fun getStartOffset(): Int {
        TODO("Not yet implemented")
    }

    /**
     * @return    end offset of the current entry (exclusive) within the target document. Rearranger engine uses this information
     * to move rearranged entries
     */
    override fun getEndOffset(): Int {
        TODO("Not yet implemented")
    }

    /**
     * Sometimes we want particular entry to serve just as another entries holder. For example, we might want to arrange
     * anonymous class entries but don't want the class itself, say, to be arranged with normal inner classes.
     *
     *
     * That is achieved for entries which return `'false'` from this method call.
     *
     * @return    `true` if current entry can be [matched][ArrangementEntryMatcher.isMatched];
     * `false` otherwise
     */
    override fun canBeMatched(): Boolean {
        TODO("Not yet implemented")
    }
}