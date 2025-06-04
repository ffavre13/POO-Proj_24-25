package ch.hevs.gdx2d.game

import ch.hevs.gdx2d.CollisionManager
import ch.hevs.gdx2d.entity.Enemy
import ch.hevs.gdx2d.lib.GdxGraphics
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.maps.{MapLayer, MapObject}
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer
import com.badlogic.gdx.maps.tiled.{TiledMapTileLayer, TmxMapLoader}

class Room(tiledMapLocation: String, enemy: Array[Enemy]) {
  var tiledMap = new TmxMapLoader().load(tiledMapLocation)
  var tiledMapRender = new OrthogonalTiledMapRenderer(tiledMap)
  var tiledLayer: TiledMapTileLayer = tiledMap.getLayers.get("map").asInstanceOf[TiledMapTileLayer]
  var objectLayer: MapLayer = tiledMap.getLayers.get("collision")
  var collisions: Array[MapObject] = CollisionManager.getCollisions(objectLayer)
  var rectangleCollisions = CollisionManager.getRectangles2D(collisions)
}