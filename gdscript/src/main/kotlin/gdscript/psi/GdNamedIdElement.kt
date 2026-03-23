package gdscript.psi

import com.intellij.model.psi.PsiExternalReferenceHost
import com.intellij.psi.PsiNameIdentifierOwner

interface GdNamedIdElement : GdNamedElement, PsiNameIdentifierOwner, PsiExternalReferenceHost
