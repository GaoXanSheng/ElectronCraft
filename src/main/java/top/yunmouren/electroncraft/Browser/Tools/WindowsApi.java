package top.yunmouren.electroncraft.Browser.Tools;

import com.sun.jna.CallbackReference;
import com.sun.jna.Pointer;
import com.sun.jna.platform.win32.User32;
import com.sun.jna.platform.win32.WinDef;
import com.sun.jna.platform.win32.WinUser;
import com.sun.jna.win32.StdCallLibrary;
import net.minecraft.client.Minecraft;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.ScreenEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import top.yunmouren.electroncraft.Client.Client;

@OnlyIn(Dist.CLIENT)
public class WindowsApi {
    // windowTop
    private final WinDef.HWND HWND_TOP = new WinDef.HWND(Pointer.NULL);
    private final String Minecraft_Title = "overlapWindows";
    private final String Web_Title = "ElectronCraftBrowser";
    private static WinDef.HWND MinecrafthWndParent = null;
    private static WinDef.HWND browserhWndParent = null;
    private static final User32 user32 = User32.INSTANCE;
    private static WinDef.RECT rect = new WinDef.RECT();
    private static boolean init = false;
    private final int SWP_NOZORDER = 0x0004;
    private final int SWP_SHOWWINDOW = 0x0040;

    public void overlapWindows() {
        // SetTheTitle
        Minecraft.getInstance().getWindow().setTitle(Minecraft_Title);
        // Find the handles of the two parent windows by header
        MinecrafthWndParent = user32.FindWindow(null, Minecraft_Title);
        browserhWndParent = user32.FindWindow(null, Web_Title);
        if (MinecrafthWndParent != null && browserhWndParent != null) {
            CalculateFrameArea();
            OverlapWindows(MinecrafthWndParent, browserhWndParent);
            user32.SetWindowPos(browserhWndParent, HWND_TOP, 0, 0, rect.right - rect.left, rect.bottom - rect.top, SWP_NOZORDER | SWP_SHOWWINDOW);
            setNoActivate(MinecrafthWndParent);
            KeyForwarding(browserhWndParent, MinecrafthWndParent);
            init = true;
        }
    }

    public interface WindowProc extends StdCallLibrary.StdCallCallback {
        WinDef.LRESULT callback(WinDef.HWND hWnd, int uMsg, WinDef.WPARAM wParam, WinDef.LPARAM lParam);
    }

    // DefinitionOfCommonMouseMessages
    public static final int WM_LBUTTONDOWN = 0x0201; // 鼠标左键按下
    public static final int WM_LBUTTONUP = 0x0202;   // 鼠标左键抬起
    public static final int WM_RBUTTONDOWN = 0x0204; // 鼠标右键按下
    public static final int WM_RBUTTONUP = 0x0205;   // 鼠标右键抬起
    public static final int WM_MOUSEMOVE = 0x0200;   // 鼠标移动

    // Get the original window process and set up a new window process
    public void MouseForwarding(WinDef.HWND childHWnd, WinDef.HWND parentHWnd) {
        // Gets the original window process pointer for the child window
        var originalProc = user32.GetWindowLongPtr(childHWnd, WinUser.GWL_WNDPROC);

        // A new window procedure that forwards mouse events to the parent window
        WindowProc newProc = (hWnd, uMsg, wParam, lParam) -> {
            // 处理鼠标事件
            user32.SendMessage(parentHWnd, uMsg, wParam, lParam);
            return user32.CallWindowProc(originalProc.toPointer(), hWnd, uMsg, wParam, lParam);
        };

        // Use the CallbackReference provided by JNA to get a new window procedure pointer
        Pointer newProcPointer = CallbackReference.getFunctionPointer(newProc);

        // TheWindowProcessOfReplacingAChildWindow
        user32.SetWindowLongPtr(childHWnd, WinUser.GWL_WNDPROC, newProcPointer);
    }

