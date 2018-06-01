# Introduction to the Design of Computational Calculi - Discussion 1

## Grammar
Allows us to answer the question: "Is this a valid program"?

Often represented in BNF (Backus-Naur) form, a notation for context-free grammars

##
Lambda Calc

$M,N ::= x | \lambda x.M | (M N)$

Rho Calc

$P,Q ::= 0 | for(y \leftarrow x)P | x!(Q) | (P|Q) | *x$

$x,y ::= @P$

## Mathematical Models of Grammars
$M,N ::= x | \lambda x.M | (M N)$

$M[X] = X + (X \times M[X]) + (M[X] \times M[X])$

. . .

$X = \lambda x | \triangle x$

. . .

$M[X] = X + X$

## Mathematical Model of Pi Calc Grammar
$P,Q ::= N | (P|Q) | !P | (\nu x)P$

$N,M ::= \pi .P | 0 | M + N$

$\pi ::= x(y) | \overline{x} y$

. . .

$P[X] = N[X] + (P[X], X] \times P[X]) + P[X] + (X \times P[X])$

$N[X] = (\pi[X] \times P[X]) + 1 + (N[X] \times N[X])

$\pi[X] = (X \times X) + (X \times X)

## Mathematical Model of RHo Calc Grammar
$P,Q ::= 0 | for(y \leftarrow x)P | x!(Q) | (P|Q) | *x$

$x,y ::= @P$

$P[X] = 1 + (X \times X \times P[X]) + (X \times P[X]) + (P[X] \times P[X]) + X$

$RP = P[RP]$

## Scala Models of Grammars
$M,N ::= x | \lambda x.M | (M N)$

trait M[X]

case class Mention[X]( x : X ) extends M[X]

case class Abstraction[X](x : X, m : M[X] ) extends M[X]

case class Application[X]( m : M[X], n : M[X] )

## Scala Model of our "simple grammar"
$X = \lambda x | \triangle x$

trait S[X]

case class lambda[X]( x : X ) extends S[X]

case class triangle[X]( x : X ) extends S[X]

## Scala Model of Pi Calc Grammar
trait P[X]

case class par[X]( p : P[X], q : P[X] ) extends P[X]
case class replication[X]( p : P[X] ) extends P[X]
case class new[X]( toBind : X, p : P[X] ) extends P[X]

trait Pi[X]
case class input[X]( subject : X, object : X ) extends Pi[X]
case class output[X]( subject : X, object : X) extends Pi[X]

case class normal[X]( sum : Set[(Pi[X], P[X])] ) extends P[X]

## Scala Model of Rho Calc Grammar
given as homework

# KFramework

##
"Programming lanaguages usually have a formally defined SYNTAX,
but SEMANTICS are usually given in the form of a reference manual."

## Uses
Has been used to explore prototypical languages like $\lambda$ calculus, system F, and Agents

Formal definitions for C, Python, Scheme, and OCL, Java 7, Haskell, and JavaScript

K has been used to define type checkers and inferencers and can be used for program verification, model checking, and researching runtime verification techniques.

# Our first definition

## Modules
module <NAME>
  ...
endmodule

## Our first definition
module EXP-SYNTAX
  imports INT

  syntax Exp ::= Int
               | Exp "+" Exp
               | Exp "*" Exp
               | Exp "/" Exp
               | "read"
               | "print" "(" Exp ")"
               | "(" Exp ")"
endmodule

module EXP
  imports EXP-SYNTAX
endmodule

## Pi Calc Syntax in K-Framework
Homework

Check out [lambda calculus tutorial lesson 1](https://github.com/kframework/k5/tree/master/k-distribution/tutorial/1_k/1_lambda/lesson_1) for hints
