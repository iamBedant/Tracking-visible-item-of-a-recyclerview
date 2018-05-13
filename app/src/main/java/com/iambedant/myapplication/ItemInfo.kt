package com.iambedant.myapplication

/**
 * Created by @iamBedant on 13/05/18.
 */
class ItemInfo(val position: Int, val time: Long){
    override fun equals(other: Any?): Boolean {
        val otherItem  = other as ItemInfo
        return otherItem.position == this.position
    }
}