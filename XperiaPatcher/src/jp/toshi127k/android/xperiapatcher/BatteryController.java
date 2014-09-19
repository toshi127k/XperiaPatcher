package jp.toshi127k.android.xperiapatcher;

//import android.util.*;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Resources;
import android.os.BatteryManager;
import android.util.DisplayMetrics;
import android.widget.ImageView;

public class BatteryController extends BroadcastReceiver  {
    private ImageView mIconView;
    private Resources mResources;
    private int density;
    
    public final static int[][] resBattery = {
    		{R.drawable.stat_sys_battery_show,R.drawable.stat_sys_battery,R.drawable.stat_sys_battery_charge},
    		{R.drawable.stat_sys_battery_vemana2000_show,R.drawable.stat_sys_battery_vemana2000,R.drawable.stat_sys_battery_vemana2000_charge}
    };

    public BatteryController(Context context) {
        this(context, context.getResources());
    }

    public BatteryController(Context context, Resources resources) {
    	mResources = resources;
        DisplayMetrics metrics = context.getResources().getDisplayMetrics();
        density =metrics.densityDpi;
        IntentFilter filter = new IntentFilter();
        filter.addAction(Intent.ACTION_BATTERY_CHANGED);
        context.registerReceiver(this, filter);
    }

    public void setIconView(ImageView v) {
        mIconView = v;
    }

    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        if (action.equals(Intent.ACTION_BATTERY_CHANGED)) {
            int level = intent.getIntExtra(BatteryManager.EXTRA_LEVEL, 0);
            boolean plugged = intent.getIntExtra(BatteryManager.EXTRA_PLUGGED, 0) != 0;
            int icon = plugged ? resBattery[0][2] : resBattery[0][1];
            mIconView.setImageDrawable(mResources.getDrawableForDensity(icon,density));
            mIconView.setImageLevel(level);
        }
    }
}
