import stainless.annotation.* 
import stainless.lang.*
import stainless.collection.*
import stainless.collection.List.*

object Calc {
    def measure(e: Expr) : BigInt = {
        e match
            case IntPow(base, exp) => base + exp
    }
  
}

sealed trait Expr

case class IntPow(base: BigInt, exp: BigInt) extends Expr {
}

object IntPow {
    def apply(base: BigInt, exp: BigInt) = {
        new IntPow(base, exp)
    }
    def unapply(i: IntPow) : Option[(BigInt, BigInt)] = {
        Some((i.base, i.exp))
    }
}
