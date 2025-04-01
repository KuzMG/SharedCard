package com.example.sharedcard.ui.purchase.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.sharedcard.R
import com.example.sharedcard.database.entity.product.Product
import com.example.sharedcard.databinding.ListItemProductBinding
import com.example.sharedcard.ui.purchase.add_purchase.AddPurchaseViewModel
import com.squareup.picasso.Picasso


class ProductSelectAdapter(
    private val list: List<Product>,
    private val viewModel: AddPurchaseViewModel
) :
    RecyclerView.Adapter<ProductSelectAdapter.ProductViewHolder>() {
    private var selectedItemPos = -1
    private var lastItemSelectedPos = -1
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = ProductViewHolder(
        ListItemProductBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        ), parent.context
    )


    override fun getItemCount() = list.size

    override fun onBindViewHolder(holder: ProductViewHolder, position: Int) {
        holder.onBind(list[position])
    }

    inner class ProductViewHolder(
        val binding: ListItemProductBinding,
        private val context: Context
    ) :
        RecyclerView.ViewHolder(binding.root) {
        private lateinit var product: Product
        init {
            binding.root.setOnClickListener {
                selectedItemPos = absoluteAdapterPosition
                when (lastItemSelectedPos) {
                    -1 -> {
                        notifyItemChanged(selectedItemPos)
                        lastItemSelectedPos = selectedItemPos
                        viewModel.product = list[selectedItemPos]
                    }
                    selectedItemPos -> selectedItemPos
                    else -> {
                        notifyItemChanged(lastItemSelectedPos)
                        notifyItemChanged(selectedItemPos)
                        lastItemSelectedPos = selectedItemPos
                        viewModel.product = list[selectedItemPos]

                    }
                }
            }
        }
        fun onBind(item: Product) {
            product = item
            if(viewModel.product?.productEntity?.id == product.productEntity.id){
                selectedItemPos =absoluteAdapterPosition
                lastItemSelectedPos = absoluteAdapterPosition
            }
            if(itemCount ==1){
                selectedItemPos = absoluteAdapterPosition
                lastItemSelectedPos = absoluteAdapterPosition
                viewModel.product = product
            }
            binding.run {
                Picasso.get().load(product.category.url).into(binding.picImageView)
                nameTextView.text = product.productEntity.name
                caloriesTextView.text =
                    context.getString(
                        R.string.pie_chart_calories,
                        item.productEntity.calories!!.toInt().toString()
                    )
                root.strokeColor = when (absoluteAdapterPosition) {
                    selectedItemPos -> binding.root.context.getColor(R.color.colorPrimary)
                    else -> binding.root.context.getColor(R.color.color_background_view)
                }
                pointCardView.setCardBackgroundColor(context.getColor(android.R.color.transparent))
                pfcTextView.text = ""
                pfcLineChart.apply {
                    setDataChart(
                        product.productEntity.protein!!.toFloat(),
                        product.productEntity.fat!!.toFloat(),
                        product.productEntity.carb!!.toFloat()
                    )
                    addOnClickListener { color, name ->
                        pointCardView.setCardBackgroundColor(
                            ContextCompat.getColor(
                                context,
                                color
                            )
                        )
                        pfcTextView.text = name
                    }
                    startWithoutAnimation()
                }
            }

        }
    }
}