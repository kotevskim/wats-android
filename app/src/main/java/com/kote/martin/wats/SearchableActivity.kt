package com.kote.martin.wats

import android.content.Intent
import android.graphics.Color
import android.os.Bundle

import android.widget.ArrayAdapter
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.SearchView
import android.text.TextUtils
import android.widget.ListView
import android.widget.Toast
import com.kote.martin.wats.adapters.PlaceListViewAdapter
import com.kote.martin.wats.callables.PlacesCallable
import com.kote.martin.wats.model.Place
import io.reactivex.Observable
import io.reactivex.Observer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import android.graphics.drawable.ColorDrawable




class SearchableActivity : AppCompatActivity() {

    private var adapter: ArrayAdapter<Place>? = null
    private var listView: ListView? = null
    private var places: List<Place> = emptyList()
    // TODO find a way to import this in another activity
    private val PLACE = "place"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_serchable)

//        loadPlacesFromDb()
        loadPlacesFromServer() // async

        listView = findViewById(R.id.list_search_result)
        adapter = PlaceListViewAdapter(this)
        listView?.adapter = adapter
        listView?.setOnItemClickListener { _, _, position, _ ->
            val intent = Intent(this, PlaceActivity::class.java).apply {
                putExtra(PLACE, places[position])
            }
            startActivity(intent)
        }

        val searchView = findViewById<SearchView>(R.id.search_bar)
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextChange(newText: String): Boolean {
                adapter?.clear()
                if (!TextUtils.isEmpty(newText)) {
                    performPlaceSearch(newText)
                }
                return false
            }

            override fun onQueryTextSubmit(query: String): Boolean {
                performPlaceSearch(query)
                return true
            }
        })
    }

    private fun performPlaceSearch(query: String) {
        adapter?.addAll(places.filter { l -> l.name.contains(query, true) })
    }

    private fun loadPlacesFromServer() {
        Observable
                .fromCallable(PlacesCallable(getString(R.string.wats_api_public_path)))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(object : Observer<List<Place>> {
                    override fun onSubscribe(d: Disposable?) {
                    }

                    override fun onComplete() {
                    }

                    override fun onNext(value: List<Place>) {
                        places = value
                    }

                    override fun onError(e: Throwable?) {
                        Toast.makeText(application, "Could not fetch places from server.",
                                Toast.LENGTH_SHORT).show()
                    }
                })
    }

//    override fun onNewIntent(intent: Intent) {
//        setIntent(intent)
//        handleIntent(intent)
//    }
//
//    private fun handleIntent(intent: Intent) {
//        if (Intent.ACTION_SEARCH == intent.action) {
//            val query = intent.getStringExtra(SearchManager.QUERY)
//            performPlaceSearch(query)
//        }
//    }
}
