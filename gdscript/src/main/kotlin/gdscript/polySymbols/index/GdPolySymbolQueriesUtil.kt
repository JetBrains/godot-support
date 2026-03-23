package gdscript.polySymbols.index

import com.intellij.openapi.project.Project
import com.intellij.polySymbols.PolySymbol
import com.intellij.polySymbols.PolySymbolKind
import com.intellij.polySymbols.query.PolySymbolQueryExecutor
import com.intellij.polySymbols.utils.unwrapMatchedSymbols
import gdscript.polySymbols.GdPolySymbol
import gdscript.polySymbols.GdPolySymbolKind
import gdscript.polySymbols.config.GdAnnotationSymbol
import gdscript.polySymbols.config.GdOperatorSymbol
import gdscript.polySymbols.sdk.GdSdkClassSymbol
import gdscript.polySymbols.sdk.GdSdkConstantSymbol
import gdscript.polySymbols.sdk.GdSdkConstructorSymbol
import gdscript.polySymbols.sdk.GdSdkEnumSymbol
import gdscript.polySymbols.sdk.GdSdkMethodSymbol
import gdscript.polySymbols.sdk.GdSdkPolySymbol
import gdscript.polySymbols.sdk.GdSdkPolySymbolsUtil.hasSameSignature
import gdscript.polySymbols.sdk.GdSdkPropertySymbol
import gdscript.polySymbols.sdk.GdSdkSignalSymbol
import gdscript.polySymbols.sdk.xml.GdSdkData

/**
 * Utilities for querying the Poly Symbols Query Executor.
 *
 * There are special methods for SDK symbols; you can either pass an executor or a project.
 * If you pass the project, it will create an executor that only queries the SDK for you.
 */
object GdPolySymbolQueriesUtil {

    /**
     * Get a symbol from the executor by name and kind, optionally filtering with a predicate.
     * It filters the results by the given class.
     *
     * If you're looking specifically for a [gdscript.polySymbols.sdk.GdSdkPolySymbol], use one of the SDK-specific methods below (with the form get*Symbol) instead.
     */
    fun <T : PolySymbol> getSymbol(
        executor: PolySymbolQueryExecutor,
        kind: PolySymbolKind,
        name: String,
        symbolClass: Class<T>,
        predicate: ((T) -> Boolean)? = null
    ): T? {
        val filtered = executor.nameMatchQuery(kind, name).run()
            .flatMap { it.unwrapMatchedSymbols() }
            .filterIsInstance(symbolClass)
        return if (predicate != null) filtered.firstOrNull(predicate) else filtered.firstOrNull()
    }

    /**
     * Get a symbol from the executor by name and kind, optionally filtering with a predicate.
     *
     * If you're looking specifically for a [gdscript.polySymbols.sdk.GdSdkPolySymbol], use one of the SDK-specific methods below (with the form get*Symbol) instead.
     */
    fun getSymbol(
        executor: PolySymbolQueryExecutor,
        kind: PolySymbolKind,
        name: String,
        predicate: ((PolySymbol) -> Boolean)? = null
    ): PolySymbol? {
        return getSymbol(executor, kind, name, PolySymbol::class.java, predicate)
    }

    /**
     * List all symbols from the executor by kind, optionally filtering with a predicate.
     * It filters the results by the given class.
     *
     * If you're only looking for [gdscript.polySymbols.sdk.GdSdkPolySymbol], use one of the SDK-specific methods below (with the form listSdk*Symbols) instead.
     */
    fun <T : PolySymbol> listSymbols(
        executor: PolySymbolQueryExecutor,
        kind: PolySymbolKind,
        symbolClass: Class<T>,
        predicate: ((T) -> Boolean)? = null
    ): List<T> {
        val filtered = executor.listSymbolsQuery(kind, false).run()
            .flatMap { it.unwrapMatchedSymbols() }
            .filterIsInstance(symbolClass)
        return if (predicate != null) filtered.filter(predicate) else filtered
    }

