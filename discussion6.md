# Introduction to the Design of Computational Calculi - Discussion 6

# Observability

##
Observation relation $\downarrow _N$ is parametarized by a set of names, $N$.

It is the smallest relation satisfying:

$\frac{y \in N, x \equiv _N y}{x!(R) \downarrow _N x}$

$\frac{P \downarrow _N x \text{ or } Q \downarrow _N x}{P|Q \downarrow _N x}$

. . .

We write $P \Downarrow _N x$ if there is $Q$ such that $P \Rightarrow Q$ and $Q \downarrow _N x$

## True or False

$x!(P) | x(y).Q \downarrow _{\{x,y,z\}} x$

##
$x(y).(P) | y!(Q) \downarrow _{\{x,y,z\}} x$

##
$x(y).y!(P) | x!(*z) \downarrow _{\{x,y,z\}} x$

##
$x(y).y!(P) | x!(*z) \downarrow _{\{x,y,z\}} y$

##
$x(z).y!(P) | x!(*z) \downarrow _{\{x,y,z\}} y$

##
$x(y).y!(P) | x!(*z) \downarrow _{\{x,y,z\}} z$

##
$x(y).y!(P) | x!(*z) \Downarrow _{\{x,y,z\}} z$

# Bisimulation

##
$N$-barbed bisimulation is parametric in a set of names, $N$.

$P S_N Q$ is a symmetric binary relation that implies:

If $P \rightarrow P'$ then $Q \Rightarrow Q'$ and $P' S_N Q'$

If $P \downarrow _N x$ then $Q \Downarrow _N x$

## True or False
$x!(*y) S_{\{x,y\}} x!(*z)$

##
$x!(*y) S_{\{x,y\}} x!(*z) | y!(*z)$

##
$x!(*y) S_{\{x,y\}} x(*z) | z!(*z)$

##
$x!(*y) S_{\{x,y\}} x!(*z) | y(z).x!(z)$

##
$x!(*y) S_{\{x,y\}} z!(*x) | z(z).z!(*y)$

# Full Abstraction

##
Parametric in two equivalences, one for the source ($\equiv _s$) and one for the target ($\equiv _t$) language

Target is a full abstraction of Source if

$P \equiv _s Q \Leftrightarrow [P] \equiv _t [Q]$
