package Arthimetic

import stainless.annotation.* 
import stainless.lang.*

sealed trait Typed {
    def thisType: TypeTree
    def isTyped: Boolean = thisType != UnTyped
}
sealed trait Term extends Typed {

    def getType : TypeTree = {
        this match
            case Zero => IntegerType
            case True => BooleanType
            case False => BooleanType
            case If(t1, t2, t3) => t1.getType match
                case BooleanType => if (t2.getType == t3.getType) 
                    {
                        t2.getType
                    } else {UnTyped}
                case _ => UnTyped           
            case Succ(t1) => t1.getType match
                case IntegerType => IntegerType 
                case _ => UnTyped
            case Pred(t1) => t1.getType match
                case IntegerType => IntegerType
                case _ => UnTyped 
            case iszero(t1) => t1.getType match
                case IntegerType => BooleanType
                case _ => UnTyped
    }

    @induct
    def smallStep: Term = {
        this match
            case If(t1, t2, t3) =>
                t1 match
                    case True => t2
                    case False => t3
                    case _ => If(t1.smallStep, t2, t3)
            case Succ(t1) => Succ(t1.smallStep)
            case Pred(t1) =>
                t1 match
                    case Succ(t2) => t2
                    case Zero => Zero
                    case _ => Pred(t1.smallStep)
            case iszero(t1) => 
                if (t1.checkNV) {
                    t1 match
                        case Succ(t1) => False
                        case Zero => True 
                } else {
                    iszero(t1.smallStep)
                }
            case _ => this
    }

    def size : BigInt = {
        BigInt(1) + (this match
            case Zero => BigInt(0)
            case True => BigInt(0)
            case False => BigInt(0)
            case If(t1, t2, t3) => t1.size + t2.size + t3.size
            case Succ(t1) => t1.size
            case Pred(t1) => t1.size
            case iszero(t1) => t1.size
        ) 
    }.ensuring(res => res >= 1)

    def checkNV : Boolean = {
        this match
            case Zero => true
            case Succ(t1) => t1.checkNV
            case _ => false
    }

    def checkBool : Boolean = {
        this match
            case True => true
            case False => true
            case _ => false 
    }

    def hasSuccForm : Boolean = {
        this match
            case Succ(t1) => true
            case _ => false
    }

    def valueForm : Boolean = {
        this.checkBool || this.checkNV
    }
}

case object Zero extends Term {
    def thisType: TypeTree = IntegerType
}
case object True extends Term {
    def thisType: TypeTree = BooleanType
}
case object False extends Term {
    def thisType: TypeTree = BooleanType
}
case class If(@induct t1:Term, @induct t2:Term, @induct t3:Term) extends Term {
    def thisType: TypeTree = {
        t1.thisType match
            case BooleanType => if (t2.thisType == t3.thisType) 
                {
                    t2.thisType
                } else {UnTyped}
            case _ => UnTyped           
    }
}

// These have many different rules
case class Succ(@induct t1: Term) extends Term {
    def thisType: TypeTree = {
        t1.thisType match
            case IntegerType => IntegerType 
            case _ => UnTyped
    }
}
case class Pred(@induct t1: Term) extends Term {
    def thisType: TypeTree = {
        t1.thisType match
            case IntegerType => IntegerType 
            case _ => UnTyped
    }
}
case class iszero(@induct t1: Term) extends Term {
    def thisType: TypeTree = {
        t1.thisType match
            case IntegerType => IntegerType 
            case _ => UnTyped
    }
}


object Term {


    // This is pure by virtue of being a functional algorithm...

    // New approach prove that a small step reduces the size or remains the same.

    def monotoneReductions(t: Term) : Unit = {
        t match
            case Zero => ()
            case True => ()
            case False => ()
            case If(t1, t2, t3) => monotoneReductions(t1)
            case Succ(t1) => monotoneReductions(t1)
            case Pred(t1) => monotoneReductions(t1)
            case iszero(t1) => monotoneReductions(t1) 
    }.ensuring(t.smallStep.size <= t.size)
}