package com.kote.martin.wats.activity

import android.content.Intent
import android.os.Bundle

import android.widget.ArrayAdapter
import android.support.v7.widget.SearchView
import android.text.TextUtils
import android.widget.ListView
import android.widget.Toast
import com.kote.martin.wats.adapter.PlaceListViewAdapter
import com.kote.martin.wats.async.rest.get.PlacesCallable
import com.kote.martin.wats.model.Place
import io.reactivex.Observable
import io.reactivex.Observer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import android.widget.TextView
import com.kote.martin.wats.R


class SearchableActivity : BaseActivity() {

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
        listView?.setOnItemLongClickListener { parent, view, position, id ->
            val v = view.findViewById(R.id.gmapsId) as TextView
            val gmapsId = v.text.toString()
            val intent = Intent(this, MapsActivity::class.java).apply {
                putExtra("gMapsPlaceId", gmapsId)
                System.out.println(gmapsId)
            }
            startActivity(intent)
            true
        }
        listView?.setOnItemClickListener { _, view, _, _ ->
            val v = view.findViewById(R.id.place_id) as TextView
            val placeId = v.text.toString().toLong()
            val intent = Intent(this, PlaceActivity::class.java).apply {
                val place = places.find { it -> it.id == placeId }
                putExtra(PLACE, place)
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
}
