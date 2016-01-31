package pe.straw.paperplus.app;

import java.lang.reflect.Method;

import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnKeyListener;
import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedHelpers;
import de.robv.android.xposed.callbacks.XC_LoadPackage.LoadPackageParam;
import pe.straw.paperplus.Paper;
import pe.straw.paperplus.PaperLogger;

public class KyoboPlatform {
	static Method prev;
	static Method next;
	
	public static void modCode(LoadPackageParam lparam, String packageName){
		// ViewContainer에서 키 이벤트를 인식한다.
		final String classViewerContainer = "com.ebook.epub.viewer.ViewerContainer";
		final Method setOnKeyListener = XposedHelpers.findMethodExact(classViewerContainer, lparam.classLoader, "setOnKeyListener", "android.view.View.OnKeyListener");

		// 페이지 넘김 메소드를 찾아온다.
		final String classEPubViewer = "com.ebook.epub.viewer.EPubViewer";
		prev = XposedHelpers.findMethodExact(classEPubViewer, lparam.classLoader, "goPriorPage");
		next = XposedHelpers.findMethodExact(classEPubViewer, lparam.classLoader, "goNextPage");

		// 책 읽는 Activity
		String className = packageName + ".viewer.epub.activity.ViewerEpubMainActivity";
		XposedHelpers.findAndHookMethod(className, lparam.classLoader, "epubViewerSetupAndEvents", new XC_MethodHook() {
			@Override
			protected void afterHookedMethod(MethodHookParam param) throws Throwable {
				PaperLogger.log("KyoboLibrary: hooked epubViewerSetupAndEvents");
				Object activity = param.thisObject;
				Object viewerContainer = XposedHelpers.getObjectField(activity, "mViewer");

				// 생성한 리스너를 등록한다.
				setOnKeyListener.invoke(viewerContainer, onKeyDown);
			}
		});
				
	}
	
	private static OnKeyListener onKeyDown = new View.OnKeyListener() {
		/**
		 * @param v : A instance of com.ebook.epub.viewer.EPubViewer
		 */
		@Override
		public boolean onKey(View v, int keyCode, KeyEvent event) {			
			if (event.getAction() != KeyEvent.ACTION_DOWN)
				return true;
			
			PaperLogger.log("onKey (" + keyCode + ")");
			try {
				switch (keyCode){
					case Paper.KEY_VOL_UP: // VolUp
					case Paper.KEY_PAGE_UP: // PgUp
						prev.invoke(v);
						return true;
					case Paper.KEY_VOL_DOWN: // VolDown
					case Paper.KEY_PAGE_DOWN: // PgDn
						next.invoke(v);
						return true;
				}
			} catch (Exception e){
				e.printStackTrace();
			}
			return false;
		}
	};
}