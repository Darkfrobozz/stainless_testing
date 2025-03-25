import stainless.annotation.* 
import stainless.lang.*
object divisible {
  
  def multipleDividible(c: BigInt, k: BigInt) = {
    require(c != 0)
  }.ensuring((c * k) % c == 0)

  def findMultiple(c: BigInt, x: BigInt) : BigInt = {
    require(c != 0)
    require(x % c == 0)
    x/c
  }.ensuring(res => res * c == x)


  def dividesMulti(c: BigInt, a: BigInt, b: BigInt) = {
    require(c > 0)
    require(b % c == 0)
    val k = findMultiple(c, b)
    multipleDividible(c, k * a)
    assert((a * c * k) % c == 0)
    assert(c * k == b)
  }.ensuring((a * b) % c == 0)

  def dividesSum(c: BigInt, x: BigInt, y: BigInt, a: BigInt, b : BigInt) = {
    require(c >= 0 && x >= 0 && y >= 0 && a >= 0 && b >= 0)
    require(c > 0 && a % c == 0 && b % c == 0)
    assert(x* a % c == 0)
  }.ensuring((x * a + y * b) % c == 0)
}
