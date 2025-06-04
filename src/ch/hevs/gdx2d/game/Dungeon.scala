package ch.hevs.gdx2d.game
import ch.hevs.gdx2d.entity.Hero
import ch.hevs.gdx2d.utility.PositionXY
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.TextureRegion
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer
import com.badlogic.gdx.maps.tiled.tiles.StaticTiledMapTile

import scala.collection.mutable.ArrayBuffer

class Dungeon(val width: Int, val height: Int, val totalRooms: Int) {
  val _map: Array[Array[Room]] = Array.fill(height, width)(null)
  var _currentPosX: Int = width / 2
  var _currentPosY: Int = height / 2


  var doorTexture = new TextureRegion(new Texture("data/images/door.jpg"))
  doorTexture = new TextureRegion(doorTexture, 0, 0, 64, 64)

  val doorTile = new StaticTiledMapTile(doorTexture)

  val dCell = new TiledMapTileLayer.Cell()
  dCell.setTile(doorTile)

  def currentPosX: Int = _currentPosX

  def currentPosY: Int = _currentPosY

  def map: Array[Array[Room]] = _map

  def generate(): Unit = {
    val startY = height / 2
    val startX = width / 2
    var nbrOfCreatedRoom: Int = 0
    map(startY)(startX) = new Room("data/maps/map.tmx",null)
    nbrOfCreatedRoom += 1

    while (nbrOfCreatedRoom != totalRooms) {
      val positions: Array[PositionXY] = possiblePosition(map)
      val pos: Int = (Math.random() * (positions.length-1)).toInt
      map(positions(pos).posY)(positions(pos).posX) = new Room("data/maps/map.tmx",null)
      nbrOfCreatedRoom += 1
    }

    displayConsole(map)

    for (l <- map.indices) {
      for (c <- map(l).indices) {
        if(map(l)(c) != null) {
          if(l - 1 >= 0 && map(l-1)(c) != null) {
            map(l)(c).tiledLayer.setCell(15, 15, dCell)
          }
          if(l + 1 <= map.length - 1 && map(l+1)(c) != null) {
            map(l)(c).tiledLayer.setCell(15, 0, dCell)
          }
          if(c - 1 >= 0 && map(l)(c-1) != null) {
            map(l)(c).tiledLayer.setCell(0, 7, dCell)
          }
          if(c + 1 <= map(0).length - 1 && map(l)(c+1) != null) {
            map(l)(c).tiledLayer.setCell(29, 7, dCell)
          }
        }
      }
    }

  }

  def possiblePosition(grid: Array[Array[Room]]): Array[PositionXY] = {
    var result: ArrayBuffer[PositionXY] = ArrayBuffer.empty

    for(l <- grid.indices) {
      for(c <- grid(l).indices) {
        if (grid(l)(c) != null) {
          if(l - 1 >= 0 && grid(l-1)(c) == null) {
            result.addOne(new PositionXY(l-1,c))
          }
          if(l + 1 <= grid.length - 1 && grid(l+1)(c) == null) {
            result.addOne(new PositionXY(l + 1, c))
          }
          if(c - 1 >= 0 && grid(l)(c-1) == null) {
            result.addOne(new PositionXY(l, c - 1))
          }
          if(c + 1 <= grid(0).length - 1 && grid(l)(c+1) == null) {
            result.addOne(new PositionXY(l, c+1))
          }
        }
      }
    }

    return result.toArray
  }

  def displayConsole(grid: Array[Array[Room]]): Unit = {
    for(x <- map.indices) {
      for(y <- map(x).indices) {
        if(map(x)(y) == null) {
          print("0 ")
        }
        else {
          print("1 ")
        }
      }
      print("\n")
    }
  }

//  def trySwitchRoom(hero: Hero): Unit = {
//    val tiledMap = map(currentPosY)(currentPosX).tiledMap
//    try {
//      val tileX = (hero.position.x / 64).toInt
//      val tileY = (hero.position.y / 64).toInt
//
//      if (layout(tileY)(tileX) == 2) {
//        if (tileY == 0 && currentPosY > 0) {
//          _currentPosY -= 1
//          hero.position.y = (layout.length - 2) * 64
//        } else if (tileY == layout.length - 1 && currentPosY < height - 1) {
//          _currentPosY += 1
//          hero.position.y = 64
//        } else if (tileX == 0 && currentPosX > 0) {
//          _currentPosX -= 1
//          hero.position.x = (layout(0).length - 2) * 64
//        } else if (tileX == layout(0).length - 1 && currentPosX < width - 1) {
//          _currentPosX += 1
//          hero.position.x = 64
//        }
//      }
//    }
//    catch {
//      case e: Exception =>
//    }
//
//  }
}
