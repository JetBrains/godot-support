package tscn.toolWindow.model

import com.intellij.jna.JnaLoader
import com.intellij.openapi.diagnostic.thisLogger
import com.intellij.util.system.LowLevelLocalMachineAccess
import com.intellij.util.system.OS
import com.intellij.util.ui.StartupUiUtil
import com.sun.jna.Library
import com.sun.jna.Native
import com.sun.jna.platform.unix.X11
import com.sun.jna.platform.win32.User32

object NativeKeyState {
    @OptIn(LowLevelLocalMachineAccess::class)
    fun isCtrlDown(): Boolean? = when (OS.CURRENT) {
        OS.Windows -> win(User32.VK_CONTROL)
        OS.macOS -> listOf(mac(kVK_Control), mac(kVK_RightControl), mac(kVK_Command), mac(kVK_RightCommand)).anyDownOrNull()
        OS.Linux -> listOf(x11(X11.XK_Control_L), x11(X11.XK_Control_R)).anyDownOrNull()
        else -> null
    }

    private fun Iterable<Boolean?>.anyDownOrNull(): Boolean? {
        return when {
            any { it == true } -> true
            any { it == null } -> null
            else -> false
        }
    }

    @OptIn(LowLevelLocalMachineAccess::class)
    fun isAltDown(): Boolean? = when (OS.CURRENT) {
        OS.Windows -> win(User32.VK_MENU)
        OS.macOS -> listOf(mac(kVK_Option), mac(kVK_RightOption)).anyDownOrNull()
        OS.Linux -> listOf(x11(X11.XK_Alt_L), x11(X11.XK_Alt_R)).anyDownOrNull()
        else -> null
    }

    // ---- Windows: user32!GetAsyncKeyState ----
    private val user32: User32? by lazy {
        val outcome = runCatching { User32.INSTANCE }
        if (outcome.isFailure) {
            thisLogger().warn("Failed to load user32 library", outcome.exceptionOrNull())
        }
        return@lazy outcome.getOrNull()
    }

    private fun win(vKey: Int): Boolean? {
        val lib = user32 ?: return null
        // high-order bit set => key is currently down
        return (lib.GetAsyncKeyState(vKey).toInt() and 0x8000) != 0
    }

    // ---- Linux/X11: libX11!XQueryKeymap ----
    private val x11lib: X11? by lazy {
        val outcome = runCatching { X11.INSTANCE }
        if (outcome.isFailure) {
            thisLogger().warn("Failed to load X11 library", outcome.exceptionOrNull())
        }
        return@lazy outcome.getOrNull()
    }

    private val isWayland: Boolean = StartupUiUtil.isWaylandToolkit()

    private fun x11(keysym: Int): Boolean? {
        if (isWayland) {
            return null
        }
        val lib = x11lib ?: return null
        val display = lib.XOpenDisplay(null) ?: return null
        return try {
            val keys = ByteArray(32) // 256 bits, one per keycode
            lib.XQueryKeymap(display, keys)
            val keycode = lib.XKeysymToKeycode(display, X11.KeySym(keysym.toLong())).toInt() and 0xFF
            if (keycode == 0) false
            else (keys[keycode / 8].toInt() and (1 shl (keycode % 8))) != 0
        } catch (t: Throwable) {
            thisLogger().warn("Was not able to get X11 keys to determine modifiers accurately: $t")
            null
        } finally {
            lib.XCloseDisplay(display)
        }
    }

    // ---- macOS: CGEventSourceKeyState (virtual keycodes) ----
    private const val kCGEventSourceStateHIDSystemState = 1
    private const val kVK_Command = 0x37
    private const val kVK_RightCommand = 0x36
    private const val kVK_Control = 0x3B
    private const val kVK_RightControl = 0x3E
    private const val kVK_Option = 0x3A
    private const val kVK_RightOption = 0x3D

    private interface CoreGraphics : Library {
        fun CGEventSourceKeyState(stateID: Int, keycode: Short): Byte
    }

    private val cg: CoreGraphics? by lazy {
        if (!JnaLoader.isLoaded()) {
            thisLogger().warn("Failed to load JNA")
            null
        } else {
            val outcome = runCatching {
                Native.load("CoreGraphics", CoreGraphics::class.java)
            }
            if (outcome.isFailure) {
                thisLogger().warn("Failed to load coregraphics library", outcome.exceptionOrNull())
            }
            outcome.getOrNull()
        }
    }

    private fun mac(keycode: Int): Boolean? {
        val value = (cg?.CGEventSourceKeyState(kCGEventSourceStateHIDSystemState, keycode.toShort())?.toInt())
        if (value == null) {
            return value
        }
        return value != 0
    }
}
