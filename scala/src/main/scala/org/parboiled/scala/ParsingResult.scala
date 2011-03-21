package org.parboiled.scala

import org.parboiled.Node
import org.parboiled.support.ValueStack
import org.parboiled.errors.ParseError
import org.parboiled.support.{ParsingResult => PParsingResult}
import annotation.unchecked.uncheckedVariance
import org.parboiled.buffers.InputBuffer
import collection.JavaConversions._

object ParsingResult {
  def apply[V](result: PParsingResult[V])  = new ParsingResult[V](result)

  implicit def unwrap[V](result: ParsingResult[V]): PParsingResult[V] = result.inner
}

/**
 * The scala wrapper for the org.parboiled.support.ParsingResult class.
 */
class ParsingResult[+V](val inner: PParsingResult[V] @uncheckedVariance) {
  val matched: Boolean = inner.matched
  val result: Option[V] = Option(inner.resultValue)
  val parseErrors: List[ParseError] = inner.parseErrors.toList
  val parseTreeRoot: Node[V] @uncheckedVariance = inner.parseTreeRoot
  val valueStack: ValueStack[Any] = inner.valueStack.asInstanceOf[ValueStack[Any]]
  val inputBuffer: InputBuffer = inner.inputBuffer
}