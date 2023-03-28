package com.example.hydroboost.ui.home

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.LinearLayout
import com.example.hydroboost.R

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [HomeFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class HomeFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    private var waterFillingView : View? = null
    private lateinit var waterBottleImage : ImageView

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

        val rootView = inflater.inflate(R.layout.fragment_home, container, false)
        rootView as ViewGroup //This FrameLayout should be changed to a ViewGroup to avoid explicit casting

        val maxHeight = 1434 //This represents the number of pixels from top of screen to bottom of water bottle
//        addWater(rootView, 371, 150, 338, 1284) //Water same height as water bottle
//        addWater(rootView, 371, 340, 338, 1094) //Water actual dimensions
        addWater(rootView, 371, ((1434 * .9).toInt()), 338, ((1434 * .1).toInt()))
        addWaterBottle(rootView)

        return rootView
    }

    override fun onViewCreated(view : View, savedInstanceState : Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        view as ViewGroup

        for (i in 1 .. 9) {
            view.postDelayed({
                fillWaterBottle(view, i)
            }, (1000 * i).toLong())
        }

        view.postDelayed({
            removeWater(view)
        }, 10000)
    }

    fun fillWaterBottle(view : View, percentage : Int) {
        view as ViewGroup

        view.removeView(waterFillingView) //Remove the water
        view.removeView(waterBottleImage) //Remove the bottle
        addWater(view, 371, ((1434.0 * (1.0 - (percentage / 10.0))).toInt()), 338, ((1434.0 * (percentage / 10.0)).toInt()))
        addWaterBottle(view)
    }

    fun addWaterBottle(view : View) {
        view as ViewGroup
        waterBottleImage = ImageView(requireContext())

        waterBottleImage.setImageResource(R.drawable.water_bottle)
        val waterBottleImageParams = LinearLayout.LayoutParams(338, 1284)
        waterBottleImageParams.setMargins(371, 150, 0, 0)
        waterBottleImage.layoutParams = waterBottleImageParams
        waterBottleImage.id = R.id.waterBottleImage
        view.addView(waterBottleImage)
    }

    fun removeWaterBottle(view : View) {
        view as ViewGroup
        view.removeView(waterBottleImage)
    }

    fun addWater(rootView: View, x : Int, y : Int, rectangleWidth : Int, rectangleHeight : Int) {
        rootView as ViewGroup
        waterFillingView = WaterFillingView(requireContext(), null, x, y, rectangleWidth, rectangleHeight)
        rootView.addView(waterFillingView)
    }

    fun removeWater(view : View) {
        view as ViewGroup
        view.removeView(waterFillingView)
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment home.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            HomeFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}