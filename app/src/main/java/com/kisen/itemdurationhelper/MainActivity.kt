package com.kisen.itemdurationhelper

import android.annotation.SuppressLint
import android.content.Context
import android.os.AsyncTask
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.support.design.widget.TabLayout
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import android.support.v4.view.ViewPager
import com.kisen.itemdurationhelper.fragment.GridFragment
import com.kisen.itemdurationhelper.fragment.LinearFragment
import com.kisen.itemdurationhelper.fragment.StaggeredFragment
import com.kisen.itemdurationhelper.fragment.SwitchFragment
import java.io.File

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        init()
    }

    private var fPagerAdapter: FPagerAdapter? = null

    private fun init() {
        val tabLayout = findViewById<TabLayout>(R.id.tab_layout)
        val viewPager = findViewById<ViewPager>(R.id.view_pager)
        fPagerAdapter = FPagerAdapter(supportFragmentManager)
        viewPager.adapter = fPagerAdapter
        tabLayout.setupWithViewPager(viewPager)
        LoadPhotosTask().execute()
    }

    private fun notifyImage(list: ArrayList<ImageEntity>) {
        fPagerAdapter?.notify(list)
    }


    class FPagerAdapter constructor(fm: FragmentManager) : FragmentPagerAdapter(fm) {

        private val fragments = arrayListOf<BaseFragment>()
        private val titles = arrayListOf<String>()

        init {
            fragments.add(LinearFragment())
            fragments.add(GridFragment())
//            fragments.add(StaggeredFragment())
//            fragments.add(SwitchFragment())
            titles.add("线性布局")
            titles.add("网格布局")
//            titles.add("瀑布流布局")
//            titles.add("切换布局")
        }

        override fun getItem(position: Int): Fragment = fragments[position]

        override fun getCount(): Int = fragments.size

        override fun getPageTitle(position: Int): CharSequence = titles[position]

        fun notify(list: ArrayList<ImageEntity>) {
            fragments.map { it.onImageLoad(list) }
        }

    }

    @SuppressLint("StaticFieldLeak")
    inner class LoadPhotosTask : AsyncTask<Void, Void, ArrayList<ImageEntity>>() {

        @SuppressLint("Recycle")
        override fun doInBackground(vararg void: Void): ArrayList<ImageEntity>? {
            val result = arrayListOf<ImageEntity>()
            val mImageUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI
            val mCursor = contentResolver.query(mImageUri, null,
                    MediaStore.Images.Media.MIME_TYPE + "=? or "
                            + MediaStore.Images.Media.MIME_TYPE + "=? or "
                            + MediaStore.Images.Media.MIME_TYPE + "=? or "
                            + MediaStore.Images.Media.MIME_TYPE + "=? or "
                            + MediaStore.Images.Media.MIME_TYPE + "=? or "
                            + MediaStore.Images.Media.MIME_TYPE + "=? or "
                            + MediaStore.Images.Media.MIME_TYPE + "=? or "
                            + MediaStore.Images.Media.MIME_TYPE + "=?",
                    arrayOf("image/jpeg", "image/png", "image/jpg", "image/gif", "image/webp", "image/ico", "image/bmp", "image/wbmp"),
                    MediaStore.Images.Media.DATE_MODIFIED)
            if (mCursor != null) {
                while (mCursor.moveToNext()) {
                    //如果界面退出，终止异步操作。
                    if (isCancelled)
                        break
                    // 获取图片的路径
                    val path = mCursor.getString(mCursor
                            .getColumnIndex(MediaStore.Images.Media.DATA))
                    val size = mCursor.getLong(mCursor
                            .getColumnIndex(MediaStore.Images.Media.SIZE))
                    val name = mCursor.getString(mCursor
                            .getColumnIndex(MediaStore.Images.Media.DISPLAY_NAME))
                    val time = mCursor.getLong(mCursor
                            .getColumnIndex(MediaStore.Images.Media.DATE_ADDED))
                    val file = File(path)
                    if (!file.exists() || !file.canRead() || size == 0L) {
                        continue
                    }
                    result.add(ImageEntity(path = path, title = name, time = time, size = size))
                }
                mCursor.close()
            }
            return result
        }

        override fun onPostExecute(list: ArrayList<ImageEntity>) {
            super.onPostExecute(list)
            //如果界面退出，终止异步操作。
            if (isCancelled)
                return
            notifyImage(list)
        }
    }

}
