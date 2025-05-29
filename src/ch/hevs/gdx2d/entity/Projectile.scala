package ch.hevs.gdx2d.entity

import ch.hevs.gdx2d.lib.GdxGraphics
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.math.Vector2

import scala.collection.mutable.ArrayBuffer

private class Projectile (pos: Vector2, vel: Vector2) {
  private var _position = pos
  private var _velocity = vel
  private var SPEED = 100f

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
  }
  /**
   * Draws the hero sprite
   * @param g GdxGraphics object
   */
  def draw(g: GdxGraphics): Unit = {
    g.drawCircle(position.x, position.y, 2, new Color(Color.RED))
  }
}

object Projectile {
  private var allProjectiles: ArrayBuffer[Projectile] = ArrayBuffer.empty

  def update(g: GdxGraphics): Unit = {
    for (e <- allProjectiles) {
      e.move()
      e.draw(g)
    }
  }

  def create(position: Vector2, velocity: Vector2): Unit = {
    val projectile = new Projectile(position, velocity)
    allProjectiles.addOne(projectile)
  }
}
