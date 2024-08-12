
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.example.puffnpoof.Activity.closingPage
import com.example.puffnpoof.Database.dbHelper
import com.example.puffnpoof.R

class profile : Fragment() {

    private lateinit var dbHelper: dbHelper
    private lateinit var titleTextView: TextView
    private lateinit var profileIcon: ImageView
    private lateinit var usernameTextView: TextView
    private lateinit var emailTextView: TextView
    private lateinit var phoneTextView: TextView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view = inflater.inflate(R.layout.fragment_profile, container, false)

        val logoutButton: Button = view.findViewById(R.id.logoutButton)
        logoutButton.setOnClickListener {
            val intent = Intent(requireActivity(), closingPage::class.java)
            startActivity(intent)
        }

        titleTextView = view.findViewById(R.id.titleTextView)
        profileIcon = view.findViewById(R.id.profileIcon)
        usernameTextView = view.findViewById(R.id.usernameTextView)
        emailTextView = view.findViewById(R.id.emailTextView)
        phoneTextView = view.findViewById(R.id.phoneNumberTextView)

        // data usernya disini
        dbHelper = dbHelper(requireContext())
        val curr_user_id = arguments?.getInt("currentUserID") ?: -1
        Log.d("dbHelper", "UserID ${curr_user_id} ")
        val users = dbHelper.getUsers()
        val user = users.find { it.userID == curr_user_id }
        if (user != null) {
            Log.d("tag", "Gender di profile page : ${user.gender}")
            usernameTextView.text = user.username
            emailTextView.text = user.email
            phoneTextView.text = user.phoneNumber
        }

        return view
    }

}