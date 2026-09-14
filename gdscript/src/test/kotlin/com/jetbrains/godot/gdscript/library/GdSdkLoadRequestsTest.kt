package com.jetbrains.godot.gdscript.library

import gdscript.library.GdSdkLoadRequests
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

/**
 * Tests the order rule of the SDK load requests.
 *
 * The rule must keep two promises. A superseded load never overwrites a newer result, and some load always runs.
 */
@RunWith(JUnit4::class)
class GdSdkLoadRequestsTest {

    @Test
    fun testNoRequestIsSupersededBeforeALoadFinishes() {
        val requests = GdSdkLoadRequests()

        val first = requests.newRequest()
        val second = requests.newRequest()

        assertFalse(requests.isSuperseded(first))
        assertFalse(requests.isSuperseded(second))
    }

    @Test
    fun testAnOlderRequestStopsAfterANewerLoadFinishes() {
        val requests = GdSdkLoadRequests()
        val first = requests.newRequest()
        val second = requests.newRequest()

        requests.finished(second)

        assertTrue(requests.isSuperseded(first))
        assertFalse("The load that finished must not report itself as superseded.", requests.isSuperseded(second))
    }

    @Test
    fun testTheOlderRequestStillRunsWhenTheNewerRequestNeverFinishes() {
        // This is the case that a plain latch broke. The newer coroutine dies before it takes the lock,
        // so the older load must still run. Otherwise no load runs at all.
        val requests = GdSdkLoadRequests()
        val first = requests.newRequest()
        requests.newRequest()

        assertFalse(requests.isSuperseded(first))
    }

    @Test
    fun testANewerRequestStillRunsAfterAnOlderLoadFinishes() {
        val requests = GdSdkLoadRequests()
        val first = requests.newRequest()
        val second = requests.newRequest()

        requests.finished(first)

        assertFalse(requests.isSuperseded(second))
    }

    @Test
    fun testAnOutOfOrderFinishDoesNotUndoANewerFinish() {
        val requests = GdSdkLoadRequests()
        val first = requests.newRequest()
        val second = requests.newRequest()
        val third = requests.newRequest()

        requests.finished(third)
        requests.finished(first)

        assertTrue(requests.isSuperseded(second))
    }
}
