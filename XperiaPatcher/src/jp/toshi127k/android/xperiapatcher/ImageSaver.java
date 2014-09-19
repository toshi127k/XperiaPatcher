package jp.toshi127k.android.xperiapatcher;

import java.io.InputStream;
import java.io.OutputStream;

import android.content.ContentResolver;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;

public class ImageSaver implements Runnable {
	PrefActivity2 main;
	Uri uri;
	Context context;
	Resources res;
	public ImageSaver(PrefActivity2 main, Uri uri) {
		this.main = main;
		this.uri = uri;
		this.context = main.getApplicationContext();
		res = context.getResources();
	}

	@SuppressWarnings("deprecation")
	@Override
	public void run() {
		try {
			main.setText(res.getString(R.string.create_bitmap));
			ContentResolver cr = context.getContentResolver();
			InputStream is;
			is = cr.openInputStream(uri);
			Bitmap bm = BitmapFactory.decodeStream(is);
			is.close();
			main.setText(res.getString(R.string.save_image));
			OutputStream os = context.openFileOutput("bm.png", Context.MODE_WORLD_READABLE);
			bm.compress(CompressFormat.PNG, 100, os);
			os.close();
			main.setText(res.getString(R.string.use_specified));
			BitmapDrawable dr = new BitmapDrawable(context.getResources(), bm);
			main.setImage(dr);
			main.setImageMod(true);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
