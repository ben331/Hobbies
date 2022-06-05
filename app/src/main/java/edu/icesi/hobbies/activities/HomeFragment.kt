package edu.icesi.hobbies.activities

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import edu.icesi.hobbies.adapters.ChatAdapter
import edu.icesi.hobbies.databinding.FragmentHomeBinding
import edu.icesi.hobbies.model.Chat
import edu.icesi.hobbies.model.User
import java.util.*

class HomeFragment : Fragment() {

    //Firebase
    private var db = Firebase.firestore

    //Binding
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    //Vars
    private var userId = Firebase.auth.currentUser?.uid!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)

        initViews()
        return binding.root
    }

    companion object {
        @JvmStatic
        fun newInstance() = HomeFragment()
    }

    private fun initViews(){
        binding.newChatBtn.setOnClickListener{ newChat()}

        binding.listChatsRecyclerView.layoutManager = LinearLayoutManager(activity)
        binding.listChatsRecyclerView.adapter = ChatAdapter { chat->
            chatSelected(chat)
        }
        val userRef = db.collection("users").document(userId)

        userRef.collection("chats")
            .get()
            .addOnSuccessListener { chats ->
                val listChats = chats.toObjects(Chat::class.java)
                (binding.listChatsRecyclerView.adapter as ChatAdapter).setData(listChats)
            }
        userRef.collection("chats")
            .addSnapshotListener { chats, error ->
                if(error == null){
                    chats?.let {
                        val listChats = it.toObjects(Chat::class.java)
                        (binding.listChatsRecyclerView.adapter as ChatAdapter).setData(listChats)
                    }
                }
            }
    }

    private fun chatSelected(chat: Chat){
        val intent = Intent(activity, ChatActivity::class.java)
        intent.putExtra("chatId", chat.id)
        intent.putExtra("user", userId)
        startActivity(intent)
    }

    private fun newChat(){
        val chatId = UUID.randomUUID().toString()
        val otherChat = binding.otherChatET.text.toString()
        val users = listOf(userId,otherChat)

        val chat = Chat(
            id = chatId,
            name = "chat con $otherChat",
            users = users
        )

        db.collection("chats").document(chatId).set(chat)
        db.collection("users").document(userId).collection("chats").document(chatId).set(chat)
        db.collection("users").document(otherChat).collection("chats").document(chatId).set(chat)

        val intent = Intent(activity, ChatActivity::class.java)
        intent.putExtra("chatId", chatId)
        intent.putExtra("user", userId)
        startActivity(intent)

    }
}