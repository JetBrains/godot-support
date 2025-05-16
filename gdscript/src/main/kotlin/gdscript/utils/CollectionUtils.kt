package gdscript.utils

fun <TKey, TValue> Map<TKey,TValue>.firstOrNull(): Map.Entry<TKey, TValue>? {
    for (v in this) {
        return v
    }
    return null
}

inline fun <T> Array<out T>.forEachReversed(action: (T) -> Unit) {
    var i = size
    while (i > 0) {
        action(this[--i])
    }
}