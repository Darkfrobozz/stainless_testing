// For more information on writing tests, see
// https://scalameta.org/munit/docs/getting-started.html
class MySuite extends munit.FunSuite {
  test("Testing Fibonnaci") {
    val obtained = Tutorial.fac(4)
    val expected = 24
    assertEquals(obtained, expected)
  }
}
