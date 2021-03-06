package edu.icesi.hobbies.activities

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.android.volley.RequestQueue
import com.android.volley.toolbox.ImageRequest
import com.android.volley.toolbox.Volley
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import edu.icesi.hobbies.R
import edu.icesi.hobbies.databinding.FragmentLiveMapBinding
import edu.icesi.hobbies.model.Club
import edu.icesi.hobbies.model.Event

class LiveMapFragment : Fragment (), MapsFragment.OnClickMarkerListener {

    //Binding
    private var _binding: FragmentLiveMapBinding? = null
    private val binding get() = _binding!!

    //Fragment GoogleMaps
    private lateinit var mapsFragment: MapsFragment

    //RequestQueue of Volley
    private lateinit var queue: RequestQueue

    //Database
    private var db = Firebase.firestore

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentLiveMapBinding.inflate(inflater, container, false)

        queue = Volley.newRequestQueue(activity)

        //Create Fragment
        mapsFragment = MapsFragment(false)
        val transaction = activity?.supportFragmentManager?.beginTransaction()
        transaction?.replace(R.id.mapConteiner, mapsFragment)
        transaction?.commit()

        //Subscribe listener
        mapsFragment.listener = this

        return binding.root
    }

    override fun onResume() {
        super.onResume()
        mapsFragment.loadEvents()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        @JvmStatic
        fun newInstance() = LiveMapFragment()
    }

    override fun showEventInfo(idMarker: String) {
        //Get Event by Id
        db.collection("events").document(idMarker).get().addOnSuccessListener { document ->
            val event = document.toObject(Event::class.java)!!

            binding.infoContainer.visibility = View.VISIBLE

            //Load image club
            val imgRequest = ImageRequest( event.imgClubUri,{ bitmap->
                binding.imageClub.setImageBitmap(bitmap)
            },0,0, ImageView.ScaleType.CENTER, null, {error->
                Log.e(">>>>>>>>>>>>", error.message.toString())
            })
            queue.add(imgRequest)

            //Set names
            binding.placeName.text = event.placeName
            binding.eventName.text = event.name
            binding.clubName.text = event.clubName

            //Request join to club
            binding.requestBtn.setOnClickListener{

                //Send request function call (chat)
                db.collection("users").document(Firebase.auth.currentUser?.uid.toString()).collection("clubs").document(event.chatClubId).get().addOnSuccessListener {
                    val club = it.toObject(Club::class.java)
                    if(club!=null){
                        Toast.makeText(activity, "You are already in this club", Toast.LENGTH_SHORT).show()
                    }else{
                        db.collection("clubs").document(event.chatClubId).get().addOnSuccessListener {

                            val club = it.toObject(Club::class.java)

                            db.collection("users").document(Firebase.auth.currentUser?.uid.toString()).collection("clubs").document(event.chatClubId).set(club!!).addOnSuccessListener {
                                Toast.makeText(activity, "Joined to a new club. Check home", Toast.LENGTH_SHORT).show()
                            }.addOnFailureListener{
                                Toast.makeText(activity, "Failed to join to club", Toast.LENGTH_SHORT).show()
                            }

                        }.addOnFailureListener{
                            Toast.makeText(activity, "Failed to download club", Toast.LENGTH_SHORT).show()
                        }
                    }
                }.addOnFailureListener{
                    Toast.makeText(activity, "Failed to download club", Toast.LENGTH_SHORT).show()
                }

                binding.infoContainer.visibility = View.GONE
            }
        }
    }

    override fun hideEventInfo() {
        binding.infoContainer.visibility = View.GONE
    }
}