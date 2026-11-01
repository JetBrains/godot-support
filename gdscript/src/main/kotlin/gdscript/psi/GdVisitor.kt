package gdscript.psi

import com.intellij.psi.PsiElement
import com.intellij.psi.PsiElementVisitor
import gdscript.psi.impl.GdAnnotationStmtImpl

open class GdVisitor : PsiElementVisitor() {
    open fun visitAnnotationParams(o: GdAnnotationParams) {
        visitPsiElement(o)
    }

    open fun visitAnnotationType(o: GdAnnotationType) {
        visitPsiElement(o)
    }

    open fun visitAnnotationTl(o: GdAnnotationTl) {
        visitTopLevelDecl(o)
    }

    open fun visitArgExpr(o: GdArgExpr) {
        visitPsiElement(o)
    }

    open fun visitArgList(o: GdArgList) {
        visitPsiElement(o)
    }

    open fun visitArrEx(o: GdArrEx) {
        visitExpr(o)
    }

    open fun visitArrayDecl(o: GdArrayDecl) {
        visitPsiElement(o)
    }

    open fun visitArrayPattern(o: GdArrayPattern) {
        visitPsiElement(o)
    }

    open fun visitAssignSign(o: GdAssignSign) {
        visitPsiElement(o)
    }

    open fun visitAssignTyped(o: GdAssignTyped) {
        visitPsiElement(o)
    }

    open fun visitAssignSt(o: GdAssignSt) {
        visitStmt(o)
    }

    open fun visitAttributeEx(o: GdAttributeEx) {
        visitExpr(o)
    }

    open fun visitAwaitEx(o: GdAwaitEx) {
        visitExpr(o)
    }

    open fun visitBindingPattern(o: GdBindingPattern) {
        visitPsiElement(o)
    }

    open fun visitBitAndSign(o: GdBitAndSign) {
        visitPsiElement(o)
    }

    open fun visitBitAndEx(o: GdBitAndEx) {
        visitExpr(o)
    }

    open fun visitBitNotEx(o: GdBitNotEx) {
        visitExpr(o)
    }

    open fun visitCallEx(o: GdCallEx) {
        visitExpr(o)
    }

