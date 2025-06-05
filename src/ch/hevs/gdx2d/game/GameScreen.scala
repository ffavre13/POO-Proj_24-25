package ch.hevs.gdx2d.game

import ch.hevs.gdx2d.CollisionManager
import ch.hevs.gdx2d.desktop.{PortableApplication, Xbox}
import ch.hevs.gdx2d.entity.enemies.{Enemy, MeleeEnemy, ShootingEnemies, TargetPlayerEnemies}
import ch.hevs.gdx2d.entity.{Hero, Projectile}
import ch.hevs.gdx2d.lib.GdxGraphics
import ch.hevs.gdx2d.lib.utils.Logger
import ch.hevs.gdx2d.utility.GameState
import com.badlogic.gdx.{Gdx, Input}
import com.badlogic.gdx.controllers.Controller
import com.badlogic.gdx.math.Vector2

class GameScreen extends PortableApplication(1920, 1080) {
  var dungeon: Dungeon = null
  var ctrl: Controller = null

  private var drawHitbox: Boolean = false

  override def onInit(): Unit = {
    setTitle("The binding of Isaac")

    dungeon = new Dungeon(16,16, 40)
    dungeon.generate()

    GameState.hero = new Hero(getWindowWidth/2, getWindowHeight/2)
    Enemy.add(new ShootingEnemies(100, 100, false, false, false, true))
    Enemy.add(new TargetPlayerEnemies(100, 100))
  }

  override def onGraphicRender(g: GdxGraphics): Unit = {
    g.clear()

    dungeon.map(dungeon.currentPosY)(dungeon.currentPosX).tiledMapRender.setView(g.getCamera)
    dungeon.map(dungeon.currentPosY)(dungeon.currentPosX).tiledMapRender.render()

    checkCollision()
    GameState.hero.update(g)
    Projectile.update(g)
    Enemy.update(g)

    if(drawHitbox) {
      displayHitbox(g)
    }
    checkHitbox()

    g.drawFPS()
  }

  // <editor-fold desc="Keyboard methods">
  override def onKeyDown(keycode: Int): Unit = {
    super.onKeyDown(keycode)

    val vel: Vector2 = GameState.hero.velocity
    keycode match {
      case Input.Keys.S     => vel.y += -1
      case Input.Keys.A     => vel.x += -1
      case Input.Keys.D     => vel.x += 1
      case Input.Keys.W     => vel.y += 1
      case Input.Keys.SPACE => GameState.hero.shoot()
      case Input.Keys.UP    => GameState.hero.turn("UP")
      case Input.Keys.DOWN  => GameState.hero.turn("DOWN")
      case Input.Keys.RIGHT => GameState.hero.turn("RIGHT")
      case Input.Keys.LEFT  => GameState.hero.turn("LEFT")
      case Input.Keys.Z     => drawHitbox = !drawHitbox
      case _                => Logger.log(s"Key '$keycode' pressed")
    }
    GameState.hero.velocity = vel
  }

  override def onKeyUp(keycode: Int): Unit = {
    var vel: Vector2 = GameState.hero.velocity

    keycode match {
      case Input.Keys.S  => vel.y = 0
      case Input.Keys.A  => vel.x = 0
      case Input.Keys.D  => vel.x = 0
      case Input.Keys.W  => vel.y = 0
      case _ =>
    }
    GameState.hero.velocity = vel
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
    if (axisCode == Xbox.L_STICK_HORIZONTAL_AXIS) GameState.hero.velocity.x = value
    if (axisCode == Xbox.L_STICK_VERTICAL_AXIS) GameState.hero.velocity.y = -value
  }

  override def onControllerKeyDown(controller: Controller, buttonCode: Int): Unit = {
    super.onControllerKeyUp(controller, buttonCode)
    buttonCode match {
      case Xbox.Y => GameState.hero.turn("UP")
      case Xbox.A => GameState.hero.turn("DOWN")
      case Xbox.X => GameState.hero.turn("LEFT")
      case Xbox.B => GameState.hero.turn("RIGHT")
      case _ =>
    }
    GameState.hero.shoot()
  }
  // </editor-fold>

  def checkHitbox(): Unit = {
    for(p <- Projectile.allProjectiles) {
      for(e <- Enemy.enemies) {
        if(e.hitbox.intersects(p.hitbox)) {
          Projectile.remove(p)
          Enemy.remove(e)
        }
      }
    }
  }

  def displayHitbox(g: GdxGraphics): Unit = {
    GameState.hero.drawHitbox(g)
  }

  def checkCollision(): Unit = {
    for(e <- dungeon.map(dungeon.currentPosY)(dungeon.currentPosX).collisionsDoor) {
      val rectangle = CollisionManager.getRectangle2D(e)
      if (GameState.hero.hitbox.intersects(rectangle)) {
        dungeon.switchRoom(GameState.hero, e)
      }
    }
    for(e <- dungeon.map(dungeon.currentPosY)(dungeon.currentPosX).collisionsWall) {
      val rectangle = CollisionManager.getRectangle2D(e)
      if (GameState.hero.hitbox.intersects(rectangle)) {
        e.getProperties.get("position") match {
          case "LEFT" =>
            if (GameState.hero.velocity.x < 0) GameState.hero.velocity.x = 0

          case "RIGHT" =>
            if (GameState.hero.velocity.x > 0) GameState.hero.velocity.x = 0

          case "DOWN" =>
            if (GameState.hero.velocity.y < 0) GameState.hero.velocity.y = 0

          case "UP" =>
            if (GameState.hero.velocity.y > 0) GameState.hero.velocity.y = 0

          case _ =>
        }
      }
    }
  }
}

object GameScreen extends App {
  new GameScreen
}