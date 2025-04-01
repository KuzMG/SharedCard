package com.example.sharedcard.ui.purchase.adapters

import android.graphics.Canvas
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.example.sharedcard.R
import com.example.sharedcard.database.entity.purchase.Purchase
import it.xabaras.android.recyclerview.swipedecorator.RecyclerViewSwipeDecorator

abstract class PurchaseSwipeCallback : ItemTouchHelper.Callback() {
    override fun getMovementFlags(
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder
    ) = makeFlag(ItemTouchHelper.ACTION_STATE_SWIPE, ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT)

    override fun onMove(
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        target: RecyclerView.ViewHolder
    ) = false

    abstract fun swipeLeft(purchase: Purchase,position: Int)
    abstract fun swipeRight(purchase: Purchase,position: Int)
    override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
        val purchase = (viewHolder as PurchaseItemListAdapter.PurchaseItemHolder).purchase
        when (direction) {
            ItemTouchHelper.LEFT -> swipeLeft(purchase,viewHolder.absoluteAdapterPosition)
            ItemTouchHelper.RIGHT -> swipeRight(purchase,viewHolder.absoluteAdapterPosition)
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
            .addSwipeRightActionIcon(R.drawable.ic_purchase)
            .create()
            .decorate()
        super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
    }
}