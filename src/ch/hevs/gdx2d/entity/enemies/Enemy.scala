package ch.hevs.gdx2d.entity.enemies

import ch.hevs.gdx2d.entity.Entity
import ch.hevs.gdx2d.hitbox.CircleHitbox
import ch.hevs.gdx2d.lib.GdxGraphics
import ch.hevs.gdx2d.lib.interfaces.DrawableObject
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.math.Vector2

import java.awt.geom.{Ellipse2D, Rectangle2D}
import scala.collection.mutable.ArrayBuffer

/**
 * Abstract class representing an Enemy
 * @param startX The starting X position of an enemy
 * @param startY The starting Y position of an enemy
 */
abstract class Enemy(startX: Int, startY: Int) extends DrawableObject with Entity {
  protected var RADIUS: Int = 10

  protected var _position: Vector2 = new Vector2(startX, startY)
  protected var _hitbox: Rectangle2D.Float = new Rectangle2D.Float(startX , startY, RADIUS*2, RADIUS*2)

  def position: Vector2 = _position
  def position_= (newPos: Vector2): Unit = _position = newPos

  def hitbox: Rectangle2D.Float = _hitbox

  /**
   * Updates the enemy (meant to be called on each frame generation)
   * @param elapsedTime Time elapsed between two frames
   */
  def update(elapsedTime: Float): Unit

  /**
   * Draws the enemy on the scrfeen
   * @param g GdxGraphics object
   */
  override def draw(g: GdxGraphics): Unit
}

/**
 * Enemy object to manage enemies
 */
object Enemy {
  private val _enemies: ArrayBuffer[Enemy] = ArrayBuffer.empty

  /** Returns an Array containing the enemies */
  def enemies: Array[Enemy] = _enemies.toArray

  /**
   * Updates the state of the enemies (move them, make them shoot, ...)
   * @param g GdxGraphics object
   */
  def update(g: GdxGraphics): Unit = {
    for (e <- _enemies) {
      e.update(Gdx.graphics.getDeltaTime)
      e.draw(g)
    }
  }

  /**
   * Adds an enemy to the game
   * @param enemy Enemy to add
   */
  def add(enemy: Enemy): Unit = _enemies.addOne(enemy)

  /**
   * Adds an enemy to the game
   * @param enemy Enemy to remove
   */
  def remove(enemy: Enemy): Unit = _enemies.remove(_enemies.indexOf(enemy))
}
