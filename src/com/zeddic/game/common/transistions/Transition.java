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

package com.zeddic.game.common.transistions;

public class Transition {

  
  private float start;
  private float end;
  private long milliseconds;
  private int type;
  
  public boolean finished;
  
  private long passedTime;
  private float progress;
  
  private boolean autoReset;
  private boolean autoReverse;
  
  
  public Transition(float start, float end, long milliseconds, int type) {
    this.start = start;
    this.end = end;
    this.milliseconds = milliseconds;
    this.type = type;
    
    this.autoReset = false;
    this.autoReverse = false;
    
    reset();
  }
  
  public void setAutoReset(boolean autoReset) {
    this.autoReset = autoReset;
  }
  
  public void setAutoReverse(boolean autoReverse) {
    this.autoReverse = autoReverse;
  }
  
  public void reset() {
    passedTime = 0;
    finished = false;
    updateProgress();
  }
  
  public void reverse() {
    float temp = start;
    start = end;
    end = temp;
  }
  
  public void end() {
    finished = true;
    updateProgress();
  }
  
  public float get() {
    return progress;
  }
  
  public float update(long time) {
    passedTime += time;
    if (passedTime >= milliseconds) {
      finished = true;
      
      if (autoReset || autoReverse) {
        reset();
        if (autoReverse) {
          reverse();
        }
      }
    }
    
    updateProgress();
    
    
    return progress;
  }
  
  public void updateProgress() {
    if (finished) {
      progress = end;
    }
    
    double ratio = (double) passedTime / (double) milliseconds;
    if (ratio >= 1) {
      ratio = 1;
      finished = true;
    }
    
    double percentDone = Transitions.getProgress(type, ratio);
    progress = start + (float) percentDone * (end - start);
  }
}
