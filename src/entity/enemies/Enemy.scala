package entity.enemies

import ch.hevs.gdx2d.lib.GdxGraphics
import ch.hevs.gdx2d.lib.interfaces.DrawableObject
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.math.Vector2
import entity.Entity
import utility.GameState

import java.awt.geom.Rectangle2D
import scala.collection.mutable.ArrayBuffer

/**
 * Abstract class representing an Enemy
 * @param startX The starting X position of an enemy
 * @param startY The starting Y position of an enemy
 */
abstract class Enemy(startX: Int, startY: Int) extends DrawableObject with Entity {
  protected val SPRITE_WIDTH: Int = 64      // Enemy width
  protected val SPRITE_HEIGHT: Int = 64     // Enemy height

  protected var _position: Vector2 = new Vector2(startX, startY)    // Enemy position
  protected var _hitbox: Rectangle2D.Float = new Rectangle2D.Float(startX - SPRITE_WIDTH/2, startY - SPRITE_HEIGHT/2, SPRITE_WIDTH, SPRITE_HEIGHT)

  def position: Vector2 = {
    _position
  }

  def position_= (newPos: Vector2): Unit = {
    _position = newPos
  }

  def hitbox: Rectangle2D.Float = {
    _hitbox
  }

  /**
   * Updates the enemy (meant to be called on each frame generation)
   * @param elapsedTime Time elapsed between two frames
   */
  def update(elapsedTime: Float): Unit = {
    _hitbox.x = _position.x - SPRITE_WIDTH/2
    _hitbox.y = _position.y - SPRITE_HEIGHT/2
    _hitbox.width = SPRITE_WIDTH
    _hitbox.height = SPRITE_HEIGHT
  }

  /**
   * Draws the enemy on the screen
   * @param g GdxGraphics object
   */
  override def draw(g: GdxGraphics): Unit

  /**
   * Delete the enemy if it has no life left
   */
  override def ko(): Unit = {
    val index: Int = GameState.room.enemys.indexOf(this.asInstanceOf[Enemy])
    GameState.room.enemys.remove(index)
    Enemy.remove(this.asInstanceOf[Enemy])
  }
}

/**
 * Enemy object to manage enemies
 */
object Enemy {
  private val _enemies: ArrayBuffer[Enemy] = ArrayBuffer.empty

  /** Returns an Array containing the enemies */
  def enemies: Array[Enemy] = _enemies.toArray

  /**
   * Updates the state of all the enemies
   * @param g GdxGraphics object
   */
  def update(g: GdxGraphics): Unit = {
    for (e <- _enemies) {
      e.update(Gdx.graphics.getDeltaTime)
      e.draw(g)
    }
  }

  /**
   * Display the hitboxes for all the enemies
   * @param g GdxGraphics object
   */
  def displayHitboxes(g: GdxGraphics): Unit = {
    for (e <- _enemies)
      g.drawRectangle(e.hitbox.getX.toFloat + e.SPRITE_WIDTH/2, e.hitbox.getY.toFloat + e.SPRITE_HEIGHT/2, e.hitbox.getWidth.toFloat, e.hitbox.getHeight.toFloat, 0)
  }

  /**
   * Adds an enemy to the game
   * @param enemy Enemy to add
   */
  def add(enemy: Enemy): Unit = {
    _enemies.addOne(enemy)
  }

  /**
   * Remove an enemy to the game
   * @param enemy Enemy to remove
   */
  def remove(enemy: Enemy): Unit = {
    _enemies.remove(_enemies.indexOf(enemy))
  }

  def removeAll(): Unit = {
    _enemies.clear()
  }
}
