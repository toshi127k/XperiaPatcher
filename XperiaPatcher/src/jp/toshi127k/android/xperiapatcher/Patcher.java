package jp.toshi127k.android.xperiapatcher;

import java.io.FileInputStream;
import java.io.InputStream;

import android.content.res.XModuleResources;
import android.content.res.XResources;
import android.content.res.XResources.DimensionReplacement;
import android.content.res.XResources.DrawableLoader;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import de.robv.android.xposed.IXposedHookInitPackageResources;
import de.robv.android.xposed.IXposedHookZygoteInit;
import de.robv.android.xposed.XSharedPreferences;
import de.robv.android.xposed.XposedBridge;
import de.robv.android.xposed.callbacks.XC_InitPackageResources.InitPackageResourcesParam;
import de.robv.android.xposed.callbacks.XC_LayoutInflated;

public class Patcher implements IXposedHookZygoteInit, IXposedHookInitPackageResources {
	private static final String SystemUI = "com.android.systemui";
	private static String modulePath = null;

	@Override
	public void initZygote(StartupParam startupParam) throws Throwable {
		modulePath = startupParam.modulePath;
	}

	@Override
	public void handleInitPackageResources(final InitPackageResourcesParam resparam) throws Throwable {
		if(!resparam.packageName.equals(SystemUI)){
			return;
		}
		final XModuleResources modRes = XModuleResources.createInstance(modulePath, resparam.res);
		final XSharedPreferences pref = new XSharedPreferences("jp.toshi127k.android.xperiapatcher"); //, "jp.toshi127k.android.xperiapatcher_preferences");
		if (pref.getBoolean("BatteryMod", false)) {
			resparam.res.hookLayout(SystemUI, "layout", "super_status_bar", new XC_LayoutInflated() {
	            @Override
	            public void handleLayoutInflated(LayoutInflatedParam liparam)
	                    throws Throwable {
	            	LinearLayout ll = (LinearLayout) liparam.view
	                        .findViewById(liparam.res.getIdentifier(
	                                "signal_battery_cluster", "id", SystemUI));
	        		//バッテリーアイコン用のビューを作成
	                ImageView imageView = new ImageView(ll.getContext());
	                BatteryController batteryController = new BatteryController(ll.getContext(), XModuleResources.createInstance(modulePath, resparam.res));
	                batteryController.setIconView(imageView);
	                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
	                float density = ll.getContext().getResources().getDisplayMetrics().density;
	                params.setMarginStart((int) (4.0f * density + 0.5f));
	                ll.addView(imageView, params);
	            }
			});
		}
		if (pref.getBoolean("WifiIconMod", false)) {
			resparam.res.hookLayout(SystemUI, "layout", "super_status_bar", new XC_LayoutInflated() {
	            @Override
	            public void handleLayoutInflated(LayoutInflatedParam liparam)
	                    throws Throwable {
		            //wifiアイコンをシグナルアイコンの後ろへ移動
		            FrameLayout wifi_combo = (FrameLayout) liparam.view.findViewById(liparam.res.getIdentifier("wifi_combo","id",SystemUI));
		            ViewGroup parent = (ViewGroup) wifi_combo.getParent();
		            parent.removeView(wifi_combo);
		            FrameLayout mobile_combo = (FrameLayout) liparam.view.findViewById(liparam.res.getIdentifier("mobile_combo","id",SystemUI));
		            ViewGroup mobile = (ViewGroup) mobile_combo.getParent();
		            int index = parent.indexOfChild(mobile);
		            parent.addView(wifi_combo, index + 1);
	            }
			});
			//resparam.res.setReplacement(SystemUI, "bool", "config_enableMobileComboIcon", true);
			//wifiアイコンの差し替え
			resparam.res.setReplacement(SystemUI, "drawable", "stat_sys_wifi_signal_null", modRes.fwd(R.drawable.stat_sys_wifi_signal_null));
			resparam.res.setReplacement(SystemUI, "drawable", "stat_sys_wifi_signal_0", modRes.fwd(R.drawable.stat_sys_wifi_signal_0));
			resparam.res.setReplacement(SystemUI, "drawable", "stat_sys_wifi_signal_1", modRes.fwd(R.drawable.stat_sys_wifi_signal_1));
			resparam.res.setReplacement(SystemUI, "drawable", "stat_sys_wifi_signal_2", modRes.fwd(R.drawable.stat_sys_wifi_signal_2));
			resparam.res.setReplacement(SystemUI, "drawable", "stat_sys_wifi_signal_3", modRes.fwd(R.drawable.stat_sys_wifi_signal_3));
			resparam.res.setReplacement(SystemUI, "drawable", "stat_sys_wifi_signal_4", modRes.fwd(R.drawable.stat_sys_wifi_signal_4));
			resparam.res.setReplacement(SystemUI, "drawable", "stat_sys_wifi_signal_1_fully", modRes.fwd(R.drawable.stat_sys_wifi_signal_1_fully));
			resparam.res.setReplacement(SystemUI, "drawable", "stat_sys_wifi_signal_2_fully", modRes.fwd(R.drawable.stat_sys_wifi_signal_2_fully));
			resparam.res.setReplacement(SystemUI, "drawable", "stat_sys_wifi_signal_3_fully", modRes.fwd(R.drawable.stat_sys_wifi_signal_3_fully));
			resparam.res.setReplacement(SystemUI, "drawable", "stat_sys_wifi_signal_4_fully", modRes.fwd(R.drawable.stat_sys_wifi_signal_4_fully));
			resparam.res.setReplacement(SystemUI, "drawable", "stat_sys_wifi_limited_signal_0", modRes.fwd(R.drawable.stat_sys_wifi_limited_signal_0));
			resparam.res.setReplacement(SystemUI, "drawable", "stat_sys_wifi_limited_signal_1", modRes.fwd(R.drawable.stat_sys_wifi_limited_signal_1));
			resparam.res.setReplacement(SystemUI, "drawable", "stat_sys_wifi_limited_signal_2", modRes.fwd(R.drawable.stat_sys_wifi_limited_signal_2));
			resparam.res.setReplacement(SystemUI, "drawable", "stat_sys_wifi_limited_signal_3", modRes.fwd(R.drawable.stat_sys_wifi_limited_signal_3));
			resparam.res.setReplacement(SystemUI, "drawable", "stat_sys_wifi_limited_signal_4", modRes.fwd(R.drawable.stat_sys_wifi_limited_signal_4));
			resparam.res.setReplacement(SystemUI, "drawable", "stat_sys_wifi_in", modRes.fwd(R.drawable.stat_sys_wifi_in));
			resparam.res.setReplacement(SystemUI, "drawable", "stat_sys_wifi_out", modRes.fwd(R.drawable.stat_sys_wifi_out));
			resparam.res.setReplacement(SystemUI, "drawable", "stat_sys_wifi_inout", modRes.fwd(R.drawable.stat_sys_wifi_inout));
			resparam.res.setReplacement(SystemUI, "drawable", "somc_sys_wifi_in", modRes.fwd(R.drawable.stat_sys_wifi_in));
			resparam.res.setReplacement(SystemUI, "drawable", "somc_sys_wifi_out", modRes.fwd(R.drawable.stat_sys_wifi_out));
			resparam.res.setReplacement(SystemUI, "drawable", "somc_sys_wifi_inout", modRes.fwd(R.drawable.stat_sys_wifi_inout));
		}
		if (pref.getBoolean("ToolsRowMod", false)) {
			resparam.res.hookLayout(SystemUI, "layout", "super_status_bar", new XC_LayoutInflated() {
	            @SuppressWarnings("deprecation")
				@Override
	            public void handleLayoutInflated(final LayoutInflatedParam liparam)
	                    throws Throwable {
	        		//クイックセッティングパネルを通知パネルへ移動
	                liparam.view.findViewById(liparam.res.getIdentifier("tabs", "id", "android")).setVisibility(View.GONE);
		            FrameLayout QS_tab = (FrameLayout) liparam.view.findViewById(liparam.res.getIdentifier("quick_settings_tab","id",SystemUI));
		            LinearLayout tools_rows = (LinearLayout) liparam.view.findViewById(liparam.res.getIdentifier("tools_rows","id",SystemUI));
		            QS_tab.removeView(tools_rows);
		            LinearLayout notification_tab = (LinearLayout) liparam.view.findViewById(liparam.res.getIdentifier("notifications_tab","id",SystemUI));
	                float density = liparam.res.getDisplayMetrics().density;
		            ImageView line = new ImageView(liparam.view.getContext());
		            line.setBackgroundColor(0x0ffffffff);
		            ImageView line2 = new ImageView(liparam.view.getContext());
		            line2.setBackgroundColor(0x0ffffffff);
		            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.FILL_PARENT, (int)(0.5f * density + 0.5f));
		            LayoutParams lp2 = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.FILL_PARENT, (int)(0.5f * density + 0.5f));
	                lp.setMargins(0, (int) (20.0f * density + 0.5f), 0, 0);
		            notification_tab.addView(line, 0, lp);
		            notification_tab.addView(tools_rows, 0);
		            notification_tab.addView(line2, 0, lp2);
		    		if (pref.getBoolean(modRes.getString(R.string.pref_image_key), false)) {
						resparam.res.setReplacement(SystemUI, "drawable", "somc_notification_panel_bg", new DrawableLoader(){
							@Override
							public Drawable newDrawable(XResources res,int id) throws Throwable {
								BitmapDrawable dr =null;
								try {
									InputStream is = new FileInputStream("/data/data/jp.toshi127k.android.xperiapatcher/files/bm.png");
									dr = (BitmapDrawable) Drawable.createFromStream(is, "bm.png");
									is.close();
									dr.setGravity(Gravity.FILL);
									
								} catch (Exception e) {
									XposedBridge.log(e);
								}
								return dr;
							}
						});
		    		}
	            }
			});
			//クイックセッティングパネルの表示設定
			int i = Integer.parseInt(pref.getString("ItemsInARow", "4"));
			resparam.res.setReplacement(SystemUI, "integer", "config_maxToolItemsInARow", i);
			float f = Float.parseFloat(pref.getString("RowHeight", "100.0"));
			resparam.res.setReplacement(SystemUI, "dimen", "notification_panel_tools_row_height", new DimensionReplacement(f,TypedValue.COMPLEX_UNIT_DIP));
		}
	}
}
