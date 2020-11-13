package com.ari.firebaseloginapp.utils

import androidx.recyclerview.widget.RecyclerView

abstract class InfiniteRecycleViewPagingAdapter< D : Any, VH : RecyclerView.ViewHolder> : RecyclerView.Adapter<VH>() {
    private var list: MutableList<D>
    private var currentPage = 1
    private var totalPage = 0
    private var isLoading = false
    var listener: PagingAdapterListener<D>? = null

    init {
        list = mutableListOf()
    }

    private var scrollListener: RecyclerView.OnScrollListener =
            object : RecyclerView.OnScrollListener() {
                override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                    super.onScrolled(recyclerView, dx, dy)
                    if (!recyclerView.canScrollVertically(1)) {
                        if (currentPage < totalPage && currentPage > 0 && !isLoading) {
                            currentPage++
                            listener?.loadMore(currentPage)
                            setLoading(true)
                        }
                    }
                }
            }
        get() = field


    fun setRecyclerView(recyclerView: RecyclerView){
        recyclerView.addOnScrollListener(scrollListener)
        recyclerView.adapter = this
    }

    open fun updateData(list: MutableList<D>?, totalPage: Int) {
        if (list != null){
            currentPage = 1
            this.totalPage = totalPage
            this.list = list
            notifyDataSetChanged()
            setLoading(false)
        }
    }

    override fun onBindViewHolder(holder: VH, position: Int) {
        setUpView(holder, list[position])
    }

    abstract fun setUpView(holder: VH, item: D)

    override fun getItemCount(): Int {
        return list.size
    }

    private fun setLoading(stateLoading: Boolean) {
        isLoading = stateLoading
        if (listener != null) listener?.onStateLoadingChange(stateLoading)
    }

    protected open fun notifyItemClicked(item: D) {
        listener?.onItemClick(item)
    }

    open fun addData(list: MutableList<D>?) {
        if (list != null){
            this.list.addAll(list)
            notifyDataSetChanged()
        }
        setLoading(false)
    }

    interface PagingAdapterListener<D> {
        fun loadMore(page: Int)
        fun onItemClick(item: D)
        fun onStateLoadingChange(isLoading: Boolean)
    }
}