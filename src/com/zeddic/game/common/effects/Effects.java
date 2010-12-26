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

package com.zeddic.game.common.effects;

import android.graphics.Canvas;

import com.zeddic.game.common.GameObject;
import com.zeddic.game.common.PhysicalObject;
import com.zeddic.game.common.util.ObjectPoolManager;
import com.zeddic.game.common.util.ObjectStockpile;
import com.zeddic.game.common.util.Vector2d;

public class Effects extends GameObject {

  ObjectStockpile effects;
  
  public static Effects singleton;
  
  static { 
    singleton = new Effects();
  }
  
  private Effects() {
    createPools();
  }
  
  private void createPools() {
    effects = new ObjectStockpile();
    effects.createSupply(Explosion.class, 50);
    effects.createSupply(HitExplosion.class, 50);
    effects.createSupply(GravityExplosion.class, 50);
    effects.createSupply(Implosion.class, 50);
  }
  
  public void createSupply(Class<? extends GameObject> clazz, int number) {
    effects.createSupply(clazz, number);
  }
  
  public <T extends GameObject> ObjectPoolManager<T> getSupply(Class<T> clazz) {
    return effects.getSupply(clazz);
  }
  
  public <T extends GameObject> T take(Class<T> clazz) {
    return effects.getSupply(clazz).take();
  } 
  
  public Explosion explode(float x, float y) {
    
    Explosion explosion = effects.getSupply(Explosion.class).take();
    if (explosion == null)
      return null;
    
    explosion.x = x;
    explosion.y = y;
    explosion.ignite();
    return explosion;
  }
  
  public HitExplosion hit(float x, float y, Vector2d direction) { 
    HitExplosion explosion = effects.getSupply(HitExplosion.class).take();
    if (explosion == null)
      return null;
    
    float angle = (float) Math.toDegrees(Math.atan( direction.y / direction.x));
    if (direction.x < 0)
      angle += 180;
    explosion.emitter.emitAngle = angle;
    explosion.x = x;
    explosion.y = y;
    explosion.ignite();
    return explosion;
  }
  
  public GravityExplosion explodeWithGravity(
      float x, float y, PhysicalObject dest) {
    
    GravityExplosion explosion = effects.getSupply(GravityExplosion.class).take();
    if (explosion == null)
      return null;
    
    explosion.x = x;
    explosion.y = y;
    explosion.ignite(dest);
    
    return explosion;
  }
  
  public Implosion implode(float x, float y, long time) {
    
    Implosion implosion = effects.getSupply(Implosion.class).take();
    if (implosion == null)
      return null;
    
    implosion.x = x;
    implosion.y = y;
    implosion.emitter.emitLife = time;
    implosion.ignite();
    
    return implosion;
  }
  
  public void reset() {
    effects.reset();
  }
  
  public void draw(Canvas canvas) {
    effects.draw(canvas);
  }
  
  public void update(long time) {
    effects.update(time);
  }
  
  public static Effects get() {
    return singleton;
  }
}
