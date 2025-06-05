package ch.hevs.gdx2d.utility

import ch.hevs.gdx2d.entity.Projectile
import ch.hevs.gdx2d.entity.enemies.Enemy
import com.badlogic.gdx.maps.{MapLayer, MapObject, MapProperties}

import java.awt.geom.Rectangle2D
import scala.collection.mutable.ArrayBuffer

object CollisionManager{
  def getCollisions(c: MapLayer): Array[MapObject] = {
    var arr: ArrayBuffer[MapObject] = new ArrayBuffer[MapObject]()
    for (a <- 0 until c.getObjects.getCount) {
      arr.append(c.getObjects.get(a))
    }
    return arr.toArray
  }

  def getRectangle2D(a: MapObject): Rectangle2D.Float = {
    val props: MapProperties = a.getProperties
    return new Rectangle2D.Float(
      props.get("x").asInstanceOf[Float],
      props.get("y").asInstanceOf[Float],
      props.get("width").asInstanceOf[Float],
      props.get("height").asInstanceOf[Float]
    )
  }

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
            Enemy.remove(e)
          }
        }
      }
      else if (p.owner == "ENEMY") {
        if (p.hitbox.intersects(GameState.hero.hitbox)) {
          println("PLAYER HAS BEEN HIT")
        }
      }

    }

  }
}
