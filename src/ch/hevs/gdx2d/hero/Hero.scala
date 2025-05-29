package ch.hevs.gdx2d.hero
import ch.hevs.gdx2d.components.bitmaps.Spritesheet
import ch.hevs.gdx2d.lib.GdxGraphics
import ch.hevs.gdx2d.lib.interfaces.DrawableObject
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.math.{Interpolation, Vector2}

class Hero(startX: Float, startY: Float) extends DrawableObject {
  private var _position: Vector2 = new Vector2(startX, startY)
  private var _velocity: Vector2 = new Vector2(0,0)

  private val SPEED: Float = 100f
  private val SPRITE_WIDTH: Int = 32
  private val SPRITE_HEIGHT: Int = 32
  private val SS = new Spritesheet("data/images/lumberjack_sheet32.png", SPRITE_WIDTH, SPRITE_HEIGHT);

  def velocity: Vector2 = _velocity
  def velocity_= (v: Vector2): Unit = {
    _velocity = v
  }

  def position: Vector2 = _position
  def position_= (pos: Vector2): Unit = {
    _position = pos
  }

  /**
   * Moves the player according to it's currently set velocity.
   */
  def move(): Unit = {
    val new_pos: Vector2 = new Vector2(0, 0)
    // .getDeltaTime is used so that the character moves at the same speed with any FPS set in the game
    new_pos.x = position.x + (velocity.x * SPEED * Gdx.graphics.getDeltaTime)
    new_pos.y = position.y + (velocity.y * SPEED * Gdx.graphics.getDeltaTime)
    position = new_pos
  }

  /**
   * Draws the hero sprite
   * @param g GdxGraphics object
   */
  def draw(g: GdxGraphics): Unit = {
    g.draw(SS.sprites(0)(0), position.x, position.y)
  }
}
