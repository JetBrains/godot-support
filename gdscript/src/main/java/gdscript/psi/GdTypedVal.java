package gdscript.psi;

import java.util.List;
import org.jetbrains.annotations.*;
import com.intellij.psi.PsiElement;

public interface GdTypedVal extends PsiElement {

  @NotNull
  List<GdTypeHint> getTypeHintList();

  @NotNull
  String getReturnType();

}