    /**
     * List all symbols from the executor by kind, optionally filtering with a predicate.
     *
     * If you're only looking for [gdscript.polySymbols.sdk.GdSdkPolySymbol], use one of the SDK-specific methods below (with the form listSdk*Symbols) instead.
     */
    fun listSymbols(
        executor: PolySymbolQueryExecutor,
        kind: PolySymbolKind,
        predicate: ((GdPolySymbol) -> Boolean)? = null
    ): List<GdPolySymbol> {
        return listSymbols(executor, kind, GdPolySymbol::class.java, predicate)
    }



    // ***** Get SDK Symbols by Name (nameMatchQuery) *****

    fun getSdkClassSymbol(executor: PolySymbolQueryExecutor, classId: String): GdSdkClassSymbol? =
        getSymbol(executor, GdPolySymbolKind.CLASS, classId, GdSdkClassSymbol::class.java) {
            it.classId == classId
        }

    fun getSdkClassSymbol(project: Project, classId: String): GdSdkClassSymbol? {
        val executor = GdSdkPolySymbolIndexUtil.getQueryExecutor(project)
        return getSdkClassSymbol(executor, classId)
    }

    fun getSdkMethodSymbol(executor: PolySymbolQueryExecutor, ownerClass: String, name: String, signature: List<GdSdkData.ParameterData>? = null): GdSdkMethodSymbol? =
        getSymbol(executor, GdPolySymbolKind.METHOD, name, GdSdkMethodSymbol::class.java) { symbol ->
            symbol.declaringClassName == ownerClass && (signature == null || symbol.data.parameters.hasSameSignature(signature))
        }

    fun getSdkMethodSymbol(project: Project, ownerClass: String, name: String, signature: List<GdSdkData.ParameterData>? = null): GdSdkMethodSymbol? {
        val executor = GdSdkPolySymbolIndexUtil.getQueryExecutor(project, ownerClass)
        return getSdkMethodSymbol(executor, ownerClass, name, signature)
    }

    fun getSdkConstructorSymbol(executor: PolySymbolQueryExecutor, ownerClass: String, signature: List<GdSdkData.ParameterData>? = null): GdSdkConstructorSymbol? =
        getSymbol(executor, GdPolySymbolKind.CONSTRUCTOR, ownerClass, GdSdkConstructorSymbol::class.java) { symbol ->
            symbol.declaringClassName == ownerClass && (signature == null || symbol.data.parameters.hasSameSignature(signature))
        }

    fun getSdkConstructorSymbol(project: Project, ownerClass: String, signature: List<GdSdkData.ParameterData>? = null): GdSdkConstructorSymbol? {
        val executor = GdSdkPolySymbolIndexUtil.getQueryExecutor(project, ownerClass)
        return getSdkConstructorSymbol(executor, ownerClass, signature)
    }

    private fun <T : GdSdkPolySymbol> getSdkMemberSymbol(
        executor: PolySymbolQueryExecutor,
        kind: PolySymbolKind,
        ownerClass: String,
        name: String,
        symbolClass: Class<T>,
    ): T? {
        return getSymbol(executor, kind, name, symbolClass) { symbol ->
            symbol.declaringClassName == ownerClass
        }
    }

    fun getSdkPropertySymbol(executor: PolySymbolQueryExecutor, ownerClass: String, name: String, ): GdSdkPropertySymbol? =
        getSdkMemberSymbol(executor, GdPolySymbolKind.PROPERTY, ownerClass, name, GdSdkPropertySymbol::class.java)

    fun getSdkPropertySymbol(project: Project, ownerClass: String, name: String, ): GdSdkPropertySymbol? {
        val executor = GdSdkPolySymbolIndexUtil.getQueryExecutor(project, ownerClass)
        return getSdkPropertySymbol(executor, ownerClass, name)
    }

    fun getSdkConstantSymbol(executor: PolySymbolQueryExecutor, ownerClass: String, name: String, ): GdSdkConstantSymbol? =
        getSdkMemberSymbol(executor, GdPolySymbolKind.CONSTANT, ownerClass, name, GdSdkConstantSymbol::class.java)

