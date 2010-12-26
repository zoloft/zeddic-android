package com.zeddic.game.common.particle;

import android.graphics.Canvas;
import android.graphics.Paint;

public class PixelParticle extends Particle {

  private static final Paint DEFAULT_PAINT = BasicParticleOptions.getRandomColor();

  public PixelParticle() {
    this(0, 0);
  }
  
  @Override
  public void onEmit(ParticleData data) {
    if (data != null) {
      this.paint = ((BasicParticleOptions) data).color;
    }
  }
  
  /**
   * Creates a new particle at the given position with default values.
   */
  public PixelParticle(float x, float y) {
    super(x, y);

    paint = DEFAULT_PAINT;
  }
  
  public void draw(Canvas canvas) {
    paint.setAlpha((int) alpha);
    paint.setStrokeWidth(1);
    //canvas.save();
    //canvas.translate(x, y); 
    canvas.drawPoint(x, y, paint);
    //canvas.restore();
  }
}
