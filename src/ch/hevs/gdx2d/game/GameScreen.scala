package ch.hevs.gdx2d.game

import ch.hevs.gdx2d.desktop.{PortableApplication, Xbox}
import ch.hevs.gdx2d.entity.{Hero, Projectile}
import ch.hevs.gdx2d.lib.GdxGraphics
import ch.hevs.gdx2d.lib.utils.Logger
import com.badlogic.gdx.{Gdx, Input}
import com.badlogic.gdx.controllers.Controller
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer
import com.badlogic.gdx.maps.tiled.{TiledMap, TiledMapRenderer, TiledMapTile, TiledMapTileLayer, TmxMapLoader}
import com.badlogic.gdx.math.Vector2

class GameScreen extends PortableApplication(1920, 1080) {
  var hero: Hero = null
  var ctrl: Controller = null
  private var tiledMap: TiledMap = null
  private var tiledMapRenderer: TiledMapRenderer = null
  private var tiledLayer: TiledMapTileLayer = null
  private var zoom: Float = 0

  override def onInit(): Unit = {
    setTitle("The binding of Isaac")

    tiledMap = new TmxMapLoader().load("data/maps/desert.tmx")
    tiledMapRenderer = new OrthogonalTiledMapRenderer(tiledMap)
    tiledLayer = tiledMap.getLayers.get(0).asInstanceOf[TiledMapTileLayer]

    hero = new Hero(getWindowWidth/2, getWindowHeight/2)
  }

  override def onGraphicRender(g: GdxGraphics): Unit = {
    g.clear()

    hero.move()

    tiledMapRenderer.setView(g.getCamera)
    tiledMapRenderer.render()

    hero.draw(g)
    Projectile.update(g)
    g.drawFPS()
  }

  // <editor-fold desc="Keyboard methods">
  override def onKeyDown(keycode: Int): Unit = {
    super.onKeyDown(keycode)

    var vel: Vector2 = hero.velocity

    keycode match {
      case Input.Keys.S  => vel.y += -1
      case Input.Keys.A  => vel.x += -1
      case Input.Keys.D => vel.x += 1
      case Input.Keys.W    => vel.y += 1
      case Input.Keys.SPACE => hero.shoot()
      case _ => Logger.log(s"Key '$keycode' pressed")
    }
    hero.velocity = vel
  }

  override def onKeyUp(keycode: Int): Unit = {
    var vel: Vector2 = hero.velocity

    keycode match {
      case Input.Keys.S  => vel.y -= -1
      case Input.Keys.A  => vel.x -= -1
      case Input.Keys.D => vel.x -= 1
      case Input.Keys.W    => vel.y -= 1
      case _ => Logger.log(s"Key '$keycode' pressed")
    }
    hero.velocity = vel
  }

  // </editor-fold>

  // <editor-fold desc="Controller methods">
  override def onControllerConnected(controller: Controller): Unit = {
    println("A controller has been connected !")
    ctrl = controller
  }

  override def onControllerDisconnected(controller: Controller): Unit = {
    println("A controller has been disconnected !")
    ctrl = null
  }

  override def onControllerAxisMoved(controller: Controller, axisCode: Int, value: Float): Unit = {
    super.onControllerAxisMoved(controller, axisCode, value)
    if (axisCode == Xbox.L_STICK_HORIZONTAL_AXIS) hero.velocity.x = value
    if (axisCode == Xbox.L_STICK_VERTICAL_AXIS) hero.velocity.y = -value
  }

  private def isWalkable(tile: TiledMapTile): Boolean = {
    if (tile == null) return false
    val test = tile.getProperties.get("walkable")
    return test.toString.toBoolean
  }

  private def getSpeed(tile: TiledMapTile): Float = {
    if (tile == null) return 100f // Default speed
    val test = tile.getProperties.get("speed")
    return test.toString.toFloat
  }
}

object GameScreen extends App {
  new GameScreen
}