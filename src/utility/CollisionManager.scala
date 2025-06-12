package utility

import com.badlogic.gdx.maps.{MapLayer, MapObject, MapProperties}
import entity.Projectile
import entity.enemies.Enemy

import java.awt.geom.Rectangle2D
import scala.collection.mutable.ArrayBuffer

object CollisionManager{

  /**
   * return collision contained in the map layer
   * @param c Map layer
   * @return return an array containning all the collision
   */
  def getCollisions(c: MapLayer): Array[MapObject] = {
    var arr: ArrayBuffer[MapObject] = new ArrayBuffer[MapObject]()
    for (a <- 0 until c.getObjects.getCount) {
      arr.append(c.getObjects.get(a))
    }
    return arr.toArray
  }

  /**
   * Transforms collisions into a rectangle2D
   * @param a MapObject
   * @return return a rectangle2D
   */
  def getRectangle2D(a: MapObject): Rectangle2D.Float = {
    val props: MapProperties = a.getProperties
    return new Rectangle2D.Float(
      props.get("x").asInstanceOf[Float],
      props.get("y").asInstanceOf[Float],
      props.get("width").asInstanceOf[Float],
      props.get("height").asInstanceOf[Float]
    )
  }

  /**
   * Transforms multiple MapObject into multiple Rectangle2D
   * @param mapObjects Array of map objects
   * @return Return an array of rectangle 2D
   */
  def getRectangles2D(mapObjects: Array[MapObject]): Array[Rectangle2D.Float] = {
    var res: ArrayBuffer[Rectangle2D.Float] = new ArrayBuffer[Rectangle2D.Float]()
    for (a <- mapObjects) {
      res.append(getRectangle2D(a))
    }
    return res.toArray
  }

  /**
   * Checks players, projectiles and enemies hitboxes
   */
  def checkHitbox(): Unit = {

    for(p <- Projectile.projectiles) {
      if (p.owner == "HERO") {
        for(e <- Enemy.enemies) {
          if(e.hitbox.intersects(p.hitbox)) {
            Projectile.remove(p)
            e.takeDamage(1)
          }
        }
      } else if (p.owner == "ENEMY") {
        if (p.hitbox.intersects(GameState.hero.hitbox)) {
          Projectile.remove(p)
          GameState.hero.takeDamage(1)
        }
      }
    }

  }
}
