package ch.hevs.gdx2d.hitbox

import ch.hevs.gdx2d.lib.GdxGraphics

case class CircleHitbox(var posX: Float, var posY: Float, radius: Int) extends Hitbox {
  override def intersects(other: Hitbox): Boolean = {
    other match {
      case CircleHitbox(pX, pY, r) => {
        val dx = posX - pX
        val dy = posY - pY
        val distanceSq = dx * dx + dy * dy
        distanceSq < (radius + r) * (radius + r)
      }
      case r: RectangleHitbox => r.intersects(this)
    }
  }

  override def draw(g: GdxGraphics): Unit = {
    g.drawCircle(posX, posY, radius)
  }
}
