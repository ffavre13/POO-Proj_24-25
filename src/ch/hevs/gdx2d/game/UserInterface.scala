package ch.hevs.gdx2d.game

import ch.hevs.gdx2d.components.bitmaps.Spritesheet
import ch.hevs.gdx2d.lib.GdxGraphics
import ch.hevs.gdx2d.utility.GameState

object UserInterface {
  private val HP_SIZE: Int = 64
  private val HP_SS: Spritesheet = new Spritesheet("images/heart.png", HP_SIZE, HP_SIZE)

  def drawUI(g: GdxGraphics): Unit = {
    drawHealth(g, GameState.hero._hp)
  }

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
