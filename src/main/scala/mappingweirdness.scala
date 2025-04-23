import stainless.lang.*


sealed trait Result

case class IntResult(content: Option[BigInt]) extends Result

object MappingWeirdness {

  def mapping(e : Result) = {
    e match
      case IntResult(content) if content.getOrElse(0) != 0 => content.map(f => 5 / f)
      case _ => IntResult(None())
  }
}
