package com.kisen.itemdurationhelper.fragment

import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.RecyclerView
import com.bumptech.glide.Glide
import com.kisen.itemdurationhelper.BaseFragment
import com.kisen.itemdurationhelper.ImageEntity
import com.kisen.itemdurationhelper.R
import com.kisen.lovers.view.ItemDivider

/**
 * description: ${TODO}
 * author: KisenHuang
 * date: 2018/3/22 17:46
 * version: ${VERSION}
 */
class GridFragment : BaseFragment() {

    override fun setupManager(recyclerView: RecyclerView?) {
        recyclerView?.layoutManager = GridLayoutManager(context, 4)
        recyclerView?.addItemDecoration(ItemDivider().
                setGroupPadding(15, 15, 15, 15).setItemSpace(15, 15))
    }

    override fun convert(holder: FViewHolder?, data: ImageEntity) {
        Glide.with(this).load(data.path).into(holder?.getView(R.id.image))
    }

    override fun getItemId(): Int = R.layout.item_grid

}