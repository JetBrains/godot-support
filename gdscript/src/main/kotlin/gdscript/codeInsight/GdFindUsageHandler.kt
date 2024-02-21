package gdscript.codeInsight

import com.intellij.find.findUsages.FindUsagesHandler
import com.intellij.find.findUsages.FindUsagesOptions
import com.intellij.openapi.application.ReadAction
import com.intellij.psi.PsiElement
import com.intellij.usageView.UsageInfo
import com.intellij.util.Processor
import gdscript.psi.GdMethodIdNmi
import gdscript.psi.GdSignalIdNmi
import tscn.psi.search.TscnMethodSearcher
import tscn.psi.search.TscnSignalSearcher

class GdFindUsageHandler(element: PsiElement) : FindUsagesHandler(element) {

    override fun processElementUsages(element: PsiElement, processor: Processor<in UsageInfo>, options: FindUsagesOptions): Boolean {
        val result = super.processElementUsages(element, processor, options)

        // in case of isUsage search for method/signal also search the scenes files
        if (options.isUsages && element is GdMethodIdNmi) {
            val tscnMethodRefs = ReadAction.compute<List<UsageInfo>, Throwable> { TscnMethodSearcher(element, project).listMethodReferences() }
            tscnMethodRefs.forEach { usage -> processor.process(usage) }
            return result || tscnMethodRefs.any()
        } else if (options.isUsages && element is GdSignalIdNmi) {
            val tscnSignalRefs = ReadAction.compute<List<UsageInfo>, Throwable> { TscnSignalSearcher(element, project).listSignalReferences() }
            tscnSignalRefs.forEach { usage -> processor.process(usage) }
            return result || tscnSignalRefs.any()
        }

        return result
    }

}
