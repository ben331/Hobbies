package edu.icesi.hobbies.activities

import android.annotation.SuppressLint
import android.content.Context
import android.location.LocationManager
import androidx.fragment.app.Fragment
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import edu.icesi.hobbies.R
import edu.icesi.hobbies.databinding.FragmentMapsBinding
import edu.icesi.hobbies.model.Event
import java.util.*
import kotlin.collections.ArrayList

class MapsFragment(private val isOnlySelector:Boolean) : Fragment() {

    //Vars
    private lateinit var mMap:GoogleMap
    private lateinit var manager:LocationManager

    //Vars on selector Mode
    var eventMarker:Marker?=null
    private var idMarker:String = "user:${Firebase.auth.currentUser?.uid}event:${UUID.randomUUID()}"

    //Listener on live map mode
    lateinit var listener:OnClickMarkerListener

    //Binding
    private var _binding: FragmentMapsBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMapsBinding.inflate(inflater, container, false)
        manager = activity?.getSystemService(Context.LOCATION_SERVICE) as LocationManager

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?
        mapFragment?.getMapAsync(callback)
    }

    //Interactions----------------------------------------------------------------------------------
    private val callback = OnMapReadyCallback { googleMap ->
        mMap = googleMap

        //Click on map--------------------------------------------------------------------------
        mMap.setOnMapClickListener { pos->
            if(isOnlySelector){
                if(eventMarker!=null){
                    eventMarker?.position = pos
                }else{
                    eventMarker = putMarker(pos.latitude, pos.longitude)
                }
            }else{
                listener.hideEventInfo()
            }
        }

        //Click on marker--------------------------------------------------------------------------
        mMap.setOnMarkerClickListener {marker->
            if(!isOnlySelector){
                listener.showEventInfo(marker.snippet!!)
            }
            true
        }

        setInitialPos()
    }

    fun loadEvents(){
        Firebase.firestore.collection("events").get().addOnSuccessListener {
            for(task in it){
                val event = task.toObject(Event::class.java)
                putMarker(event.lat, event.lon, event.id)
            }
        }.addOnFailureListener{
            Toast.makeText(activity, "Failed to load events", Toast.LENGTH_SHORT).show()
        }
    }

    private fun putMarker(lat:Double, lng:Double): Marker?{
        val pos = LatLng(lat, lng)
        return mMap.addMarker(MarkerOptions().position(pos))
    }

    private fun putMarker(lat:Double, lng:Double, id:String): Marker?{
        val pos = LatLng(lat, lng)
        return mMap.addMarker(MarkerOptions().position(pos).snippet(id))
    }

    @SuppressLint("MissingPermission")
    private fun setInitialPos(){
        var pos = LatLng(3.3, -73.0)
        val gsc = manager.getLastKnownLocation(LocationManager.GPS_PROVIDER )
        if(gsc!=null){
            pos = LatLng(gsc.latitude, gsc.longitude)
        }
        mMap.animateCamera(CameraUpdateFactory.newLatLng(pos))
    }

    interface OnClickMarkerListener{
        fun showEventInfo(idMarker:String)
        fun hideEventInfo()
    }
}