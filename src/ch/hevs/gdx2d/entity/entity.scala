package ch.hevs.gdx2d.entity

trait Entity {
  protected var _life: Int

  def life: Int = _life

  /**
   * Decrements the life of the player
   * @param amount Amount of HP to remove
   */
  def takeDamage(amount: Int): Unit = {
    _life -= amount
    if (_life <= 0) ko()
  }

  def ko(): Unit
}
