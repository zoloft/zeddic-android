/*
 * Copyright (C) 2010 Zeddic Game Library
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

import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff.Mode;
import android.os.Handler;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.SurfaceHolder;

import com.zeddic.game.common.GameSurface.GameSurfaceEventListener;

/**
 * Manages a background thread to update and render a {@link Game}. The updater
 * acts as a middle man between the the canvas surface and the game, proxying
 * events from the canvas. 
 * 
 * <p>Also provides a mechanism for the main android UI thread to register to
 * receive events from the background thread.
 * 
 * <p>An example of using the updater and setting up a game:
 * 
 * <code>
 * // Create the drawing Surface;
 * // May optionally be placed inside the xml file.
 * surface = new GameSurface(this);
 * surface.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT));
 *  
 * // Create the game.
 * game = new MyGame();
 * 
 * // Create the updater to coordinate the background update/render thread.
 * updater = new Updater(surface);
 * updater.setGame(game);
 * updater.showFps(true);
 * 
 * // Add the drawing surface to the screen (may be skipped if placed inline in xml)
 * LinearLayout container = (LinearLayout)findViewById(R.id.my_game_layout);
 * container.addView(surface);
 *
 * // Start the background thread which will do our setup.
 * updater.start();
 * </code>
 * 
 * @author scott@zeddic.com
 */
public class Updater implements GameSurfaceEventListener  {

  // The game to render.
  public Game game = null;
  
  // The target frames per second.
  private static final int UPDATES_PER_SECOND = 60;
  private final Timer timer;
  private final GameSurface gameSurface;
  private final SurfaceHolder surfaceHolder; 
  private boolean running = false;
  private boolean showFps = false;
  
  private UpdateTask updateTask;
  private long timestamp;

  private Paint paint;
  private int frameCount = 0;
  private int fps = 0;
  private long fpsTimestamp;
  
  private boolean screenSizeKnown = false;
  private int screenWidth;
  private int screenHeight;
  
  private Map<Integer, Handler> eventHandlers;
  
  public Handler handler;
  
  /**
   * Creates a new updater to manage draws and updates to the provided
   * GameSurface.
   */
  public Updater(GameSurface surface) {
    this.timer = new Timer();
    this.timestamp = System.currentTimeMillis();
    
    this.eventHandlers = new HashMap<Integer, Handler>();
    
    this.paint = new Paint();
    this.paint.setColor(Color.WHITE); 
    
    // Get a handle to the holder so we can draw to the surface.
    this.gameSurface = surface;
    this.surfaceHolder = surface.getHolder();

    // Register to receive various events on the underlying surface. These
    // can then be piped to the game.
    gameSurface.setEventListener(this);
  }
  
  /**
   * Starts running the updater.
   */
  public void start() {
    if (running) {
      return;
    }
    
    running = true;
    long updateInterval = 1000 / UPDATES_PER_SECOND;
    updateTask = new UpdateTask();
    timer.scheduleAtFixedRate(updateTask, 0, updateInterval);
  }
  
  /**
   * Stops the updater.
   */
  public void stop() {
    running = false;
    updateTask.cancel();
    timer.purge();
  }
  
  /**
   * Pauses the updater.
   */
  public void pause() {
    stop();
  }
  
  /**
   * Updates the game. Triggered via the updater.
   */
  private void update() {
    if (game == null) {
      return;
    }
    
    // The game is only initialized after the screen dimensions are known.
    // Otherwise the game may not be able to allocate and setup correctly.
    if (!game.initialized && screenSizeKnown) {
      game.init(screenWidth, screenHeight);
    }
    
    long now = System.currentTimeMillis();
    long delta = now - timestamp;
      
    game.update(delta);
    
    timestamp = now;
  }
  
  /**
   * Obtains the game canvas and draws the game.
   */
  private void draw() {
    if (surfaceHolder == null || !gameSurface.isSurfaceReady()) {
      // Can't draw yet. The surface is still being created.
      return;
    }
        
    if (game == null || !game.initialized) {
      // Can't draw yet. The game isn't available.
      return;
    }
    
    Canvas c = null;
    try {
      c = surfaceHolder.lockCanvas(null);
      
      synchronized (surfaceHolder) {
        draw(c);
      }
    } catch (Exception e) {
      Log.i(Updater.class.getName(), "Rendering Exception", e);
    } finally {
      // do this in a finally so that if an exception is thrown
      // during the above, we don't leave the Surface in an
      // inconsistent state
      if (c != null) {
        surfaceHolder.unlockCanvasAndPost(c);
      }
    }
  }
  
