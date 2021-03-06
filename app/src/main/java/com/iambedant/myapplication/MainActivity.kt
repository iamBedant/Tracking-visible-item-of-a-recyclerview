package com.iambedant.myapplication

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import io.reactivex.disposables.Disposable
import kotlinx.android.synthetic.main.activity_main.*
import io.reactivex.subjects.PublishSubject


class MainActivity : AppCompatActivity() {
    lateinit var disposable: Disposable
    private val dataSource: ArrayList<String> = ArrayList()
    lateinit var layoutManager: LinearLayoutManager
    var subject = PublishSubject.create<Int>()

    private var startTime: Long = 0

    private var firstTrackFlag = false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        layoutManager = LinearLayoutManager(this)
        recyclerView.layoutManager = layoutManager
        loadData()
        recyclerView.adapter = RvAdapter(dataSource)
        setRecyclerViewScrollListener()
        disposable = subject.distinctUntilChanged().subscribe { i -> Log.d("RV", " $i ") }

        recyclerView.viewTreeObserver
                .addOnGlobalLayoutListener {
                    if (!firstTrackFlag) {
                        startTime = System.currentTimeMillis()
                        for (i in layoutManager.findFirstVisibleItemPosition()..layoutManager.findLastVisibleItemPosition()) {
                            subject.onNext(i)
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
                if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                    for (i in layoutManager.findFirstVisibleItemPosition()..layoutManager.findLastVisibleItemPosition()) {
                        subject.onNext(i)
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
