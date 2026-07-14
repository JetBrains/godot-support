package com.jetbrains.godot.gdscript.model

import com.intellij.polySymbols.query.PolySymbolQueryExecutor
import com.intellij.polySymbols.query.PolySymbolQueryExecutorFactory
import com.jetbrains.godot.gdscript.GdModelTestBase
import gdscript.polySymbols.index.GdPolySymbolQueriesUtil
import gdscript.polySymbols.index.GdSdkPolySymbolIndexUtil
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

@RunWith(JUnit4::class)
class GdExtensionsModelTest : GdModelTestBase() {

    companion object{
        const val RiderLocator = "RiderLocator"
        const val Object = "Object"
    }

    @Test
    fun testRiderLocator() {
        val executor: PolySymbolQueryExecutor = GdSdkPolySymbolIndexUtil.getQueryExecutor(myFixture.project)

        // Find RiderLocator class from gdextensions
        val riderLocatorSymbol = GdPolySymbolQueriesUtil.getSdkClassSymbol(executor, RiderLocator)

        assertNotNull("$RiderLocator symbol not found", riderLocatorSymbol)
        assertEquals(RiderLocator, riderLocatorSymbol!!.name)

        // Verify it inherits from Object
        val superSymbol = riderLocatorSymbol.resolveSuperClassSymbol()
        assertNotNull("$RiderLocator should inherit from $Object", superSymbol)
        assertEquals(Object, superSymbol!!.classId)

        // Create executor for RiderLocator's scope and find get_installations method
        val riderLocatorExecutor = PolySymbolQueryExecutorFactory.createCustom {
            addRootScopes(riderLocatorSymbol.queryScope)
        }

        val getInstallations = "get_installations"
        val getInstallationsMethod = GdPolySymbolQueriesUtil.getSdkMethodSymbol(
            riderLocatorExecutor, RiderLocator, getInstallations, emptyList()
        )

        assertNotNull("$getInstallations method not found", getInstallationsMethod)
        assertEquals(getInstallations, getInstallationsMethod!!.name)
    }
}
