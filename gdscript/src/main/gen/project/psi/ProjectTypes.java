// This is a generated file. Not intended for manual editing.
package project.psi;

import com.intellij.psi.tree.IElementType;
import com.intellij.psi.PsiElement;
import com.intellij.lang.ASTNode;
import project.psi.impl.*;

public interface ProjectTypes {

  IElementType DATA = new ProjectElementType("DATA");
  IElementType DATA_KEY = new ProjectElementType("DATA_KEY");
  IElementType DATA_VALUE = new ProjectElementType("DATA_VALUE");
  IElementType SECTION = new ProjectElementType("SECTION");
  IElementType SECTION_NM = new ProjectElementType("SECTION_NM");

  IElementType BAD_CHARACTER = new ProjectTokenType("bad_character");
  IElementType COMMENT = new ProjectTokenType("comment");
  IElementType EQ = new ProjectTokenType("EQ");
  IElementType KEY = new ProjectTokenType("KEY");
  IElementType SECTION_KEY = new ProjectTokenType("SECTION_KEY");
  IElementType VALUE = new ProjectTokenType("VALUE");

  class Factory {
    public static PsiElement createElement(ASTNode node) {
      IElementType type = node.getElementType();
      if (type == DATA) {
        return new ProjectDataImpl(node);
      }
      else if (type == DATA_KEY) {
        return new ProjectDataKeyImpl(node);
      }
      else if (type == DATA_VALUE) {
        return new ProjectDataValueImpl(node);
      }
      else if (type == SECTION) {
        return new ProjectSectionImpl(node);
      }
      else if (type == SECTION_NM) {
        return new ProjectSectionNmImpl(node);
      }
      throw new AssertionError("Unknown element type: " + type);
    }
  }
}