    public void KeyForwarding(WinDef.HWND childHWnd, WinDef.HWND parentHWnd) {
        // Gets the original window process pointer for the child window
        var originalProc = user32.GetWindowLongPtr(childHWnd, WinUser.GWL_WNDPROC);
        // New window process that forwards key events to the parent window
        WindowProc newProc = (hWnd, uMsg, wParam, lParam) -> {
            user32.SendMessage(parentHWnd, uMsg, wParam, lParam);
            return user32.CallWindowProc(originalProc.toPointer(), hWnd, uMsg, wParam, lParam);
        };

        // Use the CallbackReference provided by JNA to get a new window procedure pointer
        Pointer newProcPointer = CallbackReference.getFunctionPointer(newProc);

        // TheWindowProcessOfReplacingAChildWindow
        user32.SetWindowLongPtr(childHWnd, WinUser.GWL_WNDPROC, newProcPointer);
    }

    public WinDef.HWND getMinecrafthWndParent() {
        return MinecrafthWndParent;
    }

    public WinDef.HWND getBrowserhWndParent() {
        return browserhWndParent;
    }


    /**
     * WindowOverlap
     */
    private void CalculateFrameArea() {
        // GetsTheOverallSizeOfTheWindow
        user32.GetWindowRect(MinecrafthWndParent, rect);
        // GetTheClientZoneSize
        WinDef.RECT clientRect = new WinDef.RECT();
        user32.GetClientRect(MinecrafthWndParent, clientRect);
        // SetTheVisualizationArea
        WindowResizeListener.Windows_Frame_Height = (rect.bottom - rect.top) - (clientRect.bottom - clientRect.top);
        WindowResizeListener.Windows_Frame_Width = (rect.left - rect.right) - (clientRect.left - clientRect.right);
    }

    @Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.FORGE, value = Dist.CLIENT)
    public static class WindowResizeListener {
        private static int height = 0;
        private static int width = 0;
        private static int Windows_Frame_Width = 0;
        private static int Windows_Frame_Height = 0;

        /**
         * synchronizeWindowSize
         */
        @SubscribeEvent
        public static void onScreenResize(ScreenEvent event) {
            if ((height != event.getScreen().height || width != event.getScreen().width) && WindowsApi.init) {
                height = event.getScreen().height;
                width = event.getScreen().width;
                user32.GetWindowRect(MinecrafthWndParent, rect);
                CalculateScaling((rect.right - rect.left), (rect.bottom - rect.top));
            }
        }

        /**
         * AutomaticallyCalculateFrameworkArea
         *
         * @param Width
         * @param Height
         */
        private static void CalculateScaling(int Width, int Height) {
            boolean isFullscreen = Minecraft.getInstance().getWindow().isFullscreen();
            new Thread(() -> {
                // IfItSNotFullscreenTheViewportIsCalculated
                if (isFullscreen) {
                    Client.browser.api.setBrowserSize(Width, Height);
                } else {
                    Client.browser.api.setBrowserSize(Width - Windows_Frame_Width, Height - Windows_Frame_Height);
                }
            }).start();

        }

        /**
         * ManuallyCalculateTheFrameworkArea
         */
        public static void onScreenResize() {
            user32.GetWindowRect(MinecrafthWndParent, rect);
            CalculateScaling((rect.right - rect.left), (rect.bottom - rect.top));
        }

    }

    public void setNoActivate(WinDef.HWND hwnd) {
        // 获取当前窗口的扩展样式
        int GWL_EXSTYLE = -20;
        int style = user32.GetWindowLong(hwnd, GWL_EXSTYLE);
        // 设置 WS_EX_NOACTIVATE 样式
        int WS_EX_NOACTIVATE = 0x08000000;
        user32.SetWindowLong(hwnd, GWL_EXSTYLE, style | WS_EX_NOACTIVATE);
    }

    /**
     * OverlayWindow
     *
     * @param childWindowHandle  browser
     * @param parentWindowHandle Minecraft
     */
    private void OverlapWindows(WinDef.HWND childWindowHandle, WinDef.HWND parentWindowHandle) {
        User32.WNDENUMPROC enumChildWindowsCallback = (browserWinHWND, lParam) -> {
            user32.SetParent(parentWindowHandle, childWindowHandle);
            user32.SetForegroundWindow(parentWindowHandle);
            return true;
        };
        user32.EnumChildWindows(parentWindowHandle, enumChildWindowsCallback, Pointer.NULL);

    }
}