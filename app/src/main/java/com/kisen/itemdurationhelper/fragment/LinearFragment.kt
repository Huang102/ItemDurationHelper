package com.kisen.itemdurationhelper.fragment

import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.kisen.itemdurationhelper.BaseFragment
import com.kisen.itemdurationhelper.ImageEntity
import com.kisen.itemdurationhelper.R
import com.kisen.itemdurationhelper.util.TimeUtil
import com.kisen.lovers.view.ItemDivider

/**
 * description: ${TODO}
 * author: KisenHuang
 * date: 2018/3/22 17:46
 * version: ${VERSION}
 */
class LinearFragment : BaseFragment() {

    override fun setupManager(recyclerView: RecyclerView?) {
        recyclerView?.layoutManager = LinearLayoutManager(context)
        recyclerView?.addItemDecoration(ItemDivider().setItemSpace(5))
    }

    override fun convert(holder: FViewHolder?, data: ImageEntity) {
        //图片异步处理
        val image = holder?.getView<ImageView>(R.id.image)
        Glide.with(this).load(data.path).into(image)
        holder?.getView<TextView>(R.id.title)?.text = data.title
        holder?.getView<TextView>(R.id.msg)?.text = TimeUtil.formatTime(data.time * 1000 , "yyyy.MM.dd HH:mm")
    }

    override fun getItemId(): Int = R.layout.item_linear

}