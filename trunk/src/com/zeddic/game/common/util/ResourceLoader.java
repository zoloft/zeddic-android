/*
 * Copyright (C) 2010 Geo Siege Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.zeddic.game.common.util;

import java.io.IOException;
import java.io.InputStream;

import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.graphics.Bitmap.Config;
import android.graphics.drawable.Drawable;
import android.media.AudioManager;
import android.media.SoundPool;
import android.util.Log;

public class ResourceLoader {
  
  public static final Bitmap.Config DEFAULT_BITMAP_CONFIG = Bitmap.Config.ARGB_8888;
  public static final Bitmap.Config FAST_BITMAP_CONFIG = Bitmap.Config.RGB_565;
  public static Resources r; 
  public static Paint paint;
  public static AssetManager a;
  public static Context c;
  
  public static void init(Context context) {
    c = context;
    r = context.getResources();
    a = context.getAssets();
    paint = new Paint();
  }
  
  public static String[] getAssetList(String path) {
    try {
      return a.list(path);
    } catch (IOException e) {
      return new String[0];
    }
  }
  
  public static Bitmap loadBitmap(int resourceId) {
    return loadBitmap(resourceId, DEFAULT_BITMAP_CONFIG);
  }
  
  public static Bitmap loadBitmap(int resourceId, Bitmap.Config config) {
    return loadBitmap(r.getDrawable(resourceId), config);
  }
  
  public static Bitmap loadBitmap(Drawable sprite, Config bitmapConfig) {
    int width = sprite.getIntrinsicWidth();
    int height = sprite.getIntrinsicHeight();
    Bitmap bitmap = Bitmap.createBitmap(width, height, bitmapConfig);

    Canvas canvas = new Canvas(bitmap);
    sprite.setBounds(0, 0, width, height);
    sprite.draw(canvas);
    
    return bitmap;
  }
  
  public static Typeface loadFont(String path) {
    return Typeface.createFromAsset(a, path);
  }
  
  public static InputStream loadAsset(String path) throws IOException {
    return c.getAssets().open(path);
  }
  
  public static SoundPool createSoundPool(int size) {
    return new SoundPool(size, AudioManager.STREAM_MUSIC, 1);
  }
  
  public static int loadSound(SoundPool pool, int resourceId) {
    try {
      return pool.load(c, resourceId, 1);
    } catch (Exception e) {
      Log.e(ResourceLoader.class.getName(), e.getMessage());
      return -1;
    }
  }
  
  public static int loadSound(SoundPool pool, String path) {
    try {
      AssetFileDescriptor file = a.openFd(path);
      return pool.load(file, 1);
    } catch (IOException e) {
      Log.e(ResourceLoader.class.getName(), "Error loading sound: " + path);
      return -1;
    }
  }
  
  public static void playSound(SoundPool pool, int soundID) {
    // Full volume, no looping, regular rate.
    playSound(pool, soundID, 0, 1);
  }
  
  public static void playSound(SoundPool pool, int soundID, int loop, float rate) {
    
    AudioManager mgr = (AudioManager)c.getSystemService(Context.AUDIO_SERVICE);  
    float streamVolumeCurrent = mgr.getStreamVolume(AudioManager.STREAM_MUSIC);  
    float streamVolumeMax = mgr.getStreamMaxVolume(AudioManager.STREAM_MUSIC);      
    float volume = streamVolumeCurrent / streamVolumeMax;  
    
    pool.play(soundID, volume, volume, 1, loop, rate);
  }
}