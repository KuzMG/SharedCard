package com.example.sharedcard.ui.group.adapter

import android.graphics.Canvas
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.example.sharedcard.R
import com.example.sharedcard.database.entity.gpoup_person.GroupPersons
import com.example.sharedcard.database.entity.group.GroupEntity
import com.example.sharedcard.database.entity.purchase.Purchase
import com.example.sharedcard.ui.group.delete_group.DeleteGroupFragment
import it.xabaras.android.recyclerview.swipedecorator.RecyclerViewSwipeDecorator

abstract class GroupSwipeCallback : ItemTouchHelper.Callback() {
    override fun getMovementFlags(
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder
    ) = makeFlag(ItemTouchHelper.ACTION_STATE_SWIPE, ItemTouchHelper.LEFT)

    override fun onMove(
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        target: RecyclerView.ViewHolder
    ) = false

    abstract fun swipeLeft(group: GroupEntity)

    override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
        val group = (viewHolder as GroupListAdapter.GroupViewHolder).group
        when (direction) {
            ItemTouchHelper.LEFT -> swipeLeft(group)
        }
        viewHolder.bindingAdapter?.notifyItemChanged(viewHolder.absoluteAdapterPosition)
        viewHolder.closeGroup()
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