package pe.straw.paperplus.app;

import java.lang.reflect.Method;

import android.app.Activity;
import android.content.Context;
import android.content.res.XModuleResources;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnKeyListener;
import android.webkit.WebView;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XC_MethodReplacement;
import de.robv.android.xposed.XSharedPreferences;
import de.robv.android.xposed.XposedHelpers;
import de.robv.android.xposed.callbacks.XC_LoadPackage.LoadPackageParam;
import pe.straw.paperplus.Paper;
import pe.straw.paperplus.PaperLogger;
import pe.straw.paperplus.PaperService;
import pe.straw.paperplus.PrefService;

public class Mekia {
	public static final String packageName = "org.mekia.android.ui";

	static XSharedPreferences pref = PrefService.getInstance();
	static final String prefKey = "mekia_library";
	
	static Activity bookViewActivity;
	static RelativeLayout reflowableControl;
	static RelativeLayout ePubView;
	static WebView skyWebView;
	static Method prev;
	static Method next;
	
	static TextView pageIndexLabel;
	static ImageButton btnSetting;
	static ImageButton btnRotate;
	
	public static void modCode(LoadPackageParam lparam){
		PaperLogger.log("Mekia library");
		if (pref.getBoolean(prefKey, false) == false)
			return;	
		PaperLogger.log("MekiaLibrary module is active");
		
		String className = "com.skytree.epub.BookViewActivity";
		XposedHelpers.findAndHookMethod(className, lparam.classLoader, "makeControls", new XC_MethodHook() {
			@Override
			protected void afterHookedMethod(MethodHookParam param) throws Throwable {
				ColorMatrix cm = new ColorMatrix();
				cm.set(new ColorMatrix(new float[] {
		                0, 0, 0, 0, 0,
		                0, 0, 0, 0, 0,
		                0, 0, 0, 0, 0,
		                0, 0, 0, 1f, 0 }));

				ColorMatrixColorFilter cf = new ColorMatrixColorFilter(cm);
				btnSetting = (ImageButton) XposedHelpers.getObjectField(param.thisObject, "설정버튼");
				btnSetting.setColorFilter(cf);
				
				btnRotate = (ImageButton) XposedHelpers.getObjectField(param.thisObject, "화면고정버튼");
				btnRotate.setVisibility(View.GONE);
				
				pageIndexLabel = (TextView) XposedHelpers.getObjectField(param.thisObject, "pageIndexLabel");
				pageIndexLabel.setTextColor(Color.BLACK);
			}
		});
		
		XposedHelpers.findAndHookMethod(className, lparam.classLoader, "hideControls", new XC_MethodHook() {
			protected void afterHookedMethod(MethodHookParam param) throws Throwable {
				pageIndexLabel.setVisibility(View.GONE);
				btnRotate.setVisibility(View.GONE);
			};
		});
		XposedHelpers.findAndHookMethod(className, lparam.classLoader, "showControls", new XC_MethodHook() {
			protected void afterHookedMethod(MethodHookParam param) throws Throwable {
				pageIndexLabel.setVisibility(View.VISIBLE);
				btnRotate.setVisibility(View.GONE);
			};
		});
		
		XposedHelpers.findAndHookMethod("com.skytree.epub.ci", lparam.classLoader, "getWebViewVersion", XC_MethodReplacement.returnConstant(40)); 
		
		setCustomFont(lparam);
	}

	static void setCustomFont(LoadPackageParam lparam){
		XposedHelpers.findAndHookMethod("android.content.res.AssetManager", lparam.classLoader,
				"open", String.class, new XC_MethodHook() {
			protected void afterHookedMethod(MethodHookParam param) throws Throwable {
				if (! ((String) param.args[0]).contains("KoPubBatangMedium.ttf"))
					return;
				
				param.setResult(
					XModuleResources.createInstance(PaperService.MODULE_PATH, null).getAssets().open("DaehanB.ttf")
				);
				return;
			};
		});
		
		XposedHelpers.findAndHookMethod("android.content.res.AssetManager", lparam.classLoader,
				"openFd", String.class, new XC_MethodHook() {
			protected void afterHookedMethod(MethodHookParam param) throws Throwable {
				if (! ((String) param.args[0]).contains("KoPubBatangMedium.ttf"))
					return;
				
				param.setResult(
					XModuleResources.createInstance(PaperService.MODULE_PATH, null).getAssets().openFd("DaehanB.ttf")
				);
				return;
			};
		});
	}
}