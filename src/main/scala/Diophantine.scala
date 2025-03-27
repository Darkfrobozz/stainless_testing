import stainless.annotation.* 
import stainless.lang.*
import stainless.collection.*
import stainless.collection.List.*

object Diophantine {
    def createDiagonalMatrix(n: BigInt): List[List[BigInt]] = {
        require(n > 2)
        List(List(1))
    }

    def createListWithOne(n: BigInt, spot: BigInt) : List[BigInt] = {
        require(n >= 0 && spot < n && spot >= -1)
        if (n == 0) {
            Nil()
        } else {
            spot match
                case spot if spot == 0 => Cons(BigInt(1), createListWithOne(n - 1, spot - 1))
                case spot if spot < 0 => Cons(BigInt(0), createListWithOne(n - 1, spot))
                case spot if spot > 0 => Cons(BigInt(0), createListWithOne(n - 1, spot - 1))
        }
    }.ensuring(res => res.size == n)
}