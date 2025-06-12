package entity.enemies

import ch.hevs.gdx2d.lib.GdxGraphics
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.math.Vector2
import entity.Projectile

class ShootingEnemies(posX: Int, posY: Int, shootUp: Boolean, shootDown: Boolean, shootLeft: Boolean, shootRight: Boolean) extends Enemy(posX, posY) {

  override var _hp: Int = 1     // life point

  private val SHOOT_COOLDOWN: Float = 1.0f    // Cooldown between all enemy fire
  private var dt: Float = 0 // Time used to calculate the cooldown

  /**
   * Updates the enemy (meant to be called on each frame generation)
   * @param elapsedTime Time elapsed between two frames
   */
  override def update(elapsedTime: Float): Unit = {
    super.update(elapsedTime)
    dt = elapsedTime + dt
    if (dt > SHOOT_COOLDOWN) {
      shoot()
      dt = 0
    }
  }

  /**
   * Draws the enemy on the screen
   * @param g GdxGraphics object
   */
  override def draw(g: GdxGraphics): Unit = {
    g.drawFilledCircle(position.x, position.y, SPRITE_WIDTH/2, Color.BLACK)
  }

  /**
   * Manages enemies fire
   */
  private def shoot(): Unit = {
    if (shootUp)    Projectile.create(new Vector2(posX, posY), new Vector2(0, 1), "ENEMY")
    if (shootDown)  Projectile.create(new Vector2(posX, posY), new Vector2(0, -1), "ENEMY")
    if (shootLeft)  Projectile.create(new Vector2(posX, posY), new Vector2(-1, 0), "ENEMY")
    if (shootRight) Projectile.create(new Vector2(posX, posY), new Vector2(1, 0), "ENEMY")
  }
}
