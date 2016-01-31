package pe.straw.paperplus.app;

import java.lang.reflect.Method;

import android.app.Activity;
import android.view.KeyEvent;
import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XSharedPreferences;
import de.robv.android.xposed.XposedHelpers;
import de.robv.android.xposed.callbacks.XC_LoadPackage.LoadPackageParam;
import pe.straw.paperplus.Paper;
import pe.straw.paperplus.PaperLogger;
import pe.straw.paperplus.PrefService;

public class BookcubeLibrary {
	public static final String packageName = "com.bookcube.digitallibrary";
	
	static XSharedPreferences pref = PrefService.getInstance();
	static final String prefKey = "bookcube_library";
	
	static Method prev;
	static Method next;
	
	public static void modCode(LoadPackageParam lparam){
		PaperLogger.log("BookcubeLibrary");
		if (pref.getBoolean(prefKey, false) == false)
			return;
		
		PaperLogger.log("BookcubeLibrary module is active");
		String className = "com.bookcube.ui.EpubViewerActivity";
		
		prev = XposedHelpers.findMethodExact(className, lparam.classLoader, "prev");
		next = XposedHelpers.findMethodExact(className, lparam.classLoader, "next");
		
		XposedHelpers.findAndHookMethod(className, lparam.classLoader, "onKeyDown", "int", KeyEvent.class, new XC_MethodHook() {
			protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
				Activity activity = (Activity) param.thisObject;
				int keyCode = (Integer) param.args[0];
				KeyEvent event = (KeyEvent) param.args[1];
				
				if (event.getAction() != KeyEvent.ACTION_DOWN)
					return;
				
				switch(keyCode){
					case Paper.KEY_VOL_UP:
					case Paper.KEY_PAGE_UP:
						prev.invoke(activity); break;
					case Paper.KEY_VOL_DOWN:
					case Paper.KEY_PAGE_DOWN:
						next.invoke(activity); break;
				}
			};
		});		
	}
}