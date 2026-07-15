package gdscript.psi

import com.intellij.psi.PsiElement
import com.intellij.psi.PsiElementVisitor
import gdscript.psi.impl.GdAnnotationStmtImpl

open class GdVisitor : PsiElementVisitor() {
    fun visitAnnotationParams(o: GdAnnotationParams) {
        visitPsiElement(o)
    }

    fun visitAnnotationType(o: GdAnnotationType) {
        visitPsiElement(o)
    }

    fun visitAnnotationTl(o: GdAnnotationTl) {
        visitTopLevelDecl(o)
    }

    fun visitArgExpr(o: GdArgExpr) {
        visitPsiElement(o)
    }

    fun visitArgList(o: GdArgList) {
        visitPsiElement(o)
    }

    fun visitArrEx(o: GdArrEx) {
        visitExpr(o)
    }

    fun visitArrayDecl(o: GdArrayDecl) {
        visitPsiElement(o)
    }

    fun visitArrayPattern(o: GdArrayPattern) {
        visitPsiElement(o)
    }

    fun visitAssignSign(o: GdAssignSign) {
        visitPsiElement(o)
    }

    fun visitAssignTyped(o: GdAssignTyped) {
        visitPsiElement(o)
    }

    fun visitAssignSt(o: GdAssignSt) {
        visitStmt(o)
    }

    fun visitAttributeEx(o: GdAttributeEx) {
        visitExpr(o)
    }

    fun visitAwaitEx(o: GdAwaitEx) {
        visitExpr(o)
    }

    fun visitBindingPattern(o: GdBindingPattern) {
        visitPsiElement(o)
    }

    fun visitBitAndSign(o: GdBitAndSign) {
        visitPsiElement(o)
    }

    fun visitBitAndEx(o: GdBitAndEx) {
        visitExpr(o)
    }

    fun visitBitNotEx(o: GdBitNotEx) {
        visitExpr(o)
    }

    open fun visitCallEx(o: GdCallEx) {
        visitExpr(o)
    }

    fun visitCastEx(o: GdCastEx) {
        visitExpr(o)
    }

    open fun visitClassDeclTl(o: GdClassDeclTl) {
        visitTopLevelDecl(o)
    }

    open fun visitClassNameNmi(o: GdClassNameNmi) {
        visitNamedIdElement(o)
    }

    open fun visitClassNaming(o: GdClassNaming) {
        visitPsiElement(o)
    }

    open fun visitClassVarDeclTl(o: GdClassVarDeclTl) {
        visitTopLevelDecl(o)
    }

    fun visitComparisonEx(o: GdComparisonEx) {
        visitExpr(o)
    }

    open fun visitConstDeclSt(o: GdConstDeclSt) {
        visitStmt(o)
    }

    open fun visitConstDeclTl(o: GdConstDeclTl) {
        visitTopLevelDecl(o)
    }

    fun visitDictDecl(o: GdDictDecl) {
        visitPsiElement(o)
    }

    fun visitDictPattern(o: GdDictPattern) {
        visitPsiElement(o)
    }

    fun visitElifSt(o: GdElifSt) {
        visitStmt(o)
    }

    fun visitElseSt(o: GdElseSt) {
        visitStmt(o)
    }

    fun visitEmptyStmt(o: GdEmptyStmt) {
        visitPsiElement(o)
    }

    fun visitEndStmt(o: GdEndStmt) {
        visitPsiElement(o)
    }

    fun visitEnumDeclNmi(o: GdEnumDeclNmi) {
        visitNamedIdElement(o)
    }

    fun visitEnumDeclTl(o: GdEnumDeclTl) {
        visitTopLevelDecl(o)
    }

    fun visitEnumValue(o: GdEnumValue) {
        visitPsiElement(o)
    }

    fun visitEnumValueNmi(o: GdEnumValueNmi) {
        visitNamedIdElement(o)
    }

    fun visitExpr(o: GdExpr) {
        visitPsiElement(o)
    }

    fun visitExprSt(o: GdExprSt) {
        visitStmt(o)
    }

    fun visitFactorSign(o: GdFactorSign) {
        visitPsiElement(o)
    }

    fun visitFactorEx(o: GdFactorEx) {
        visitExpr(o)
    }

    fun visitFlowSt(o: GdFlowSt) {
        visitStmt(o)
    }

    fun visitForSt(o: GdForSt) {
        visitStmt(o)
    }

    fun visitFuncDeclIdNmi(o: GdFuncDeclIdNmi) {
        visitNamedIdElement(o)
    }

    open fun visitFuncDeclEx(o: GdFuncDeclEx) {
        visitExpr(o)
    }

    fun visitGetDecl(o: GdGetDecl) {
        visitPsiElement(o)
    }

    fun visitGetMethodIdRef(o: GdGetMethodIdRef) {
        visitPsiElement(o)
    }

    fun visitIfSt(o: GdIfSt) {
        visitStmt(o)
    }

    fun visitInEx(o: GdInEx) {
        visitExpr(o)
    }

    open fun visitInheritance(o: GdInheritance) {
        visitPsiElement(o)
    }

    fun visitInheritanceId(o: GdInheritanceId) {
        visitPsiElement(o)
    }

    fun visitInheritanceIdRef(o: GdInheritanceIdRef) {
        visitPsiElement(o)
    }

