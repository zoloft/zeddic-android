package com.zeddic.game.common.transistions;

/**
 * Represents a maximum and minimum range of values.
 * 
 * @author baileys
 *
 */
public class Range {

  /** The minimum bound. */
  private float min;
  
  /** The maximum bound. */
  private float max;

  public Range(float min, float max) {
    this.min = min;
    this.max = max;
  }
  
  /**
   * Given a progress level, returns the value associated with that progress
   * within the range.
   */
  public float getValue(float progress) {
    return getValue(progress, Transitions.LINEAR);
  }
  
  public float getValue(float progress, int transitionType) {
    progress = (float) Transitions.getProgress(transitionType, progress);
    return min + progress * (max - min);
  }
  
  /**
   * Converts a value to a percent [0-1], representing where it lies in the
   * range. For example: in the range 5-15, a value of 10 would be 50% [.5].
   * Enforces a maximum and minimum bound of 1 and 0.
   */
  public float getProgress(float value) {
    return getProgress(value, min, max);
  }
  
  /**
   * Converts a value to a percent range using the given transition type.
   */
  public float getProgress(float value, int transitionType) {
    return (float) Transitions.getProgress(transitionType, getProgress(value, min, max));
  }
  
  /**
   * Converts a value in a this range to a corresponding value in another range.
   * For example: in the range 0-10: 5 would be .5. This would correspond to
   * 50 in the range 0-100.
   */
  public float convertValueToOtherRange(float value, Range range) {
    return convert(value, min, max, range.min, range.max);
  }
  
  /**
   * Converts a value in this range to a corresponding value in another range,
   * applying the given transition type in to the destination range.
   */
  public float convertValueToOtherRange(float value, Range range, int transitionType) {
    return (float) Transitions.getProgress(
        transitionType,
        convert(value, min, max, range.min, range.max));
  }
  
  public static final float getProgress(float value, float min, float max) {
    return Math.max(0, Math.min(1, (value - min) / (max - min)));
  }
  
  public static final float convert(
      float value,
      float valueMin,
      float valueMax,
      float resultMin,
      float resultMax) {
    float progress = getProgress(value, valueMin, valueMax);
    return (progress * (resultMax - resultMin) + resultMin);
  }
}
