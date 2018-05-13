package com.iambedant.myapplication

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import io.reactivex.disposables.Disposable
import kotlinx.android.synthetic.main.activity_main.*
import io.reactivex.subjects.PublishSubject
import java.util.concurrent.TimeUnit


class MainActivity : AppCompatActivity() {
    lateinit var disposable: Disposable
    private val dataSource: ArrayList<String> = ArrayList()
    lateinit var layoutManager: LinearLayoutManager
    var subject = PublishSubject.create<List<Int>>()

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

        disposable = subject
                .debounce(300, TimeUnit.MILLISECONDS)
                .scan { t1: List<Int>, t2: List<Int> ->
                    t2.filter { it > getInt(t1) }
                }
                .flatMapIterable { x -> x }
                .subscribe({ i ->
                    Log.d("RV", " $i ")
                }, { t ->
                    Log.e("RV_ERROR", t.localizedMessage)
                })


        recyclerView.viewTreeObserver
                .addOnGlobalLayoutListener {
                    if (!firstTrackFlag) {
                        startTime = System.currentTimeMillis()
                        subject.onNext((layoutManager.findFirstVisibleItemPosition()..layoutManager.findLastVisibleItemPosition()).toList())
                        firstTrackFlag = true
                    }
                }
    }

    fun getInt(t1: List<Int>): Int {
        return if (t1.isNotEmpty()) t1[t1.size - 1]
        else 0
    }

    private fun loadData() {
        for (i in 0..50) {
            dataSource.add("item no $i")
        }
    }


    private fun setRecyclerViewScrollListener() {
        recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView?, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                    for (i in layoutManager.findFirstVisibleItemPosition()..layoutManager.findLastVisibleItemPosition()) {
                        subject.onNext((layoutManager.findFirstVisibleItemPosition()..layoutManager.findLastVisibleItemPosition()).toList())
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
