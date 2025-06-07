package ch.hevs.gdx2d.entity.enemies

import ch.hevs.gdx2d.entity.Projectile
import ch.hevs.gdx2d.lib.GdxGraphics
import ch.hevs.gdx2d.utility.GameState
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.math.Vector2

class TargetPlayerEnemies(posX: Int, posY: Int) extends Enemy(posX, posY) {
  override var _life: Int = 1
  var speed: Int = 100

  private var _velocity: Vector2 = new Vector2(0, 0)

  override def update(elapsedTime: Float): Unit = {
    super.update(elapsedTime)
    move()
  }

  override def draw(g: GdxGraphics): Unit = {
    g.drawFilledCircle(position.x, position.y, SPRITE_WIDTH/2, new Color(Color.BLACK))
  }

  private def move(): Unit = {
    val new_pos: Vector2 = new Vector2(0, 0)

    _velocity.x = GameState.hero.position.x - position.x
    _velocity.y = GameState.hero.position.y - position.y

    _velocity.nor()

    new_pos.x = position.x + (_velocity.x * speed * Gdx.graphics.getDeltaTime)
    new_pos.y = position.y + (_velocity.y * speed * Gdx.graphics.getDeltaTime)
    position = new_pos
  }
}
