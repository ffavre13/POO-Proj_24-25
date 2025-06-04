package ch.hevs.gdx2d.game

import ch.hevs.gdx2d.CollisionManager
import ch.hevs.gdx2d.desktop.{PortableApplication, Xbox}
import ch.hevs.gdx2d.entity.{Enemy, Hero, Projectile}
import ch.hevs.gdx2d.lib.GdxGraphics
import ch.hevs.gdx2d.lib.utils.Logger
import com.badlogic.gdx.Input
import com.badlogic.gdx.controllers.Controller
import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.math.Vector2

class GameScreen extends PortableApplication(1920, 1080) {
  var hero: Hero = null
  var dungeon: Dungeon = null
  var ctrl: Controller = null
  var cam : OrthographicCamera = null

  private var drawHitbox: Boolean = false

  override def onInit(): Unit = {
    setTitle("The binding of Isaac")

    dungeon = new Dungeon(16,16, 3)
    dungeon.generate()

    hero = new Hero(getWindowWidth/2, getWindowHeight/2)


    for(i <- 0 until 3) {
      val posEnemy = new Vector2()
      posEnemy.x = 500 + i*30
      posEnemy.y = 500 + i*30
      Enemy.create(posEnemy)
    }
  }

  override def onGraphicRender(g: GdxGraphics): Unit = {
    g.clear()

    dungeon.map(dungeon.currentPosY)(dungeon.currentPosX).tiledMapRender.setView(g.getCamera)
    dungeon.map(dungeon.currentPosY)(dungeon.currentPosX).tiledMapRender.render()

    checkCollision()
    hero.update(g)
    Projectile.update(g)
    Enemy.update(g, hero.position)


    if(drawHitbox) {
      displayHitbox(g)
    }

    checkHitbox()


    g.drawFPS()
  }

  // <editor-fold desc="Keyboard methods">
  override def onKeyDown(keycode: Int): Unit = {
    super.onKeyDown(keycode)

    val vel: Vector2 = hero.velocity
    keycode match {
      case Input.Keys.S  => vel.y += -1
      case Input.Keys.A  => vel.x += -1
      case Input.Keys.D => vel.x += 1
      case Input.Keys.W    => vel.y += 1
      case Input.Keys.SPACE => hero.shoot()
      case Input.Keys.UP => hero.turn("UP")
      case Input.Keys.DOWN => hero.turn("DOWN")
      case Input.Keys.RIGHT => hero.turn("RIGHT")
      case Input.Keys.LEFT => hero.turn("LEFT")
      case Input.Keys.Z => if(drawHitbox) drawHitbox = false else drawHitbox = true
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
      case _ =>
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

  def checkHitbox(): Unit = {
    for(p <- Projectile.allProjectiles) {
      for(e <- Enemy.allEnemy) {
        if(e.hitbox.intersects(p.hitbox)) {
          Projectile.remove(p)
          Enemy.remove(e)
        }
      }
    }
  }

  def displayHitbox(g: GdxGraphics): Unit = {
    hero.drawHitbox(g)
  }

  def checkCollision(): Unit = {
    for(e <- dungeon.map(dungeon.currentPosY)(dungeon.currentPosX).collisionsDoor) {
      val rectangle = CollisionManager.getRectangle2D(e)
      if (hero.hitbox.intersects(rectangle)) {
        dungeon.switchRoom(hero, e)
      }
    }
    for(e <- dungeon.map(dungeon.currentPosY)(dungeon.currentPosX).rectangleCollisionsWall) {
      if(hero.hitbox.intersects(e)) {
        (e.getX, e.getY) match {
          case (0,0)  => {
            e.getHeight match {
              case 1024 =>println("") // LEFT
              case _ => println("") // DOWN
            }
          }
          case (0,960) => println("") //UP
          case (1856,0) => println("") //RIGHT
          case _ => println("Error in collision")
        }
      }
    }
  }
}

object GameScreen extends App {
  new GameScreen
}