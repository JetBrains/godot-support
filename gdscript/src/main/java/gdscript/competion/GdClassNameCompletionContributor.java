package gdscript.competion;

import com.intellij.codeInsight.completion.*;
import com.intellij.codeInsight.lookup.LookupElementBuilder;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.PsiManager;
import com.intellij.psi.search.FileTypeIndex;
import com.intellij.psi.search.GlobalSearchScope;
import com.intellij.psi.util.PsiTreeUtil;
import gdscript.GdFileType;
import gdscript.GdIcon;
import gdscript.competion.staticLoader.StaticClassLoader;
import gdscript.competion.utils.CompletionPriority;
import gdscript.competion.utils.FileCompletionUtil;
import gdscript.competion.utils.PositionUtil;
import gdscript.psi.GdClassNaming;
import gdscript.psi.GdFile;
import gdscript.psi.GdTypes;
import gdscript.psi.impl.GdClassNamingImpl;
import gdscript.psi.utils.PsiGdFileUtil;
import org.jetbrains.annotations.NotNull;

public class GdClassNameCompletionContributor extends CompletionContributor {

    @Override
    public void fillCompletionVariants(@NotNull CompletionParameters parameters, @NotNull CompletionResultSet resultSet) {

        var project = parameters.getPosition().getProject();
        var  virtualFiles =
                FileTypeIndex.getFiles(GdFileType.INSTANCE, GlobalSearchScope.allScope(project));
        for (VirtualFile it : virtualFiles) {
                var gdf = (GdFile) PsiManager.getInstance(project).findFile(it);
                var properties = (GdClassNamingImpl) PsiTreeUtil.findChildOfType(gdf, GdClassNamingImpl.class);
                var ss = properties.getGreenStub();
                var asd=  546;
            }

        GdClassNaming ar = (GdClassNaming) parameters.getPosition().getParent().getParent();

        var asd = ar.getClassname();

        // After EXTENDS keyword
        if (PositionUtil.isLeafPreceded(parameters, GdTypes.EXTENDS)) {
            String myName = PsiGdFileUtil.INSTANCE.myFilename(parameters);
            PsiGdFileUtil.INSTANCE.customs(parameters)
                    .forEach(name -> {
                                if (!name.equals(myName)) {
                                    resultSet.addElement(
                                            PrioritizedLookupElement.withPriority(
                                                    LookupElementBuilder
                                                            .create(name)
                                                            .withIcon(GdIcon.OBJECT),
                                                    CompletionPriority.USER_DEFINED
                                            ));
                                }
                            }
                    );
            StaticClassLoader
                    .getClasses()
                    .forEach((_key, gd) -> resultSet.addElement(
                            PrioritizedLookupElement.withPriority(
                                    LookupElementBuilder
                                            .create(gd.name)
                                            .withIcon(GdIcon.getEditorIcon(gd.name)),
                                    CompletionPriority.STATIC_TOP
                            )));

        // After CLASS_NAME keyword
        } else if (PositionUtil.isLeafPreceded(parameters, GdTypes.CLASS_NAME)) {
            GdFile file = FileCompletionUtil.currentFile(parameters);
            String name = file.getName(); // remove .gd suffix
            resultSet.addElement(
                    LookupElementBuilder.create(name.substring(0, name.length() - 3))
            );
        }

        super.fillCompletionVariants(parameters, resultSet);
    }

}