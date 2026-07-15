package gdscript.psi;

import gdscript.psi.types.GdDocumented;
import org.jetbrains.annotations.NotNull;
import com.intellij.psi.PsiElement;

public interface GdEnumValue extends PsiElement, GdDocumented {

  @NotNull
  GdEnumValueNmi getEnumValueNmi();

}
