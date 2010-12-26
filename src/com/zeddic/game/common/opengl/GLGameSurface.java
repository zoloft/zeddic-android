package com.zeddic.game.common.opengl;

import android.content.Context;
import android.opengl.GLSurfaceView;

import com.zeddic.game.common.GameSurface.GameSurfaceEventListener;

public class GLGameSurface extends GLSurfaceView {
  
  GameSurfaceEventListener listener;
  Renderer renderer; 
  
  public GLGameSurface(Context context) {
    super(context);
    //mRenderer = new ClearRenderer();
    //setRenderer(mRenderer);
  }
  
  public void setRenderer(Renderer renderer) {
    super.setRenderer(renderer);
  }
  
  /**
   * Sets a listener.
   */
  public void setEventListener(GameSurfaceEventListener listener) {
    this.listener = listener;
  }
}