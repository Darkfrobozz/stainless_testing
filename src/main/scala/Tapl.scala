import stainless.annotation.* 
import stainless.lang.*

sealed trait Term
case class Nil() extends Term
case class True() extends Term
case class False() extends Term
case class If(t1:Term, t2:Term, t3:Term) extends Term
case class Succ(t1: Term) extends Term
case class Pred(t1: Term) extends Term
case class iszero(t1: Term) extends Term

object t {

    // This is pure by virtue of being a functional algorithm...
    @induct
    def smallStep(t: Term): Term = {
        t match
            case If(t1, t2, t3) => {
                // How do I handle this?
                // Make sure to get right output
                t1 match
                    case True() => t2
                    case False() => t3
                    case If(t11, t12, t13) => If(smallStep(t11), t12, t13)
                    case _ => t
            } 
            case _ => t
    }

    // This is also pure by virtue of being a functional algorithm
    // So given that it terminates we know that normal forms are the same
    def termination(t : Term) : Unit = {
        t match
            case If(t1, t2, t3) => termination(t1)
            case _ => ()
    }

    def size(t : Term) : BigInt = {
        BigInt(1) + (t match
            case Nil() => BigInt(0)
            case True() => BigInt(0)
            case False() => BigInt(0)
            case If(t1, t2, t3) => size(t1) + size(t2) + size(t3)
            case Succ(t1) => size(t1)
            case Pred(t1) => size(t1)
            case iszero(t1) => size(t1)
        ) 
    }.ensuring(res => res >= 1)

    def monotoneReductions(t: Term) : Unit = {
        t match
            case If(t1, t2, t3) => monotoneReductions(t1)
            case _ => ()
    }.ensuring(size(smallStep(t)) <= size(t))

    def normal(t : Term) : Term = {
        decreases(size(t))
        val res = smallStep(t)
        // If res can change size...
        // If we can get this to be true we are good!
        monotoneReductions(t)
        assert(size(res) <= size(t))
        res match
            case If(t1, t2, t3) => {
                assert(size(t1) < size(res))
                assert(size(t2) < size(res))
                assert(size(t3) < size(res))
                val k = normal(t1) 
                k match
                    case True() => normal(t2)
                    case False() => normal(t3)
                    case _ => If(k, t2, t3) 
            } 
            case _ => res
    }

}