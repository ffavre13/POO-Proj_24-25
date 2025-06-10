package ch.hevs.gdx2d.entity.enemies

import ch.hevs.gdx2d.entity.Projectile
import ch.hevs.gdx2d.lib.GdxGraphics
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.math.Vector2

class Boss(posX: Int, posY: Int) extends Enemy(posX, posY) {
  override var _hp: Int = 3
  override val SPRITE_WIDTH: Int = 128
  override val SPRITE_HEIGHT: Int = 128

  private val SHOOT_COOLDOWN: Float = 0.2f
  private val BULLET_SPEED: Float = 2f

  private var dt: Float = 0 // Time used to calculate the cooldown
  private val currVelocity_1: Vector2 = new Vector2(0, BULLET_SPEED)
  private val currVelocity_2: Vector2 = new Vector2(0, -BULLET_SPEED)
  private val currVelocity_3: Vector2 = new Vector2(BULLET_SPEED, 0)
  private val currVelocity_4: Vector2 = new Vector2(-BULLET_SPEED, 0)

  override def update(elapsedTime: Float): Unit = {
    super.update(elapsedTime)
    dt = elapsedTime + dt
    if (dt > SHOOT_COOLDOWN) {
      shoot(dt)
      dt = 0
    }
  }

  override def draw(g: GdxGraphics): Unit = {
    g.drawFilledCircle(position.x, position.y, SPRITE_WIDTH/2, Color.RED)
  }

  private def shoot(dt: Float): Unit = {
    Projectile.create(new Vector2(posX, posY), currVelocity_1, "ENEMY")
    Projectile.create(new Vector2(posX, posY), currVelocity_2, "ENEMY")
    Projectile.create(new Vector2(posX, posY), currVelocity_3, "ENEMY")
    Projectile.create(new Vector2(posX, posY), currVelocity_4, "ENEMY")
    currVelocity_1.rotate(10)
    currVelocity_3.rotate(10)
    currVelocity_2.rotate(10)
    currVelocity_4.rotate(10)
  }
}
