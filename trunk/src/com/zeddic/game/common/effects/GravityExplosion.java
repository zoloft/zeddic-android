package com.zeddic.game.common.effects;

import com.zeddic.game.common.PhysicalObject;
import com.zeddic.game.common.particle.MoneyParticle;
import com.zeddic.game.common.particle.ParticleEmitter;
import com.zeddic.game.common.particle.ParticleEmitter.ParticleEmitterBuilder;

public class GravityExplosion extends Explosion {

  public GravityExplosion() {
    super(0, 0);
  }
  
  public GravityExplosion(float x, float y) {
    super(x, y);
  }

  protected void createEmitter() {
    emitter = new ParticleEmitterBuilder()
        .at(x, y)
        .withEmitMode(ParticleEmitter.MODE_OMNI)
        .withParticleSpeed(10)
        .withEmitSpeedJitter(8)
        .withEmitLife(20 * 1000)
        .withEmitCycle(false)
        .withParticleSpeed(6)
        .withMaxParticleSpeed(60)
        //.withParticleAlpha(100)
        //.withParticleAlphaRate(0)
        .withParticleLife(20 * 1000)
        .withMaxParticles(5)
        .withEmitRate(0)
        .withParticleClass(MoneyParticle.class)
        .withGravityWellMaxDistance(200)
        .withGravityWellDispawnDistance(25)
        .withGravityWellCollide(true)
        .build();
    emitter.pGravityWellForce = 25;
  }
  
  public void ignite(PhysicalObject destination) {
    super.ignite();
    emitter.pGravityWell = destination;
  }
}
