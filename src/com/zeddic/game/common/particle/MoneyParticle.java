package com.zeddic.game.common.particle;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

import com.zeddic.game.common.util.Polygon;
import com.zeddic.game.common.util.Polygon.PolygonBuilder;

public class MoneyParticle extends Particle {

  
  ////SETUP OBJECT SHAPE AND PAINT
  private static final Paint GREEN_PAINT;
  private static Polygon MONEY_SHAPE;

  static {
    GREEN_PAINT = new Paint();
    //GREEN_PAINT.setColor(Color.GREEN);
    GREEN_PAINT.setARGB(255, 90, 255, 0);
    GREEN_PAINT.setStyle(Paint.Style.FILL);
    //GREEN_PAINT.setStrokeWidth(3);
    // paint.setMaskFilter(new BlurMaskFilter(1, BlurMaskFilter.Blur.OUTER));
    // blurFilter = new BlurMaskFilter(18, BlurMaskFilter.Blur.NORMAL);

    MONEY_SHAPE = new PolygonBuilder()
        .add(0, -3)
        .add(3, 0)
        .add(0, 3)
        .add(-3, 0)
        .build();
  }
  
  
  /**
   * Creates a new particle at the origin with default values.
   */
  public MoneyParticle() {
    this(0, 0);
  }
  
  /**
   * Creates a new particle at the given position with default values.
   */
  public MoneyParticle(float x, float y) {
    super(x, y);

    paint = new Paint();
    paint.setColor(Color.GREEN);
    paint.setStrokeWidth(4);
  }
  
  public void draw(Canvas canvas) {
    paint.setAlpha((int) alpha);
    
    
    canvas.save();

    canvas.translate(x, y);
    
    canvas.drawPath(MONEY_SHAPE.path, GREEN_PAINT);

    canvas.restore();
    
    // Draw the particle as a line from its current position to where it
    // used to be. The length of the line is based on its current velocity.
    //c.drawLine(x, y, x + -scaledVelocity.x, y + -scaledVelocity.y, paint);
    //c.drawCircle(x, y, scale, paint);
  }
  
}
