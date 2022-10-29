package common.index

import com.intellij.openapi.project.Project
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiNamedElement
import com.intellij.psi.search.GlobalSearchScope
import com.intellij.psi.stubs.StringStubIndexExtension

abstract class StringStubIndexExtensionExt<Psi : PsiElement?> : StringStubIndexExtension<Psi>() {

    fun getGlobally(element: PsiNamedElement): Collection<Psi> {
        return get(element.name.orEmpty(), element.project, GlobalSearchScope.allScope(element.project));
    }

    fun getGlobally(name: String, element: PsiElement): Collection<Psi> {
        return get(name, element.project, GlobalSearchScope.allScope(element.project));
    }

    fun getGlobally(name: String, project: Project): Collection<Psi> {
        return get(name, project, GlobalSearchScope.allScope(project));
    }

    fun getGloballyWithoutSelf(element: PsiNamedElement): Collection<Psi> {
        return get(element.name.orEmpty(), element.project,GlobalSearchScope.notScope(
            GlobalSearchScope.fileScope(element.containingFile)
        ));
    }

    fun getGloballyWithoutSelf(name: String, element: PsiElement): Collection<Psi> {
        return get(name, element.project, GlobalSearchScope.notScope(
            GlobalSearchScope.fileScope(element.containingFile)
        ));
    }

    fun getInFile(element: PsiNamedElement): Collection<Psi> {
        return get(element.name.orEmpty(), element.project, GlobalSearchScope.fileScope(element.containingFile));
    }

    fun getInFile(name: String, element: PsiElement): Collection<Psi> {
        return get(name, element.project, GlobalSearchScope.fileScope(element.containingFile));
    }

}
