package Hero

import ch.hevs.gdx2d.components.bitmaps.Spritesheet
import ch.hevs.gdx2d.lib.GdxGraphics
import ch.hevs.gdx2d.lib.interfaces.DrawableObject
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.math.{Interpolation, Vector2}

class Hero(x: Float, y: Float) extends DrawableObject {
  private var _position: Vector2 = new Vector2(x, y)
  private var _velocity: Vector2 = new Vector2(0, 0)

  private val _SS: Spritesheet = new Spritesheet("SproutLandsSprites/Characters/Basic Charakter Spritesheet.png", 48, 48)
  private val _SPEED: Float = 100

  // <editor-fold desc="Getter Setters">
  def velocity: Vector2 = _velocity
  def velocity_= (v: Vector2): Unit = _velocity = v
  // </editor-fold>

  // <editor-fold desc="Public Methods">

  /**
   * Moves the player according to it's currently set velocity.
   */
  def move(): Unit = {
    val new_pos = new Vector2(0, 0)
    // .getDeltaTime is used so that the character moves at the same speed with any FPS set in the game
    new_pos.x = _position.x + (_velocity.x * _SPEED * Gdx.graphics.getDeltaTime)
    new_pos.y = _position.y + (_velocity.y * _SPEED * Gdx.graphics.getDeltaTime)
    //    _position.interpolate(new_pos, Gdx.graphics.getDeltaTime, Interpolation.linear)
    _position = new_pos
  }

  /**
   * Draws the hero sprite
   * @param g GdxGraphics object
   */
  def draw(g: GdxGraphics): Unit = {
    g.draw(_SS.sprites(0)(0), _position.x, _position.y)
  }
  // </editor-fold>
}
