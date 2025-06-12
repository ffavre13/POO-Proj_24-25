package entity

import ch.hevs.gdx2d.lib.GdxGraphics
import ch.hevs.gdx2d.lib.interfaces.DrawableObject
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.math.Vector2

import java.awt.geom.Rectangle2D
import scala.collection.mutable.ArrayBuffer

/**
 * Class representing a projectile
 * @param pos Initial position of the projectile
 * @param vel Velocity of the projectile (indicates where the bullet will go)
 * @param own Owner of the projectile. Can be "HERO" to deal damage to enemies or "ENEMY" to deal damages to the enemies
 */
class Projectile (pos: Vector2, vel: Vector2, own: String) extends DrawableObject {
  private val SPEED: Float = 400f   // Speed of projectile
  private val RADIUS: Float = 5     // radius for the projectile size

  private var _position = pos     // Current position of the projectile
  private var _velocity = vel     // Velocity of the projectile
  private val _hitbox: Rectangle2D.Float = new Rectangle2D.Float(pos.x,pos.y, RADIUS*2, RADIUS*2)
  private val _owner = own // "HERO" to deal damage to enemies or "ENEMY" to deal damage to the player

  private val HERO_COLOR: Color   = Color.YELLOW    // The color of hero projectiles
  private val ENEMY_COLOR: Color  = Color.RED       // The color of enemies projectiles

  // Getter & setter

  def hitbox: Rectangle2D.Float = {
    _hitbox
  }
  def owner: String = {
    _owner
  }

  def position: Vector2 = {
    _position
  }
  def position_= (newPos: Vector2): Unit = {
    _position = newPos
  }

  def velocity: Vector2 = {
    _velocity
  }
  def velocity_= (newVel: Vector2): Unit = {
    _velocity = newVel
  }

  /**
   * Change projectile position
   */
  def move():Unit = {
    val new_pos: Vector2 = new Vector2(0, 0)
    new_pos.x = position.x + (velocity.x * SPEED * Gdx.graphics.getDeltaTime)
    new_pos.y = position.y + (velocity.y * SPEED * Gdx.graphics.getDeltaTime)
    position = new_pos

    hitbox.x = position.x
    hitbox.y = position.y
  }

  /**
   * Draws the hero sprite
   * @param g GdxGraphics object
   */
  override def draw(g: GdxGraphics): Unit = {
    var color: Color = Color.GOLD
    if (_owner == "HERO") color = HERO_COLOR
    else color = ENEMY_COLOR

    g.drawFilledCircle(position.x, position.y, RADIUS, color)
  }

  /**
   * Display the hitboxes for The hero
   * @param g GdxGraphics object
   */
  def drawHitbox(g: GdxGraphics): Unit = {
    g.drawRectangle(hitbox.getX.toFloat, hitbox.getY.toFloat, hitbox.getHeight.toFloat, hitbox.getWidth.toFloat, 90)
  }
}

object Projectile {
  private var _projectiles: ArrayBuffer[Projectile] = ArrayBuffer.empty   // Array containing all projectiles

  def projectiles: Array[Projectile] = {
    _projectiles.toArray
  }

  def getHeroProjectile: Array[Projectile]  = {
    _projectiles.filter(a => a._owner == "HERO").toArray
  }

  def getEnemyProjectile: Array[Projectile] = {
    _projectiles.filter(a => a._owner == "ENEMY").toArray
  }

  /**
   * Updates the state of all the projectiles
   * @param g GdxGraphics object
   */
  def update(g: GdxGraphics): Unit = {
    for (e <- projectiles) {
      e.move()
      e.draw(g)
    }
  }

  /**
   * Display the hitboxes for all the projectiles
   * @param g GdxGraphics object
   */
  def displayHitboxes(g: GdxGraphics): Unit = {
    for (p <- _projectiles)
      g.drawRectangle(p.hitbox.getX.toFloat, p.hitbox.getY.toFloat, p.hitbox.getWidth.toFloat, p.hitbox.getHeight.toFloat, 0)
  }

  /**
   * Adds a projectile to the game
   * @param position Posisition of the projectile
   * @param velocity Veclocity of the projectile
   * @param owner Hero or enemy projectile
   */
  def create(position: Vector2, velocity: Vector2, owner: String): Unit = {
    val projectile = new Projectile(position, new Vector2(velocity.x, velocity.y), owner)
    _projectiles.addOne(projectile)
  }

  /**
   * Remove an projcetile
   * @param p Projectile
   */
  def remove(p: Projectile): Unit = {
    val tmp: Int = _projectiles.indexOf(p)
    if (tmp >= 0) _projectiles.remove(tmp)
  }

  /** Removes all projectiles */
  def removeAll(): Unit = _projectiles.clear()
}
