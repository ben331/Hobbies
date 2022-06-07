package edu.icesi.hobbies.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import edu.icesi.hobbies.adapter.MessageAdapter
import edu.icesi.hobbies.databinding.ActivityChatBinding
import edu.icesi.hobbies.model.Club
import edu.icesi.hobbies.model.Message

class ChatActivity : AppCompatActivity() {

    private lateinit var binding : ActivityChatBinding

    //Launchers
    private val launcher = registerForActivityResult(ActivityResultContracts.StartActivityForResult(), ::onResultNewEvent)

    private var chatId = ""
    private var userId = ""
    private var db = Firebase.firestore
    private var adapter = MessageAdapter(userId)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityChatBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //Allow admin functions---------------------------------------------------------------------
        intent.getStringExtra("chatId")?.let { chatId = it }
        userId = Firebase.auth.currentUser!!.uid
        db.collection("clubs").document(chatId).get().addOnSuccessListener {
            val adminId = it.toObject(Club::class.java)?.adminId
            if(adminId == userId){
                binding.btnNewEvent.visibility = View.VISIBLE
            }
        }.addOnFailureListener{
            Toast.makeText(this, "Fail Check admin", Toast.LENGTH_SHORT).show()
        }
        //------------------------------------------------------------------------------------------

        binding.messagesRecylerView.adapter = adapter

        binding.btnNewEvent.setOnClickListener{
            val intent = Intent(this, NewEventActivity::class.java)
            intent.putExtra("clubId", chatId)
            launcher.launch(intent)
        }

        if(chatId.isNotEmpty() && userId.isNotEmpty()) {
            initViews()
        }
    }
    private fun initViews(){
        binding.messagesRecylerView.layoutManager = LinearLayoutManager(this)
        binding.messagesRecylerView.adapter = MessageAdapter(userId)

        val chatRef = db.collection("chats").document(chatId)

        chatRef.collection("messages").orderBy("dob", Query.Direction.ASCENDING)
            .get()
            .addOnSuccessListener { messages ->
                val listMessages = ArrayList(messages.toObjects(Message::class.java))
                (binding.messagesRecylerView.adapter as MessageAdapter).setData(listMessages)
            }

        chatRef.collection("messages").orderBy("dob", Query.Direction.ASCENDING)
            .addSnapshotListener { messages, error ->
                if(error == null){
                    messages?.let {
                        val listMessages = ArrayList(it.toObjects(Message::class.java))
                        (binding.messagesRecylerView.adapter as MessageAdapter).setData(listMessages)
                    }
                }
            }

        //Send Message
        binding.sendMessageButton.setOnClickListener{
            val message = Message(
                message = binding.messageTextField.text.toString(),
                from = userId
            )
            binding.messageTextField.setText("")
            db.collection("chats").document(chatId).collection("messages").document().set(message)
            adapter.addMessage(message)
        }
    }
    
    private fun onResultNewEvent(result:ActivityResult){

    }
}
