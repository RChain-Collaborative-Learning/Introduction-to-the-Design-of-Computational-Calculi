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

      override def compare(that: Digit): Int = this match {
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

    // This is annoying, but if felt like cheating to instantiate this class with Ints
    final case object Zero extends Digit {
      protected val prev: Digit = Nine
      protected val next: Digit = One

      override def toString: String = "0"
    }

    final case object One extends Digit {
      protected val prev: Digit = Zero
      protected val next: Digit = Two

      override def toString: String = "1"
    }

    final case object Two extends Digit {
      protected val prev: Digit = One
      protected val next: Digit = Three

      override def toString: String = "2"
    }

    final case object Three extends Digit {
      protected val prev: Digit = Two
      protected val next: Digit = Four

      override def toString: String = "3"
    }

    final case object Four extends Digit {
      protected val prev: Digit = Three
      protected val next: Digit = Five

      override def toString: String = "4"
    }

    final case object Five extends Digit {
      protected val prev: Digit = Four
      protected val next: Digit = Six

      override def toString: String = "5"
    }

    final case object Six extends Digit {
      protected val prev: Digit = Five
      protected val next: Digit = Seven

      override def toString: String = "6"
    }

    final case object Seven extends Digit {
      protected val prev: Digit = Six
      protected val next: Digit = Eight

      override def toString: String = "7"
    }

    final case object Eight extends Digit {
      protected val prev: Digit = Seven
      protected val next: Digit = Nine

      override def toString: String = "8"
    }

    final case object Nine extends Digit {
      protected val prev: Digit = Eight
      protected val next: Digit = Zero

      override def toString: String = "9"
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

      override final def equals(o: scala.Any): Boolean = o match {
        case that: Numeral[X] => number == that.number
        case _ => false
      }

      override final def hashCode: Int = 41 + number.hashCode

      override final def toString: String = number.mkString

    }

    final case class Lift[X <: Addable[X] with Ordered[X] with HasOne[X]]( x: X )
      extends Numeral( number = Seq(x) )

    final case class Concat[X <: Addable[X] with Ordered[X] with HasOne[X]]( a: Numeral[X], b: Numeral[X] )
      extends Numeral[X]( if ( a.number.last < a.number.head.one ) b.number else b.number ++ a.number )

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

      override final def hashCode: Int = 41 + sum.hashCode

      final def reductions: Set[Sum[X]] = {
        for ( summandA <- sum;
              summandB <- sum - summandA
        ) yield new Sum( (sum - (summandA, summandB)) + summandA.+(summandB) )
      }.toSet

      override final def toString: String = sum.mkString(sep = "+")

    }

    // This MultiSet library is a bit gross: Bag.configuration.compact means we store a map from
    //  elements to Ints representing their frequency
    final case class Lift[X <: Addable[X]]( x: X ) extends Sum[X]( sum = Bag(Bag.configuration.compact[X])( x ) )

    final class Add[X <: Addable[X]]( a: Sum[X], b: Sum[X] ) extends Sum[X]( sum = a.sum ++ b.sum )

  }

  type ArabicDecimal = Sum[Numeral[Digit]]

}
