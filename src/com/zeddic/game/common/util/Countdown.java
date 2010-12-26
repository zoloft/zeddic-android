package com.zeddic.game.common.util;

/**
 * Handles a countdown timer for a specified number of milliseconds. The time
 * is tracked based on how much time it is TOLD that is passed, not by 
 * directly following any system time internally.
 * 
 * @author scott@zeddic.com (Scott Bailey)
 */
public class Countdown {

  /** True if the downdown is activly counting down. */
  public boolean counting = false;
  
  /** True if the downdown finished. */
  public boolean done = false;
  
  /** The total duration of the countdown. */
  public long duration;
  
  /** How many milliseconds are left in the countdown. */
  public long countdown;
  
  /**
   * Creates a new countdown of the specified duration. Defaults to off.
   */
  public Countdown(long duration) {
    this.duration = duration;
    reset();
  }
  
  /**
   * Updates the countdown with any passed time. Returns true if the countdown
   * reached its end.
   */
  public boolean update(long time) {
    if (!counting) {
      return done;
    }
     
    countdown = countdown - time;
    if (countdown <= 0) {
      done = true;
      counting = false;
    }
   
    return done;
  }
  
  /**
   * Returns true if the countdown has reached its end.
   */
  public boolean isDone() {
    return done;
  }
  
  /**
   * Returns true if the countdown is actively counting down.
   */
  public boolean isCounting() {
    return counting;
  }
  
  /**
   * Starts a countdown.
   */
  public void start() {
    counting = true;
  }
  
  /**
   * Pasues a countdown.
   */
  public void pause() {
    counting = false;
  }
  
  /**
   * Resets a countdown to its original state and clears any passed time.
   * Any active count is paused after the reset.
   */
  public void reset() {
    countdown = duration;
    counting = false;
    done = false;
  }
  
  /**
   * Rests a countdown and changes its total duration. After a reset, the
   * countdown must be resumed with start.
   */
  public void reset(long newDuration) {
    this.duration = newDuration;
    reset();
  }
  
  /**
   * Restarts counting from the starting value.
   */
  public void restart() {
    countdown = duration;
    counting = true;
    done = false;
  }
  
  /**
   * Returns a double representation of the percent complete. For example:
   * .5 for 50% complete.
   */
  public double getProgress() {
    return (double) (duration - countdown) / (double) duration;
  }
}
