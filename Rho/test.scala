val z: Process = Zero
val nz: Name = new Quote(Zero)

val z1: Process = new Parallel(Zero, Zero)
val z2: Process = new Parallel(Zero, Zero, Zero)
val nz1: Name = new Quote(z1)
val nz2: Name = new Quote(z2)

z1 == z
z == z2
z1 == z2
nz1 == nz2
nz2 == nz
nz1 == nz

val l1: Process = new Lift(nz, new Drop(nz))
val i1: Process = new Input(nz, nz, Zero)

l1 != z
i2 != z
l1 != i1

val nl1: Name = new Quote(l1)
val ni1: Name = new Quote(i1)

nl1 != ni1
nl1 != nz
ni1 != nz

new Parallel(l1, Zero) == l1
new Parallel(i1, Zero) == i1
val y: Process = new Parallel(i1, l1)
y == new Parallel(l1, i1)
val v: Process = new Parallel(new Parallel(i1, new Drop(nl1)), l1)
v == new Parallel(i1, new Parallel(new Drop(nl1), l1))

val x: Name = nz
val zz: Name = nl1
val w: Name = ni1
val ny: Name = new Quote(y)
val nv: Name = new Quote(v)

x != zz
x != w
x != ny
x != nv
zz != w
zz != ny
zz != nv
w != ny
w != nv
ny != nv

val a: Process = new Input(zz, x, new Lift(w, new Lift(ny, Drop(zz))))
val b: Process = new Input(nv, x, new Lift(w, new Lift(ny, Drop(nv))))

a == b

val na: Name = Quote(a)
Quote(Drop(na)) == na
na == Quote(Drop(na))
Quote(a) == Quote(b)

val c: Process = new Lift(w, new Lift(ny, new Drop(zz)))
val d: Process = new Lift(w, new Drop(new Quote(new Lift(ny, new Drop(zz)))))

val pc: Process = new Input(zz, nz, c)
fal pd: Process = new Input(zz, nz, d)
