import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.puffnpoof.Model.transactionModel
import com.example.puffnpoof.Adapter.transaction_adapter
import com.example.puffnpoof.Database.dbHelper
import com.example.puffnpoof.R

class transaction : Fragment() {

    private lateinit var dbHelper: dbHelper
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: transaction_adapter
    private var trans_list = ArrayList<transactionModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view = inflater.inflate(R.layout.fragment_transaction, container, false)

        dbHelper = dbHelper(requireContext())
        val curr_user_id = arguments?.getInt("currentUserID") ?: -1
        Log.d("dbHelper", "Home UserID ${curr_user_id} ")

        recyclerView = view.findViewById(R.id.transrecyclerView)
        adapter = transaction_adapter(trans_list, (curr_user_id),dbHelper)
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        val transactions = dbHelper.getTransaction(curr_user_id)
        Log.d("tag", "Number of transactions for user ${curr_user_id}: ${transactions.size}")
        trans_list.addAll(transactions)

        return view
    }
}