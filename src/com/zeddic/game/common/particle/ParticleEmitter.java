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

package com.zeddic.game.common.particle;

import java.util.Random;

import android.graphics.Canvas;
import android.util.Log;

import com.zeddic.game.common.PhysicalObject;
import com.zeddic.game.common.util.ObjectPoolManager;
import com.zeddic.game.common.util.ObjectPool.ObjectBuilder;

/**
 * Spawns particles with particular properties to create animations. 
 * For example, a particle emmitter may create particles in a randomized
 * bursting pattern to create 'sparks' when a bullet collides with a
 * wall. The emmitter handles the life of individual particles and 
 * inserting a sufficient amount of randomness into the spawn patterns.
 * 
 * Emitters may be build using a {@link EmitterBuilder}.
 * 
 * @author scott (scott@zeddic.com)
 *
 */
public class ParticleEmitter extends PhysicalObject {

  //// SPAWN MODES
  // Avoiding an enum here due to high speed costs associated with them
  // on Android.
  
  /** Spawns particles in a particular angle with some randomness. */
  public static int MODE_DIRECTIONAL = 0;
  
  /** Spawns particles in a 360 degree circle around the particle emitter. */
  public static int MODE_OMNI = 1;
  
  //// EMITTER PROPERTIES
  
  /** How particles should be spawned. */
  public int emitMode;
  
  /** If using directional mode, what angle they should be spawned at. */
  public float emitAngle;
  
  /** Level of randomness to apply to the emit angle in directional mode. */
  public float emitAngleJitter;
  
  /** Level of randomness to apply to the particle speed. */
  public float emitSpeedJitter;
  
  /** How far away from the emitter particles should be spawned at. */
  public float emitSpawnDistance;
  
  /** How many particles to emit per second. */
  public long emitRate;
  
  /** How many milliseconds the emitter should countinue to work for.*/
  public long emitLife;
  
  /** The emitters current lifespan in milliseconds. */
  public long life;
  
  /** If true, the particle emitter will reuse particles again until death. */
  public boolean emitCycleParticles;
  
  /** The maximum amount of particles that can alive at the same time. */
  private int maxParticles;
  
  /** How many particles have been spawned so far. */
  private int spawnCount;
  
  /**
   * How long to wait between emitting new particles. Determined by setting
   * a new emitRate. Do not change directly.
   */
  private long fireCooldown;
  
  /** The last time that a particle was emitted. */
  private long lastFire;
  
  /** Creates randomness when jitter is requested. */
  private Random random;
  
  /** A pool of particles that can be reused as needed .*/
  ObjectPoolManager<Particle> poolManager;
  

  //// PROPERTIES FOR EMITTED PARTICLES
  
  /** The acceleration of spawned particles. */
  public float pAcceleration;
  
  /** The speed of spawned particles .*/
  public float pSpeed;
  
  /** The maximum speed that the particle can reach. */
  public float pMaxSpeed;
  
  /** How many milliseconds spawned particles should live for. */
  public float pLife;
  
  /** The alpha visibility of spawned particles. */
  public float pAlpha;
  
  /** The rate at which particle transparency should change. */
  public float pAlphaRate;

  /**
   * A gravity well (object) that particles should be pulled towards. Null,
   * default, means there is no gravity well.
   */
  public PhysicalObject pGravityWell;
  
  /** The force at which the gravity well pulls on particles. */
  public float pGravityWellForce;
  
  /** 
   * The maximum distance from the gravity well in which the effects 
   * of gravity can be felt. 0 means no limit.
   */
  public float pGravityWellMaxDistance;
  
  /**
   * When a particle gets within this distance range of the gravity well
   * it will despawn. 0 means the particle will not despawn.
   */
  public float pGravityWellDespawnDistance;
  
  /**
   * If true, a particle in a gravity well will issue a collide event
   * once it reaches the despawn distance. Only applies when
   * {@link pGravityWellDespawnDistance} is not equal to 0.
   */
  public boolean pGravityWellCollide;
  
  /** The type of particles to spawn. Default is basic {@link Particle}. */
  private Class<? extends Particle> pClass;
  
  /** Custom data that should be passed from the emitter to every particle. */
  private ParticleData pData;
  
  /**
   * Should only be created via the Builder.
   */
  private ParticleEmitter(float x, float y) {
    super(x, y);
    random = new Random();
  }
  
  /**
   * Sets up the life and object pool for the emitter.
   */
  public void init() {
    // Activate.
    reset();
    setEmitRate(emitRate);
    
    // Create a pool of particles, each particle of the specified class.
    // Don't set particle properties until they are actually emitted.
    poolManager = new ObjectPoolManager<Particle>(
        Particle.class,
        maxParticles,
        new ObjectBuilder<Particle>() {
          @Override
          public Particle get(int count) {
            Particle particle = null;
            try {
              particle = pClass.newInstance();
              particle.active = false;
            } catch (IllegalAccessException e) {
              Log.e(this.getClass().toString(), "Error creating particle", e);
            } catch (InstantiationException e) {
              Log.e(this.getClass().toString(), "Error creating particle", e);
            }
            return particle;
          }
        });
  }
  
