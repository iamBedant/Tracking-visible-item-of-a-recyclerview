package com.iambedant.myapplication

/**
 * Created by @iamBedant on 13/05/18.
 */
class VisibleState(val start:Int, val end:Int){
    override fun equals(o: Any?): Boolean {
        if (this === o) return true
        if (o == null || javaClass != o.javaClass) return false

        val that = o as VisibleState?

        return if (start !== that!!.start) false else end === that!!.end

    }

    override fun hashCode(): Int {
        var result = start
        result = 31 * result + end
        return result
    }

    override fun toString(): String {
        return "VisibleState{" +
                "first=" + start +
                ", last=" + end +
                '}'.toString()
    }
}