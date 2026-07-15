package gdscript.psi;

import com.intellij.psi.PsiElement;
import org.jetbrains.annotations.Nullable;

public interface GdKeyValuePattern extends PsiElement {

  @Nullable
  GdPattern getPattern();

}
