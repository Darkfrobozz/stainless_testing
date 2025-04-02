package Arthimetic

import stainless.annotation.* 
import stainless.lang.*

object TypeProofs {
    def preservation(p: Term) : Unit = {
        require(p.getType != UnTyped)
        p.getType match
            case BooleanType => p match
                case True => ()
                case False => ()
                case If(t1, t2, t3) => {
                    t1 match
                        case True => {
                            assert(t2.getType == p.getType)
                        }
                        case False => {
                            assert(t3.getType == p.getType)
                        }
                        case _ => preservation(t1)
                }
                case iszero(t1) => {
                    if (t1.checkNV) {
                        t1 match
                            case Succ(t1) => {
                                assert(p.smallStep == False)
                            }
                            case Zero => {
                                assert(p.smallStep == True)
                            } 
                    } else {
                        preservation(t1)
                    }
                }
            
            case IntegerType => p match
                case Zero => ()
                case If(t1, t2, t3) => {
                    t1 match
                        case True => {
                            assert(t2.getType == p.getType)
                        }
                        case False => {
                            assert(t3.getType == p.getType)
                        }
                        case _ => preservation(t1)
                }
                case Succ(t1) => preservation(t1)
                case Pred(t1) => preservation(t1)   
    }.ensuring(p.getType == p.smallStep.getType)

    def progress(t: Term) : Unit = {
        require(t.getType != UnTyped)
        require(!t.checkBool)
        require(!t.checkNV)
        t.getType match
            case BooleanType => t match
                case If(t1, t2, t3) => {
                    t1 match
                        case True => ()
                        case False => ()
                        case _ => {
                            smallStepIf(t, t1, t2, t3)
                            assert(t.smallStep == If(t1.smallStep, t2, t3))
                            progress(t1)
                        }
                    
                }
                case iszero(t1) => {
                    if (t1.checkNV) {
                        t1 match
                            case Zero => 
                                assert(iszero(t1).smallStep == True)
                            case Succ(t11) =>
                                smallStepiszeroWithNV(t, t1)
                                assert(iszero(t1).smallStep == False) 
                    } else {
                        progress(t1)
                    }
                }
            
            case IntegerType => t match
                case If(t1, t2, t3) =>
                    assert(t1.getType == BooleanType)
                    t1 match
                        case True => ()
                        case False => ()
                        case _ => 
                            smallStepIf(t, t1, t2, t3)
                            assert(t.smallStep == If(t1.smallStep, t2, t3))
                            progress(t1) 
                case Succ(t1) => 
                    assert(!t1.checkNV)
                    progress(t1)
                case Pred(t1) => t1 match
                    case Zero => ()
                    case Succ(t1) => ()
                    case _ => 
                        progress(t1)
    }.ensuring(t.smallStep != t) 

    def smallStepIf(t: Term, t1: Term, t2: Term, t3: Term) : Unit = {
        require(t match
            case If(t11, t12, t13) => t11 == t1 && t12 == t2 && t13 == t3
            case _ => false 
        )
        require(!t1.checkBool)
    }.ensuring(t.smallStep == If(t1.smallStep, t2, t3))

    def smallStepiszeroWithNV(t: Term, t1:Term) : Unit = {
        require(t match
            case iszero(t11) => t11 == t1
            case _ => false
        )
        require(t1.checkNV && t1 != Zero)
    }.ensuring(t.smallStep == False)
}
