package Arthimetic

import stainless.annotation.* 
import stainless.lang.*

sealed trait TypeTree

case object UnTyped extends TypeTree
case object BooleanType extends TypeTree
case object IntegerType extends TypeTree
