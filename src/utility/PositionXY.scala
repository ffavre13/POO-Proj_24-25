package utility

class PositionXY(y: Int, x: Int) {
  var _posX: Int = x    // Position X
  var _posY: Int = y    // Position Y

  // Getter & Setter
  def posX: Int = _posX
  def posY: Int = _posY

  def posX_= (newPosX: Int): Unit = {
    _posX = newPosX
  }

  def posY_= (newPosY: Int): Unit = {
    _posY = newPosY
  }
}
