package gdscript.competion;

import com.intellij.codeInsight.completion.CompletionContributor;
import com.intellij.codeInsight.completion.CompletionParameters;
import com.intellij.codeInsight.completion.CompletionResultSet;
import com.intellij.codeInsight.completion.PrioritizedLookupElement;
import com.intellij.codeInsight.lookup.LookupElementBuilder;
import com.intellij.psi.PsiElement;
import com.intellij.psi.impl.source.tree.LeafPsiElement;
import com.intellij.psi.impl.source.tree.PsiErrorElementImpl;
import gdscript.competion.utils.*;
import gdscript.psi.GdClassNameNm;
import gdscript.psi.impl.GdInheritanceIdNmiImpl;
import org.jetbrains.annotations.NotNull;

public class GdRootCompletionContributor extends CompletionContributor {

    @Override
    public void fillCompletionVariants(@NotNull CompletionParameters parameters, @NotNull CompletionResultSet resultSet) {
        PsiElement parent = parameters.getPosition().getParent();
        if (!(parent instanceof PsiErrorElementImpl)) {
            return;
        }

        LeafPsiElement precedor = PositionUtil.getPrecedor(parameters);
        if (precedor == null) {
            resultSet.addElement(GdLookupElementBuilder
                    .create("extends", " ")
                    .withInsertHandler(GdLookupInsertHandler.INSTANCE)
            );
            return;
        }

        if (PositionUtil.isPreceded(parameters, GdInheritanceIdNmiImpl.class, precedor)) {
            resultSet.addElement(PrioritizedLookupElement.withPriority(
                    GdLookupElementBuilder
                            .create("class_name", " ")
                            .withInsertHandler(GdLookupInsertHandler.INSTANCE),
                    CompletionPriority.PRIORITY_KEYWORDS
            ));
        }

        if (PositionUtil.isPreceded(parameters, GdClassNameNm.class, precedor)) {
            resultSet.addElement(PrioritizedLookupElement.withPriority(
                    LookupElementBuilder.create("tool"),
                    CompletionPriority.PRIORITY_KEYWORDS
            ));
        }

        FileCompletionUtil.topLevelCompletions(resultSet);

        super.fillCompletionVariants(parameters, resultSet);
    }

}