package gdscript.competion;

import com.intellij.codeInsight.completion.CompletionContributor;
import com.intellij.codeInsight.completion.CompletionParameters;
import com.intellij.codeInsight.completion.CompletionResultSet;
import com.intellij.psi.util.PsiTreeUtil;
import gdscript.competion.utils.FileCompletionUtil;
import gdscript.competion.utils.MethodCompletionUtil;
import gdscript.competion.utils.PositionUtil;
import gdscript.psi.GdInheritance;
import gdscript.psi.GdTypes;
import org.jetbrains.annotations.NotNull;

public class GdMethodNameCompletionContributor extends CompletionContributor {

    @Override
    public void fillCompletionVariants(@NotNull CompletionParameters parameters, @NotNull CompletionResultSet resultSet) {
        // TODO tohle nesmí platit v root -> rozdělit metodu a lambdu přes jflex stavy
        if (PositionUtil.isLeafPreceded(parameters, GdTypes.FUNC)) {
            GdInheritance inheritance = PsiTreeUtil.findChildOfType(FileCompletionUtil.currentFile(parameters), GdInheritance.class);
            if (inheritance != null) {
               // MethodCompletionUtil.parentMethods(inheritance.getParentClassName(), resultSet);
            }
        }

        super.fillCompletionVariants(parameters, resultSet);
    }

}
