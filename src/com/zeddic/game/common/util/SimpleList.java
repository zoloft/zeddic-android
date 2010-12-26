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

package com.zeddic.game.common.util;

import java.lang.reflect.Array;

import android.util.Log;

public class SimpleList<T> {
  
  private static int INITIAL_SPOT_CAPACITY = 40;
  private static int CAPACITY_GROWTH_RATE = 2;

  /**
   * The objects in this spot. Note: purposefully using a primative array
   * here for performance reasons, even though this means certain calculations
   * need to be done manually.
   */
  public T[] items;
  
  Class<?> type;
  
  /**
   * The number of objects in this spot.
   */
  public int size;
  
  public SimpleList(Class<?> type) {
    this(type, INITIAL_SPOT_CAPACITY);
  }
  
  public SimpleList(Class<?> type, int initialSize) {
    this.type = type;
    this.items = makeArray(initialSize);
  }
  
  /**
   * Adds a new object to this grid position.
   */
  public void add(T object) {
    
    if (contains(object)) 
      return;
    
    if (size == items.length) {
      grow();
    }
    
    items[size] = object;
    size++;
  }
  
  public boolean contains(T object) {
    for (int i = 0 ; i < size ; i++) {
      if (items[i] == object) {
        return true;
      }
    }
    return false;
  }
  
  /**
   * Removes an object from the grid spot.
   */
  public boolean remove(T object) {
    for (int i = 0 ; i < size ; i++) {
      if (items[i] == object) {
        for ( int j = i ; j < size - 1 ; j++) {
          items[j] = items[j + 1];
        }
        size--;
        return true;
      }
    }
    return false;
  }
  
  /**
   * Clears the list. Note that this doesn't resize the underlying datastructure
   * and the memory will still be used. This just resets future entries to
   * overwrite the old data.
   */
  public void clear() {
    size = 0;
  }
  
  /**
   * Clears the list, resets it to its original size, and frees any memory used
   * originally.
   */
  public void reset() {
    items = makeArray(INITIAL_SPOT_CAPACITY);
    size = 0;
  }
  
  @SuppressWarnings("unchecked")
  private T[] makeArray(int size) {
    //return (T[]) new Object[size];
    return (T[]) Array.newInstance(type, size);
    //return (T[]) Array.newInstance(classType, size);
  }
  
  /**
   * Grows the spots internal array for holding objects.
   */
  private void grow() {
    
    int newSize = items.length * CAPACITY_GROWTH_RATE;
    
    // Due to the costs of garbage collection in android, the initial capacity
    // should be large enough 
    Log.d(SimpleList.class.getCanonicalName(), "Increasing list from size " + 
        items.length + " to " + newSize);
    
    T[] newArray = makeArray(newSize);
    for ( int i = 0 ; i < items.length ; i++) {
      newArray[i] = items[i];
    }
    items = newArray;
  }
}
