package game

import com.badlogic.gdx.maps.objects.RectangleMapObject
import com.badlogic.gdx.maps.{MapLayer, MapObject}
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer
import com.badlogic.gdx.maps.tiled.{TiledMapTileLayer, TmxMapLoader}
import entity.enemies.{Enemy, ShootingEnemies}
import utility.CollisionManager

import scala.collection.mutable.ArrayBuffer

class Room(tiledMapLocation: String, enemy: ArrayBuffer[Enemy], isBossRoom: Boolean = false) {
  var tiledMap = new TmxMapLoader().load(tiledMapLocation)
  var tiledMapRender = new OrthogonalTiledMapRenderer(tiledMap)

  var tiledLayer: TiledMapTileLayer = tiledMap.getLayers.get("map").asInstanceOf[TiledMapTileLayer]
  private var objectLayerWall: MapLayer = tiledMap.getLayers.get("collision")
  private var objectLayerDoor: MapLayer = tiledMap.getLayers.get("door_collision")

  var collisionsWall: Array[MapObject] = CollisionManager.getCollisions(objectLayerWall)
  var rectangleCollisionsWall = CollisionManager.getRectangles2D(collisionsWall)

  var collisionsDoor: Array[MapObject] = CollisionManager.getCollisions(objectLayerDoor)
  var rectangleCollisionsDoor = CollisionManager.getRectangles2D(collisionsDoor)

  var enemys: ArrayBuffer[Enemy] = enemy

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
  var allRoomsFile: Array[(String, ArrayBuffer[Enemy])] = Array(
    ("data/maps/room01.tmx", ArrayBuffer(
      new ShootingEnemies(736,416,true, true, true, true),
      new ShootingEnemies(736,544,true, true, true, true),
      new ShootingEnemies(1248,416,true, true, true, true),
      new ShootingEnemies(1248,544,true, true, true, true),
      new ShootingEnemies(928,736,true, true, true, true),
      new ShootingEnemies(1056,736,true, true, true, true),
      new ShootingEnemies(928,288,true, true, true, true),
      new ShootingEnemies(1056,288,true, true, true, true))),
    ("data/maps/room02.tmx", ArrayBuffer(
      new ShootingEnemies(416,416,true, true, false, false),
      new ShootingEnemies(544,608,true, true, false, false),
      new ShootingEnemies(672,416,true, true, true, true),
      new ShootingEnemies(800,608,true, true, true, true),
      new ShootingEnemies(928,416,true, true, false, false),
      new ShootingEnemies(1056,608,true, true, false, false),
      new ShootingEnemies(1184,416,true, true, true, true),
      new ShootingEnemies(1312,608,true, true, true, true),
      new ShootingEnemies(1440,416,true, true, false, false)
    )),
    ("data/maps/room03.tmx", ArrayBuffer(
      new ShootingEnemies(160,160,true, true, true, true),
      new ShootingEnemies(1790,160,true, true, true, true),
      new ShootingEnemies(1790,864,true, true, true, true),
      new ShootingEnemies(160,864,true, true, true, true)
    )),
    ("data/maps/room04.tmx", ArrayBuffer(
      new ShootingEnemies(160,160,true, true, false, false),
      new ShootingEnemies(672,160,true, true, true, true),
      new ShootingEnemies(160,800,true, true, false, false),
      new ShootingEnemies(672,800,true, true, true, true),
      new ShootingEnemies(1312,160,true, true, false, false),
      new ShootingEnemies(1760,160,true, true, true, true),
      new ShootingEnemies(1312,800,true, true, false, false),
      new ShootingEnemies(1760,800,true, true, true, true)
    ))
  )


  def getRandomRoom: Room = {
    val roomInfo: (String, ArrayBuffer[Enemy]) = allRoomsFile(Math.round(Math.random()*(allRoomsFile.length-1)).toInt)
    new Room(roomInfo._1,roomInfo._2.clone())
  }
}