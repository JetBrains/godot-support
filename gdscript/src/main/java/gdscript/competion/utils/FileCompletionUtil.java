package gdscript.competion.utils;

import com.intellij.codeInsight.completion.CompletionParameters;
import com.intellij.codeInsight.completion.CompletionResultSet;
import com.intellij.codeInsight.completion.PrioritizedLookupElement;
import com.intellij.codeInsight.lookup.LookupElementBuilder;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.PsiManager;
import com.intellij.psi.search.FileTypeIndex;
import com.intellij.psi.search.GlobalSearchScope;
import com.intellij.util.indexing.FileBasedIndex;
import gdscript.GdFileType;
import gdscript.psi.GdFile;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.Collection;
import java.util.stream.Collectors;

public class FileCompletionUtil {

    private static final String[] topLevelKeywords = {"func", "signal", "const", "var"};

    public static GdFile currentFile(@NotNull CompletionParameters parameters) {
        return (GdFile) parameters.getOriginalFile();
    }

    public static Collection<GdFile> listFilesWithoutSelf(@NotNull CompletionParameters parameters) {
        Collection<VirtualFile> virtualFiles =
                FileBasedIndex.getInstance().getContainingFiles(FileTypeIndex.NAME, GdFileType.INSTANCE,
                        GlobalSearchScope.allScope(parameters.getPosition().getProject()));

        String filename = parameters.getOriginalFile().getName();

        return virtualFiles
                .stream()
                .map((VirtualFile file) -> (GdFile) PsiManager.getInstance(parameters.getPosition().getProject()).findFile(file))
                .filter((GdFile file) -> file != null && !filename.equals(file.getName()))
                .collect(Collectors.toList());
    }

    public static void topLevelCompletions(@NotNull CompletionResultSet resultSet) {
        // TODO annotations
        Arrays.stream(topLevelKeywords)
                .forEach(key -> resultSet.addElement(
                        PrioritizedLookupElement.withPriority(
                                GdLookupElementBuilder
                                        .create(key, " ")
                                        .withInsertHandler(GdLookupInsertHandler.INSTANCE)
                                ,
                                CompletionPriority.KEYWORDS
                        )
                ));
    }


}
