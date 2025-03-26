import stainless.annotation.* 
import stainless.lang.*

sealed trait Term
case class Nil() extends Term
case class True() extends Term
case class False() extends Term
case class If(t1:Term, t2:Term, t3:Term) extends Term

// These have many different rules
case class Succ(t1: Term) extends Term
case class Pred(t1: Term) extends Term
case class iszero(t1: Term) extends Term


sealed trait TypeError
case class Fine() extends TypeError
case class BadNat() extends TypeError
case class BadBool() extends TypeError

// I want to try a new strategy of applying each rule seperately from smallstep

// Computation True If
// Computation False If
// Congruence If
// E succ is like if
// E pred is like if
// predNil says pred Nil gives Nil
// predSucc t1 gives t1
// Eiszero is like if
// Eiszero Nil says true if Nil 
// How should we say that Eiszero false is applicable?

object Term {

    def checkNV(t : Term) : Boolean = {
        t match
            case Nil() => true
            case Succ(t1) => checkNV(t1)
            case _ => false
    }

    def checkBool(t: Term) : Boolean = {
        t match
            case True() => true
            case False() => true
            case _ => false 
    }

    // This is pure by virtue of being a functional algorithm...
    @induct
    def smallStep(t: Term): Term = {
        t match
            case If(t1, t2, t3) =>
                t1 match
                    case True() => t2
                    case False() => t3
                    case If(t11, t12, t13) => If(smallStep(t11), t12, t13)
                    case _ => t
            case Succ(t1) => smallStep(t1)
            case Pred(t1) =>
                t1 match
                    case Succ(t2) => t2
                    case Nil() => Nil()
                    case _ => smallStep(t1)
            case iszero(t1) => 
                t1 match
                    // This one is by far the hardest...
                    case Succ(t2) => t2
                    case Nil() => Nil()
                    case _ => smallStep(t1)
                smallStep(t1)
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
            case Nil() => ()
            case True() => ()
            case False() => ()
            case If(t1, t2, t3) => monotoneReductions(t1)
            case Succ(t1) => monotoneReductions(t1)
            case Pred(t1) => monotoneReductions(t1)
            case iszero(t1) => monotoneReductions(t1) 
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
            case Succ(t1) => 
                assert(size(t1) < size(res))
                val k = normal(t1)
                Succ(k)
            case Pred(t1) => 
                assert(size(t1) < size(res))
                val k = normal(t1)
                k match
                    case Nil() => Nil()
                    case Succ(t1) => t1
                    case _ => Pred(k)
            case iszero(t1) => 
                assert(size(t1) < size(res))
                val k = normal(t1)
                k match
                    case Nil() => True()
                    case k if checkNV(k) => False()
                    case _ => iszero(k)
            case _ => res
    }

    def normalValues(t : Term) : Unit = {
        require(checkBool(t) || checkNV(t))
        monotoneReductions(t)
        t match
            case t if checkBool(t) => ()
            case t if checkNV(t) => 
                t match
                    case Succ(t1) =>
                        normalValues(normal(t1))
                    case Nil() => () 
    }.ensuring(normal(t) == t)

    // def getTypeError(t: Term): TypeError = {
    //     val res = normal(t)
    //     res match
    //         case Nil() => Fine()
    //         case True() => Fine()
    //         case False() => Fine()
    //         case If(t1, t2, t3) => {
    //             if (checkNV(t1)) {
    //                 BadBool()
    //             } else {

    //             } 
    //         }
    //         case Succ(t1) =>
    //         case Pred(t1) =>
    //         case iszero(t1) =>
        
    // }
}