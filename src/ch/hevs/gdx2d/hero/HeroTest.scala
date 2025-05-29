package ch.hevs.gdx2d.hero

import ch.hevs.gdx2d.desktop.{PortableApplication, Xbox}
import ch.hevs.gdx2d.lib.GdxGraphics
import ch.hevs.gdx2d.lib.utils.Logger
import com.badlogic.gdx.Input
import com.badlogic.gdx.controllers.Controller
import com.badlogic.gdx.math.Vector2

class HeroTest extends PortableApplication(1920, 1080) {
  var hero: Hero = null
  var ctrl: Controller = null

  override def onInit(): Unit = {
    hero = new Hero(10, 10)

    setTitle("test")
  }

  override def onGraphicRender(g: GdxGraphics): Unit = {
    g.clear()

    hero.draw(g)
    hero.move()
  }

  // <editor-fold desc="Keyboard methods">
  override def onKeyDown(keycode: Int): Unit = {
    super.onKeyDown(keycode)

    var vel: Vector2 = hero.velocity

    keycode match {
      case Input.Keys.DOWN | Input.Keys.S  => vel.y += -1
      case Input.Keys.LEFT | Input.Keys.A  => vel.x += -1
      case Input.Keys.RIGHT | Input.Keys.D => vel.x += 1
      case Input.Keys.UP | Input.Keys.W    => vel.y += 1
      case _ => Logger.log(s"Key '$keycode' pressed")
    }

//    if (keycode == Input.Keys.DOWN) vel.y = -1
//    if (keycode == Input.Keys.LEFT) vel.x = -1
//    if (keycode == Input.Keys.RIGHT) vel.x = 1
//    if (keycode == Input.Keys.UP) vel.y = 1

    vel.nor()
    println(vel)
    hero.velocity = vel
  }

  override def onKeyUp(keycode: Int): Unit = {
    var vel: Vector2 = hero.velocity

    keycode match {
      case Input.Keys.DOWN | Input.Keys.UP | Input.Keys.S | Input.Keys.W    => vel.y = 0
      case Input.Keys.LEFT | Input.Keys.RIGHT | Input.Keys.A | Input.Keys.D => vel.x = 0
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
  // </editor-fold>
}

object HeroTest extends App {
  new HeroTest()
}
