package com.example.sharedcard.ui.purchase.adapters

import android.graphics.Canvas
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.example.sharedcard.R
import com.example.sharedcard.database.entity.basket.Basket
import it.xabaras.android.recyclerview.swipedecorator.RecyclerViewSwipeDecorator

abstract class BasketSwipeCallback : ItemTouchHelper.Callback() {
    override fun getMovementFlags(
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder
    ): Int {
        val basket = (viewHolder as CountPurchaseItemAdapter.CountPurchaseItemHolder).basket
        return when(basket.history){
            null -> makeFlag(ItemTouchHelper.ACTION_STATE_SWIPE, ItemTouchHelper.LEFT)
            else ->makeFlag(ItemTouchHelper.ACTION_STATE_IDLE, ItemTouchHelper.LEFT)
        }
    }
    override fun onMove(
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        target: RecyclerView.ViewHolder
    ) = false


    abstract fun action(basket: Basket, position: Int)
    override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
        val basket = (viewHolder as CountPurchaseItemAdapter.CountPurchaseItemHolder).basket
        if (direction == ItemTouchHelper.LEFT) {
            action(basket,viewHolder.absoluteAdapterPosition)
        }
    }

    override fun onChildDraw(
        c: Canvas,
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        dX: Float,
        dY: Float,
        actionState: Int,
        isCurrentlyActive: Boolean
    ) {
        RecyclerViewSwipeDecorator.Builder(
            c,
            recyclerView,
            viewHolder,
            dX,
            dY,
            actionState,
            isCurrentlyActive
        )
            .addSwipeLeftActionIcon(R.drawable.swipe_delete_icon)
            .create()
            .decorate()
        super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
    }
}