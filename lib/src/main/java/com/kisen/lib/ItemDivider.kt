package com.kisen.lovers.view

import android.graphics.Canvas
import android.graphics.Rect
import android.graphics.drawable.Drawable
import android.support.v7.widget.RecyclerView
import android.view.View

/**
 * description: RecyclerView Item 分割线
 * author: KisenHuang
 * date: 2018/3/20 14:20
 * version: 1
 */
class ItemDivider : RecyclerView.ItemDecoration() {

    private var mItemDividerHelper = ItemDividerHelper()

    /**
     * 设置Divider 把背景资源
     */
    fun setDivider(divider: Drawable): ItemDivider {
        mItemDividerHelper.setDivider(divider)
        return this
    }

    /**
     * 设置divider宽度
     */
    fun setItemSpace(space: Int): ItemDivider {
        mItemDividerHelper.setItemSpace(space, space)
        return this
    }

    /**
     * 设置divider 宽度
     */
    fun setItemSpace(width: Int, height: Int): ItemDivider {
        mItemDividerHelper.setItemSpace(width, height)
        return this
    }

    /**
     * 设置RecyclerView内边距，可随着RecyclerView滑动而滑动
     */
    fun setGroupPadding(left: Int, top: Int, right: Int, bottom: Int): ItemDivider {
        mItemDividerHelper.setGroupPadding(left, top, right, bottom)
        return this
    }

    override fun onDrawOver(c: Canvas?, parent: RecyclerView?, state: RecyclerView.State?) {
        super.onDrawOver(c, parent, state)
        setupDelegate(parent)?.onDrawOver(c, parent, state)
    }

    override fun onDraw(c: Canvas?, parent: RecyclerView?, state: RecyclerView.State?) {
        super.onDraw(c, parent, state)
        setupDelegate(parent)?.onDraw(c, parent, state)
    }

    /**
     * 设置一个Item 的 Offset
     */
    override fun getItemOffsets(outRect: Rect?, view: View?, parent: RecyclerView?, state: RecyclerView.State?) {
        super.getItemOffsets(outRect, view, parent, state)
        setupDelegate(parent)?.getItemOffsets(outRect, view, parent, state)
    }

    private fun setupDelegate(parent: RecyclerView?): ItemDividerHelper.IDividerDelegate? =
            mItemDividerHelper.getDelegate(parent)

}