package org.twoproject.drake;

import android.app.WallpaperManager;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.IBinder;

public class DrakeWP {
	
	private WallpaperManager mWallpaperManager;
	private Workspace that;
	private Boolean fakeIt = false;
	
	public DrakeWP(Context context, Workspace workspace) {
		this.that = workspace;
		mWallpaperManager = WallpaperManager.getInstance(context);
		if(mWallpaperManager == null){
			this.fakeIt = true;
		}
	}

	public void setWallpaperOffsetSteps(float f, int i) {
		// TODO Auto-generated method stub
		if(this.fakeIt) return;
		
		mWallpaperManager.setWallpaperOffsetSteps(f, i);
	}

	public void setWallpaperOffsets(IBinder windowToken, float f, int i) {
		// TODO Auto-generated method stub
		if(this.fakeIt) return;
		
		mWallpaperManager.setWallpaperOffsets( windowToken,  f,  i);
	}

	public void sendWallpaperCommand(IBinder windowToken, String string, int i,
			int j, int k, Bundle object) {
		// TODO Auto-generated method stub
		if(this.fakeIt) return;
		
		mWallpaperManager.sendWallpaperCommand( windowToken,  string,  i,
				 j,  k,  object);
	}

	public Drawable getDrawable() {
		// TODO Auto-generated method stub
		if(this.fakeIt){
			Bitmap buff = BitmapFactory.decodeResource(this.that.getResources(), R.drawable.ic_launcher_application);
			buff = Bitmap.createBitmap(createColors(), 0, STRIDE, WIDTH, HEIGHT,
                    Bitmap.Config.ARGB_8888);

			return new BitmapDrawable(buff);//new BitmapDrawable(R.drawable.ic_launcher_application);
		}
		return mWallpaperManager.getDrawable();
	}

	public Object getWallpaperInfo() {
		// TODO Auto-generated method stub
		if(this.fakeIt) return null;
		
		return mWallpaperManager.getWallpaperInfo();
	}

	/*****/
    private static final int WIDTH = 1280;
    private static final int HEIGHT = 600;
    private static final int STRIDE = 1280;   // must be >= WIDTH

    private static int[] createColors() {
        int[] colors = new int[STRIDE * HEIGHT];
        for (int y = 0; y < HEIGHT; y++) {
            for (int x = 0; x < WIDTH; x++) {
                int r = x * 255 / (WIDTH - 1);
                int g = y * 255 / (HEIGHT - 1);
                int b = 255 - Math.min(r, g);
                int a = Math.max(r, g);
                colors[y * STRIDE + x] = (a << 24) | (r << 16) | (g << 8) | b;
            }
        }
        return colors;
    }
}
