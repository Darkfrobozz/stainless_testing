// For more information on writing tests, see
// https://scalameta.org/munit/docs/getting-started.html
class MySuite extends munit.FunSuite {
  test("Testing Fibonnaci") {
    val obtained = Tutorial.fac(BigInt(4))
    val expected = BigInt(24)
    assertEquals(obtained, expected)
  }


  test("Testing gcd") {
    val obtained = Tutorial.gcd(BigInt(4), BigInt(26))
    val expected = BigInt(2)
    assertEquals(obtained, expected)
  }
}
