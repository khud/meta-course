package metacourse.tsg


sealed class Exp

abstract class Val : Exp()

abstract class Var(val name: Name) : Exp() {
    override fun equals(other: Any?): Boolean {
        return other != null
                && other is Var
                && this::class == other::class
                && this.name == other.name
    }

    override fun hashCode(): Int {
        return name.hashCode()
    }
}

class Atom(val name: String) : Val() {
    override fun equals(other: Any?): Boolean {
        return other != null && other is Atom && other.name == name
    }

    override fun hashCode(): Int {
        return name.hashCode()
    }
}

class Cons(val head: Exp, val tail: Exp) : Exp()

open class Pve(name: Name) : Var(name) {
    override fun toString(): String = "e.$name"
}

class Pva(name: Name) : Pve(name) {
    override fun toString(): String = "a.$name"
}

typealias Name = String

sealed class Cond

data class IsEqa(val x: Exp, val y: Exp) : Cond()

data class IsCons(val exp: Exp, val head: Pve, val tail: Pve, val atom: Pva) : Cond()

typealias FName = String
typealias Param = Var

sealed class Term

data class Alt(val cond: Cond, val then: Term, val otherwise: Term) : Term()

data class Call(val name: FName, val args: List<Exp>) : Term()

data class Ret(val exp: Exp) : Term() {
    override fun toString(): String = exp.toString()
}

data class FDef(val name: FName, val params: List<Param>, val body: Term)

typealias ProgR = List<FDef>


fun DEFINE(name: FName, vararg params: Var, body: () -> Term) = FDef(name, params.toList(), body())

fun CALL(name: FName, vararg args: Exp) = Call(name, args.toList())

class Alt1(private val cond: Cond, private val then: Term) {
    infix fun ELSE(body: () -> Term): Alt = Alt(cond, then, body())
}

fun IF(cond: Cond, then: () -> Term) = Alt1(cond, then())

fun MATCH(exp: Exp, head: Pve, tail: Pve, atom: Pva) = IsCons(exp, head, tail, atom)

fun EQ(x: Exp, y: Exp) = IsEqa(x, y)

