package gdscript.parser

import com.intellij.psi.tree.IElementType
import com.intellij.util.containers.LimitedPool

class GdPsiState {

    var currentFrame: GdPsiFrame

    companion object {
        val FRAME_POOL_SIZE = 500
        val FRAMES = LimitedPool<GdPsiFrame>(FRAME_POOL_SIZE, GdPsiFrame::create)
    }

    constructor() {
        currentFrame = FRAMES.alloc()
    }

    fun enterSection(elementType: IElementType) {
        val newFrame = FRAMES.alloc().init(elementType)
        newFrame.parent = currentFrame
        currentFrame = newFrame
    }

}

class GdPsiFrame {

    companion object {
        fun create(): GdPsiFrame {
            return GdPsiFrame()
        }
    }

    var elementType: IElementType? = null
    var parent: GdPsiFrame? = null
    var errorAt: Int? = null

    fun init(elementType: IElementType): GdPsiFrame {
        this.elementType = elementType
        return this
    }

}
