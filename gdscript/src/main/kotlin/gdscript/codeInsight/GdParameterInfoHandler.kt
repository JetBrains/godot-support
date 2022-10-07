package gdscript.codeInsight

import com.intellij.lang.parameterInfo.*
import com.intellij.openapi.project.DumbAware
import com.intellij.openapi.util.TextRange
import com.intellij.psi.util.PsiTreeUtil
import gdscript.psi.GdCallEx
import gdscript.psi.GdMethodDeclTl
import gdscript.psi.GdMethodIdNmi
import gdscript.psi.GdTypes
import gdscript.reference.GdClassMemberReference

class GdParameterInfoHandler : ParameterInfoHandler<GdCallEx, GdMethodDeclTl>, DumbAware {

    override fun findElementForParameterInfo(context: CreateParameterInfoContext): GdCallEx? {
        val element = getFunctionCall(context) ?: return null;

        val refId = element.firstChild?.firstChild ?: return null;
        when (val declId = GdClassMemberReference(refId, refId.textRangeInParent).resolve()) {
            is GdMethodIdNmi -> {
                val decl = PsiTreeUtil.getParentOfType(declId, GdMethodDeclTl::class.java) ?: return element;
                context.itemsToShow = arrayOf(decl);
            }
        }

        return element;
    }

    override fun findElementForUpdatingParameterInfo(context: UpdateParameterInfoContext): GdCallEx? {
        val element = getFunctionCall(context) ?: return null;

        val offset = context.offset;
        val elRange: TextRange = element.textRange;
        val index =
            if (offset <= elRange.startOffset || offset >= elRange.endOffset) -1
            else ParameterInfoUtils.getCurrentParameterIndex(
                element.argList?.node ?: return element,
                offset,
                GdTypes.COMMA)
        context.setCurrentParameter(index);

        return element;
    }

    override fun updateUI(declaration: GdMethodDeclTl?, context: ParameterInfoUIContext) {
        val currentParam = context.currentParameterIndex;
        var startOffset = -1;
        var endOffset = -1;

        val builder = StringBuilder();

        var i = 0;
        declaration?.parameters?.forEach { it ->
            if (i > 0) {
                builder.append(", ");
            }
            val start = builder.length;
            builder.append("${it.key}: ${it.value}");
            if (currentParam == i) {
                startOffset = start;
                endOffset = builder.length;
            }
            i += 1;
        }

        if (i <= 0) {
            if (declaration?.isVariadic == true) {
                repeat(currentParam) {
                    builder.append("$it, ");
                };
                builder.append("args...");
            } else {
                builder.append("no parameters");
            }
        }

        context.setupUIComponentPresentation(
            builder.toString(),
            startOffset,
            endOffset,
            false,
            false,
            false,
            context.defaultParameterColor,
        );
    }

    override fun updateParameterInfo(parameterOwner: GdCallEx, context: UpdateParameterInfoContext) {
        context.parameterOwner = parameterOwner;
    }

    override fun showParameterInfo(element: GdCallEx, context: CreateParameterInfoContext) {
        context.showHint(element, element.textRange.startOffset + 1, this);
    }

    private fun getFunctionCall(context: ParameterInfoContext): GdCallEx? {
        val element = context.file.findElementAt(context.offset) ?: return null;
        return PsiTreeUtil.getParentOfType(element, GdCallEx::class.java);
    }
}