    fun visitInheritanceSubIdRef(o: GdInheritanceSubIdRef) {
        visitPsiElement(o)
    }

    fun visitIsEx(o: GdIsEx) {
        visitExpr(o)
    }

    fun visitKeyNmi(o: GdKeyNmi) {
        visitNamedIdElement(o)
    }

    fun visitKeyValue(o: GdKeyValue) {
        visitPsiElement(o)
    }

    fun visitKeyValuePattern(o: GdKeyValuePattern) {
        visitPsiElement(o)
    }

    fun visitLiteralEx(o: GdLiteralEx) {
        visitExpr(o)
    }

    fun visitLogicEx(o: GdLogicEx) {
        visitExpr(o)
    }

    fun visitMatchBlock(o: GdMatchBlock) {
        visitPsiElement(o)
    }

    open fun visitMatchSt(o: GdMatchSt) {
        visitStmt(o)
    }

    open fun visitMethodDeclTl(o: GdMethodDeclTl) {
        visitTopLevelDecl(o)
    }

    open fun visitMethodIdNmi(o: GdMethodIdNmi) {
        visitNamedIdElement(o)
    }

    fun visitMethodSpecifier(o: GdMethodSpecifier) {
        visitPsiElement(o)
    }

    fun visitNegateEx(o: GdNegateEx) {
        visitExpr(o)
    }

    fun visitNewLineEnd(o: GdNewLineEnd) {
        visitPsiElement(o)
    }

    fun visitNodePath(o: GdNodePath) {
        visitPsiElement(o)
    }

    fun visitOperator(o: GdOperator) {
        visitPsiElement(o)
    }

    open fun visitParam(o: GdParam) {
        visitPsiElement(o)
    }

    fun visitParamList(o: GdParamList) {
        visitPsiElement(o)
    }

    fun visitParenthesizedEx(o: GdParenthesizedEx) {
        visitExpr(o)
    }

    fun visitPattern(o: GdPattern) {
        visitPsiElement(o)
    }

    fun visitPatternList(o: GdPatternList) {
        visitPsiElement(o)
    }

    fun visitPlusMinusPreEx(o: GdPlusMinusPreEx) {
        visitExpr(o)
    }

    fun visitPlusMinusEx(o: GdPlusMinusEx) {
        visitExpr(o)
    }

    fun visitPlusEx(o: GdPlusEx) {
        visitExpr(o)
    }

    fun visitPrimaryEx(o: GdPrimaryEx) {
        visitExpr(o)
    }

    fun visitRefIdNm(o: GdRefIdRef) {
        visitPsiElement(o)
    }

    fun visitReturnHint(o: GdReturnHint) {
        visitPsiElement(o)
    }

    fun visitReturnHintVal(o: GdReturnHintVal) {
        visitPsiElement(o)
    }

    fun visitSetDecl(o: GdSetDecl) {
        visitPsiElement(o)
    }

    fun visitSetMethodIdRef(o: GdSetMethodIdRef) {
        visitPsiElement(o)
    }

    fun visitSetgetDecl(o: GdSetgetDecl) {
        visitPsiElement(o)
    }

    fun visitShiftEx(o: GdShiftEx) {
        visitExpr(o)
    }

    fun visitSign(o: GdSign) {
        visitPsiElement(o)
    }

    fun visitSignEx(o: GdSignEx) {
        visitExpr(o)
    }

    open fun visitSignalDeclTl(o: GdSignalDeclTl) {
        visitTopLevelDecl(o)
    }

    fun visitSignalIdNmi(o: GdSignalIdNmi) {
        visitNamedIdElement(o)
    }

    fun visitStmt(o: GdStmt) {
        visitPsiElement(o)
    }

    fun visitStmtOrSuite(o: GdStmtOrSuite) {
        visitPsiElement(o)
    }

    fun visitStringVal(o: GdStringValRef) {
        visitPsiElement(o)
    }

    fun visitSuite(o: GdSuite) {
        visitPsiElement(o)
    }

    fun visitTernaryEx(o: GdTernaryEx) {
        visitExpr(o)
    }

    fun visitTopLevelDecl(o: GdTopLevelDecl) {
        visitPsiElement(o)
    }

    fun visitTypeHint(o: GdTypeHint) {
        visitPsiElement(o)
    }

    fun visitTypeHintRef(o: GdTypeHintRef) {
        visitPsiElement(o)
    }

    fun visitTyped(o: GdTyped) {
        visitPsiElement(o)
    }

    fun visitTypedVal(o: GdTypedVal) {
        visitPsiElement(o)
    }

    open fun visitVarDeclSt(o: GdVarDeclSt) {
        visitStmt(o)
    }

    fun visitAnnotationSt(o: GdAnnotationStmtImpl) {
        visitStmt(o)
    }

    fun visitVarNmi(o: GdVarNmi) {
        visitNamedIdElement(o)
    }

    fun visitWhileSt(o: GdWhileSt) {
        visitStmt(o)
    }

    fun visitNamedElement(o: GdNamedElement) {
        visitPsiElement(o)
    }

    fun visitNamedIdElement(o: GdNamedIdElement) {
        visitPsiElement(o)
    }

    fun visitPsiElement(o: PsiElement) {
        visitElement(o)
    }
}
