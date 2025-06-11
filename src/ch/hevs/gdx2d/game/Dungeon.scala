package ch.hevs.gdx2d.game
import ch.hevs.gdx2d.entity.{Hero, Projectile}
import ch.hevs.gdx2d.entity.enemies.{Boss, Enemy}
import ch.hevs.gdx2d.utility.{GameState, PositionXY}
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.TextureRegion
import com.badlogic.gdx.maps.MapObject
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer
import com.badlogic.gdx.maps.tiled.tiles.StaticTiledMapTile
import com.badlogic.gdx.math.Vector2

import java.awt.geom.Rectangle2D
import scala.collection.mutable.ArrayBuffer

class Dungeon(val width: Int, val height: Int, val totalRooms: Int) {
  val _map: Array[Array[Room]] = Array.fill(height, width)(null)
  var _currentPosX: Int = width / 2
  var _currentPosY: Int = height / 2


  var doorTexture = new TextureRegion(new Texture("data/images/door_open_64.png"))
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
    map(startY)(startX) = new Room("data/maps/spawnRoom.tmx",null)
    nbrOfCreatedRoom += 1

    while (nbrOfCreatedRoom != totalRooms) {
      val positions: Array[PositionXY] = possiblePosition(map)
      val pos: Int = (Math.round(Math.random() * (positions.length-1))).toInt
      if (nbrOfCreatedRoom == totalRooms - 1) {
        map(positions(pos).posY)(positions(pos).posX) = new Room(
          "data/maps/bossRoom.tmx",
          ArrayBuffer[Enemy](new Boss(960, 540, Array(
            new Vector2(200, 300),
            new Vector2(1000, 600),
            new Vector2(750, 800),
            new Vector2(500, 800),
            new Vector2(1250, 200),
          ))),
          true
        )
      }
      else {
        map(positions(pos).posY)(positions(pos).posX) = Room.getRandomRoom
      }
      nbrOfCreatedRoom += 1
    }


    displayConsole(map)

    for (l <- map.indices) {
      for (c <- map(l).indices) {
        if(map(l)(c) != null) {
          if(l - 1 >= 0 && map(l-1)(c) != null) {
            //UP
            map(l)(c).tiledLayer.setCell(15, 15, dCell)
            map(l)(c).createNewDoorHitbox(15*65, 15*64, 64, 66, "UP")
          }
          if(l + 1 <= map.length - 1 && map(l+1)(c) != null) {
            //DOWN
            map(l)(c).tiledLayer.setCell(15, 0, dCell)
            map(l)(c).createNewDoorHitbox(15*64, 0, 64, 66, "DOWN")
          }
          if(c - 1 >= 0 && map(l)(c-1) != null) {
            //LEFT
            map(l)(c).tiledLayer.setCell(0, 7, dCell)
            map(l)(c).createNewDoorHitbox(0, 7*64, 66, 64, "LEFT")
          }
          if(c + 1 <= map(0).length - 1 && map(l)(c+1) != null) {
            //RIGHT
            map(l)(c).tiledLayer.setCell(29, 7, dCell)
            map(l)(c).createNewDoorHitbox(29*64, 7*64, 66, 64, "RIGHT")
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

  def switchRoom(hero: Hero, door: MapObject): Unit = {
    val direction = door.getProperties.get("direction").toString
    val room = map(currentPosY)(currentPosX)
    val roomWidth = room.tiledLayer.getWidth
    val roomHeight = room.tiledLayer.getHeight

    Enemy.removeAll()
    Projectile.removeAll()

    direction match {
      case "LEFT" => {
        _currentPosX -= 1
        hero.position.x = (roomWidth - 2) * 64 - 10
      }
      case "RIGHT" => {
        _currentPosX += 1
        hero.position.x = 64 + 10
      }
      case "UP" =>{
        _currentPosY -= 1
        hero.position.y = 64 + 10
      }
      case "DOWN" => {
        _currentPosY += 1
        hero.position.y = (roomHeight - 2) * 64 - 10
      }
      case _ => println("wrong direction")
    }

    GameState.room = map(currentPosY)(currentPosX)

    if(map(currentPosY)(currentPosX).enemys != null) {
      for(e <- map(currentPosY)(currentPosX).enemys) {
        Enemy.add(e)
      }
    }
  }
}
