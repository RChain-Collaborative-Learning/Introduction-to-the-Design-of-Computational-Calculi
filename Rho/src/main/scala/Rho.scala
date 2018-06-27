import collection.immutable.Map
import scala.annotation.tailrec

object Rho {

  // Inputs is the type that defines all of our "listeners" in a process.
  //   They are uniquely identified by the Name being listened on, and a following process.
  //   There is no need to store the bound name here, as the bound name will be replaced by an identifier
  //   based on its position in a process. (Similar to a De Bruijn Index). Two inputs will be regarded as equivalent
  //   if they only differ in the bound names (are alpha equivalent).
  private final type Inputs = MultiSet[(Name, Process)]
  // Lifts is the type that defines all of our "outputs" in a process.
  private final type Lifts = MultiSet[(Name, Process)]
  // Drops defines all of the names that we "request to run" in a top level par context.
  private final type Drops = MultiSet[Name]


  // Process is the type that represents a rho calculus process
  sealed class Process( val inputs: Inputs = MultiSet.empty,
                        val lifts: Lifts = MultiSet.empty,
                        val drops: Drops = MultiSet.empty,
                        // height is used for De Bruijn-like indexing of bound names. Could be calculated on the fly,
                        //   but is stored here for ease of use.
                        val height: Int = 0) {

    // Process equality is defined by structural equivalence.
    //   (Final here means we should be safe without the canEqual method, even with extensions of this class)
    final override def equals(other: Any): Boolean = {
      other match {
        case that: Process => (inputs == that.inputs) &&
                              (lifts == that.lifts) &&
                              (drops == that.drops)
        case _ => false
      }
    }

    // hashCode needs to be overridden with equals. Done as described in:
    //   Programming in Scala Second Edition by Odersky, Spoon, and Venners
    final override val hashCode: Int = {
      41 * (
        41 * (
          41 + (if( inputs == null ) 0 else inputs.hashCode)
        ) + (if( lifts == null ) 0 else lifts.hashCode)
      ) + (if( drops == null ) 0 else drops.hashCode)
    }

    // Get some sort of string representation of our process.
    override def toString: String = {
      // TODO: this could look much nicer
      (if (inputs.nonEmpty) "in " + inputs.toString + "\n" else "") +
        (if (lifts.nonEmpty) "out " + lifts.toString + "\n" else "") +
        (if (drops.nonEmpty) "drop " + drops.toString + "\n" else "")
    }
  }

  // Type that represents rho calculus names
  sealed trait Name

  // A Name can be a quoted process
  final class Quote(x: Process) extends Name {
    // Quote(Drop(x)) is name equivalent to x
    //   This means the the drops embedded in x cannot ever be bound via input prefix
    val p: Process = runDrops(x)

    override def equals(other: Any): Boolean = {
      other match {
        case that: Quote => p == that.p
        case _ => false
      }
    }

    // hashCode needs to be overridden with equals. Done as described in:
    //   Programming in Scala Second Edition by Odersky, Spoon, and Venners
    override val hashCode: Int = {
      41 + p.hashCode
    }
  }

  // A Name can also be bound, or "new", in a process, in which case it can be substituted by alpha equivalence
  //   while the process remains structurally equivalent. The type BoundName is used within a process to abstract
  //   away the specific process behind a name (to something similar to a De Bruijn Index). These should never appear
  //   at the top level definition of a process, but only under prefix of an Input.
  private final case class BoundName(int: Int) extends Name

  // the rho calculus null process
  final object Zero extends Process

  final class Drop( x: Name ) extends Process( drops = MultiSet(x -> 1) )

  final class Parallel( p: Process, q: Process ) extends Process( inputs = union(Seq(p.inputs, q.inputs)),
                                                                  lifts = union(Seq(p.lifts, q.lifts)),
                                                                  drops = union(Seq(p.drops, q.drops)),
                                                                  height = math.max(p.height, q.height) ) {
  }

  // The rho calculus output process
  final class Lift( x: Name, p: Process ) extends Process( lifts = MultiSet((x, p) -> 1),
                                                           height = p.height )

