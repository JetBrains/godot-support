package gdscript.library

import java.util.concurrent.atomic.AtomicLong

/**
 * Orders the SDK load requests of one project.
 *
 * Every request takes a token from [newRequest]. A load runs unless a load with a newer token
 * already finished. That rule keeps two promises at the same time:
 *
 * - A superseded load never overwrites the result of a newer load.
 * - Some load always runs, because a load stops only when a newer load already finished.
 *
 * A request whose coroutine dies before it takes the lock therefore blocks nothing.
 */
class GdSdkLoadRequests {
    private val nextToken = AtomicLong()

    @Volatile
    private var finishedToken: Long = 0

    /** Returns the token of a new request. A token is always greater than every earlier token. */
    fun newRequest(): Long = nextToken.incrementAndGet()

    /** Returns whether a load with a newer token already finished. */
    fun isSuperseded(token: Long): Boolean = token < finishedToken

    /** Records that the load of [token] finished. */
    fun finished(token: Long) {
        synchronized(this) {
            if (token > finishedToken) finishedToken = token
        }
    }
}
