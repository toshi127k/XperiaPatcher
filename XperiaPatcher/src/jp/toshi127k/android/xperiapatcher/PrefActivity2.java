package jp.toshi127k.android.xperiapatcher;

import java.io.InputStream;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import android.content.Intent;
import android.content.SharedPreferences.Editor;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.preference.CheckBoxPreference;
import android.preference.EditTextPreference;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.util.DisplayMetrics;
import android.view.View;

public class PrefActivity2 extends PreferenceActivity {
	private static final int REQUEST_GALLARY = 11; 
	private Preference NotifImage;
	private CheckBoxPreference BatteryMod;
	private CheckBoxPreference ToolsRowMod;
	private EditTextPreference ItemInARow;
	private EditTextPreference RowHeight;
	Handler handler;
	ExecutorService executor;
	Runnable task;
	@SuppressWarnings("rawtypes")
	Future pending;
//	Context context;

	@SuppressWarnings("deprecation")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		//setTheme(R.style.AppBaseTheme);
		super.onCreate(savedInstanceState);
		setTheme(R.style.AppBaseTheme);
		getPreferenceManager().setSharedPreferencesMode(MODE_WORLD_READABLE);
		addPreferencesFromResource(R.xml.preference);
		handler = new Handler();
		executor = Executors.newSingleThreadExecutor();
//		context = this;
		BatteryMod =  (CheckBoxPreference) findPreference(getString(R.string.pref_batterymod_key));
		ToolsRowMod = (CheckBoxPreference) findPreference(getString(R.string.Pref_toolsrowmod_key));
		ItemInARow = (EditTextPreference) findPreference(getString(R.string.pref_itemsinarow_key));
		RowHeight = (EditTextPreference) findPreference(getString(R.string.pref_rowheight_key));
		NotifImage = (Preference) findPreference(getString(R.string.pref_image_key));
		Boolean s = NotifImage.getSharedPreferences().getBoolean(getString(R.string.pref_image_key), false);
		if (s) {
			NotifImage.setSummary(getString(R.string.use_specified));
			try {
				InputStream is = openFileInput("bm.png");
				Drawable d = Drawable.createFromStream(is, "bm.png");
				is.close();
				NotifImage.setIcon(d);
			} catch (Exception e) {
				// TODO 自動生成された catch ブロック
				e.printStackTrace();
			}
		} else {
			NotifImage.setSummary(getString(R.string.not_modify));
		}
		if (BatteryMod.isChecked()){
	        DisplayMetrics metrics = getResources().getDisplayMetrics();
	        int density =metrics.densityDpi;
			Drawable icon = getResources().getDrawableForDensity(BatteryController.resBattery[0][0],density);
			BatteryMod.setIcon(icon);
		}
		if (ToolsRowMod.isChecked()) {
			NotifModEnable(true);
		} else {
			NotifModEnable(false);
		}
		ItemInARow.setSummary(String.format(getString(R.string.pref_itemsinarow_summary), ItemInARow.getText()));
		RowHeight.setSummary(String.format(getString(R.string.pref_rowheight_summary), RowHeight.getText()));
		
		ToolsRowMod.setOnPreferenceChangeListener(new CheckBoxPreference.OnPreferenceChangeListener(){
			@Override
			public boolean onPreferenceChange(Preference preference,Object newValue) {
				if (newValue.equals(true)) {
					NotifModEnable(true);
				} else {
					NotifModEnable(false);
				}
				return true;
			}
		});
		ItemInARow.setOnPreferenceChangeListener(new EditTextPreference.OnPreferenceChangeListener(){
			@Override
			public boolean onPreferenceChange(Preference preference,Object newValue) {
				preference.setSummary(String.format(getString(R.string.pref_itemsinarow_summary), newValue.toString()));
				return true;
			}
		});
		RowHeight.setOnPreferenceChangeListener(new EditTextPreference.OnPreferenceChangeListener(){
			@Override
			public boolean onPreferenceChange(Preference preference,Object newValue) {
				preference.setSummary(String.format(getString(R.string.pref_rowheight_summary), newValue.toString()));
				return true;
			}
		});
		NotifImage.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener(){
			@Override
			public boolean onPreferenceClick(Preference preference) {
				String key = getString(R.string.pref_image_key);
				Boolean s = NotifImage.getSharedPreferences().getBoolean(key, false);
				if (s) {
					Editor editor = NotifImage.getSharedPreferences().edit();
					editor.putBoolean(key, false);
					editor.apply();
					NotifImage.setIcon(null);
					NotifImage.setSummary(getString(R.string.not_modify));
				} else {
					Intent intent =new Intent();
					intent.setType("image/*");
					intent.setAction(Intent.ACTION_GET_CONTENT);
					startActivityForResult(intent,REQUEST_GALLARY);
				}
				return true;
			}
		});
	}
	@Override
	public void onActivityResult(int requestCode,int resultCode,final Intent data){
		if (requestCode == REQUEST_GALLARY && resultCode == RESULT_OK){
			try {
				task = new Runnable() {
					@Override
					public void run() {
						ImageSaver saver = new ImageSaver(PrefActivity2.this, data.getData());
						pending = executor.submit(saver);
					}
				};
				handler.removeCallbacks(task);
				handler.post(task);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	public void setText(final String text) {
		handler.post(new Runnable(){
			@Override
			public void run() {
				NotifImage.setSummary(text);
			}
		});
	}

	public void setImageMod(final Boolean flag) {
		handler.post(new Runnable(){
			@Override
			public void run() {
				Editor editor = NotifImage.getSharedPreferences().edit();
				editor.putBoolean(getString(R.string.pref_image_key), flag);
				editor.apply();
			}
		});
	}

	public void setImage(final Drawable dr) {
		handler.post(new Runnable(){
			@Override
			public void run() {
				NotifImage.setIcon(dr);
			}
		});
	}

	private void NotifModEnable(Boolean status) {
		ItemInARow.setEnabled(status);
		RowHeight.setEnabled(status);
		NotifImage.setEnabled(status);
	}
}
