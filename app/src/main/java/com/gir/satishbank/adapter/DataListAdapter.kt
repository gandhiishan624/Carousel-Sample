package com.gir.satishbank.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import androidx.recyclerview.widget.RecyclerView
import com.gir.satishbank.R
import com.gir.satishbank.databinding.AdapterItemListBinding
import com.gir.satishbank.model.DataList
import com.gir.satishbank.model.ProductList
import com.squareup.picasso.Picasso

class DataListAdapter(val context:Context): RecyclerView.Adapter<DataListAdapter.DataListVH>(),
    Filterable {
    class DataListVH(view : View): RecyclerView.ViewHolder(view) {
        val binding= AdapterItemListBinding.bind(itemView)

    }

     var productFilterList= mutableListOf<ProductList>()

    var productList= mutableListOf<ProductList>()

    /**
     * Called when RecyclerView needs a new [ViewHolder] of the given type to represent
     * an item.
     *
     *
     * This new ViewHolder should be constructed with a new View that can represent the items
     * of the given type. You can either create a new View manually or inflate it from an XML
     * layout file.
     *
     *
     * The new ViewHolder will be used to display items of the adapter using
     * [.onBindViewHolder]. Since it will be re-used to display
     * different items in the data set, it is a good idea to cache references to sub views of
     * the View to avoid unnecessary [View.findViewById] calls.
     *
     * @param parent The ViewGroup into which the new View will be added after it is bound to
     * an adapter position.
     * @param viewType The view type of the new View.
     *
     * @return A new ViewHolder that holds a View of the given view type.
     * @see .getItemViewType
     * @see .onBindViewHolder
     */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DataListVH {
        val inflater = LayoutInflater.from(parent.context)
        return DataListVH(
            inflater.inflate(
                R.layout.adapter_item_list,
                parent,
                false
            )


        )
    }

    /**
     * Called by RecyclerView to display the data at the specified position. This method should
     * update the contents of the [ViewHolder.itemView] to reflect the item at the given
     * position.
     *
     *
     * Note that unlike [android.widget.ListView], RecyclerView will not call this method
     * again if the position of the item changes in the data set unless the item itself is
     * invalidated or the new position cannot be determined. For this reason, you should only
     * use the `position` parameter while acquiring the related data item inside
     * this method and should not keep a copy of it. If you need the position of an item later
     * on (e.g. in a click listener), use [ViewHolder.getAdapterPosition] which will
     * have the updated adapter position.
     *
     * Override [.onBindViewHolder] instead if Adapter can
     * handle efficient partial bind.
     *
     * @param holder The ViewHolder which should be updated to represent the contents of the
     * item at the given position in the data set.
     * @param position The position of the item within the adapter's data set.
     */
    override fun onBindViewHolder(holder: DataListVH, position: Int) {

        val product=productFilterList[position]

        val binding=holder.binding

        Picasso.get().load(product.image).into(binding.imageView)

        binding.ailTvLabel.text=product.name
    }

    /**
     * Returns the total number of items in the data set held by the adapter.
     *
     * @return The total number of items in this adapter.
     */
    override fun getItemCount(): Int {
        return productFilterList.size
    }

    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(constraint: CharSequence?): FilterResults {
                val charSearch = constraint.toString()
                productFilterList = if (charSearch.isEmpty()) {
                    productList
                } else {
                    val resultList = mutableListOf<ProductList>()
                    for (row in productList) {
                        if (row.name.toLowerCase().contains(constraint.toString().toLowerCase())) {
                            resultList.add(row)
                        }
                    }
                    resultList
                }
                val filterResults = FilterResults()
                filterResults.values = productFilterList
                return filterResults
            }

            override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
                productFilterList = results?.values as MutableList<ProductList>
                notifyDataSetChanged()
            }
        }
    }


}