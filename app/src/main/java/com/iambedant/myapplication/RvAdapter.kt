package com.iambedant.myapplication

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.rv_item.view.*

/**
 * Created by @iamBedant on 13/05/18.
 */
class RvAdapter(val dataSource: ArrayList<String>) : RecyclerView.Adapter<ViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.rv_item, parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder?.tvAnimalType?.text = dataSource.get(position)
    }

    override fun getItemCount(): Int {
        return dataSource.size
    }


}

class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
    val tvAnimalType = view.textView
}

