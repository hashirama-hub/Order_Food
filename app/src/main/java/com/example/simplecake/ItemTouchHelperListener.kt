package com.example.simplecake

import androidx.recyclerview.widget.RecyclerView

interface ItemTouchHelperListener {
    fun onSwiped(viewHolder:RecyclerView.ViewHolder)
}