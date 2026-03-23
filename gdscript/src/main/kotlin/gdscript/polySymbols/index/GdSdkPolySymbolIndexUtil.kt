package gdscript.polySymbols.index

import com.intellij.openapi.project.Project
import com.intellij.polySymbols.query.PolySymbolQueryExecutor
import com.intellij.polySymbols.query.PolySymbolQueryExecutorFactory
import gdscript.polySymbols.scope.GdSdkAnnotationsPolySymbolScope
import gdscript.polySymbols.scope.GdSdkClassesPolySymbolScope
import gdscript.polySymbols.scope.GdSdkGlobalPolySymbolScope
import gdscript.polySymbols.scope.GdSdkOperationsPolySymbolScope

object GdSdkPolySymbolIndexUtil {

    fun getQueryExecutor(project: Project): PolySymbolQueryExecutor {
        return PolySymbolQueryExecutorFactory.createCustom {
            addRootScopes(
                listOf(
                    GdSdkClassesPolySymbolScope(project),
                    GdSdkGlobalPolySymbolScope(project),
                )
            )
        }
    }

    /** Returns the query executor for the given SDK class scope */
    fun getQueryExecutor(project: Project, classId: String): PolySymbolQueryExecutor {
        val classSymbol = GdPolySymbolQueriesUtil.getSdkClassSymbol(project, classId)
        return PolySymbolQueryExecutorFactory.createCustom {
            addRootScopes(
                buildList {
                    classSymbol?.let { addAll(it.queryScope) }
                }
            )
        }
    }

    fun getAnnotationsQueryExecutor(project: Project): PolySymbolQueryExecutor {
        return PolySymbolQueryExecutorFactory.createCustom {
            addRootScopes(
                listOf(
                    GdSdkAnnotationsPolySymbolScope(project),
                )
            )
        }
    }

    fun getOperationsQueryExecutor(project: Project): PolySymbolQueryExecutor {
        return PolySymbolQueryExecutorFactory.createCustom {
            addRootScopes(
                listOf(
                    GdSdkOperationsPolySymbolScope(project),
                )
            )
        }
    }
}