  /**
   * Resets the state of the emitter as if it had just been created.
   */
  public void reset() {
    enable();
    life = 0;
    spawnCount = 0;
    lastFire = System.currentTimeMillis();
  }
  
  /**
   * Sets the number of particles that should be emitted in a single
   * second.
   */
  public void setEmitRate(long rate) {
    this.emitRate = rate;
    fireCooldown = (emitRate == 0 ? 0 : 1000 / emitRate);
  }
  
  /**
   * Emits a new particle.
   */
  public boolean emmit() {
    // Get a particle from the pool. Give up if we are 
    // out of particles.
    Particle particle = poolManager.take();
    if (particle == null)
      return false;
    
    // Set all the particle properties
    particle.enable();
    particle.life = 0;
    particle.maxLife = pLife;
    particle.acceleration = pAcceleration;
    particle.maxSpeed = pMaxSpeed;
    particle.alpha = pAlpha;
    particle.alphaRate = pAlphaRate;
    particle.gravityWell = pGravityWell;
    particle.gravityWellForce = pGravityWellForce;
    particle.gravityWellMaxDistance = pGravityWellMaxDistance;
    particle.gravityWellDespawnDistance = pGravityWellDespawnDistance;
    particle.gravityWellCollide = pGravityWellCollide;
    
    // Determine the direction the particle should be fired at.
    if (emitMode == MODE_DIRECTIONAL) {
      emitAngle = emitAngle + -emitAngleJitter + random.nextFloat() * emitAngleJitter * 2;
    } else {
      emitAngle = random.nextFloat() * 360;
    }
    
    //  Offset the particles spawn distance as needed.
    if (emitSpawnDistance > 0) {
      double radians = Math.toRadians(emitAngle);
      particle.x = x + emitSpawnDistance * (float) Math.cos(radians);
      particle.y = y + emitSpawnDistance * (float) Math.sin(radians);
    } else {
      particle.x = x;
      particle.y = y;
    }
    
    // Determine the speed that the particle should be fired at.
    float emitSpeed = pSpeed + -emitSpeedJitter + random.nextFloat() * emitSpeedJitter * 2;
    
    // EMIT! GO GO GO!!!!
    particle.setVelocityBySpeed(emitAngle, emitSpeed);
    particle.onEmit(pData);
    
    return true;
  }
  
  /**
   * Draws all particles that have been spawned. The emitter itself
   * is not visible.
   */
  public void draw(Canvas canvas) {
    poolManager.draw(canvas);
  }
  
  /**
   * Emits as many particles as needed based on the surpassed time. 
   * For example, if the emitter has a rate of 500 particles per second, and
   * 500 milliseconds have passed, 250 particles will be emitted.
   */
  private void emmitNeededParticles(long time) {

    long now = System.currentTimeMillis();
    long passedTime = now - lastFire;
    if (passedTime > fireCooldown) {
      long timesToFire =
          (fireCooldown == 0 ? maxParticles : passedTime / fireCooldown);
      for (int i = 0 ; i < timesToFire ; i++) {
        if (!emitCycleParticles && spawnCount >= maxParticles)
          break;
        if(!emmit())
          break;
        spawnCount++;
      }
      lastFire = (fireCooldown == 0 ? now : now - passedTime % fireCooldown);
    }
  }
  
  /**
   * Spawns new particles and updates the state of all spawned 
   * particles.
   */
  public void update(long time) {
    
    // Update life and spawn particles if still alive.
    life += time;
    if (life <= emitLife || emitLife == 0) {
      emmitNeededParticles(time);
    } else {
      kill();
      poolManager.reclaimPool();
    }
    
    // If we have a limited number of particles to supply, and all of them 
    // have been used, go ahead and kill the emitter.
    if (!emitCycleParticles &&
        spawnCount >= maxParticles &&
        poolManager.pool.numLeft() == poolManager.pool.items.length) {
      kill();
    }
    
    poolManager.update(time);
  }
  
  /**
   * Builds a new {@link ParticleEmitter} with the specified settings.
   */
  public static class ParticleEmitterBuilder {
    
    float x = 0;
    float y = 0;
    int emitMode = MODE_DIRECTIONAL;
    float emitAngle = 0;
    float emitAngleJitter = 0;
    float emitSpeedJitter = 0;
    float emitSpawnDistance = 0;
    long emitRate = 10;
    long emitLife = 0;
    float pAcceleration = 0;
    float pSpeed = 0;
    float pMaxSpeed = 0;
    float pLife = 1000;
    float pAlpha = 255;
    float pAlphaRate = 0;
    PhysicalObject pGravityWell;
    float pGravityWellForce;
    float pGravityWellMaxDistance = 0;
    float pGravityWellDespawnDistance = 0;
    boolean pGravityWellCollide = false;
    boolean emitCycleParticles = true;
    ParticleData pData = null;
    
