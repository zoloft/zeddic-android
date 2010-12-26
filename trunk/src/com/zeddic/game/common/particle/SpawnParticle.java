package com.zeddic.game.common.particle;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

import com.zeddic.game.common.util.Polygon;
import com.zeddic.game.common.util.Polygon.PolygonBuilder;

public class SpawnParticle extends Particle {

  
  ////SETUP OBJECT SHAPE AND PAINT
  private static final Paint PAINT;
  private static Polygon SHAPE;

  static {
    PAINT = new Paint();
    PAINT.setColor(Color.RED);
    PAINT.setStyle(Paint.Style.FILL);
    //GREEN_PAINT.setStrokeWidth(3);
    // paint.setMaskFilter(new BlurMaskFilter(1, BlurMaskFilter.Blur.OUTER));
    // blurFilter = new BlurMaskFilter(18, BlurMaskFilter.Blur.NORMAL);

    SHAPE = new PolygonBuilder()
        .add(0, -3)
        .add(3, 0)
        .add(0, 3)
        .add(-3, 0)
        .build();
  }
  
  
  /**
   * Creates a new particle at the origin with default values.
   */
  public SpawnParticle() {
    this(0, 0);
  }
  
  /**
   * Creates a new particle at the given position with default values.
   */
  public SpawnParticle(float x, float y) {
    super(x, y);

    /*paint = new Paint();
    paint.setColor(Color.GREEN);
    paint.setStrokeWidth(4); */
  }
  
  public void draw(Canvas canvas) {
    //paint.setAlpha((int) alpha);
    
    canvas.save();

    canvas.translate(x, y);
    
    canvas.drawPath(SHAPE.path, PAINT);

    canvas.restore();
  }
  
}
