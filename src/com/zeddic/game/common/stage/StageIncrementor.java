package com.zeddic.game.common.stage;

import java.util.ArrayList;
import java.util.Iterator;

public class StageIncrementor<T extends Stage> {

  ArrayList<T> stages;
  Iterator<T> iterator;
  T next;
  int value;
  
  public StageIncrementor() {
    stages = new ArrayList<T>();
    reset();
  }
  
  public StageIncrementor(ArrayList<T> stages) {
    this.stages = stages;
    reset();
  }
  
  public void add(T stage) {
    stages.add(stage);
    reset();
  }
  
  public void reset() {
    value = 0;
    next = null;
    iterator = stages.iterator();
    
    if (iterator.hasNext()) {
      next = iterator.next();      
    }
  }
  
  public void add() {
    add(1);
  }
  
  public void add(int add) {
    
    value += add;
    
    if (next == null) {
      return;
    }
    
    while (next != null && value >= next.getValue()) {
      next.onTrigger();
      if (iterator.hasNext()) {
        next = iterator.next();
      } else {
        next = null;
      }
    }
  }
  
  public boolean isIncrementing() {
    return value > 0;
  }
  
  public int getValue() {
    return value;
  }
  
  public static class Builder<T extends Stage> {
    
    ArrayList<T> items = new ArrayList<T>();
    
    public Builder<T> add(T item) {
      items.add(item);
      return this;
    }
    
    public StageIncrementor<T> build() {
      return new StageIncrementor<T>(items);
    }
  }
  
}
