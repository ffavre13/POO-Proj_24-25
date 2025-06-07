package ch.hevs.gdx2d.game

import ch.hevs.gdx2d.utility.CollisionManager
import ch.hevs.gdx2d.entity.enemies.Enemy
import ch.hevs.gdx2d.lib.GdxGraphics
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.maps.objects.RectangleMapObject
import com.badlogic.gdx.maps.{MapLayer, MapObject, MapProperties}
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer
import com.badlogic.gdx.maps.tiled.{TiledMapTileLayer, TmxMapLoader}

class Room(tiledMapLocation: String, enemy: Array[Enemy], isBossRoom: Boolean = false) {
  var tiledMap = new TmxMapLoader().load(tiledMapLocation)
  var tiledMapRender = new OrthogonalTiledMapRenderer(tiledMap)

  var tiledLayer: TiledMapTileLayer = tiledMap.getLayers.get("map").asInstanceOf[TiledMapTileLayer]
  private var objectLayerWall: MapLayer = tiledMap.getLayers.get("collision")
  private var objectLayerDoor: MapLayer = tiledMap.getLayers.get("door_collision").asInstanceOf[MapLayer]

  var collisionsWall: Array[MapObject] = CollisionManager.getCollisions(objectLayerWall)
  var rectangleCollisionsWall = CollisionManager.getRectangles2D(collisionsWall)

  var collisionsDoor: Array[MapObject] = CollisionManager.getCollisions(objectLayerDoor)
  var rectangleCollisionsDoor = CollisionManager.getRectangles2D(collisionsDoor)

  def addDoor(door: RectangleMapObject): Unit = {
    objectLayerDoor.getObjects.add(door)

    collisionsDoor = CollisionManager.getCollisions(objectLayerDoor)
    rectangleCollisionsDoor = CollisionManager.getRectangles2D(collisionsDoor)
  }

  def createNewDoorHitbox(x: Float, y: Float, width: Float, height: Float, direction: String): Unit = {
    val rectangle = new RectangleMapObject(x,y,width,height)
    val props = rectangle.getProperties
    props.put("x",x)
    props.put("y",y)
    props.put("width",width)
    props.put("height",height)
    props.put("direction", direction)
    addDoor(rectangle)
  }

  def roomIsClear(): Boolean = {
    if(enemy == null) {
      true
    }
    else {
      false
    }
  }
}

object Room {
  var allRoomsFile: Array[(String, Array[Enemy])] = Array(
    ("data/maps/room01.tmx", null),
    ("data/maps/room02.tmx", null),
    ("data/maps/room03.tmx", null),
    ("data/maps/room04.tmx", null),
    ("data/maps/room05.tmx", null),
    ("data/maps/room06.tmx", null)
  )


  def getRandomRoom: Room = {
    val roomInfo: (String, Array[Enemy]) = allRoomsFile(Math.round(Math.random()*(allRoomsFile.length-1)).toInt)
    new Room(roomInfo._1,roomInfo._2)
  }
}