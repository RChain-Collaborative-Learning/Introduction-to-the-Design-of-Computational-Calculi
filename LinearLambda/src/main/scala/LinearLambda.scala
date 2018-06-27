object LinearLambda {

  sealed trait Proposition

  case class Conjunction[A, B]( a: A, b: B ) extends Proposition
  case class Implication[A, B]( a: A, b: B ) extends Proposition
  case class Disjunction[A, B]( a: A, b: B ) extends Proposition

  def Id[A](x: A): A = x

  type Gamma = Seq[Proposition]
  type Delta = Seq[Proposition]

  def Exchange[A, B, C]( gamma: Gamma, x: A, y: B, delta: Delta, t: C ): Proposition =

}
