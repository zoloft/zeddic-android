package com.zeddic.game.common.transistions;

/**
 * A utility method that converts a value in one range into a value of another
 * range. For example: a value of 5 in the range [0-10] would correspond to the
 * value of 50 in the range [0-100]. Mappings may be done linearly or via 
 * any other transition type in {@link Transitions}.
 * 
 * <p>This can be useful when creating relationships between two distinct
 * elements in a game. For example: the rate of exhaust coming from a tailpipe
 * may be linearly linked to the speed of a car. The faster the car, the
 * greater the exhaust. In this case a range converter could be used:
 * 
 * <code>
 * converter = new RangeConverter(
 *    new Range(MIN_CAR_SPEED, MAX_CAR_SPEED), 
 *    new Range(MIN_EXHAST_RATE, MAX_EXHAST_RATE));      
 * 
 * currentExhastRate = converter.convert(currentSpeed);
 * </code>
 */
public class RangeConverter {
  private Range from;
  private Range to;
  private int transition = Transitions.LINEAR;
  
  public RangeConverter(Range from, Range to) {
    this(from, to, Transitions.LINEAR);
  }
  
  public RangeConverter(Range from, Range to, int transition) {
    this.from = from;
    this.to = to;
    this.transition = transition;
  }
  
  public float convert(float value) {
    return from.convertValueToOtherRange(value, to, transition);
  }
}
