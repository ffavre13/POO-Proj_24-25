package entity

trait Entity {
  protected var _hp: Int      // Life point of the entity

  def hp: Int = {
    _hp
  }

  /**
   * Decrements the HP of the entity
   * @param amount Amount of HP to remove
   */
  def takeDamage(amount: Int): Unit = {
    _hp -= amount
    if (_hp <= 0) ko()
  }

  def ko(): Unit
}
