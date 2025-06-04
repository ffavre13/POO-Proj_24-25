package ch.hevs.gdx2d.game

import ch.hevs.gdx2d.entity.Enemy
import ch.hevs.gdx2d.lib.GdxGraphics
import com.badlogic.gdx.graphics.Texture

class Room(lt: Array[Array[Int]], enemy: Array[Enemy]) {
  val _layout = lt
  val tilesize: Int = 64
  val rows: Int = layout.length
  val cols: Int = layout(0).length

  val wallTexture = new Texture("data/images/wall.png")
  val floorTexture = new Texture("data/images/floor.png")
  val doorTexture = new Texture("data/images/door.jpg")

  def layout: Array[Array[Int]] = _layout

  def display(g: GdxGraphics): Unit = {
    for (y <- 0 until rows; x <- 0 until cols) {
      layout(y)(x) match {
        case 0 => g.draw(floorTexture, x * tilesize, y * tilesize)
        case 1 => g.draw(wallTexture, x * tilesize, y * tilesize)
        case 2 => g.draw(doorTexture, x * tilesize, y * tilesize)
        case _ => () // rien si code inconnu
      }
    }
  }
}

object Room {
  def generateLayout(): Array[Array[Int]] = {
    val width: Int = 30
    val height: Int = 16
    var result: Array[Array[Int]] = Array.ofDim(height, width)
    for (l <- result.indices) {
      for (c <- result(l).indices) {
        result(l)(c) = if (l == 0 || c == 0 || l == height - 1 || c == width - 1) 1 else 0
      }
    }
    result
  }
}
