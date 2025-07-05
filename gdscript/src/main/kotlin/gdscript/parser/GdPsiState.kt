package gdscript.parser

import com.intellij.lang.PsiBuilder.Marker
import com.intellij.psi.tree.IElementType
import com.intellij.util.containers.LimitedPool
import gdscript.psi.GdTypes.ARG_LIST

class GdPsiState {

    private var currentFrame: GdPsiFrame? = null
    private val b: GdPsiBuilder

    companion object {
        val FRAME_POOL_SIZE = 500
        val FRAMES = LimitedPool<GdPsiFrame>(FRAME_POOL_SIZE, GdPsiFrame::create)
    }

    constructor(b: GdPsiBuilder) {
        currentFrame = FRAMES.alloc()
        this.b = b
    }

    val isArgs get() = currentFrame?.withinArg ?: false
    val isError get() = currentFrame?.errorAt != null
    var errorAt get() = currentFrame?.errorAt
        set(value) { currentFrame?.errorAt = value }

    fun enterSection(elementType: IElementType, mark: Marker) {
        val newFrame = FRAMES.alloc().init(elementType, mark)
        newFrame.parent = currentFrame
        newFrame.withinArg = currentFrame?.withinArg ?: false || elementType == ARG_LIST
        currentFrame = newFrame
    }

    fun exitSection(result: Boolean, drop: Boolean = false): Boolean {
        val res = currentFrame?.exit(result) ?: false

        var errorType: IElementType? = null
        if (!res && currentFrame?.errorAt != null && !drop) {
            errorType = currentFrame!!.elementType
        }

        currentFrame = currentFrame?.parent
        if (errorType != null && !isError) {
            b.error(errorType.toString(), false)
        }

        return res
    }

    fun dropSection(result: Boolean): Boolean {
        currentFrame?.drop(result)

        var errorAt: Int? = null
        if (result && isError) {
            errorAt = currentFrame!!.errorAt
        }

        currentFrame = currentFrame?.parent
        if (errorAt != null && !isError) {
            b.errorAt = errorAt
        }

        return result
    }

    fun remapElement(elementType: IElementType) {
        currentFrame?.elementType = elementType
    }

    fun pin(result: Boolean = true): Boolean {
        currentFrame?.pinned = result || currentFrame?.pinned ?: false
        return currentFrame?.pinned ?: false
    }

    fun pinned(): Boolean {
        return currentFrame?.pinned ?: false
    }

    fun unpin() {
        currentFrame?.pinned = false
    }

}

class GdPsiFrame {

    companion object {
        fun create(): GdPsiFrame {
            return GdPsiFrame()
        }
    }

    var elementType: IElementType? = null
    var mark: Marker? = null
    var parent: GdPsiFrame? = null
    var errorAt: Int? = null
    var pinned: Boolean = false
    var required: Boolean = true
    var withinArg: Boolean = false

    fun init(elementType: IElementType, mark: Marker): GdPsiFrame {
        this.elementType = elementType
        this.mark = mark
        return this
    }

    fun exit(result: Boolean): Boolean {
        if (elementType == ARG_LIST) withinArg = false
        if (mark == null || elementType == null) return true
        if (result || pinned) mark!!.done(elementType!!)
        else mark!!.rollbackTo()

        return result || pinned
    }

    fun drop(result: Boolean): Boolean {
        if (elementType == ARG_LIST) withinArg = false
        if (mark == null) return true
        if (result || pinned) mark!!.drop()
        else mark!!.rollbackTo()

        return result || pinned
    }

}
