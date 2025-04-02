import stainless.annotation.* 
import stainless.lang.*
import stainless.collection.*
import stainless.collection.List.*

object Diophantine {

    def adder(is: Int*) = {
        is.fold(0)((ack, i) => ack + i)
    }

    /**
      * 
      * Creates starting at m a list which contains a 1 in spot m then
      * Creates another list with a 1 at spot (m - 1)
      * All of these lists have size n
      *
      * @param n This is the rows
      * @param m This is the columns
      * @return
      */
    def createDiagonalMatrix(n: BigInt, m: BigInt): List[List[BigInt]] = {
        require(n >= 0)
        require(m >= 1)
        decreases(m)
        if (m == 1) {
            Cons(createListWithOne(n, 1), Nil())
        } else {
            Cons(createListWithOne(n, m), createDiagonalMatrix(n, m - 1))
        }
    }

    def squareDiagonalMatrix(n: BigInt) : List[List[BigInt]] = {
        require(n >= 2)
        createDiagonalMatrix(n, n)
    }

    // Induction won't work on this because a previous form is not really a smaller matrix...
    def squareDiagonalSize(n: BigInt) : Unit = {
        require(n >= 2)
        if (n == 2) {
            ()
        } else {
            squareDiagonalSize(n - 1)
        }
    }.ensuring(createDiagonalMatrix(n, n).size == n)



    def createListWithOne(n: BigInt, spot: BigInt) : List[BigInt] = {
        require(n >= 0)
        require(spot >= 1)
        if (n == 0) {
            Nil()
        } else if (spot == n) {
            Cons(BigInt(1), createListWithOne(n - BigInt(1), spot))
        } else {
            Cons(BigInt(0), createListWithOne(n - BigInt(1), spot))
        }
    }.ensuring(res => res.size == n)

    def zeroContentTheorem(n: BigInt, spot: BigInt): Unit = {
        require(spot > n)
        require(n >= 0)
        require(spot >= 1)
        if (n == 0) {
            ()
        } else {
            zeroContentTheorem(n - 1, spot)
        }
    }.ensuring(createListWithOne(n, spot).count(p => p == BigInt(1)) == 0)

    // Prove that decreasing number only equals another at most once...
    def contentTheorem(n: BigInt, spot: BigInt): Unit = {
        require(spot <= n)
        require(n >= 0)
        require(spot >= 1)
        if (n == 0) {
            ()
        } else if (n == spot) {
            zeroContentTheorem(n - 1, spot)
        } else {
            contentTheorem(n - 1, spot)

        }
    }.ensuring(createListWithOne(n, spot).count(p => p == BigInt(1)) == 1)


    def sumOfContent(n: BigInt): Unit = {
        require(n >= 0)
        if (n == 0) {
            ()
        } else {
            contentTheorem(n, n)
            sumOfContent(n - 1)
        }
    }

    def getTotalCount(l : List[List[BigInt]]) = {
        l.foldLeft[BigInt](0)((sum, subl) => sum + subl.count(p => p == 1))
    }
}