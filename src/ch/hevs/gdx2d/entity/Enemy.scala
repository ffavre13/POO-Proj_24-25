package ch.hevs.gdx2d.entity

import ch.hevs.gdx2d.hitbox.{CircleHitbox, RectangleHitbox}
import ch.hevs.gdx2d.lib.GdxGraphics
import ch.hevs.gdx2d.lib.interfaces.DrawableObject
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.math.Vector2

import scala.collection.mutable.ArrayBuffer

class Enemy(startX: Float, startY: Float) extends DrawableObject {
  private var RADIUS: Int = 10
  private var SPEED: Float = 50f

  private var _position: Vector2 = new Vector2(startX, startY)
  private var _velocity: Vector2 = new Vector2(0,0)
  private var _hitbox: CircleHitbox = CircleHitbox(startX, startY, RADIUS)


  def hitbox: CircleHitbox = _hitbox

  def position: Vector2 = _position
  def position_= (newPos: Vector2): Unit = {
    _position = newPos
  }

  def velocity: Vector2 = _velocity
  def velocity_= (newVel: Vector2): Unit = {
    _velocity = newVel
  }

  def move(posHero: Vector2):Unit = {
    val new_pos: Vector2 = new Vector2(0, 0)

    velocity.x = posHero.x - position.x
    velocity.y = posHero.y - position.y

    velocity.nor()

    new_pos.x = position.x + (velocity.x * SPEED * Gdx.graphics.getDeltaTime)
    new_pos.y = position.y + (velocity.y * SPEED * Gdx.graphics.getDeltaTime)
    position = new_pos

    hitbox.posX = position.x
    hitbox.posY = position.y
  }

  override def draw(g: GdxGraphics): Unit = {
    g.drawFilledCircle(position.x, position.y, RADIUS, new Color(Color.RED))
  }
}

object Enemy {
  private var _allEnemy: ArrayBuffer[Enemy] = ArrayBuffer.empty

  def allEnemy: Array[Enemy] = _allEnemy.toArray

  def update(g: GdxGraphics, posHero: Vector2): Unit = {
    for (e <- allEnemy) {
      e.move(posHero)
      e.draw(g)
    }
  }

  def create(position: Vector2): Unit = {
    val enemy = new Enemy(position.x, position.y)
    _allEnemy.addOne(enemy)
  }

  def remove(e: Enemy): Unit = {
    val tmp: Int = _allEnemy.indexOf(e)
    _allEnemy.remove(tmp)
  }
}