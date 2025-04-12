package format_issue
import stainless.annotation.*
import stainless.lang.*

sealed trait Expr

case class Integer(x: BigInt) extends Expr 
case class Add(x : Expr, y : Expr) extends Expr
case object Unknown extends Expr

object Num {
    def doublesOne(x: Expr, y : Expr): Option[BigInt] = {
        // Simple double match style
        (simple_transform(x), simple_transform(y)) match
            case (Some(a), Some(b)) =>
                Some((a + b) * 2) 
            case _ => None() 
    }

    def doublesTwo(x: Expr, y : Expr): Option[BigInt] = {
        // Monad style
        for {
            a <- simple_transform(x)
            b <- simple_transform(y)
        } yield {
            (a + b) * 2
        }
    }.ensuring(res => res == doublesOne(x, y))

    def add(x : Expr, y : Expr) : Option[BigInt] = {
        for {
            a <- simple_transform(x)
            b <- simple_transform(y)
        } yield {a + b}
    }

    def simple_transform(x: Expr) : Option[BigInt] = {
        x match
            case Integer(x) => Some(x)
            case Add(x, y) => add(x,y)
            case Unknown => None()
    }

}