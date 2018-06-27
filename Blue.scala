//P[X] = X
//       + (P[X] x X)
//       + (X x P[X])
//       + (X x P[X])
//       + (X x P[X])
//       + (X x P[X])
//       + (P[X] x P[X])
sealed trait P[X]

case class mention[X]    ( x: X )             extends P[X] // x
case class application[X]( p: P[X], x: X )    extends P[X] // (P x)
case class abstraction[X]( x: X, p: P[X] )    extends P[X] // lambda x.P
case class new[X]        ( x: X, p: P[X] )    extends P[X] //vx.P
case class fetch[X]      ( x: X, p: P[X] )    extends P[X] // (x <= P)
case class rep[X]        ( x: X, p: P[X] )    extends P[X] // (x = P)
case class par[X]        ( p: P[X], q: P[X] ) extends P[X] // P | Q

// The "new" operator also feels awkward here
