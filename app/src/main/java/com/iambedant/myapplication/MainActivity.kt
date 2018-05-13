package com.iambedant.myapplication

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import io.reactivex.disposables.Disposable
import io.reactivex.functions.Predicate
import kotlinx.android.synthetic.main.activity_main.*
import io.reactivex.subjects.PublishSubject


class MainActivity : AppCompatActivity() {
    lateinit var disposable: Disposable
    private val dataSource: ArrayList<String> = ArrayList()
    lateinit var layoutManager: LinearLayoutManager
    var subject = PublishSubject.create<ItemInfo>()
    private val viewsViewed = java.util.ArrayList<Int>()
    private var startTime: Long = 0
    private var endTime: Long = 0

    private var firstTrackFlag = false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        layoutManager = LinearLayoutManager(this)
        recyclerView.layoutManager = layoutManager
        loadData()
        recyclerView.adapter = RvAdapter(dataSource)
        setRecyclerViewScrollListener()
        disposable = subject.distinctUntilChanged().filter { i -> i.time > 300 }.subscribe { i -> Log.d("RV", " $i ") }

        recyclerView.viewTreeObserver
                .addOnGlobalLayoutListener {
                    if (!firstTrackFlag) {
                        startTime = System.currentTimeMillis()
                        for (i in layoutManager.findFirstVisibleItemPosition()..layoutManager.findLastVisibleItemPosition()) {
                            viewsViewed.add(i)
                        }
                        firstTrackFlag = true
                    }
                }


    }

    private fun loadData() {
        for (i in 0..10) {
            dataSource.add("item no $i")
        }
    }

    private fun setRecyclerViewScrollListener() {
        recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView?, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                if (newState == RecyclerView.SCROLL_STATE_DRAGGING) {
                    endTime = System.currentTimeMillis()
                    for (i in viewsViewed) {
                        subject.onNext(ItemInfo(i, startTime - endTime))
                    }
                    viewsViewed.clear()
                }
                if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                    startTime = System.currentTimeMillis()
                    for (i in layoutManager.findFirstVisibleItemPosition()..layoutManager.findLastVisibleItemPosition()) {
                        viewsViewed.add(i)
                    }
                }
            }
        })
    }


    override fun onDestroy() {
        super.onDestroy()
        disposable.dispose()
    }
}
