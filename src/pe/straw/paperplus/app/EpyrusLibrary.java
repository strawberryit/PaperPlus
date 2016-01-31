package pe.straw.paperplus.app;

import android.view.KeyEvent;
import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XSharedPreferences;
import de.robv.android.xposed.XposedHelpers;
import de.robv.android.xposed.callbacks.XC_LoadPackage.LoadPackageParam;
import pe.straw.paperplus.Paper;
import pe.straw.paperplus.PaperLogger;
import pe.straw.paperplus.PrefService;

public class EpyrusLibrary {
	public static final String packageName = "com.bms.xdfplus";
	
	static XSharedPreferences pref = PrefService.getInstance();
	static final String prefKey = "epyrus_library";
	
	public static void modCode(LoadPackageParam lparam){
		PaperLogger.log("EpyrusLibrary");
		if (pref.getBoolean(prefKey, false) == false)
			return;
		
		PaperLogger.log("EpyrusLibrary module is active");
		String className = "com.bmsoft.xdfplus.xmlBookMain";
		XposedHelpers.findAndHookMethod(className, lparam.classLoader, "onKeyDown", "int", KeyEvent.class, new XC_MethodHook() {
			protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
				PaperLogger.log((int)param.args[0]);
				int keyCode = (int) param.args[0];
				
				// Page Up -> Vol Up
				// Page Down -> Vol Down
				if (keyCode == Paper.KEY_PAGE_UP)
					param.args[0] = Paper.KEY_VOL_UP;
				else if (keyCode == Paper.KEY_PAGE_DOWN)
					param.args[0] = Paper.KEY_VOL_DOWN;
			};
		});		
	}
}