package pe.straw.paperplus.app;

import java.lang.reflect.Method;

import android.app.Activity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.WindowManager;
import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XSharedPreferences;
import de.robv.android.xposed.XposedHelpers;
import de.robv.android.xposed.callbacks.XC_LoadPackage.LoadPackageParam;
import pe.straw.paperplus.Paper;
import pe.straw.paperplus.PaperLogger;
import pe.straw.paperplus.PrefService;
import static de.robv.android.xposed.XposedHelpers.*;

public class KakaoPage {
	public static final String packageName = "com.kakao.page";
	
	static XSharedPreferences pref = PrefService.getInstance();
	static final String prefKey = "kakaopage";
	
	public static void modCode(LoadPackageParam lpparam){
		PaperLogger.log("KakaoPage");
		if (pref.getBoolean(prefKey, false) == false)
			return;
		
		PaperLogger.log("KakaoPage module is active");
		
		disableAnimation(lpparam);
		setFullscreenComicViewer(lpparam);
		
		String className = "com.podotree.kakaopage.viewer.comicviewer.view.ComicViewPager";
		final Method next = XposedHelpers.findMethodExact(XposedHelpers.findClass(className, lpparam.classLoader), "c");
		final Method prev = XposedHelpers.findMethodExact(XposedHelpers.findClass(className, lpparam.classLoader), "d");
		
		className = "com.podotree.kakaopage.viewer.comicviewer.ComicViewerActivity";
		findAndHookMethod(className, lpparam.classLoader, "onKeyDown", "int", KeyEvent.class, new XC_MethodHook() {
			protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
				int keyCode = (int) param.args[0];
				KeyEvent event = (KeyEvent) param.args[1];
				
				// Key Down¸¸ Ã³¸®
				if (event.getAction() != KeyEvent.ACTION_DOWN)
					return;
				
				Object comicViewPager = (Object) XposedHelpers.getObjectField(param.thisObject, "e");
				
				switch (keyCode){
				case Paper.KEY_VOL_UP:
				case Paper.KEY_PAGE_UP:
					prev.invoke(comicViewPager);
					return;
				case Paper.KEY_VOL_DOWN:
				case Paper.KEY_PAGE_DOWN:
					next.invoke(comicViewPager);
					return;
			}
				
			};
		});
	}
	
	static void disableAnimation(LoadPackageParam lpparam){
		String className = "android.support.v4.view.ViewPager";
		findAndHookMethod(className, lpparam.classLoader, "setCurrentItem", "int", "boolean", new XC_MethodHook() {
			protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
				param.args[1] = false;
			};
		});
	}
	
	static void setFullscreenComicViewer(LoadPackageParam lpparam){
		String className = "com.podotree.kakaoslide.viewer.app.comic.activity.UserComicViewerActivity";
		findAndHookMethod(className, lpparam.classLoader, "onCreate", Bundle.class, new XC_MethodHook() {
			protected void afterHookedMethod(MethodHookParam param) throws Throwable {
				Activity activity = (Activity) param.thisObject;
				activity.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
						WindowManager.LayoutParams.FLAG_FULLSCREEN);
			};
		});
	}
}