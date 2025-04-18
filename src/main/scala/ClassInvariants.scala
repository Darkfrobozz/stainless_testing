package class_invariants
sealed trait Expr
object Expr {
    def IntPow(t: Expr, n: BigInt): Expr = {
        require(n > 0)
        Trees.IntPow(t, n)
    }
}

object Trees {
    case class IntPow(base: Expr, exp: BigInt) extends Expr {
        require(exp > 0)
    }

    case class Terminal(value: BigInt) extends Expr

}

