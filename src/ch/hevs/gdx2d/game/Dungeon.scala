package ch.hevs.gdx2d.game
import ch.hevs.gdx2d.entity.Hero
import ch.hevs.gdx2d.utility.PositionXY

import scala.collection.mutable.ArrayBuffer

class Dungeon(val width: Int, val height: Int, val totalRooms: Int) {
  val _map: Array[Array[Room]] = Array.fill(height, width)(null)
  var _currentPosX: Int = width / 2
  var _currentPosY: Int = height / 2

  def currentPosX: Int = _currentPosX

  def currentPosY: Int = _currentPosY

  def map: Array[Array[Room]] = _map

  def generate(): Unit = {
    val startY = height / 2
    val startX = width / 2
    var nbrOfCreatedRoom: Int = 0
    map(startY)(startX) = new Room(Room.generateLayout(),null)
    nbrOfCreatedRoom += 1

    while (nbrOfCreatedRoom != totalRooms) {
      val positions: Array[PositionXY] = possiblePosition(map)
      val pos: Int = (Math.random() * (positions.length-1)).toInt
      map(positions(pos).posY)(positions(pos).posX) = new Room(Room.generateLayout(),null)
      nbrOfCreatedRoom += 1
    }

    displayConsole(map)

    for (l <- map.indices) {
      for (c <- map(l).indices) {
        if(map(l)(c) != null) {
          if(l - 1 >= 0 && map(l-1)(c) != null) {
            map(l)(c).layout(0)(15) = 2
          }
          if(l + 1 <= map.length - 1 && map(l+1)(c) != null) {
            map(l)(c).layout(15)(15) = 2
          }
          if(c - 1 >= 0 && map(l)(c-1) != null) {
            map(l)(c).layout(8)(0) = 2
          }
          if(c + 1 <= map(0).length - 1 && map(l)(c+1) != null) {
            map(l)(c).layout(8)(29) = 2
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

  def trySwitchRoom(hero: Hero): Unit = {
    val layout = map(currentPosY)(currentPosX).layout
    try {
      val tileX = (hero.position.x / 64).toInt
      val tileY = (hero.position.y / 64).toInt

      if (layout(tileY)(tileX) == 2) {
        if (tileY == 0 && currentPosY > 0) {
          _currentPosY -= 1
          hero.position.y = (layout.length - 2) * 64
        } else if (tileY == layout.length - 1 && currentPosY < height - 1) {
          _currentPosY += 1
          hero.position.y = 64
        } else if (tileX == 0 && currentPosX > 0) {
          _currentPosX -= 1
          hero.position.x = (layout(0).length - 2) * 64
        } else if (tileX == layout(0).length - 1 && currentPosX < width - 1) {
          _currentPosX += 1
          hero.position.x = 64
        }
      }
    }
    catch {
      case e: Exception =>
    }

  }
}
