package ch.hevs.gdx2d.utility

import ch.hevs.gdx2d.components.audio.SoundSample

import scala.util.Random

/**
 * Class that loads and can play sfx and musics.
 */
object AudioManager {
  var s_dmg: SoundSample = new SoundSample("data/audio/sfx/damage.mp3")
  s_dmg.setVolume(0.1f)

  var s_shoot: Array[SoundSample] = Array(
    new SoundSample("data/audio/sfx/ak47_shot_01.wav"),
    new SoundSample("data/audio/sfx/ak47_shot_02.wav"),
    new SoundSample("data/audio/sfx/ak47_shot_03.wav"),
  )
  s_shoot.foreach(sfx => sfx.setVolume(0.05f))

  var s_win: SoundSample = new SoundSample("data/audio/sfx/FFVII_victory.mp3")

  /** Plays a damage sound */
  def damage(): Unit = try s_dmg.play()
  catch {
    case e: Exception => println("Couldn't play damage sound.")
  }

  /** Plays a random gun sound. */
  def shoot(): Unit = try Random.shuffle(s_shoot.toList).head.play()
  catch {
    case e: Exception => println("Couldn't play shoot sound.")
  }

  /** Plays the win theme, because the player deserves it. */
  def win(): Unit = try s_win.play()
  catch {
    case e: Exception => println("Couldn't play win sound.")
  }

  /** Stops the win theme. */
  def stop_win(): Unit = try s_win.stop()
  catch {
    case e: Exception => println("Couldn't stop win sound.")
  }
}
