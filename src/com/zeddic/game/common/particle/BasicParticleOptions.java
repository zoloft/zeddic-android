package com.zeddic.game.common.particle;

import android.graphics.Paint;

import com.zeddic.game.common.util.RandomUtil;

public class BasicParticleOptions extends ParticleData {

  private static final int MAX_RANDOM_COLORS = 5;
  private static final Paint[] RANDOM_COLORS = new Paint[MAX_RANDOM_COLORS];
  private static final float DEFAULT_STROKE_WIDTH = 2;
  private static final Paint PAINT_TEMPLATE; 
  static {
    PAINT_TEMPLATE = new Paint();
    PAINT_TEMPLATE.setStyle(Paint.Style.STROKE);
    PAINT_TEMPLATE.setStrokeWidth(DEFAULT_STROKE_WIDTH);
    
    RANDOM_COLORS[0] = createPaint(207, 12, 36);
    RANDOM_COLORS[1] = createPaint(255, 196, 0);
    RANDOM_COLORS[2] = createPaint(255, 120, 120);
    RANDOM_COLORS[3] = createPaint(155, 0, 255);
    RANDOM_COLORS[4] = createPaint(85, 255, 0);
  }
  
  public Paint color;
  public float particleWidth = DEFAULT_STROKE_WIDTH;
  
  public BasicParticleOptions() {
    color = getRandomColor();
  }
  
  public BasicParticleOptions(Paint color) {
    this.color = color;
  }
  
  public BasicParticleOptions(Paint color, float particleWidth) {
    this.color = color;
    this.particleWidth = particleWidth;
  }
  
  public static Paint getRandomColor() {
    return RANDOM_COLORS[RandomUtil.nextInt(MAX_RANDOM_COLORS)];
  }
  
  public static Paint createPaint(int r, int g, int b) {
    Paint paint = new Paint(PAINT_TEMPLATE);
    paint.setARGB(255, r, g, b);
    return paint;
  }
}
