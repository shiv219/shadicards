package com.shadi.feature.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.shadi.R
import com.shadi.data.response.Result
import com.shadi.databinding.ListItemMatchBinding

class MatchListAdapter(private val onClick: (data: Result) -> Unit) :
    PagingDataAdapter<Result, MatchListAdapter.MatchViewHolder>(diffCallback) {
    override fun onBindViewHolder(holder: MatchListAdapter.MatchViewHolder, position: Int) {
        getItem(position)?.let {
            holder.bind(it)
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ) =
        MatchViewHolder(
            DataBindingUtil.inflate(
                LayoutInflater.from(parent.context), R.layout.list_item_match, parent
                , false
            )
        )


    inner class MatchViewHolder(private val mBinding: ListItemMatchBinding) :
        RecyclerView.ViewHolder(mBinding.root) {

        fun bind(result: Result) {
            with(mBinding) {
                data = result
                tvAccept.setOnClickListener {
                    if (!result.isAccepted && !result.isDeclined) {
                        result.isAccepted = true
                        onClick(result)
                    }
                }
                tvDecline.setOnClickListener {
                    if (!result.isAccepted && !result.isDeclined) {
                        result.isDeclined = true
                        onClick(result)
                    }
                }
            }
        }
    }

    companion object {
        val diffCallback = object : DiffUtil.ItemCallback<Result>() {
            override fun areItemsTheSame(oldItem: Result, newItem: Result) =
                oldItem.login?.uuid == newItem.login?.uuid

            override fun areContentsTheSame(oldItem: Result, newItem: Result) =
                oldItem == newItem
        }
    }
}
