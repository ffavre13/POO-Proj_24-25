package game

import ch.hevs.gdx2d.components.bitmaps.Spritesheet
import ch.hevs.gdx2d.lib.GdxGraphics
import com.badlogic.gdx.utils.Align
import utility.GameState

object UserInterface {
  private val HP_SIZE: Int = 64   // Size of life point icon
  private val HP_SS: Spritesheet = new Spritesheet("images/heart.png", HP_SIZE, HP_SIZE)    // Sprite sheet for hero life point

  /**
   * Draw the user interface
   * @param g GdxGraphics object
   */
  def drawUI(g: GdxGraphics): Unit = {
    drawHealth(g, GameState.hero._hp)
  }

  /**
   * Display Boss life point
   * @param g  GdxGraphics object
   * @param hp  Boss life point
   */
  def drawBossHealth(g: GdxGraphics, hp: Int): Unit = {
    g.drawString(g.getScreenWidth, g.getScreenHeight, s"Boss: $hp HP", Align.right)
  }

  /**
   * Draw the hero life point
   * @param g GdxGraphics object
   * @param hp Life point
   */
  private def drawHealth(g: GdxGraphics, hp: Int): Unit = {
    var tmp: Int = 0
    for (h <- 0 until hp) {
      g.draw(HP_SS.sprites(0)(0), tmp + 20, g.getScreenHeight - HP_SIZE)
      tmp += HP_SIZE
    }

    for (h <- hp until GameState.hero.totalHP) {
      g.draw(HP_SS.sprites(0)(2), tmp + 20, g.getScreenHeight - HP_SIZE)
      tmp += HP_SIZE
    }
  }
}
