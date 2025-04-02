package Arthimetic

import stainless.annotation.* 
import stainless.lang.*

sealed trait TypeTree

case object UnTyped extends TypeTree
case object BooleanType extends TypeTree
case object IntegerType extends TypeTree

// object Types {

//     sealed trait Typed {
//         val getType: TypeTree
//         def isTyped: Boolean = getType != UnTyped
//     }


//     case class Problem(x : TypeTree) extends Typed {
//         val getType = x
//     }
// }

