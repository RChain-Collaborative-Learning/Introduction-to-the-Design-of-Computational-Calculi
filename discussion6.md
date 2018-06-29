# Introduction to the Design of Computational Calculi - Discussion 6

# Observability

##
Observation relation $\downarrow _N$ is parametarized by a set of names, $N$.

It is the smallest relation satisfying:

$\frac{y \in N, x \equiv _N y}{x!(R) \downarrow _N x}$

$\frac{P \downarrow _N x \text{ or } Q \downarrow _N x}{P|Q \downarrow _N x}$

. . .

We write $P \Downarrow _N x$ if there is $Q$ such that $P \Leftarrow Q$ and $Q \downarrow _N x$

## True or False
$x!(P) | x(y).Q \downarrow _N x$

##
$x(y).(P) | y!(Q) \downarrow _N x$

##
$x(y).y!(P) | x!(*z) \downarrow _N x$

##
$x(y).y!(P) | x!(*z) \downarrow _N y$

##
$x(z).y!(P) | x!(*z) \downarrow _N y$

##
$x(y).y!(P) | x!(*z) \downarrow _N z$

##
$x(y).y!(P) | x!(*z) \Downarrow _N z$

# Bisimulation