  final class Input( y: Name, x: Name, p: Process )
    extends Process( inputs = MultiSet((x, substitute(y, BoundName(p.height), p)) -> 1),
                     height = p.height + 1 ) {
  }

  // Substitute x for y in process p
  private def substitute( x: Name, y: Name, p: Process ): Process = {
    // TODO: Common pattern here, could be refactored to look much nicer
    val subInputs: Inputs = p.inputs.foldLeft(MultiSet.empty[(Name, Process)])({
      case (acc, ((n, q), i)) if n == x => union( Seq(acc, Map((y, substitute(x, y, q)) -> i)) )
      case (acc, ((n, q), i))           => union( Seq(acc, Map((n, substitute(x, y, q)) -> i)) )
    })
    val subLifts: Lifts = p.lifts.foldLeft(MultiSet.empty[(Name, Process)])({
      case (acc, ((n, q), i)) if n == x => union( Seq(acc, Map((y, substitute(x, y, q)) -> i)) )
      case (acc, ((n, q), i))           => union( Seq(acc, Map((n, substitute(x, y, q)) -> i)) )
    })
    val subDrops: Drops = p.drops.foldLeft(MultiSet.empty[Name])({
      case (acc, (n, i)) if n == x => union( Seq(acc, Map(y -> i)) )
      case (acc, (n, i))           => union( Seq(acc, Map(n -> i)) )
    })

    new Process(subInputs, subLifts, subDrops, p.height)
  }

  // One-step reductions for a process p.
  //   Note, all the processes returned in this set have their drops "run",
  //   so you will not be able to bind them with a prefixed input.
  def reductions( p: Process ): Set[Process] = {
    val running: Process = runDrops(p)
    val ret: Iterable[Process] = for(
      ((nameIn, continuation), nIn) <- running.inputs;
      ((nameOut, processOut), nOut) <- running.lifts
      // TODO: uhg, using tuples and type aliases is coming back to bite me.
      // If the names are equivalent
      if nameIn == nameOut
    ) yield runDrops(substitute(BoundName(continuation.height), new Quote(processOut), new Process(
      inputs = union(Seq(
        if( nIn == 1 ) {
          running.inputs - Tuple2(nameIn, continuation)
        } else {
          running.inputs.updated((nameIn, continuation), nIn - 1)
        },
        continuation.inputs)
      ),
      lifts = union(Seq(
        if( nOut == 1 ) {
          running.lifts - Tuple2(nameOut, processOut)
        } else {
          running.inputs.updated((nameOut, processOut), nOut - 1)
        },
        continuation.lifts)
      ),
      drops = running.drops,
      height = math.max(running.height, continuation.height)
    )))
    ret.toSet
  }

  @tailrec
  def runDrops( p: Process ): Process = {
    if (p.drops.isEmpty) {
      p
    } else {
      val (inputs, lifts, drops, height) = p.drops.foldLeft( (p.inputs, p.lifts, MultiSet.empty[Name], p.height) )({
        case ((i, l, d, h), (n: Quote, f)) => (union(Seq( (i, 1), (n.p.inputs, f) )),
                                               union(Seq( (l, 1), (n.p.lifts, f) )),
                                               union(Seq( (d, 1), (n.p.drops, f) )),
                                               math.max(h, n.p.height))
      })
      runDrops(new Process(inputs, lifts, drops, height))
    }
  }

  // FreqMap[X] can be thought of as an unordered collection of X's, where a particular X (or something == to X) may
  //   occur multiple times.
  private final type MultiSet[X] = Map[X, Int]

  private final object MultiSet {
    def empty[X] = Map.empty[X, Int]
  }

  private def union[X](maps: Seq[(MultiSet[X], Int)]): MultiSet[X] = {
    maps.foldLeft(MultiSet.empty[X])({
      case (acc, (map, i)) =>
        map.foldLeft(acc)({
          case (l, (k, v)) => l.+((k, (i * v) + acc.getOrElse(k, 0)))
        })
    })
  }

  private final def union[X](maps: Seq[MultiSet[X]]): MultiSet[X] = {
    union(maps.foldLeft(Seq.empty[(MultiSet[X], Int)])({
      case (acc, map) => acc.+:((map, 1))
    }))
  }

}
