package ch.hevs.gdx2d.entity

import ch.hevs.gdx2d.hitbox.CircleHitbox
import ch.hevs.gdx2d.lib.GdxGraphics
import ch.hevs.gdx2d.lib.interfaces.DrawableObject
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.math.Vector2

import scala.collection.mutable.ArrayBuffer

class Projectile (pos: Vector2, vel: Vector2) extends DrawableObject {
  private var SPEED = 200f
  private var RADIUS = 5

  private var _position = pos
  private var _velocity = vel
  private var _hitbox: CircleHitbox = CircleHitbox(pos.x,pos.y, RADIUS)

  def hitbox: CircleHitbox = _hitbox

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

    hitbox.posX = position.x
    hitbox.posY = position.y
  }
  /**
   * Draws the hero sprite
   * @param g GdxGraphics object
   */
  override def draw(g: GdxGraphics): Unit = {
    g.drawFilledCircle(position.x, position.y, RADIUS, new Color(Color.BLUE))
  }

  def drawHitbox(g: GdxGraphics): Unit = {
    hitbox.draw(g)
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
    _allProjectiles.remove(tmp)
  }
}
