package com.example.sharedcard.ui.group.adapter

import android.util.TypedValue
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.sharedcard.R
import com.example.sharedcard.database.entity.gpoup_person.UserEntityWithStatus
import com.example.sharedcard.databinding.ListItemUserBinding
import com.example.sharedcard.ui.group.GroupViewModel
import com.example.sharedcard.ui.group.person_group.PersonGroupBottomSheet
import com.project.shared_card.database.dao.group_users.GroupPersonsEntity
import com.squareup.picasso.Picasso
import java.util.UUID


class UserListAdapter(
    private val idGroup: UUID,
    private val viewModel: GroupViewModel,
    private val fragmentManager: FragmentManager
) :
    ListAdapter<UserEntityWithStatus, UserListAdapter.UserViewHolder>(object : DiffUtil.ItemCallback<UserEntityWithStatus>() {
        override fun areItemsTheSame(
            oldItem: UserEntityWithStatus, newItem: UserEntityWithStatus
        ): Boolean {
            return oldItem.person.id == newItem.person.id
        }

        override fun areContentsTheSame(
            oldItem: UserEntityWithStatus, newItem: UserEntityWithStatus
        ): Boolean {
            return oldItem == newItem
        }

    }) {
    var status = GroupPersonsEntity.USER
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = UserViewHolder(
        ListItemUserBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
    )

    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
        holder.run {
            onBind(getItem(position))

            itemView.setOnLongClickListener {
                if (status == GroupPersonsEntity.CREATOR && getItem(position).status != GroupPersonsEntity.CREATOR ||
                    status == GroupPersonsEntity.ADMIN && getItem(position).status == GroupPersonsEntity.USER
                ) {
                    PersonGroupBottomSheet.newInstance(
                        getItem(position).person.id,
                        idGroup,
                        status,
                        getItem(position).status
                    ).show(
                        fragmentManager,
                        PersonGroupBottomSheet.DIALOG_JOIN_GROUP
                    )
                }
                true
            }
        }
    }

    inner class UserViewHolder(private val binding: ListItemUserBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun onBind(user: UserEntityWithStatus) {
            binding.run {
                itemUserName.text = user.person.name
                if (viewModel.getMyId() == user.person.id) {
                    userLayout.setBackgroundColor(
                        ContextCompat.getColor(
                            root.context,
                            R.color.colorPrimaryGray
                        )
                    )
                } else {
                    val outValue = TypedValue()
                    root.context.theme.resolveAttribute(
                        android.R.attr.selectableItemBackground,
                        outValue,
                        true
                    )
                    userLayout.setBackgroundResource(outValue.resourceId)
                }
                Picasso.get()
                    .load(user.person.url)
                    .into(itemUserImageView)
                itemUserAdministratorImageView.setImageResource(
                    when (user.status) {
                        GroupPersonsEntity.CREATOR -> R.drawable.creator_icon
                        GroupPersonsEntity.ADMIN -> R.drawable.administrator_icon
                        GroupPersonsEntity.USER -> R.color.transparent
                        else -> throw IndexOutOfBoundsException()
                    }
                )
            }
        }


    }
}