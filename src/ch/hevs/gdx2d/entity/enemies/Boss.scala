package ch.hevs.gdx2d.entity.enemies

import ch.hevs.gdx2d.entity.Projectile
import ch.hevs.gdx2d.game.UserInterface
import ch.hevs.gdx2d.lib.GdxGraphics
import ch.hevs.gdx2d.utility.GameState
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.math.Vector2

import scala.util.Random

/**
 * The Boss. A very powerful enemy, that moves and shoots
 * @param posX Starting X position of the boss
 * @param posY Starting Y position of the boss
 * @param positions Array containing the positions where the boss will move
 */
class Boss(posX: Int, posY: Int, positions: Array[Vector2]) extends Enemy(posX, posY) {
  override var _hp: Int = 20
  override val SPRITE_WIDTH: Int = 128
  override val SPRITE_HEIGHT: Int = 128

  private val SHOOT_COOLDOWN: Float = 0.2f      // Time to wait between 2 shots
  private val TIME_BETWEEN_PHASES: Float = 10f  // Time between the 2 boss phases
  private val BULLET_SPEED: Float = 2f          // Speed multiplier for the bullets
  private var shootAngle: Float = 10            // Angle that is added each time the boss shoots

  private var dt_shoot: Float = 0   // Time used to calculate the shoot cooldown
  private var dt_phases: Float = 0  // Time used to calculate the time between phases
  private var _velocity: Vector2 = new Vector2(0, 0)
  private var _speed: Float = 100
  private var targetPosition: Vector2 = null
  private val currVelocity_1: Vector2 = new Vector2(0, BULLET_SPEED) // Bullet Velocity
  private val currVelocity_2: Vector2 = new Vector2(0, -BULLET_SPEED)
  private val currVelocity_3: Vector2 = new Vector2(BULLET_SPEED, 0)
  private val currVelocity_4: Vector2 = new Vector2(-BULLET_SPEED, 0)

  override def update(elapsedTime: Float): Unit = {
    super.update(elapsedTime)
    dt_shoot = elapsedTime + dt_shoot
    dt_phases = elapsedTime + dt_phases
    move()
    if (dt_shoot > SHOOT_COOLDOWN) {
      shoot(dt_shoot)
      dt_shoot = 0
    }
    if (dt_phases > TIME_BETWEEN_PHASES) {
      shootAngle = -shootAngle
      dt_phases = 0
    }
  }

  override def draw(g: GdxGraphics): Unit = {
    g.drawFilledCircle(position.x, position.y, SPRITE_WIDTH/2, Color.RED)
    UserInterface.drawBossHealth(g, _hp)
  }

  /**
   * Shoots projectiles in 4 directions
   * @param dt Time elapsed between two frames
   */
  private def shoot(dt: Float): Unit = {
    Projectile.create(new Vector2(position.x, position.y), currVelocity_1, "ENEMY")
    Projectile.create(new Vector2(position.x, position.y), currVelocity_2, "ENEMY")
    Projectile.create(new Vector2(position.x, position.y), currVelocity_3, "ENEMY")
    Projectile.create(new Vector2(position.x, position.y), currVelocity_4, "ENEMY")
    currVelocity_1.rotate(shootAngle)
    currVelocity_3.rotate(shootAngle)
    currVelocity_2.rotate(shootAngle)
    currVelocity_4.rotate(shootAngle)
  }

  /** Moves the boss to a random position (provided by positions array) */
  private def move(): Unit = {
    if (targetPosition == null) targetPosition = Random.shuffle(positions.toList).head

    val new_pos: Vector2 = new Vector2(0, 0)

    _velocity.x = targetPosition.x - position.x
    _velocity.y = targetPosition.y - position.y

    _velocity.nor()

    new_pos.x = position.x + (_velocity.x * _speed * Gdx.graphics.getDeltaTime)
    new_pos.y = position.y + (_velocity.y * _speed * Gdx.graphics.getDeltaTime)
    position = new_pos

    if (position.dst(targetPosition) <= 10) targetPosition = null
  }
}