    open fun visitCastEx(o: GdCastEx) {
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

    open fun visitComparisonEx(o: GdComparisonEx) {
        visitExpr(o)
    }

    open fun visitConstDeclSt(o: GdConstDeclSt) {
        visitStmt(o)
    }

    open fun visitConstDeclTl(o: GdConstDeclTl) {
        visitTopLevelDecl(o)
    }

    open fun visitDictDecl(o: GdDictDecl) {
        visitPsiElement(o)
    }

    open fun visitDictPattern(o: GdDictPattern) {
        visitPsiElement(o)
    }

    open fun visitElifSt(o: GdElifSt) {
        visitStmt(o)
    }

    open fun visitElseSt(o: GdElseSt) {
        visitStmt(o)
    }

    open fun visitEmptyStmt(o: GdEmptyStmt) {
        visitPsiElement(o)
    }

    open fun visitEndStmt(o: GdEndStmt) {
        visitPsiElement(o)
    }

    open fun visitEnumDeclNmi(o: GdEnumDeclNmi) {
        visitNamedIdElement(o)
    }

    open fun visitEnumDeclTl(o: GdEnumDeclTl) {
        visitTopLevelDecl(o)
    }

    open fun visitEnumValue(o: GdEnumValue) {
        visitPsiElement(o)
    }

    open fun visitEnumValueNmi(o: GdEnumValueNmi) {
        visitNamedIdElement(o)
    }

    open fun visitExpr(o: GdExpr) {
        visitPsiElement(o)
    }

    open fun visitExprSt(o: GdExprSt) {
        visitStmt(o)
    }

    open fun visitFactorSign(o: GdFactorSign) {
        visitPsiElement(o)
    }

    open fun visitFactorEx(o: GdFactorEx) {
        visitExpr(o)
    }

    open fun visitFlowSt(o: GdFlowSt) {
        visitStmt(o)
    }

    open fun visitForSt(o: GdForSt) {
        visitStmt(o)
    }

    open fun visitFuncDeclIdNmi(o: GdFuncDeclIdNmi) {
        visitNamedIdElement(o)
    }

    open fun visitFuncDeclEx(o: GdFuncDeclEx) {
        visitExpr(o)
    }

    open fun visitGetDecl(o: GdGetDecl) {
        visitPsiElement(o)
    }

    open fun visitGetMethodIdRef(o: GdGetMethodIdRef) {
        visitPsiElement(o)
    }

    open fun visitIfSt(o: GdIfSt) {
        visitStmt(o)
    }

    open fun visitInEx(o: GdInEx) {
        visitExpr(o)
    }

    open fun visitInheritance(o: GdInheritance) {
        visitPsiElement(o)
    }

    open fun visitInheritanceId(o: GdInheritanceId) {
        visitPsiElement(o)
    }

    open fun visitInheritanceIdRef(o: GdInheritanceIdRef) {
        visitPsiElement(o)
    }

    open fun visitInheritanceSubIdRef(o: GdInheritanceSubIdRef) {
        visitPsiElement(o)
    }

    open fun visitIsEx(o: GdIsEx) {
        visitExpr(o)
    }

    open fun visitKeyNmi(o: GdKeyNmi) {
        visitNamedIdElement(o)
    }

    open fun visitKeyValue(o: GdKeyValue) {
        visitPsiElement(o)
    }

    open fun visitKeyValuePattern(o: GdKeyValuePattern) {
        visitPsiElement(o)
    }

    open fun visitLiteralEx(o: GdLiteralEx) {
        visitExpr(o)
    }

    open fun visitLogicEx(o: GdLogicEx) {
        visitExpr(o)
    }

    open fun visitMatchBlock(o: GdMatchBlock) {
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

    open fun visitMethodSpecifier(o: GdMethodSpecifier) {
        visitPsiElement(o)
    }

    open fun visitNegateEx(o: GdNegateEx) {
        visitExpr(o)
    }

    open fun visitNewLineEnd(o: GdNewLineEnd) {
        visitPsiElement(o)
    }

    open fun visitNodePath(o: GdNodePath) {
        visitPsiElement(o)
    }

    open fun visitOperator(o: GdOperator) {
        visitPsiElement(o)
    }

    open fun visitParam(o: GdParam) {
        visitPsiElement(o)
    }

    open fun visitParamList(o: GdParamList) {
        visitPsiElement(o)
    }

    open fun visitParenthesizedEx(o: GdParenthesizedEx) {
        visitExpr(o)
    }

    open fun visitPattern(o: GdPattern) {
        visitPsiElement(o)
    }

    open fun visitPatternList(o: GdPatternList) {
        visitPsiElement(o)
    }

    open fun visitPlusMinusPreEx(o: GdPlusMinusPreEx) {
        visitExpr(o)
    }

    open fun visitPlusMinusEx(o: GdPlusMinusEx) {
        visitExpr(o)
    }

    open fun visitPlusEx(o: GdPlusEx) {
        visitExpr(o)
    }

    open fun visitPrimaryEx(o: GdPrimaryEx) {
        visitExpr(o)
    }

    open fun visitRefIdNm(o: GdRefIdRef) {
        visitPsiElement(o)
    }

    open fun visitReturnHint(o: GdReturnHint) {
        visitPsiElement(o)
    }

    open fun visitReturnHintVal(o: GdReturnHintVal) {
        visitPsiElement(o)
    }

    open fun visitSetDecl(o: GdSetDecl) {
        visitPsiElement(o)
    }

    open fun visitSetMethodIdRef(o: GdSetMethodIdRef) {
        visitPsiElement(o)
    }

    open fun visitSetgetDecl(o: GdSetgetDecl) {
        visitPsiElement(o)
    }

    open fun visitShiftEx(o: GdShiftEx) {
        visitExpr(o)
    }

    open fun visitSign(o: GdSign) {
        visitPsiElement(o)
    }

    open fun visitSignEx(o: GdSignEx) {
        visitExpr(o)
    }

    open fun visitSignalDeclTl(o: GdSignalDeclTl) {
        visitTopLevelDecl(o)
    }

    open fun visitSignalIdNmi(o: GdSignalIdNmi) {
        visitNamedIdElement(o)
    }

    open fun visitStmt(o: GdStmt) {
        visitPsiElement(o)
    }

    open fun visitStmtOrSuite(o: GdStmtOrSuite) {
        visitPsiElement(o)
    }

    open fun visitStringVal(o: GdStringValRef) {
        visitPsiElement(o)
    }

    open fun visitSuite(o: GdSuite) {
        visitPsiElement(o)
    }

    open fun visitTernaryEx(o: GdTernaryEx) {
        visitExpr(o)
    }

    open fun visitTopLevelDecl(o: GdTopLevelDecl) {
        visitPsiElement(o)
    }

    open fun visitTypeHint(o: GdTypeHint) {
        visitPsiElement(o)
    }

    open fun visitTypeHintRef(o: GdTypeHintRef) {
        visitPsiElement(o)
    }

    open fun visitTyped(o: GdTyped) {
        visitPsiElement(o)
    }

    open fun visitTypedVal(o: GdTypedVal) {
        visitPsiElement(o)
    }

    open fun visitVarDeclSt(o: GdVarDeclSt) {
        visitStmt(o)
    }

    open fun visitAnnotationSt(o: GdAnnotationStmtImpl) {
        visitStmt(o)
    }

    open fun visitVarNmi(o: GdVarNmi) {
        visitNamedIdElement(o)
    }

    open fun visitWhileSt(o: GdWhileSt) {
        visitStmt(o)
    }

    open fun visitNamedIdElement(o: GdNamedIdElement) {
        visitPsiElement(o)
    }

    open fun visitPsiElement(o: PsiElement) {
        visitElement(o)
    }
}
