package pe.straw.paperplus.app;

import de.robv.android.xposed.XSharedPreferences;
import de.robv.android.xposed.callbacks.XC_LoadPackage.LoadPackageParam;
import pe.straw.paperplus.PaperLogger;
import pe.straw.paperplus.PrefService;

public class KyoboLibrary {
	public static final String packageName = "com.kyobo.ebook.b2b.phone.type3";

	static XSharedPreferences pref = PrefService.getInstance();
	static final String prefKey = "kyobo_library";

	public static void modCode(LoadPackageParam lparam){
		PaperLogger.log("KyoboLibrary");
		if (pref.getBoolean(prefKey, false) == false)
			return;
		
		PaperLogger.log("KyoboLibrary module is active");
		KyoboPlatform.modCode(lparam, packageName);
	}
}