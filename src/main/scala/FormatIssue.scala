import stainless.annotation.*
import stainless.lang.*

object Num {
    def doublesOne(x: Option[BigInt], y : Option[BigInt]) : Option[BigInt] = {
        // Simple double match style
        (x, y) match
            case (Some(a), Some(b)) =>
                Some((a + b) * 2) 
            case _ => None() 
    }

    def doublesTwo(x: Option[BigInt], y : Option[BigInt]) : Option[BigInt] = {
        // Monad style
        for {
            a <- x
            b <- y
        } yield {
            (a + b) * 2
        }
    }.ensuring(res => res == doublesOne(x, y))
}