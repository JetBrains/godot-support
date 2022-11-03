package common.index

import com.intellij.openapi.project.DumbService
import com.intellij.openapi.project.Project
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiNamedElement
import com.intellij.psi.search.GlobalSearchScope
import com.intellij.psi.stubs.StringStubIndexExtension
import tscn.index.impl.TscnResourceIndex
import tscn.psi.TscnResourceHeader

abstract class StringStubIndexExtensionExt<Psi : PsiElement?> : StringStubIndexExtension<Psi>() {

    fun getGlobally(element: PsiNamedElement): Collection<Psi> {
        if (DumbService.isDumb(element.project)) return emptyList();
        return get(element.name.orEmpty(), element.project, GlobalSearchScope.allScope(element.project));
    }

    fun getGlobally(name: String, element: PsiElement): Collection<Psi> {
        if (DumbService.isDumb(element.project)) return emptyList();

        return DumbService.getInstance(element.project).runReadActionInSmartMode<Collection<Psi>> {
            get(name, element.project, GlobalSearchScope.allScope(element.project))
        } ?: return emptyList();

        //return get(name, element.project, GlobalSearchScope.allScope(element.project));
    }

    fun getGlobally(name: String, project: Project): Collection<Psi> {
        if (DumbService.isDumb(project)) return emptyList();
        return get(name, project, GlobalSearchScope.allScope(project));
    }

    fun getGloballyWithoutSelf(element: PsiNamedElement): Collection<Psi> {
        if (DumbService.isDumb(element.project)) return emptyList();
        return get(element.name.orEmpty(), element.project,GlobalSearchScope.notScope(
            GlobalSearchScope.fileScope(element.containingFile)
        ));
    }

    fun getGloballyWithoutSelf(name: String, element: PsiElement): Collection<Psi> {
        if (DumbService.isDumb(element.project)) return emptyList();
        return get(name, element.project, GlobalSearchScope.notScope(
            GlobalSearchScope.fileScope(element.containingFile)
        ));
    }

    fun getInFile(element: PsiNamedElement): Collection<Psi> {
        if (DumbService.isDumb(element.project)) return emptyList();
        return get(element.name.orEmpty(), element.project, GlobalSearchScope.fileScope(element.containingFile));
    }

    fun getInFile(name: String, element: PsiElement): Collection<Psi> {
        if (DumbService.isDumb(element.project)) return emptyList();
        return get(name, element.project, GlobalSearchScope.fileScope(element.containingFile));
    }

    fun getNonEmptyKeys(project: Project): Collection<String> {
        if (DumbService.isDumb(project)) return emptyList();
        return getAllKeys(project).mapNotNull {
            if (it.isNotBlank() && getGlobally(it, project).isNotEmpty()) it else null
        }
    }

    fun getAllValues(project: Project): Collection<Psi> {
        if (DumbService.isDumb(project)) return emptyList();
        return getAllKeys(project).flatMap { getGlobally(it, project) };
    }

}
