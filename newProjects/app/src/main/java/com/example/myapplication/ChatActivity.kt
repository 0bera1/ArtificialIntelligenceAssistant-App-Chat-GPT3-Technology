package com.example.myapplication

import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.myapplication.adapter.MessageAdapter
import com.example.myapplication.api.ApiUtilities
import com.example.myapplication.databinding.ActivityChatBinding
import com.example.myapplication.models.MessageModel
import com.example.myapplication.models.request.ChatRequest
import com.google.gson.Gson
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.MediaType
import okhttp3.RequestBody

class ChatActivity : AppCompatActivity() {
    private lateinit var binding: ActivityChatBinding
    private lateinit var adapter: MessageAdapter

    var list = ArrayList<MessageModel>()
    private lateinit var mLayoutManager:LinearLayoutManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityChatBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.backBtn.setOnClickListener { finish() }

        mLayoutManager = LinearLayoutManager(this)
        mLayoutManager.stackFromEnd = true

        adapter = MessageAdapter(list)

        binding.recylerView.adapter = MessageAdapter(list)
        binding.recylerView.layoutManager = mLayoutManager

        binding.sendBtn.setOnClickListener{
            if (binding.userMsg.text!!.isEmpty()){
                Toast.makeText(this,"Please type your question !",Toast.LENGTH_SHORT).show()
            }else {
                callApi()
            }
        }
    }
    private fun callApi(){

        list.add(MessageModel(true,false,binding.userMsg.text.toString()))
        adapter.notifyItemInserted(list.size - 1)

        binding.recylerView.recycledViewPool.clear()

        binding.recylerView.smoothScrollToPosition(list.size - 1)

        val apiInterface = ApiUtilities.getApiInterface()
        val requestBody = RequestBody.create(MediaType.parse("application/json"),
            Gson().toJson(
                ChatRequest (
                    250,
                    "gpt-3.5-turbo-instruct",
                    binding.userMsg.text.toString(),
                    0.7
                )

            )
        )

        val contentType = "application/json"
        val authorization = "Bearer ${Utils.API_KEY}"

        lifecycleScope.launch (Dispatchers.IO){

            try {
                val response = apiInterface.getChat(
                    contentType, authorization ,requestBody
                )
                val textResponse = response.choices.first().text

                list.add(MessageModel(false,false,textResponse))
                withContext(Dispatchers.Main){
                    adapter.notifyItemInserted(list.size - 1)

                    binding.recylerView.recycledViewPool.clear()

                    binding.recylerView.smoothScrollToPosition(list.size - 1)
                }

                binding.userMsg.text!!.clear()

            } catch (e: Exception) {
                withContext(Dispatchers.Main){
                    Toast.makeText(this@ChatActivity, e.message, Toast.LENGTH_SHORT).show()
                }
            }


        }
    }

}