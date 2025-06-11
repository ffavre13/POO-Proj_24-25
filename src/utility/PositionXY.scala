package utility

class PositionXY(y: Int, x: Int) {
  var _posX: Int = x
  var _posY: Int = y

  def posX: Int = _posX
  def posY: Int = _posY

  def posX_= (newPosX: Int): Unit = {
    _posX = newPosX
  }

  def posY_= (newPosY: Int): Unit = {
    _posY = newPosY
  }
}
