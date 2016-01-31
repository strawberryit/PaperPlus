package pe.straw.paperplus.app;

import java.lang.reflect.Method;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnKeyListener;
import android.webkit.WebView;
import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XSharedPreferences;
import de.robv.android.xposed.XposedHelpers;
import de.robv.android.xposed.callbacks.XC_LoadPackage.LoadPackageParam;
import pe.straw.paperplus.Paper;
import pe.straw.paperplus.PaperLogger;
import pe.straw.paperplus.PrefService;

public class Yes24Library {
	public static final String packageName = "com.incube.newepub";

	static XSharedPreferences pref = PrefService.getInstance();
	static final String prefKey = "yes24_library";
	
	static Object mEngine;
	static Method prev;
	static Method next;
	
	public static void modCode(LoadPackageParam lparam){
		PaperLogger.log("Yes24Library");
		if (pref.getBoolean(prefKey, false) == false)
			return;	
		PaperLogger.log("Yes24Library module is active");
		
		String className = "com.daouincube.android.ebook.epub.EpubView";
		XposedHelpers.findAndHookMethod(className, lparam.classLoader, "loadEngine", new XC_MethodHook() {
			protected void afterHookedMethod(MethodHookParam param) throws Throwable {
				mEngine = XposedHelpers.getObjectField(param.thisObject, "mEngine");
				prev = XposedHelpers.findMethodExact(mEngine.getClass(), "moveToPrevPage");
				next = XposedHelpers.findMethodExact(mEngine.getClass(), "moveToNextPage");
			};
		});
		
		className = "com.daouincube.android.ebook.epub.EpubXhtmlView";
		XposedHelpers.findAndHookMethod(className, lparam.classLoader, "init", new XC_MethodHook(){
			@Override
			protected void afterHookedMethod(MethodHookParam param) throws Throwable {
				WebView view = (WebView) param.thisObject;
				view.setOnKeyListener(onKeyDown);
			}
		});		
	}
	
	static OnKeyListener onKeyDown = new View.OnKeyListener() {
		@Override
		public boolean onKey(View v, int keyCode, KeyEvent event) {
			if (event.getAction() != KeyEvent.ACTION_DOWN)
				return false;

			try {
				switch(keyCode){
					case Paper.KEY_VOL_UP:
					case Paper.KEY_PAGE_UP:
						prev.invoke(mEngine); return true;
					case Paper.KEY_VOL_DOWN:
					case Paper.KEY_PAGE_DOWN:
						next.invoke(mEngine); return true;
				}
			} catch (Exception e){
				e.printStackTrace();
			}
			return false;
		}
	};
}