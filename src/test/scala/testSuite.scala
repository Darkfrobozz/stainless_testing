import Arthimetic.{Term, Zero, Pred, If, iszero, True, False}
// For more information on writing tests, see
import Tutorial.{pow, theoreomSlowEqualsFast}
import Tutorial.slowPow
import Arthimetic.NormalProof
import Arthimetic.TypeProofs
// https://scalameta.org/munit/docs/getting-started.html
class MySuite extends munit.FunSuite {
  test("Testing Fibonnaci") {
    val obtained = Tutorial.fac(BigInt(4))
    val expected = BigInt(24)
    assertEquals(obtained, expected)
  }


  test("Testing gcd") {
    val obtained = Tutorial.gcd(BigInt(26), BigInt(4))
    val expected = BigInt(2)
    assertEquals(obtained, expected)
  }

  // test("testing mod case") {
  //   assertEquals(-1 % 2, 1)
  // }

  test("testing error case") {
    assertEquals(pow(BigInt(-1), BigInt(1)), BigInt(-1))
  }


  test("testing theoreom") {
    assertEquals(theoreomSlowEqualsFast(BigInt(-1), BigInt(2)), true)
  }


  test("testing equality fastpow") {
    assertEquals(pow(BigInt(-1), BigInt(2)), BigInt(1))
  }


  test("testing equality slowpow") {
    assertEquals(slowPow(BigInt(-1), BigInt(2)), BigInt(1))
  }

  test("testing counter-example for retains Succ") {
    NormalProof.findNormal(Pred(True))
  }

  // Found this huge bug from this counter example!!
  test("Testing counter-example for preservation") {
    val k = If(If(True, False, True), If(True, Zero, Zero), Zero)
    val after = k.smallStep
    TypeProofs.preservation(k)
  }


  // Found this huge bug from this counter example!!
  test("Testing counter-example for smallstepIf") {
    val k = If(If(True, False, True), If(True, Zero, Zero), Zero)
    val t1 = Pred(Zero)
    val t2 = Zero
    val t3 = Zero
    val t = If(t1, t2, t3)
    val after = t.smallStep
    print(after)
    TypeProofs.smallStepIf(t, t1, t2, t3)
  }

}
