package gdscript.psi

interface GdAnnotationTl : GdTopLevelDecl {
    val annotationParams: GdAnnotationParams?

    val annotationType: GdAnnotationType

    val endStmt: GdEndStmt?
}
