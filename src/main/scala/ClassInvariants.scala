sealed trait Expr {
    def toPow(n: BigInt) : IntPow = {
        IntPow(this, n)
    }
}

case class IntPow(base: Expr, n: BigInt) extends Expr {
    require(n > 0)
}

case class Terminal(value: BigInt) extends Expr