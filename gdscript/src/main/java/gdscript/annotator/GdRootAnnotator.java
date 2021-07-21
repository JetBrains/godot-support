package gdscript.annotator;

import com.intellij.lang.annotation.AnnotationHolder;
import com.intellij.lang.annotation.Annotator;
import com.intellij.psi.PsiElement;
import gdscript.psi.impl.GdInheritanceIdImpl;
import org.jetbrains.annotations.NotNull;

public class GdRootAnnotator implements Annotator {

    @Override
    public void annotate(@NotNull PsiElement element, @NotNull AnnotationHolder annotationHolder) {
        if (element.getParent() instanceof GdInheritanceIdImpl) {
        }

        // Ensure the Psi element contains a string that starts with the prefix and separator
//        PsiLiteralExpression literalExpression = (PsiLiteralExpression) element;
//        String value = literalExpression.getValue() instanceof String ? (String) literalExpression.getValue() : null;
//        if ((value == null) || !value.startsWith(SIMPLE_PREFIX_STR + SIMPLE_SEPARATOR_STR)) {
//            return;
//        }
//
//        // Define the text ranges (start is inclusive, end is exclusive)
//        // "simple:key"
//        //  01234567890
//        TextRange prefixRange = TextRange.from(element.getTextRange().getStartOffset(), SIMPLE_PREFIX_STR.length() + 1);
//        TextRange separatorRange = TextRange.from(prefixRange.getEndOffset(), SIMPLE_SEPARATOR_STR.length());
//        TextRange keyRange = new TextRange(separatorRange.getEndOffset(), element.getTextRange().getEndOffset() - 1);
//
//        // highlight "simple" prefix and ":" separator
//        holder.newSilentAnnotation(HighlightSeverity.INFORMATION)
//                .range(prefixRange).textAttributes(DefaultLanguageHighlighterColors.KEYWORD).create();
//        holder.newSilentAnnotation(HighlightSeverity.INFORMATION)
//                .range(separatorRange).textAttributes(SimpleSyntaxHighlighter.SEPARATOR).create();
//
//
//        // Get the list of properties for given key
//        String key = value.substring(SIMPLE_PREFIX_STR.length() + SIMPLE_SEPARATOR_STR.length());
//        List<SimpleProperty> properties = SimpleUtil.findProperties(element.getProject(), key);
//        if (properties.isEmpty()) {
//            holder.newAnnotation(HighlightSeverity.ERROR, "Unresolved property")
//                    .range(keyRange)
//                    .highlightType(ProblemHighlightType.LIKE_UNKNOWN_SYMBOL)
//                    // ** Tutorial step 18.3 - Add a quick fix for the string containing possible properties
//                    .withFix(new SimpleCreatePropertyQuickFix(key))
//                    .create();
//        } else {
//            // Found at least one property, force the text attributes to Simple syntax value character
//            holder.newSilentAnnotation(HighlightSeverity.INFORMATION)
//                    .range(keyRange).textAttributes(SimpleSyntaxHighlighter.VALUE).create();
//        }
    }

}
