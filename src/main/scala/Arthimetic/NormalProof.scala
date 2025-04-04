package Arthimetic

import stainless.annotation.* 
import stainless.lang.*
import Arthimetic.TypeProofs.progress
import Arthimetic.TypeProofs.preservation

// Could I do these proofs without checkNV?
object NormalProof {

    def findNormal(t : Term) : Term = {
        decreases(t.size)
        val k = t.smallStep
        reducesOrRemainsSame(t)
        if (k == t) {
            t
        } else {
            findNormal(k)
        }
    }.ensuring(res => res.smallStep == res)

    def reducesOrRemainsSame(t : Term) : Unit = {
        decreases(t)
        t match
            case If(t1, t2, t3) =>
                t1 match
                    case True => assert(t2.size < t.size)
                    case False => assert(t3.size < t.size)
                    case _ => reducesOrRemainsSame(t1)
            case Succ(t1) => reducesOrRemainsSame(t1)
            case Pred(t1) =>
                t1 match
                    case Succ(t2) =>
                        assert(t2.size < t1.size)
                        assert(t2.size < t.size)
                    case Zero => ()
                    case _ => reducesOrRemainsSame(t1)
            case iszero(t1) => 
                if (t1.checkNV) {
                    t1 match
                        case Succ(t1) => ()
                        case Zero => () 
                } else {
                    reducesOrRemainsSame(t1)
                }
            case _ => ()
    }.ensuring(t.smallStep == t || t.smallStep.size < t.size)

    def smallstepRetainsNv(t : Term) : Unit = {
        require(t.checkNV)
        t match
            case Zero => ()
            case Succ(t1) => smallstepRetainsNv(t1)
    }.ensuring(t.smallStep.checkNV)

    def smallstepRetainsSize(t : Term) : Unit = {
        require(t.checkNV)
        t match
            case Zero => ()
            case Succ(t1) => smallstepRetainsSize(t1)
    }.ensuring(t.size == t.smallStep.size)

    def nvDefinedBySize(t1 : Term, t2 : Term) : Unit = {
        require(t1.size == t2.size && t1.checkNV && t2.checkNV)
        (t1, t2) match
            case (Zero, Zero) => ()
            case (Succ(t11), Succ(t21)) => nvDefinedBySize(t11, t21)
    }.ensuring(t1 == t2)

    def nvIsNormal(t : Term) : Unit = {
        require(t.checkNV)
        smallstepRetainsNv(t)
        smallstepRetainsSize(t)
        nvDefinedBySize(t, t.smallStep)
    }.ensuring(t == t.smallStep)


    def normalOfTypedIsNV(t : Term) : Unit = {
        require(t.getType != UnTyped)
        decreases(t.size)
        val k = t.smallStep
        reducesOrRemainsSame(t)
        // This should inform us that we will break at normal form.
        if (t.valueForm) {
            t match
                case False => ()
                case True => ()
                case Zero => ()
                case Succ(t1) => nvIsNormal(t) 
        } else {
            preservation(t)
            progress(t)
            assert(k != t)
            // Preservation makes sure that t.getType does not become UnTyped
            // This means that progress will apply for next recursion as well
            normalOfTypedIsNV(k)
        }
    }.ensuring(findNormal(t).valueForm && findNormal(t).getType == t.getType)
}