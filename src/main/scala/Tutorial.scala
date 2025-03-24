import stainless.annotation.* 
import stainless.lang.*

object Tutorial {

  // You can start writing your code here
  // def fac(n: BigInt) : BigInt = {
  //   require(n >= 0)
  //   // decreases(n)
  //   if (n == 0) 1 else fac(n-1) * n
  // }


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
}