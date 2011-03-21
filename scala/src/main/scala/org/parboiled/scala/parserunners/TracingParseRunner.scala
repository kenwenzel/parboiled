package org.parboiled.scala.parserunners

import org.parboiled.parserunners.{TracingParseRunner => PTracingParseRunner}
import org.parboiled.scala._
import org.parboiled.Context
import utils.Predicate
import org.parboiled.support.MatcherPath
import org.parboiled.common.{Sink, Predicate => PPredicate, Tuple2 => T2}

/**
 * A wrapper for org.parboiled.parserunners.TracingParseRunner which returns a scala ParsingResult.
 * It provides for the ability to attach filter expressions for limiting the tracing printout to certain input and/or
 * grammar areas.
 * Note that the ParseRunner only accepts rules with zero or one value type parameter, as parsers leaving more
 * than one value on the value stack are considered to be bad style.
 */
object TracingParseRunner {
  def apply(rule: Rule0) = new TracingParseRunner[Nothing](new PTracingParseRunner[Nothing](rule))

  def apply[V](rule: Rule1[V]) = new TracingParseRunner[V](new PTracingParseRunner[V](rule))
}

class TracingParseRunner[V](val inner: PTracingParseRunner[V]) extends ParseRunner[V] {
  def run(input: Input): ParsingResult[V] = ParsingResult(inner.run(input.inputBuffer))

  /**
   * Attaches the given filter predicate limiting the tracing printout.
   * You can supply an expression consisting of either the predefined Predicates (see the Lines, Rules, Matched and
   * Mismatched objects) or custom functions with one of the following signatures as TracingPredicates:
   * Context[_] => Boolean
   * (Context[_], Boolean) => Boolean
   *
   * For example: "Lines(10 until 20) && Rules.below(parser.Factor)".
   */
  def filter(filter: TracingPredicate) = {
    inner.withFilter(new PPredicate[T2[Context[_], Boolean]] {
      def apply(t: T2[Context[_], Boolean]) = filter(t.a, t.b)
    })
    this
  }

  /**
   * Attaches the given log sink to this TracingParseRunner instance.
   */
  def log(log: Sink[String]) = {
    inner.withLog(log)
    this
  }
}

abstract class TracingPredicate extends Predicate[(Context[_], Boolean)]

object TracingPredicate {
  implicit def fromRawPredicate(p: Predicate[(Context[_], Boolean)]): TracingPredicate = new TracingPredicate {
    def apply(t: (Context[_], Boolean)) = p(t)
  }

  implicit def fromContextPredicate(f: Context[_] => Boolean): TracingPredicate = new TracingPredicate {
    def apply(t: (Context[_], Boolean)) = f(t._1)
  }
}

object Lines {
  /**
   *  Creates a TracingPredicate which selects all lines with a number greater or equal to the given one. 
   */
  def from(firstLine: Int): TracingPredicate = toPredicate(_ >= firstLine)

  /**
   * Creates a TracingPredicate which selects all lines with a number smaller or equal to the given one.
   */
  def until(lastLine: Int): TracingPredicate = toPredicate(_ <= lastLine)

  /**
   * Creates a TracingPredicate which selects all lines in the given range of line numbers.
   */
  def apply(lineRange: Range): TracingPredicate = {
    require(lineRange.step == 1, "Ranges with step != 1 are not supported here")
    toPredicate(line => { lineRange.start <= line && line < lineRange.end })
  }

  private def toPredicate(f: Int => Boolean): TracingPredicate =
    (c: Context[_]) => f(c.getInputBuffer.getPosition(c.getCurrentIndex).line)
}

object Rules {
  /**
   * Creates a TracingPredicate which selects only the given rules.
   */
  def only(rules: Rule*): TracingPredicate = {
    val matchers = rules.map(_.matcher)
    (c:Context[_]) => matchers.contains(c.getMatcher)
  }

  /**
   * Creates a TracingPredicate which selects only rules on levels below the given rules.
   */
  def below(rules: Rule*): TracingPredicate = {
    val matchers = rules.map(_.matcher)
    toPredicate(path => { matchers.exists(m => (path.element.matcher ne m) && path.contains(m)) })
  }

  /**
   * Creates a TracingPredicate which selects all the given rules and their sub rules.
   */
  def apply(rules: Rule*): TracingPredicate = {
    val matchers = rules.map(_.matcher)
    toPredicate(path => { matchers.exists(path.contains) })
  }

  private def toPredicate(f: MatcherPath => Boolean): TracingPredicate = (c: Context[_]) => f(c.getPath)
}

/**
 * A TracingPredicate selecting only rules that have matched.
 */
object Matched extends TracingPredicate {
  def apply(t: (Context[_], Boolean)) = t._2
}

/**
 * A TracingPredicate selecting only rules that have not matched.
 */
object Mismatched extends TracingPredicate {
  def apply(t: (Context[_], Boolean)) = !t._2
}