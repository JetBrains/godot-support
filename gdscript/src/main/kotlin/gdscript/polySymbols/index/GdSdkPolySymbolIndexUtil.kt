package gdscript.polySymbols.index

import com.intellij.openapi.project.Project
import com.intellij.polySymbols.query.PolySymbolQueryExecutor
import com.intellij.polySymbols.query.PolySymbolQueryExecutorFactory
import gdscript.polySymbols.scope.GdSdkOperationsPolySymbolScope
import gdscript.polySymbols.scope.gdSdkAnnotationsPolySymbolScope
import gdscript.polySymbols.scope.gdSdkClassesPolySymbolScope
import gdscript.polySymbols.scope.gdSdkGlobalPolySymbolScope

object GdSdkPolySymbolIndexUtil {

    fun getQueryExecutor(project: Project): PolySymbolQueryExecutor {
        return PolySymbolQueryExecutorFactory.createCustom {
            addRootScopes(
                listOf(
                    gdSdkClassesPolySymbolScope(project),
                    gdSdkGlobalPolySymbolScope(project),
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
                    gdSdkAnnotationsPolySymbolScope(project),
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