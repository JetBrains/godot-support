package common.index

import com.intellij.openapi.project.DumbService
import com.intellij.openapi.project.Project
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiNamedElement
import com.intellij.psi.search.GlobalSearchScope
import com.intellij.psi.stubs.StringStubIndexExtension
import com.intellij.psi.stubs.StubIndex

@Suppress("UNCHECKED_CAST")
abstract class StringStubIndexExtensionExt<Psi : PsiElement?> : StringStubIndexExtension<Psi>() {

    fun getGlobally(element: PsiNamedElement): Collection<Psi> {
        if (DumbService.isDumb(element.project)) return emptyList()
        return StubIndex.getElements(key, element.name.orEmpty(), element.project, GlobalSearchScope.allScope(element.project), PsiElement::class.java as Class<Psi>)
    }

    fun getGlobally(name: String, element: PsiElement): Collection<Psi> {
        if (DumbService.isDumb(element.project)) return emptyList()
        return DumbService.getInstance(element.project).runReadActionInSmartMode<Collection<Psi>> {
            get(name, element.project, GlobalSearchScope.allScope(element.project))
        } ?: return emptyList()
    }

    fun getGlobally(name: String, project: Project): Collection<Psi> {
        if (DumbService.isDumb(project)) return emptyList()
        return StubIndex.getElements(key, name, project, GlobalSearchScope.allScope(project), PsiElement::class.java as Class<Psi>)
    }

    fun getGloballyWithoutSelf(element: PsiNamedElement): Collection<Psi> {
        if (DumbService.isDumb(element.project)) return emptyList()
        return StubIndex.getElements(key, element.name.orEmpty(), element.project, GlobalSearchScope.notScope(
            GlobalSearchScope.fileScope(element.containingFile)
        ), PsiElement::class.java as Class<Psi>)
    }

    fun getGloballyWithoutSelf(name: String, element: PsiElement): Collection<Psi> {
        if (DumbService.isDumb(element.project)) return emptyList()
        return StubIndex.getElements(key, name, element.project, GlobalSearchScope.notScope(
            GlobalSearchScope.fileScope(element.containingFile)
        ), PsiElement::class.java as Class<Psi>)
    }

    fun getInFile(element: PsiNamedElement): Collection<Psi> {
        if (DumbService.isDumb(element.project)) return emptyList()
        return StubIndex.getElements(key, element.name.orEmpty(), element.project, GlobalSearchScope.fileScope(element.containingFile), PsiElement::class.java as Class<Psi>)
    }

    fun getInFile(name: String, element: PsiElement): Collection<Psi> {
        if (DumbService.isDumb(element.project)) return emptyList()
        return StubIndex.getElements(key, name, element.project, GlobalSearchScope.fileScope(element.containingFile), PsiElement::class.java as Class<Psi>)
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
