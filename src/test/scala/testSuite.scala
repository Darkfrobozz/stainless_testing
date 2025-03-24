// For more information on writing tests, see
import Tutorial.{pow, theoreomSlowEqualsFast}
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
    assertEquals(theoreomSlowEqualsFast(BigInt(2), BigInt(-1)), true)
  }
}