  /**
   * Draws the game.
   */
  private void draw(Canvas canvas) {
    
    canvas.drawColor(Color.BLACK, Mode.SRC_IN);
    
    game.draw(canvas);
    
    if (showFps) {
      frameCount++;
      long now = System.currentTimeMillis();
      if ( now - fpsTimestamp > 1000) {
         fps = frameCount / (int)((now - fpsTimestamp) / 1000);
         fpsTimestamp = now;
         frameCount = 0;
      }
      canvas.save();
      canvas.scale(2, 2);
      canvas.drawText(Integer.toString(fps), 20, 20, paint);
      canvas.restore();
      
    }
    canvas.restore();
  }
  
  /**
   * Registers to recieve an event in the UI thread when it is published
   * by the background game thread.
   */
  public void addEventHandler(int eventId, Handler handler) {
    eventHandlers.put(eventId, handler);
  }

  /**
   * Unregisters to receive an event.
   */
  public void removeEventHandler(int eventId) {
    eventHandlers.remove(eventId);
  }
  
  /**
   * Unregisters all event handlers.
   */
  public void clearEventHandlers() {
    eventHandlers.clear();
  }
  
  /**
   * Triggers a specific event. This can be used to pass events in the game
   * thread (such as deaths) to the android ui thread so it can perform
   * responses (such as showing native popup boxes).
   * @param eventId
   */
  public void triggerEventHandler(int eventId) {
    if (!running) {
      Log.d(Updater.class.getName(), "Not triggering event. The game is not running");
      return;
    }
    
    if (!eventHandlers.containsKey(eventId)) {
      Log.e(Updater.class.getName(), "Event Handler for " + eventId + " not found.");
    }
    Handler handler = eventHandlers.get(eventId);
    
    if (handler == null) {
      return;
    }
    
    handler.sendEmptyMessage(0);
  }
  
  /**
   * Sets the game that should be updated and drawn.
   */
  public void setGame(Game mode) {
    this.game = mode;
    if (mode != null) {
      this.game.updater = this;
      
      // If the surface is already good to go, notify the game.
      if (gameSurface.isSurfaceReady()) {
        onSurfaceCreated();
      }
    }
  }
  
  /**
   * Sets whether the current frames per second should be rendered in the top
   * left. Defaults to false.
   */
  public void showFps(boolean state) {
    this.showFps = state;
  }
  
  /**
   * An updater that tries to update and draw the game on a timed basis. 
   */
  private class UpdateTask extends TimerTask {
    @Override
    public void run() {
      try {
        update();
        draw();
      } catch (Exception e) {
        Log.i("zeddic", "Update Exception", e);
      }
    }    
  }
  
  /// IMPLEMENTS GameSurfaceEventListener
  // Pipe events from the rendering surface to the active game.
  
  public boolean onTouchEvent(MotionEvent e) {
    return game != null && game.initialized ? game.onTouchEvent(e) : false;
  }
  
  public boolean onKeyDown(int key, KeyEvent e) {
    return game != null && game.initialized ? game.onKeyDown(key, e) : false;
  }
  
  public boolean onKeyUp(int key, KeyEvent e) {    
    return game != null && game.initialized ? game.onKeyUp(key, e) : false;
  }
  
  public boolean onTrackballEvent(MotionEvent e) {
    return game != null && game.initialized ? game.onTrackballEvent(e) : false;
  }
  
  public void onFocusChangedEvent(boolean hasFocus) {
    if (game != null) {
      game.onFocusChangedEvent(hasFocus);
    }
  }
  
  public void onLayoutChangedEvent(boolean changed, int left, int top, int right, int bottom) {
    screenWidth = right - left;
    screenHeight = bottom -top;
    screenSizeKnown = true;
    
    if (game != null) {
      game.onLayoutChangedEvent(changed, left, top, right, bottom);
    }
  }

  public void onSurfaceCreated() {
    if (game != null) {
      game.onSurfaceReady();
    }
  }

  public void onSurfaceDestroyed() {
    stop();
  }
}
