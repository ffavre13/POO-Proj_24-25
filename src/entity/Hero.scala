package entity

import ch.hevs.gdx2d.components.bitmaps.Spritesheet
import ch.hevs.gdx2d.lib.GdxGraphics
import ch.hevs.gdx2d.lib.interfaces.DrawableObject
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.math.Vector2
import utility.{AudioManager, GameState}

import java.awt.geom.Rectangle2D

class Hero(startX: Float, startY: Float) extends DrawableObject with Entity {
  private val SHOOT_COOLDOWN : Float = 0.5f
  private val SPRITE_WIDTH: Int = 64
  private val SPRITE_HEIGHT: Int = 64
  private val SS = new Spritesheet("data/images/walk.png", SPRITE_WIDTH, SPRITE_HEIGHT)

  override var _hp: Int = 3
  private val _totalHP: Int = _hp
  private var _speed: Float = 400f
  private var _position: Vector2 = new Vector2(startX, startY)
  private var _velocity: Vector2 = new Vector2(0,0)
  private var _hitbox: Rectangle2D = new Rectangle2D.Float(startX, startY, SPRITE_WIDTH.toFloat, SPRITE_HEIGHT.toFloat)

  private var _direction: Vector2 = new Vector2(0,1)
  private var textureX: Int = 0
  private var dt_shoot: Float = 0

  def hitbox: Rectangle2D = _hitbox

  def totalHP: Int = _totalHP

  def speed: Float = _speed
  def speed_= (newSpeed: Float): Unit = {
    _speed = newSpeed
  }

  def velocity: Vector2 = _velocity
  def velocity_= (newVelocity: Vector2): Unit = {
    _velocity = newVelocity
  }

  def position: Vector2 = _position
  def position_= (newPosition: Vector2): Unit = {
    _position = newPosition
  }

  def direction: Vector2 = _direction
  def direction_= (newDirection: Vector2): Unit = {
    _direction = newDirection
  }

  def update(g: GdxGraphics): Unit = {
    move()
    _hitbox = new Rectangle2D.Float(position.x, position.y, SPRITE_WIDTH.toFloat, SPRITE_HEIGHT.toFloat)
    dt_shoot = Gdx.graphics.getDeltaTime + dt_shoot
    draw(g)
  }

  /**
   * Moves the player according to it's currently set velocity.
   */
  def move(): Unit = {
    val new_pos: Vector2 = new Vector2(0, 0)
    // .getDeltaTime is used so that the character moves at the same speed with any FPS set in the game
    new_pos.x = position.x + (velocity.x * speed * Gdx.graphics.getDeltaTime)
    new_pos.y = position.y + (velocity.y * speed * Gdx.graphics.getDeltaTime)
    position = new_pos
  }

  def turn(dir: String): Unit = {
    dir match {
      case "UP" => {
        direction.x = 0
        direction.y = 1
        textureX = 0
      }
      case "DOWN" => {
        direction.x = 0
        direction.y = -1
        textureX = 2
      }
      case "RIGHT" => {
        direction.x = 1
        direction.y = 0
        textureX = 3
      }
      case "LEFT" => {
        direction.x = -1
        direction.y = 0
        textureX = 1
      }
      case _ => println("Wrong direction")
    }
  }

  /**
   * Draws the hero sprite
   * @param g GdxGraphics object
   */
  def draw(g: GdxGraphics): Unit = {
    g.draw(SS.sprites(textureX)(0), position.x - 17, position.y - 10, 100, 100)
  }

  def drawHitbox(g: GdxGraphics): Unit = {
    g.drawRectangle(hitbox.getX.toFloat + SPRITE_WIDTH/2, hitbox.getY.toFloat+ SPRITE_HEIGHT/2, hitbox.getWidth.toFloat, hitbox.getHeight.toFloat, 90)
  }

  def shoot(): Unit = {
    if (dt_shoot > SHOOT_COOLDOWN) {
      AudioManager.shoot()
      val projVel = new Vector2()
      projVel.x = direction.x
      projVel.y = direction.y

      val projPos = new Vector2()
      projPos.x = position.x + SPRITE_WIDTH / 2
      projPos.y = position.y + SPRITE_HEIGHT / 2

      Projectile.create(projPos, projVel, "HERO")
      dt_shoot = 0
    }
  }

  override def takeDamage(amount: Int): Unit = {
    super.takeDamage(amount)
    AudioManager.damage()
    println(s"Hero took damage ! (HP : $hp)")
  }

  override def ko(): Unit = {
    println(s"Hero is KO ! (HP : $hp)")
  }
}