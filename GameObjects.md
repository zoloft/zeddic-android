# Game Tree #

Using a "Game Tree" is a helpful way of organizing the objects in your game. In this pattern, each of the main pieces of your game, such as Players, Bullets, Ships, Enemies, etc. are a node in your tree. Each node may in change have child objects. For example, a Ship may be made up of an engine and gun.

For each frame of your game, you will need to update and draw each object in your game tree. This is done by a simple depth first traversal of the tree.

A common and helpful technique when using this pattern is to have game objects that provide some sort of functionality for their parent. For example, an Enemy object may have a `PathingComponent` or an `AimingComponent` that provide movement and direction functionality. This allows you to use composition, rather than inheritance, to build up your game.

http://zeddic-android.googlecode.com/svn-history/r27/wiki/game_tree.PNG


# Game Object #
A [GameObject](http://code.google.com/p/zeddic-android/source/browse/trunk/src/com/zeddic/game/common/GameObject.java?r=26) is used to represent any object in your game tree, such as a Bullet, Ship, Player, or component that provides some functionality. A `GameObject` is anything that should be have it's state updated and be drawn during each frame.

Extending from the `GameObject` class allows your objects to make use of `ObjectPool`s and other utility classes in the library.

You're `GameObject` should override two main methods:

## `update(long time)` ##

The update method is responsible for updating the state of the object and all of it's children objects. It is passed the number of milliseconds that have passed since the last full frame.

Any movements that you do in your game should be scaled based on the amount of time that has passed. This allows your game to run the same regardless of how fast it is rendering.

For example:

```
public void update(long time) {
  float timeFraction = (float) time / TIME_SCALER;
  x += velocity.x * timeFraction;
  y += velocity.y * timeFraction;
  setAngle(angle + rotation * timeFraction);
}

```

## `draw(Canvas canvas)` ##
The draw command is called whenever it is time for the object to draw itself. It is provided a reference to a prepared canvas on which to draw.

If your object should be drawn at a particular position in the canvas, it helps to use the built in canvas `translate` and `rotate` commands.

For example:

```
public void drawBounds(Canvas canvas) {
  canvas.save();

  canvas.translate(x, y);
  canvas.rotate(angle);
  canvas.scale(scale, scale);
  
  canvas.drawPath(OBJECT_PATH, PAINT);
  canvas.restore();
}
```

# Example Game Objects #
The [Geo Siege](http://www.geosiege.com) project provides a few examples of `GameObjects`. A few helpful ones to check out:

  * [SimpleEnemyShip](http://code.google.com/p/geosiege/source/browse/trunk/src/com/geosiege/game/ships/SimpleEnemyShip.java)

  * [ArrowEnemy](http://code.google.com/p/geosiege/source/browse/trunk/src/com/geosiege/game/ships/Arrow.java)

  * [MapBoundsComponent](http://code.google.com/p/geosiege/source/browse/trunk/src/com/geosiege/game/components/MapBoundsComponent.java)