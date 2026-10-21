package com.jetbrains.godot.gdscript.model

import com.intellij.polySymbols.query.PolySymbolQueryExecutor
import com.intellij.polySymbols.query.PolySymbolQueryExecutorFactory
import com.intellij.testFramework.junit5.TestApplication
import com.jetbrains.godot.gdscript.GdModelFixture
import com.jetbrains.godot.gdscript.gdModelFixture
import gdscript.polySymbols.index.GdPolySymbolQueriesUtil
import gdscript.polySymbols.index.GdSdkPolySymbolIndexUtil
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Test

@TestApplication
class GdExtensionsModelTest {
    private val gd: GdModelFixture by gdModelFixture()

    companion object{
        const val RiderLocator = "RiderLocator"
        const val Object = "Object"
    }

    @Test
    fun testRiderLocator() {
        val executor: PolySymbolQueryExecutor = GdSdkPolySymbolIndexUtil.getQueryExecutor(gd.fixture.project)

        // Find RiderLocator class from gdextensions
        val riderLocatorSymbol = GdPolySymbolQueriesUtil.getSdkClassSymbol(executor, RiderLocator)

        assertNotNull(riderLocatorSymbol, "$RiderLocator symbol not found")
        assertEquals(RiderLocator, riderLocatorSymbol!!.name)

        // Verify it inherits from Object
        val superSymbol = riderLocatorSymbol.resolveSuperClassSymbol()
        assertNotNull(superSymbol, "$RiderLocator should inherit from $Object")
        assertEquals(Object, superSymbol!!.classId)

        // Create executor for RiderLocator's scope and find get_installations method
        val riderLocatorExecutor = PolySymbolQueryExecutorFactory.createCustom {
            addRootScopes(riderLocatorSymbol.queryScope)
        }

        val getInstallations = "get_installations"
        val getInstallationsMethod = GdPolySymbolQueriesUtil.getSdkMethodSymbol(
            riderLocatorExecutor, RiderLocator, getInstallations, emptyList()
        )

        assertNotNull(getInstallationsMethod, "$getInstallations method not found")
        assertEquals(getInstallations, getInstallationsMethod!!.name)
    }
}
