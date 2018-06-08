# Introduction to the Design of Computational Calculi - Discussion 2

## Structural Equivalence
Allows us to erase distinctions in our grammar that we do not want to exist.

$P|Q \equiv Q|P$

##
Structural equivalences are two-way rewrite rules.

$P|Q \rightleftharpoons Q|P$

. . .

Computational steps (or reductions) are one-way rewrite rules.

$x!(P) | for (y \leftarrow x) \{Q\} \rightarrow Q[@P/x]$

##
$\alpha$-equivalence

We can "rename" our variables without introducing conflicts and our program behaves the same.

$\lambda x.x \equiv \lambda y.y$

##
Bound names and Free Names

$M,N ::= x | \lambda x.M | (M N)$

$FN(x) = \{x\}$

$FN(\lambda x.M) = FN(M) \setminus \{x\}$

$FN( (M N) ) = FN(M) \cup FN(N)$

A name is bound if it occurs in the term, and is not free.

. . .

How would we define names of a lambda term?

# Hands-on

## Greg

Define Domain Equation & Scala Types for the Grammar of:

* Ambient Process Calculus
* Blue Calculus
* Linear Lambda Calculus

Port the Free Names & Bound Names equations to the scala representation of our Rho Grammar.

## Ambient Process Calculus
An Ambient is a bounded place where computation happens

. . .

An Ambient can contain other Ambients

. . .

An Ambient can be moved

. . .

An Ambient can contain multiple processes running directly in the Ambient, controlling it.

. . .

A Name is something that can be created, passed around, and used to name new ambients.

## Jake

Define the grammar of Pi-Calc in KFramework

. . . 

Define the semantics of Pi-Calc in KFramework

See [KFramework Tutorials](https://github.com/kframework/k5/tree/master/k-distribution/tutorial/1_k) and [the beginnings of a definition](https://github.com/rchain/rchain/blob/master/rholang/src/main/k/minpi2/minpi.k)
