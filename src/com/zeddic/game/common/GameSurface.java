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

package com.zeddic.game.common;

import android.content.Context;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

public class GameSurface extends SurfaceView implements SurfaceHolder.Callback {

  private GameSurfaceEventListener listener = null;
  private boolean surfaceReady = false;
  
  public GameSurface(Context context) {
    super(context);
    
    this.setFocusableInTouchMode(true);
    getHolder().setKeepScreenOn(true);
    getHolder().addCallback(this);
  }
  
  public void setEventListener(GameSurfaceEventListener listener) {
    this.listener = listener;
  }
  
  public boolean isSurfaceReady() {
    return surfaceReady;
  }
  
  
  @Override
  public boolean onTouchEvent(MotionEvent e) {
    return listener != null ? listener.onTouchEvent(e) : false;
  } 
  
  @Override
  public boolean onTrackballEvent(MotionEvent e) {
    return listener != null ? listener.onTrackballEvent(e) : false;
  }

  @Override
  public boolean onKeyDown(int key, KeyEvent e) {
    return listener != null ? listener.onKeyDown(key, e) : false;
  }
  
  @Override
  public boolean onKeyUp(int key, KeyEvent e) {
    return listener != null ? listener.onKeyUp(key, e) : false;
  }
  
  @Override
  public void onWindowFocusChanged(boolean hasFocus) {
    if (listener != null) {
      listener.onFocusChangedEvent(hasFocus);
    }
  }

  @Override
  public void onLayout(boolean changed, int left, int top, int right, int bottom) {
    Log.i(GameSurface.class.getName(), String.format("Layout changed to: %d x %d", right - left, bottom - top));
    if (listener != null) {
      listener.onLayoutChangedEvent(changed, left, top, right, bottom);
    }
  }
  
  // IMPLEMENTS SurfaceHolder.Callback
  
  public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
    // Do nothing.
  }

  public void surfaceCreated(SurfaceHolder holder) {
    surfaceReady = true;
    Log.d(GameSurface.class.getName(), "Game surface was created");
    if (listener != null) {
      listener.onSurfaceCreated();
    }
  }

  public void surfaceDestroyed(SurfaceHolder holder) {
    Log.d(GameSurface.class.getName(), "Game surface was destroyed");
    if (listener != null) {
      listener.onSurfaceDestroyed();
    }
  }
  

  public static interface GameSurfaceEventListener {
    void onSurfaceCreated();
    
    void onSurfaceDestroyed();
    
    boolean onTouchEvent(MotionEvent e);
    
    boolean onKeyDown(int key, KeyEvent e);
    
    boolean onKeyUp(int key, KeyEvent e);
    
    boolean onTrackballEvent(MotionEvent e);
    
    void onFocusChangedEvent(boolean hasFocus);
    
    void onLayoutChangedEvent(boolean changed, int left, int top, int right, int bottom);
  }
  
}
