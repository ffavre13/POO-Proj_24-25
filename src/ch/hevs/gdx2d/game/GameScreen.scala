package ch.hevs.gdx2d.game

import ch.hevs.gdx2d.utility.{AudioManager, CollisionManager, GameState}
import ch.hevs.gdx2d.desktop.{PortableApplication, Xbox}
import ch.hevs.gdx2d.entity.enemies.{Enemy, ShootingEnemies, TargetPlayerEnemies}
import ch.hevs.gdx2d.entity.{Hero, Projectile}
import ch.hevs.gdx2d.lib.GdxGraphics
import ch.hevs.gdx2d.lib.utils.Logger
import com.badlogic.gdx.{Gdx, Input}
import com.badlogic.gdx.controllers.Controller
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.utils.Align

class GameScreen extends PortableApplication(1920, 1080) {
  var dungeon: Dungeon = null
  var ctrl: Controller = null

  // Tells if the hitboxes should be displayed to the player
  private var drawHitbox: Boolean = false

  /** Initializes the game. */
  override def onInit(): Unit = {
    setTitle("Bullet Dungeon")

    Enemy.removeAll()
    Projectile.removeAll()

    dungeon = new Dungeon(16,16, 2)
    dungeon.generate()

    AudioManager // Call object to load Audios (avoids FPS drop whesn playing a sound for the 1st time)
    GameState.hero = null
    GameState.hero = new Hero(getWindowWidth/2, getWindowHeight/2)
    GameState.room = dungeon.map(dungeon.currentPosY)(dungeon.currentPosX)
  }

  /**
   * Called at each frame to render different graphical elements.
   * @param g GdxGraphics object
   */
  override def onGraphicRender(g: GdxGraphics): Unit = {
    g.clear()

    if (GameState.hero.hp <= 0) {
     g.drawString(getWindowWidth/2, getWindowHeight/2, "Game over, press R to restart the game",Align.center)
    }
    else if(!GameState.bossIsAlive) {
      g.drawString(getWindowWidth/2, getWindowHeight/2, "You won the game gg wp, press R to restart the game",Align.center)
    }
    else {
      dungeon.map(dungeon.currentPosY)(dungeon.currentPosX).tiledMapRender.setView(g.getCamera)
      dungeon.map(dungeon.currentPosY)(dungeon.currentPosX).tiledMapRender.render()

      checkCollision()
      GameState.hero.update(g)
      Projectile.update(g)
      Enemy.update(g)

      if(drawHitbox) {
        displayHitbox(g)
      }

      CollisionManager.checkHitbox()
      UserInterface.drawUI(g)
      g.drawFPS()
    }
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
      case Input.Keys.R     => onInit()
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
    var shoot: Boolean = false
    buttonCode match {
      case Xbox.Y => GameState.hero.turn("UP"); shoot = true
      case Xbox.A => GameState.hero.turn("DOWN"); shoot = true
      case Xbox.X => GameState.hero.turn("LEFT"); shoot = true
      case Xbox.B => GameState.hero.turn("RIGHT"); shoot = true
      case Xbox.R_BUMPER => GameState.hero.speed *= 2
      case _ =>
    }
    if (shoot) GameState.hero.shoot()
  }

  override def onControllerKeyUp(controller: Controller, buttonCode: Int): Unit = {
    super.onControllerKeyDown(controller, buttonCode)
    buttonCode match {
      case Xbox.R_BUMPER => GameState.hero.speed /= 2
      case _ =>
    }
  }
  // </editor-fold>

  def displayHitbox(g: GdxGraphics): Unit = {
    GameState.hero.drawHitbox(g)
    Enemy.displayHitboxes(g)
    Projectile.displayHitboxes(g)
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