sealed trait M[X]

case class Mention[X]( x : X ) extends M[X]
case class Abstraction[X]( x : X, m : M[X] ) extends M[X]
case class Application[X]( m : M[X], n : M[X] ) extends M[X]
