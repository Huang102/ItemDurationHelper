package com.kisen.itemdurationhelper

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.RecyclerView
import android.util.SparseArray
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

/**
 * description: ${TODO}
 * author: KisenHuang
 * date: 2018/3/22 18:50
 * version: ${VERSION}
 */
abstract class BaseFragment : Fragment() {

    var mAdapter: FAdapter? = null

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater?.inflate(R.layout.fragment_base, container, false)
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init()
    }

    private fun init() {
        val recyclerView = view?.findViewById<RecyclerView>(R.id.recycler_view)
        mAdapter = FAdapter()
        recyclerView?.adapter = mAdapter
        setupManager(recyclerView)
    }

    fun onImageLoad(list: ArrayList<ImageEntity>) {
        mAdapter?.notifyList(list)
    }

    abstract fun setupManager(recyclerView: RecyclerView?)

    abstract fun convert(holder: FViewHolder?, data: ImageEntity)

    abstract fun getItemId(): Int

    inner class FAdapter : RecyclerView.Adapter<FViewHolder>() {

        private val mList: ArrayList<ImageEntity> = arrayListOf()

        fun notifyList(list: ArrayList<ImageEntity>) {
            mList.addAll(list)
            notifyDataSetChanged()
        }

        override fun onBindViewHolder(holder: FViewHolder?, position: Int) {
            convert(holder, mList[position])
        }

        override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): FViewHolder {
            val view = LayoutInflater.from(parent?.context).inflate(getItemId(), parent, false)
            return FViewHolder(view)
        }

        override fun getItemCount(): Int = mList.size

    }

    class FViewHolder constructor(view: View) : RecyclerView.ViewHolder(view) {

        private val arrayView: SparseArray<View> = SparseArray()

        fun <T> getView(resId: Int): T {
            var view = arrayView[resId]
            if (view == null) {
                view = itemView.findViewById(resId)
                arrayView.append(resId, view)
            }
            return view as T
        }

    }
}