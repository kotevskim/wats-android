package com.kote.martin.wats.activity

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.kote.martin.wats.R
import com.kote.martin.wats.async.rest.get.GooglePlaceDetailsCallable
import com.kote.martin.wats.model.GooglePlaceDetails
import io.reactivex.Observable
import io.reactivex.Observer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers

class MapsActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap
    private lateinit var gMapsPlaceId: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_maps)

        this.gMapsPlaceId = intent.getStringExtra("gMapsPlaceId")

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
                .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        fetchPlaceDetailsAsync()
//        // Add a marker in Sydney and move the camera
//        val sydney = LatLng(-34.0, 151.0)
//        mMap.addMarker(MarkerOptions().position(sydney).title("Marker in Sydney"))
//        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney))
    }

    fun fetchPlaceDetailsAsync() {
        val callable = GooglePlaceDetailsCallable(getString(R.string.google_maps_key), gMapsPlaceId)
        val reviewsObservable = Observable.fromCallable(callable)
        reviewsObservable
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(object : Observer<GooglePlaceDetails> {
                    override fun onSubscribe(d: Disposable?) {
                    }

                    override fun onComplete() {
                    }

                    override fun onNext(value: GooglePlaceDetails) {
                        val location = value.result?.geometry?.location
                        val place = LatLng(location?.lat!!, location.lng!!)
                        mMap.addMarker(MarkerOptions().position(place).title(value.result.name))
                        mMap.moveCamera(CameraUpdateFactory.newLatLng(place))
                        supportActionBar?.title = value.result.name
                    }

                    override fun onError(e: Throwable?) {
                        Toast.makeText(
                                application,
                                "Could not fetch place from Google Maps API.",
                                Toast.LENGTH_SHORT).show()
                    }
                })
    }
}
