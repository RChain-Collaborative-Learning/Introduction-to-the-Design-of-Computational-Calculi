//P[N] = (N x P[N])      // restriction
//       + 1             // inactivity
//       + (P[N] x P[N]) // composition
//       + P[N]          // replication
//       + (N x P[N])    // ambient
//       + (N x P[N])    // in
//       + (N x P[N])    // out
//       + (N x P[N])    // open
sealed trait P[N]

case class restriction[N]( n: N, p: P[N])     extends P[N] // (vn)P
case class inactivity[N] ()                   extends P[N] // 0
case class composition[N]( p: P[N], q: P[N] ) extends P[N] // P|Q
case class replication[N]( p: P[N] )          extends P[N] // !P
case class ambient[N]    ( n: N, p: P[N] )    extends P[N] // n[P]
case class action[N]     ( m: M[N], p: P[N])  extends P[N] // M.P

sealed trait M[N]

case class in[N]  ( n: N ) extends M[N]
case class out[N] ( n: N ) extends M[N]
case class open[N]( n: N ) extends M[N]

// The "new" operator is awkward. It assumes that we can generate a new name
// that is not already contained in P.
// Really, what we would like to have as an argument to the restriction
// is a syntactic variable inside of P that we can replace with a newly
// generated name.
