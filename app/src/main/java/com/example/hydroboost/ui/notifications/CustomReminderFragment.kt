package com.example.hydroboost.ui.notifications

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ListView
import androidx.fragment.app.Fragment
import com.example.hydroboost.R
import com.example.hydroboost.databinding.CustomReminderItemBinding
import com.example.hydroboost.ui.Adapter
import com.example.hydroboost.ui.SharedPreferences

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [CustomReminderFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class CustomReminderFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null


    private var sharedPreferences: SharedPreferences? = null
    private lateinit var _binding: CustomReminderItemBinding
    val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        // Inflate the layout for this fragment
        sharedPreferences = SharedPreferences(requireContext())

        val t=inflater.inflate(R.layout.fragment_custom_reminder, container, false)

        val addButton = t.findViewById<ImageButton>(R.id.addCustomReminder)
        addButton.setOnClickListener {
            val fragment = CreateCustomReminderFragment()
            val transaction = fragmentManager?.beginTransaction()
            transaction?.replace(R.id.frameLayout,fragment)?.commit()
        }

        val customReminderPreferences = context?.getSharedPreferences(
            context!!.getString(R.string.custom_reminders_settings),
            Context.MODE_PRIVATE
        )
        var customReminders = customReminderPreferences?.getString("CUSTOM_REMINDERS", "")


        var listView = t.findViewById<ListView>(R.id.remindersList)


        var crs = customReminders?.split("|")
        println(crs.toString())
        var titles = arrayListOf<String>()
        for (reminder in crs!!){
            if (reminder != ""){
                var fields = reminder.split(",")
                titles.add(fields.get(0))
            }
        }

        var adapter = Adapter(context!!, titles, )
        listView.adapter = adapter

        return t
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment CustomReminderFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            CustomReminderFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}