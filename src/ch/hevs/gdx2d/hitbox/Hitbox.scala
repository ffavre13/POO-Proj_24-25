package ch.hevs.gdx2d.hitbox

import ch.hevs.gdx2d.lib.GdxGraphics

trait Hitbox {
  def intersects(other: Hitbox): Boolean

  def draw(g: GdxGraphics): Unit
}
