# Physical Object #

A [PhysicalObject](http://code.google.com/p/zeddic-android/source/browse/trunk/src/com/zeddic/game/common/PhysicalObject.java)  is a special [Game Object](GameObjects#Game_Object.md) that already has an x & y position, bounds, velocity, and scale.

It's a useful class to extend from if you intend on having some object that will move about the screen, such as a bullet, player, or enemy.

Any `GameObject` that you want to use with the [Collision Detection](CollisionDetection.md) classes must also extend PhysicalObject, as this is how the system is able to determine bounds.

# Extending Physical Object #
If you extend `PhysicalObject` make sure that you call super.update() in your `GameObject`s update method. If not, your object will not have its velocity applied.

A few examples of classes extending from PhysicalObject can be found in the GeoSiege project:

  * [Bullet](http://code.google.com/p/geosiege/source/browse/trunk/src/com/geosiege/game/guns/Bullet.java)
  * [PlayerShip](http://code.google.com/p/geosiege/source/browse/trunk/src/com/geosiege/game/ships/PlayerShip.java)

You can also check out the `Particle` from the Zeddic particle emitter code:

  * [Particle](http://code.google.com/p/zeddic-android/source/browse/trunk/src/com/zeddic/game/common/particle/Particle.java)