    Class<? extends Particle> pClass = Particle.class;
    int maxParticles = 100;
    
    public ParticleEmitterBuilder at(float x, float y) {
      this.x = x;
      this.y = y;
      return this;
    }
    
    public ParticleEmitterBuilder withEmitMode(int emitMode) {
      this.emitMode = emitMode;
      return this;
    }
    
    public ParticleEmitterBuilder withEmitAngle(float emitAngle) {
      this.emitAngle = emitAngle;
      return this;
    }
    
    public ParticleEmitterBuilder withEmitRate(long emitRate) {
      this.emitRate = emitRate;
      return this;
    }
    
    public ParticleEmitterBuilder withEmitCycle(boolean cycleParticles) {
      this.emitCycleParticles = cycleParticles;
      return this;
    }
    
    public ParticleEmitterBuilder withEmitAngleJitter(float emitAngleJitter) {
      this.emitAngleJitter = emitAngleJitter;
      return this;
    }
    
    public ParticleEmitterBuilder withEmitSpeedJitter(float emitSpeedJitter) {
      this.emitSpeedJitter = emitSpeedJitter;
      return this;
    }
    
    public ParticleEmitterBuilder withEmitSpawnDistance(float emitSpawnDistance){
      this.emitSpawnDistance = emitSpawnDistance;
      return this;
    }
    
    public ParticleEmitterBuilder withEmitLife(long emitLife) {
      this.emitLife = emitLife;
      return this;
    }
    
    public ParticleEmitterBuilder withMaxParticles(int maxParticles) {
      this.maxParticles = maxParticles;
      return this;
    }
    
    public ParticleEmitterBuilder withParticleAcceleration(float pAcceleration) {
      this.pAcceleration = pAcceleration;
      return this;
    }
    
    public ParticleEmitterBuilder withParticleSpeed(float pSpeed) {
      this.pSpeed = pSpeed;
      return this;
    }
    
    public ParticleEmitterBuilder withMaxParticleSpeed(float pMaxSpeed) {
      this.pMaxSpeed = pMaxSpeed;
      return this;
    }
    
    public ParticleEmitterBuilder withParticleLife(float pLife) {
      this.pLife = pLife;
      return this;
    }
    
    public ParticleEmitterBuilder withParticleAlpha(float pAlpha) {
      this.pAlpha = pAlpha;
      return this;
    }
    
    public ParticleEmitterBuilder withParticleAlphaRate(float pAlphaRate) {
      this.pAlphaRate = pAlphaRate;
      return this;
    }
    
    public ParticleEmitterBuilder withParticleClass(Class<? extends Particle> pClass) {
      this.pClass = pClass;
      return this;
    }
    
    public ParticleEmitterBuilder withDataForParticles(ParticleData data) {
      this.pData = data;
      return this;
    }
    
    public ParticleEmitterBuilder withGravityWell(
        PhysicalObject well,
        float force) {
      
      this.pGravityWell = well;
      this.pGravityWellForce = force;
      return this;
    }
    
    public ParticleEmitterBuilder withGravityWellMaxDistance(float maxDistance) {
      this.pGravityWellMaxDistance = maxDistance;
      return this;
    }
    
    public ParticleEmitterBuilder withGravityWellDispawnDistance(float distance) {
      this.pGravityWellDespawnDistance = distance;
      return this;
    }
    
    public ParticleEmitterBuilder withGravityWellCollide(boolean collideWith) {
      this.pGravityWellCollide = collideWith;
      return this;
    }
 
    public ParticleEmitter build() {
      ParticleEmitter emitter = new ParticleEmitter(x, y);
      emitter.emitMode = emitMode;
      emitter.emitAngle = emitAngle;
      emitter.emitAngleJitter = emitAngleJitter;
      emitter.emitCycleParticles = emitCycleParticles;
      emitter.emitSpeedJitter = emitSpeedJitter;
      emitter.emitSpawnDistance = emitSpawnDistance;
      emitter.emitRate = emitRate;
      emitter.emitLife = emitLife;
      emitter.pAcceleration = pAcceleration;
      emitter.pSpeed = pSpeed;
      emitter.pMaxSpeed = pMaxSpeed;
      emitter.pLife = pLife;
      emitter.pAlpha = pAlpha;
      emitter.pAlphaRate = pAlphaRate;
      emitter.pGravityWell = pGravityWell;
      emitter.pGravityWellForce = pGravityWellForce;
      emitter.pGravityWellMaxDistance = pGravityWellMaxDistance;
      emitter.pGravityWellDespawnDistance = pGravityWellDespawnDistance;
      emitter.pGravityWellCollide = pGravityWellCollide;
      emitter.pClass = pClass;
      emitter.pData = pData;
      emitter.maxParticles = maxParticles;
      
      emitter.init();
      
      return emitter;
    }
  }
}
