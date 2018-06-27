import Arabic.Digit.Digit
import Arabic.Numeral.Numeral
import Arabic.Sum.Sum

import scala.collection.immutable.Bag

object Arabic {

  object Digit {

    // Digit = 0 | 1 | 2 | ...
    sealed abstract class Digit
      extends Numeral.Addable[Digit] with Ordered[Digit] with Numeral.HasOne[Digit] {

      final def one: Digit = One

      protected def prev: Digit
      protected def next: Digit

      final override def compare(that: Digit): Int = this match {
        case Zero => that match {
          case Zero => 0
          case _ => 1
        }
        case Nine => that match {
          case Nine => 0
          case _ => -1
        }
        case _ =>
          if (that.compareTo(prev) <= 0) -1
          else if (that.compareTo(next) >= 0) 1
          else 0
      }

      final override def +(b: Digit): Digit = this match {
        case Zero => b
        case _ => this.prev + b.next
      }

    }

    final case object Zero extends Digit {
      protected val prev: Digit = Nine
      protected val next: Digit = One
    }

    final case object One extends Digit {
      protected val prev: Digit = Zero
      protected val next: Digit = Two
    }

    final case object Two extends Digit {
      protected val prev: Digit = One
      protected val next: Digit = Three
    }

    final case object Three extends Digit {
      protected val prev: Digit = Two
      protected val next: Digit = Four
    }

    final case object Four extends Digit {
      protected val prev: Digit = Three
      protected val next: Digit = Five
    }

    final case object Five extends Digit {
      protected val prev: Digit = Four
      protected val next: Digit = Six
    }

    final case object Six extends Digit {
      protected val prev: Digit = Five
      protected val next: Digit = Seven
    }

    final case object Seven extends Digit {
      protected val prev: Digit = Six
      protected val next: Digit = Eight
    }

    final case object Eight extends Digit {
      protected val prev: Digit = Seven
      protected val next: Digit = Nine
    }

    final case object Nine extends Digit {
      protected val prev: Digit = Eight
      protected val next: Digit = Zero
    }

  }

  object Numeral {

    trait Addable[X] {
      def +(x: X): X
    }

    trait HasOne[X] {
      def one: X
    }

    // Numeral = Digit | Numeral Numeral
    sealed class Numeral[X <: Addable[X] with Ordered[X] with HasOne[X]] protected ( val number: Seq[X] = Seq.empty[X] )
      extends Sum.Addable[Numeral[X]] {

      def +( b: Numeral[X] ): Numeral[X] = number match {
        case Nil => b
        case (ah :: atl) => b.number match {
          case Nil => this
          case (bh :: btl) =>
            val x = ah + bh
            if ( x < ah && ah >= ah.one ) {
              Concat( new Numeral(atl) + new Numeral(btl) + Lift(x.one), Lift(x) )
            } else {
              Concat( new Numeral(atl) + new Numeral(btl), Lift(x) )
            }
        }
      }

      final override def equals(o: scala.Any): Boolean = o match {
        case that: Numeral[X] => number == that.number
        case _ => false
      }

      final override def hashCode: Int = 41 + number.hashCode

    }

    final case class Lift[X <: Addable[X] with Ordered[X] with HasOne[X]]( x: X )
      extends Numeral[X]( number = Seq(x) )

    final case class Concat[X <: Addable[X] with Ordered[X] with HasOne[X]]( a: Numeral[X], b: Numeral[X] )
      extends Numeral[X]( number = b.number ++ a.number )

  }

  object Sum {

    trait Addable[X] {
      def +(x: X): X
    }

    // Sum = Numeral | Sum + Sum
    sealed class Sum[X <: Addable[X]] protected ( val sum: Bag[X] ) {

      override def equals( o: scala.Any ): Boolean = o match {
        case that: Sum[X] => sum == that.sum
        case _ => false
      }

      final override def hashCode: Int = 41 + sum.hashCode

      final def reductions: Set[Sum[X]] = {
        for ( summandA <- sum;
              summandB <- sum - summandA
        ) yield new Sum( (sum - (summandA, summandB)) + summandA.+(summandB) )
      }.toSet

    }

    // This MultiSet library is a bit gross: Bag.configuration.compact means we store a map from
    //  elements to Ints representing their frequency
    final case class Lift[X <: Addable[X]]( x: X ) extends Sum[X]( sum = Bag(Bag.configuration.compact[X])( x ) )

    final class Add[X <: Addable[X]]( a: Sum[X], b: Sum[X] ) extends Sum[X]( sum = a.sum ++ b.sum )

  }

  type ArabicDecimal = Sum[Numeral[Digit]]

}
