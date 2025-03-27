// For more information on writing tests, see
import Tutorial.{pow, theoreomSlowEqualsFast}
import Tutorial.slowPow
// https://scalameta.org/munit/docs/getting-started.html
class diophSuite extends munit.FunSuite {
  test("testing generating List") {
    val k = Diophantine.createListWithOne(5, 0)
    assert(k.contains(BigInt(1)))
    assert(!k.drop(1).contains(BigInt(1)))

  }

}
