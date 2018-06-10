package com.kote.martin.wats

import android.os.Bundle

import android.app.SearchManager
import android.content.Intent
import android.widget.ArrayAdapter
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.SearchView
import android.text.TextUtils
import android.widget.ListView
import com.kote.martin.wats.model.Location


class SearchableActivity : AppCompatActivity() {

    private var adapter: ArrayAdapter<Location>? = null
    private var listView: ListView? = null
    private val locations = arrayListOf(
            Location(1L, "Skopje"),
            Location(1L, "San Francisco"),
            Location(1L, "San Antonio")
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_serchable)

        adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1)
        listView = findViewById(R.id.list_search_result)
        listView?.adapter = adapter

        val searchView = findViewById<SearchView>(R.id.search_bar)
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextChange(newText: String): Boolean {
                if (TextUtils.isEmpty(newText)){
                    adapter?.clear()
                } else {
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

    override fun onNewIntent(intent: Intent) {
        setIntent(intent)
        handleIntent(intent)
    }

    private fun handleIntent(intent: Intent) {
        if (Intent.ACTION_SEARCH == intent.action) {
            val query = intent.getStringExtra(SearchManager.QUERY)
            performPlaceSearch(query)
        }
    }

    private fun performPlaceSearch(query: String) {
        adapter?.clear()
        adapter?.addAll(locations.filter { l -> l.name.contains(query, true) })
    }
}
