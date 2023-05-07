//package gdscript.codeInsight.renamer
//
//import com.intellij.psi.PsiElement
//import com.intellij.refactoring.rename.naming.AutomaticRenamer
//import com.intellij.refactoring.rename.naming.AutomaticRenamerFactory
//import com.intellij.usageView.UsageInfo
//import gdscript.psi.GdClassNameNmi
//
///**
// * Renames file when class_name is refactored
// */
//class GdFileRenamerFactory : AutomaticRenamerFactory {
//
//    override fun isApplicable(element: PsiElement): Boolean {
//        return element is GdClassNameNmi
//    }
//
//    override fun getOptionName(): String {
//        return "Rename a file (snake_case)"
//    }
//
//    override fun isEnabled(): Boolean {
//        return true
//    }
//
//    override fun setEnabled(enabled: Boolean) {
//    }
//
//    override fun createRenamer(
//        element: PsiElement?,
//        newName: String?,
//        usages: MutableCollection<UsageInfo>?,
//    ): AutomaticRenamer {
//        return AutomaticRenamer()
//
//        TODO("Not yet implemented")
//    }
//
//}
