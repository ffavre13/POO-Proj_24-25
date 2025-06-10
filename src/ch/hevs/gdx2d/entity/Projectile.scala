package ch.hevs.gdx2d.entity

import ch.hevs.gdx2d.lib.GdxGraphics
import ch.hevs.gdx2d.lib.interfaces.DrawableObject
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.math.Vector2

import java.awt.geom.Rectangle2D
import scala.collection.mutable.ArrayBuffer

class Projectile (pos: Vector2, vel: Vector2, own: String) extends DrawableObject {
  private val SPEED: Float = 300f
  private val RADIUS: Float = 5

  private var _position = pos
  private var _velocity = vel
  private val _hitbox: Rectangle2D.Float = new Rectangle2D.Float(pos.x,pos.y, RADIUS*2, RADIUS*2)
  private val _owner = own // "HERO" to deal damage to enemies or "ENEMY" to deal damage to the player

  private val HERO_COLOR: Color   = Color.BLUE
  private val ENEMY_COLOR: Color  = Color.RED

  def hitbox: Rectangle2D.Float = _hitbox
  def owner: String = _owner

  def position: Vector2 = _position
  def position_= (newPos: Vector2): Unit = {
    _position = newPos
  }

  def velocity: Vector2 = _velocity
  def velocity_= (newVel: Vector2): Unit = {
    _velocity = newVel
  }

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

  def drawHitbox(g: GdxGraphics): Unit = {
    g.drawRectangle(hitbox.getX.toFloat, hitbox.getY.toFloat, hitbox.getHeight.toFloat, hitbox.getWidth.toFloat, 90)
  }
}

object Projectile {
  private var _projectiles: ArrayBuffer[Projectile] = ArrayBuffer.empty

  def projectiles: Array[Projectile] = _projectiles.toArray

  def getHeroProjectile: Array[Projectile]  = _projectiles.filter(a => a._owner == "HERO").toArray
  def getEnemyProjectile: Array[Projectile] = _projectiles.filter(a => a._owner == "ENEMY").toArray

  def update(g: GdxGraphics): Unit = {
    for (e <- projectiles) {
      e.move()
      e.draw(g)
    }
  }

  def displayHitboxes(g: GdxGraphics): Unit = {
    for (p <- _projectiles)
      g.drawRectangle(p.hitbox.getX.toFloat, p.hitbox.getY.toFloat, p.hitbox.getWidth.toFloat, p.hitbox.getHeight.toFloat, 0)
  }

  def create(position: Vector2, velocity: Vector2, owner: String): Unit = {
    val projectile = new Projectile(position, new Vector2(velocity.x, velocity.y), owner)
    _projectiles.addOne(projectile)
  }

  def remove(p: Projectile): Unit = {
    val tmp: Int = _projectiles.indexOf(p)
    if (tmp >= 0) _projectiles.remove(tmp)
  }

  def removeAll(): Unit = _projectiles.clear()
}
