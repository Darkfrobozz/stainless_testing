import stainless.annotation.* 
import stainless.lang.*

object Tutorial {

  // You can start writing your code here
  def fac(n: BigInt) : BigInt = {
    require(n >= 0)
    // decreases(n)
    if (n == 0) 1 else fac(n-1) * n
  }


  // def dividesSum(c: BigInt, x: BigInt, y: BigInt, a: BigInt, b : BigInt) = {
  //   require(c > 0 && a % c == 0 && b % c == 0)
  // }.ensuring((x * a + y * b) % c == 0)

  /**
    * 
    *
    * @param c This is a divisor of a and b
    * @param a 
    * @param b
    */
  // def conserveGcd(c: BigInt, a: BigInt, b : BigInt) = {
  //   require(a % c == 0 && b % c == 0)
  // }.ensuring((a % b) % c == 0)

  def gcd(a: BigInt, b: BigInt) : BigInt = {
    require(a >= b && b > 0)
    if (a % b == 0) b else gcd(b, a % b)
  }.ensuring(res => res >= 1)

  def postPow(x: BigInt, n: BigInt, res: BigInt): Boolean = {
    require(n >= 0)
    ((x == 0 && res == 0) ||
    (n == 0 && res == 1) ||
    (x != 0 && res % x == 0))
  }

  def theoreomSlowEqualsFast(x : BigInt, n: BigInt) : Boolean = {
    require(n >= 0)
    pow(x, n) == slowPow(x, n)
  }.ensuring(res => res)

  def slowPow(x: BigInt, n: BigInt) : BigInt = {
    require(n >= 0)
    if (n == 0) BigInt(1) else x * slowPow(x, n - 1)
  }
  
  // When working with number it seems modulo is king
  // It is actually able to verify this that is incredible!!
  def pow(x: BigInt, n: BigInt) : BigInt = {
    require(n >= 0)
    if (n == 0) BigInt(1) else {
      val rem = (n % 2 + 2) % 2
      x match
        case x if rem == 1 => x * pow(x * x, (n - 1)/2)
        case x if rem == 0 => pow(x * x, n/2) 
    }
  }.ensuring(res => {
    postPow(x, n, res)
  })
}