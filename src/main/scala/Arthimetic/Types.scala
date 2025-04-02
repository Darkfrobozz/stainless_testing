package Arthimetic

import stainless.annotation.* 
import stainless.lang.*

sealed trait Typed {
    val getType: Types
    def isTyped: Boolean = getType != UnTyped
}

sealed trait Types

case object UnTyped extends Types
case object BooleanType extends Types
case object IntegerType extends Types


case object Problem extends Typed {
    override val getType : Types = UnTyped
}