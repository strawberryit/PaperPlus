package pe.straw.paperplus.app;

import java.lang.reflect.Method;

import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XSharedPreferences;
import de.robv.android.xposed.XposedHelpers;
import de.robv.android.xposed.callbacks.XC_LoadPackage.LoadPackageParam;
import pe.straw.paperplus.Paper;
import pe.straw.paperplus.PaperLogger;
import pe.straw.paperplus.PrefService;

public class Yes24Ebook {
	public static final String packageName = "com.yes24.ebook.fourth";

	static XSharedPreferences pref = PrefService.getInstance();
	static final String prefKey = "yes24_ebook";
	
	static Object mEpubView;
	static Method prev;
	static Method next;
	
	public static void modCode(LoadPackageParam lparam){
		PaperLogger.log("Yes24Ebook");
		if (pref.getBoolean(prefKey, false) == false)
			return;	
		PaperLogger.log("Yes24Ebook module is active");
		
		String className = "com.keph.crema.lunar.ui.viewer.epub.fragment.CremaEPUBMainFragment";
		final Class<?> classCremaEPUBMainFragment = XposedHelpers.findClass(className, lparam.classLoader);
		
		// 페이지 넘김에 필요한 메서드 추출
		XposedHelpers.findAndHookMethod(className, lparam.classLoader, "onCreateView", LayoutInflater.class, ViewGroup.class, Bundle.class,
				new XC_MethodHook() {
					protected void afterHookedMethod(MethodHookParam param) throws Throwable {
						mEpubView = XposedHelpers.getStaticObjectField(classCremaEPUBMainFragment, "mEpubView");
						prev = XposedHelpers.findMethodExact(mEpubView.getClass(), "GotoPreviousPage");
						next = XposedHelpers.findMethodExact(mEpubView.getClass(), "GotoNextPage");
					};
				}
		);	
		
		// 키입력 감지
		XposedHelpers.findAndHookMethod(className, lparam.classLoader, "onKeyDown", int.class, KeyEvent.class,
				new XC_MethodHook() {
			protected void beforeHookedMethod(MethodHookParam param) throws Throwable {

				int keyCode = (int) param.args[0];
				KeyEvent event = (KeyEvent) param.args[1];
				
				if (event.getAction() != KeyEvent.ACTION_DOWN)
					return;
				
				switch (keyCode){
					case Paper.KEY_PAGE_UP:
					case Paper.KEY_VOL_UP:
						prev.invoke(mEpubView);
						param.setResult(true);
						break;
					case Paper.KEY_PAGE_DOWN:
					case Paper.KEY_VOL_DOWN:
						next.invoke(mEpubView);
						param.setResult(true);
						break;
				}
			};
		});
	}
}