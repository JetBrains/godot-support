package gdscript.formatter

import com.intellij.formatting.Alignment
import com.intellij.formatting.FormattingContext
import com.intellij.formatting.FormattingModel
import com.intellij.formatting.FormattingModelBuilder
import com.intellij.formatting.Indent
import gdscript.formatter.block.GdBlock

class GdFormattingModelBuilder : FormattingModelBuilder {

    override fun createModel(formattingContext: FormattingContext): FormattingModel {
        val settings = formattingContext.codeStyleSettings
        val ctx = GdFmtContext.create(settings)

        val initialBlock = GdBlock(
            null,
            formattingContext.node,
            null, // Wrap.createWrap(WrapType.NONE, false),
            Alignment.createAlignment(),
            Indent.getNoneIndent(),
            ctx,
        )

        // Use a GDScript-specific model so that document-less reformatting (e.g. the code-style
        // settings preview, which formats a non-physical PsiFile) correctly rewrites the
        // indentation-sensitive NEW_LINE/INDENT leading whitespace. See [GdFormattingModel].
        return GdFormattingModel(formattingContext.containingFile, initialBlock)
    }
}
