package utility

import entity.Hero
import game.Room

object GameState {
  var hero: Hero = null
  var room: Room = null
  var bossIsAlive: Boolean = true
}
