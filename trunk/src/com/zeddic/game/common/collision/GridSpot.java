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

package com.zeddic.game.common.collision;

import com.zeddic.game.common.PhysicalObject;
import com.zeddic.game.common.util.SimpleList;

/**
 * Represents a single position within the {@link CollisionGrid}. Contains
 * a list of PhysicalObjects that are currently residing within that position.
 * 
 * @author baileys (Scott Bailey)
 */
public class GridSpot {
  
  private static int INITIAL_CAPACITY = 40;
  
  /**
   * A list of lists that contains a reference to every object inside this
   * grid. Each inner list contains only objects of the same primitive
   * type.
   */
  public SimpleList<PhysicalObject> items;
  
  /**
   * A list of the corresponding types of each object list.
   */
  //public SimpleList<Class<? extends PhysicalObject>> types;
  
  /**
   * Maps a class type to the index where a list of that type of class may
   * be found inside of {@link lists}.
   */
  //private Map<Class<? extends PhysicalObject>, Integer> typeToList;
  
  
  public GridSpot() {
    items = new SimpleList<PhysicalObject>(PhysicalObject.class, INITIAL_CAPACITY);
    //lists = new SimpleList<SimpleList<PhysicalObject>>(SimpleList.class, INITIAL_CAPACITY);
    //types = new SimpleList<Class<? extends PhysicalObject>>(Class.class, INITIAL_CAPACITY);
    //typeToList = new HashMap<Class<? extends PhysicalObject>, Integer>();
    
  }
 
  /**
   * Adds a new object to this grid position.
   */
  public void add(PhysicalObject object) {
    items.add(object);
  }
  
  
  public boolean contains(PhysicalObject object) {
    return items.contains(object);
  }
  
  /**
   * Removes an object from the grid spot.
   */
  public void remove(PhysicalObject object) {
    items.remove(object);
  }
}
