package ch.hevs.gdx2d.hitbox

import ch.hevs.gdx2d.lib.GdxGraphics

case class RectangleHitbox(var posX: Float, var posY: Float, width: Int, height: Int) extends Hitbox {
  override def intersects(other: Hitbox): Boolean = {
    other match {
      case RectangleHitbox(pX, pY, w, h) => {
        return posX < pX + w && posX + width > pX && posY < pY + h && posY + height > pY
      }
      case CircleHitbox(pX, pY, r) => {
        val closestX = math.max(posX, math.min(pX, posX + width))
        val closestY = math.max(posY, math.min(pY, posY + height))
        val dx = pX - closestX
        val dy = pY - closestY
        return dx * dx + dy * dy < r * r
      }
    }
  }

  override def draw(g: GdxGraphics): Unit = {
    g.drawRectangle(posX,posY,width,height, 90)
  }
}
