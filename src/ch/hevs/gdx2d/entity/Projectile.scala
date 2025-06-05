package ch.hevs.gdx2d.entity

import ch.hevs.gdx2d.hitbox.CircleHitbox
import ch.hevs.gdx2d.lib.GdxGraphics
import ch.hevs.gdx2d.lib.interfaces.DrawableObject
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.math.Vector2

import java.awt.geom.Rectangle2D
import scala.collection.mutable.ArrayBuffer

class Projectile (pos: Vector2, vel: Vector2) extends DrawableObject {
  private var SPEED: Float = 200f
  private var RADIUS: Float = 5

  private var _position = pos
  private var _velocity = vel
  private var _hitbox: Rectangle2D.Float = new Rectangle2D.Float(pos.x,pos.y, RADIUS*2, RADIUS*2)

  def hitbox: Rectangle2D.Float = _hitbox

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
    g.drawFilledCircle(position.x, position.y, RADIUS, new Color(Color.BLUE))
  }

  def drawHitbox(g: GdxGraphics): Unit = {
    g.drawRectangle(hitbox.getX.toFloat, hitbox.getY.toFloat, hitbox.getHeight.toFloat, hitbox.getWidth.toFloat, 90)

  }
}

object Projectile {
  private var _allProjectiles: ArrayBuffer[Projectile] = ArrayBuffer.empty

  def allProjectiles: Array[Projectile] = _allProjectiles.toArray

  def update(g: GdxGraphics): Unit = {
    for (e <- allProjectiles) {
      e.move()
      e.draw(g)
    }
  }

  def create(position: Vector2, velocity: Vector2): Unit = {
    val projectile = new Projectile(position, velocity)
    _allProjectiles.addOne(projectile)
  }

  def remove(p: Projectile): Unit = {
    val tmp: Int = _allProjectiles.indexOf(p)
    if (tmp >= 0) _allProjectiles.remove(tmp)
  }
}
