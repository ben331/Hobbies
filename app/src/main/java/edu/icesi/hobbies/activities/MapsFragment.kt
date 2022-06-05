package edu.icesi.hobbies.activities

import android.annotation.SuppressLint
import android.content.Context
import android.location.LocationManager
import androidx.fragment.app.Fragment
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import edu.icesi.hobbies.R
import edu.icesi.hobbies.databinding.FragmentMapsBinding
import java.util.*

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
                    eventMarker = putMarket(pos.latitude, pos.longitude)
                }
            }else{
                listener.hideEventInfo()
            }
        }

        //Click on marker--------------------------------------------------------------------------
        mMap.setOnMarkerClickListener {marker->
            if(!isOnlySelector){
                idMarker = marker.snippet!!
                listener.showEventInfo(idMarker)
            }
            true
        }

        setInitialPos()
    }

    private fun putMarket(lat:Double, lng:Double): Marker?{
        val pos = LatLng(lat, lng)
        return mMap.addMarker(MarkerOptions().position(pos).snippet(idMarker))
    }

    @SuppressLint("MissingPermission")
    private fun setInitialPos(){
        var pos = LatLng(3.3, -73.0)
        val gsc = manager.getLastKnownLocation(LocationManager.GPS_PROVIDER )
        if(gsc!=null){
            pos = LatLng(gsc.latitude, gsc.longitude)
        }
        mMap.moveCamera(CameraUpdateFactory.newLatLng(pos))
    }

    interface OnClickMarkerListener{
        fun showEventInfo(idMarker:String)
        fun hideEventInfo()
    }
}