    fun getSdkConstantSymbol(project: Project, ownerClass: String, name: String, ): GdSdkConstantSymbol? {
        val executor = GdSdkPolySymbolIndexUtil.getQueryExecutor(project, ownerClass)
        return getSdkConstantSymbol(executor, ownerClass, name)
    }

    fun getSdkEnumSymbol(executor: PolySymbolQueryExecutor, ownerClass: String, name: String, ): GdSdkEnumSymbol? =
        getSdkMemberSymbol(executor, GdPolySymbolKind.ENUM, ownerClass, name, GdSdkEnumSymbol::class.java)

    fun getSdkEnumSymbol(project: Project, ownerClass: String, name: String, ): GdSdkEnumSymbol? {
        val executor = GdSdkPolySymbolIndexUtil.getQueryExecutor(project, ownerClass)
        return getSdkEnumSymbol(executor, ownerClass, name)
    }

    fun getSdkSignalSymbol(executor: PolySymbolQueryExecutor, ownerClass: String, name: String, ): GdSdkSignalSymbol? =
        getSdkMemberSymbol(executor, GdPolySymbolKind.SIGNAL, ownerClass, name, GdSdkSignalSymbol::class.java)

    fun getSdkSignalSymbol(project: Project, ownerClass: String, name: String, ): GdSdkSignalSymbol? {
        val executor = GdSdkPolySymbolIndexUtil.getQueryExecutor(project, ownerClass)
        return getSdkSignalSymbol(executor, ownerClass, name)
    }

    fun getAnnotationSymbol(executor: PolySymbolQueryExecutor, annotationName: String): GdAnnotationSymbol? =
        getSymbol(executor, GdPolySymbolKind.ANNOTATION, annotationName, GdAnnotationSymbol::class.java)

    fun getAnnotationSymbol(project: Project, annotationName: String): GdAnnotationSymbol? {
        val executor = GdSdkPolySymbolIndexUtil.getAnnotationsQueryExecutor(project)
        return getAnnotationSymbol(executor, annotationName)
    }


    /**
     * Trim compound assignment operators e.g. += to +
     */
    private fun parseOperator(operator: String): String {
        for (prefix in GdOperatorSymbol.TO_TRIM) {
            if (operator == "$prefix=") {
                return prefix
            }
        }
        return operator
    }
    fun getOperatorSymbol(executor: PolySymbolQueryExecutor, left: String, operator: String, right: String): GdOperatorSymbol? {
        val op = parseOperator(operator)
        val operatorName = left + op + right
        return getSymbol(executor, GdPolySymbolKind.OPERATOR, operatorName, GdOperatorSymbol::class.java)
    }

    fun getOperatorSymbol(project: Project, left: String, operator: String, right: String): GdOperatorSymbol? {
        val executor = GdSdkPolySymbolIndexUtil.getOperationsQueryExecutor(project)
        return getOperatorSymbol(executor, left, operator, right)
    }



    // ***** List SDK Symbols by Kind (listSymbolsQuery) *****

    fun listSdkClassSymbols(executor: PolySymbolQueryExecutor): List<GdSdkClassSymbol> {
        return listSymbols(executor, GdPolySymbolKind.CLASS, GdSdkClassSymbol::class.java)
    }

    fun listSdkClassSymbols(project: Project): List<GdSdkClassSymbol> {
        val executor = GdSdkPolySymbolIndexUtil.getQueryExecutor(project)
        return listSdkClassSymbols(executor)
    }

    // List SDK symbols without owner class check

    fun <T : GdSdkPolySymbol> listSdkMemberSymbols(executor: PolySymbolQueryExecutor, kind: PolySymbolKind, symbolClass: Class<T>): List<T> {
        return listSymbols(executor, kind, symbolClass)
    }

    fun listSdkMemberSymbols(executor: PolySymbolQueryExecutor, kind: PolySymbolKind): List<GdSdkPolySymbol> {
        return listSdkMemberSymbols(executor, kind, GdSdkPolySymbol::class.java)
    }
}