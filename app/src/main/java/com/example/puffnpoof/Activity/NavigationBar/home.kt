package com.example.puffnpoof.Activity.NavigationBar

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.example.puffnpoof.Activity.DetailPage
import com.example.puffnpoof.Adapter.home_adapter
import com.example.puffnpoof.Database.dbHelper
import com.example.puffnpoof.Model.dollModel
import com.example.puffnpoof.R
import org.json.JSONException
import org.json.JSONObject



class home : Fragment() {

    private lateinit var dbHelper: dbHelper
    private lateinit var recyclerView: RecyclerView
    private lateinit var doll_home_adapter: home_adapter
    private lateinit var requestQueue: RequestQueue
    private lateinit var request: JsonObjectRequest

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        requestQueue = Volley.newRequestQueue(requireContext())
        val url= "https://api.npoint.io/9d7f4f02be5d5631a664"
        request = JsonObjectRequest(
            Request.Method.GET, url, null,
            Response.Listener { response ->
                praseJSON(response)
            },
            Response.ErrorListener { error ->
                Log.e("Volley Error", error.toString())
            }
        )
        requestQueue.add(request)
        dbHelper = dbHelper(requireContext())
        val view = inflater.inflate(R.layout.fragment_home, container, false)

        val curr_user_id = arguments?.getInt("currentUserID") ?: -1
        Log.d("dbHelper", "Home UserID ${curr_user_id} ")

        recyclerView = view.findViewById(R.id.dollrecyclerView)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        val dollData= dbHelper.getDoll()
        doll_home_adapter = home_adapter(dollData) { doll ->

            val intent = Intent(requireContext(), DetailPage::class.java)

            intent.putExtra("doll", doll)
            intent.putExtra("currentUserID", curr_user_id)
            startActivity(intent)
        }
        recyclerView.adapter = doll_home_adapter
        return view
    }

    private fun praseJSON(jsonObject: JSONObject){
        try {
            val getdolls= jsonObject.getJSONArray("dolls")
            for (i in 0 until getdolls.length()){
                val dollobject=getdolls.getJSONObject(i)
                val name =dollobject.getString("name")
                val desc = dollobject.getString("desc")
                val size = dollobject.getString("size")
                val price = dollobject.getInt("price")
                val rating = dollobject.getDouble("rating")
                val imageLink = dollobject.getString("imageLink")
                val check= dbHelper.getDoll().find { it.name==name }
                if(check==null) {
                    dbHelper.insertDoll(dollModel(0, name, price, size, desc, imageLink, rating))
                }
            }
        }catch (e: JSONException){
            e.printStackTrace()
        }
    }
}
