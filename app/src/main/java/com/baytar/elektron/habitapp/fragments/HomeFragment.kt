package com.baytar.elektron.habitapp.fragments


import android.annotation.SuppressLint
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.preference.PreferenceManager
import com.baytar.elektron.habitapp.R
import com.baytar.elektron.habitapp.databinding.FragmentHomeBinding
import me.itangqi.waveloadingview.WaveLoadingView
import java.math.RoundingMode
import java.text.DecimalFormat


private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"


/**
 * A simple [Fragment] subclass.
 * Use the [HomeFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class HomeFragment : Fragment() {
    private lateinit var binding: FragmentHomeBinding
    private var param1: String? = null
    private var param2: String? = null
    private lateinit var dropPlus: ImageView
    private lateinit var dropBackground: ViewGroup
    private lateinit var drinkVolume: EditText
    private lateinit var goalMl: TextView
    private lateinit var waterStatistics: TextView
    private lateinit var cokeStatistics: TextView
    private lateinit var juicyStatistics: TextView
    private lateinit var coffeeStatistics: TextView
    private lateinit var remainingDrink: TextView
    private lateinit var complete: TextView
    private lateinit var waterIntake: TextView
    private lateinit var waterWave: WaveLoadingView
    private lateinit var waterWave2: WaveLoadingView


    /**
     *
     */
    private val df = DecimalFormat("#")
    private val wf = DecimalFormat("#")
    private var sharedPref: SharedPreferences? = null
    private val integerChars: CharRange = '0'..'9'
    private var goalVolume = 0f
    private var remain = 0
    private var intake = 0
    private var select = 1
    private var waterStat = 0
    private var juicyStat = 0
    private var coffeeStat = 0
    private var cokeStat = 0
    private var waterMl = 0
    private var juicyMl = 0
    private var coffeeMl = 0
    private var cokeMl = 0
    private var completed = 0f
    private var progress1 = 0
    private var progress2 = 0
    private var cup = 0

    /**
     *
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    /**
     *
     */
    @SuppressLint("SetTextI18n")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        // Inflate the layout for this fragment
        return binding.root
    }

    private fun pickFromGallery() {
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        intent.type = "image/*"
        val mimeTypes = arrayOf("image/jpeg", "image/png", "image/jpg")
        intent.putExtra(Intent.EXTRA_MIME_TYPES, mimeTypes)
        intent.flags = Intent.FLAG_GRANT_READ_URI_PERMISSION
    }

    /**
     *
     */
    private fun isInteger(input: String): Boolean = input.all { it in integerChars }


    /**
     *
     */
    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        dropPlus = binding.dropPlus
        dropBackground = binding.dropBackground
        drinkVolume = binding.drinkMlVolume
        goalMl = binding.goalMl
        waterStatistics = binding.waterStatistics
        cokeStatistics = binding.cokeStatistics
        juicyStatistics = binding.juicyStatistics
        coffeeStatistics = binding.coffeeStatistics
        remainingDrink = binding.remainingDrink
        complete = binding.complete!!
        waterIntake = binding.waterIntake
        waterWave = binding.waterWave
        waterWave2 = binding.waterWave2
        dropPlus.setOnClickListener {
            dropBackground.visibility = View.VISIBLE
        }


        val waterSelect = view.findViewById<View>(R.id.water_select)
        val juicySelect = view.findViewById<View>(R.id.juicy_select)
        val coffeeSelect = view.findViewById<View>(R.id.coffee_select)
        val cokeSelect = view.findViewById<View>(R.id.coke_select)
        val saveDrinkMl = view.findViewById<View>(R.id.save_drink_ml)
        val drinkProfile = view.findViewById<View>(R.id.water_profile_picture)
        val image = view.findViewById<View>(R.id.reset_drink)
        image.setOnClickListener {
            resetData()
        }

        //////////////////// selecting  ///////////////////////////////////

        waterSelect?.setBackgroundResource(R.drawable.good_button)
        waterSelect?.setOnClickListener {
            waterSelect.setBackgroundResource(R.drawable.good_button)
            juicySelect?.setBackgroundResource(R.color.gainsboro)
            coffeeSelect?.setBackgroundResource(R.color.gainsboro)
            cokeSelect?.setBackgroundResource(R.color.gainsboro)
            select = 1
            ////////////////////////////////////
        }
        juicySelect?.setOnClickListener {
            waterSelect?.setBackgroundResource(R.color.gainsboro)
            juicySelect.setBackgroundResource(R.drawable.good_button)
            coffeeSelect?.setBackgroundResource(R.color.gainsboro)
            cokeSelect?.setBackgroundResource(R.color.gainsboro)
            select = 2
        }
        coffeeSelect?.setOnClickListener {
            waterSelect?.setBackgroundResource(R.color.gainsboro)
            juicySelect?.setBackgroundResource(R.color.gainsboro)
            coffeeSelect.setBackgroundResource(R.drawable.good_button)
            cokeSelect?.setBackgroundResource(R.color.gainsboro)
            select = 3
        }
        cokeSelect?.setOnClickListener {
            waterSelect?.setBackgroundResource(R.color.gainsboro)
            juicySelect?.setBackgroundResource(R.color.gainsboro)
            coffeeSelect?.setBackgroundResource(R.color.gainsboro)
            cokeSelect.setBackgroundResource(R.drawable.good_button)
            select = 4
        }
        //////////////////////////////////////////////////////////////////////////////////
        saveDrinkMl.setOnClickListener {
            intake += drinkVolume.text.toString().toInt()
            cup =
                100 / (goalMl.text.toString().toInt() / drinkVolume.text.toString()
                    .toInt())
            progress1 += cup * 2
            dropBackground.visibility = View.INVISIBLE
            remain = goalMl.text.toString().toInt() - intake
            completed = (intake.toFloat() / goalMl.text.toString().toFloat()) * 100

            when (select) {
                1 -> {
                    waterMl += drinkVolume.text.toString().toInt()
                }
                2 -> {
                    juicyMl += drinkVolume.text.toString().toInt()
                }
                3 -> {
                    coffeeMl += drinkVolume.text.toString().toInt()
                }
                4 -> {
                    cokeMl += drinkVolume.text.toString().toInt()
                }
            }
            df.roundingMode = RoundingMode.CEILING
            waterStat = (waterMl / intake) * 100
            juicyStat = (juicyMl / intake) * 100
            coffeeStat = (coffeeMl / intake) * 100
            cokeStat = (cokeMl / intake) * 100
            waterStatistics.text = "${wf.format(waterStat)}%"
            juicyStatistics.text = "${wf.format(juicyStat)}%"
            coffeeStatistics.text = "${wf.format(coffeeStat)}%"
            cokeStatistics.text = "${wf.format(cokeStat)}%"

            waterIntake.text = "$intake"
            waterWave.progressValue = progress1
            if (remain <= 0) {
                remainingDrink.text = "0ml"
            } else {
                remainingDrink.text = "$remain ml"
            }
            if (completed >= 100) {
                complete.text = "100%"
            } else {
                complete.text = df.format(
                    (completed)
                ) + " %"
            }
            if (intake >= goalMl.text.toString().toInt() / 2) {
                if (drinkVolume.text.toString().toInt() >= 350) {
                    progress2 += cup
                    waterWave2.progressValue = progress2
                } else {
                    progress2 += cup * 2
                    waterWave2.progressValue = progress2
                }
            }
            sharedPref?.edit()
                ?.putInt("waterMl", waterMl)
                ?.putInt("juicyMl", juicyMl)
                ?.putInt("coffeeMl", coffeeMl)
                ?.putInt("cokeMl", cokeMl)
                ?.putInt("intake", intake)
                ?.putInt("progress1", progress1)
                ?.putInt("progress2", progress2)
                ?.putInt("remain", remain)
                ?.putInt("cup", cup)
                ?.putFloat("completed", completed)
                ?.putInt("waterStat", waterStat)
                ?.putInt("juicyStat", juicyStat)
                ?.putInt("coffeeStat", coffeeStat)
                ?.putInt("cokeStat", cokeStat)
                ?.apply()
        }
        drinkProfile.setOnClickListener {
            pickFromGallery()
        }
        loadSetting()
    }

    @SuppressLint("SetTextI18n")
    private fun loadSetting() {
        sharedPref = PreferenceManager.getDefaultSharedPreferences(requireContext())
        intake = (sharedPref?.getInt("intake", 0) ?: return)
        df.roundingMode = RoundingMode.CEILING
        cup = (sharedPref?.getInt("cup", 0) ?: return)
        progress1 = (sharedPref?.getInt("progress1", 0) ?: return)
        progress2 = (sharedPref?.getInt("progress2", 0) ?: return)
        remain = (sharedPref?.getInt("remain", remain) ?: return)
        completed = (sharedPref?.getFloat("completed", completed) ?: return)
        waterStat = (sharedPref?.getInt("waterStat", waterStat) ?: return)
        juicyStat = (sharedPref?.getInt("juicyStat", juicyStat) ?: return)
        coffeeStat = (sharedPref?.getInt("coffeeStat", coffeeStat) ?: return)
        cokeStat = (sharedPref?.getInt("cokeStat", cokeStat) ?: return)
        waterStatistics.text = "${wf.format(waterStat)}%"
        juicyStatistics.text = "${wf.format(juicyStat)}%"
        coffeeStatistics.text = "${wf.format(coffeeStat)}%"
        cokeStatistics.text = "${wf.format(cokeStat)}%"

        waterIntake.text = "$intake"
        waterWave.progressValue = progress1
        if (remain <= 0) {
            remainingDrink.text = "0ml"
        } else {
            remainingDrink.text = "$remain ml"
        }
        if (completed >= 100) {
            complete.text = "100%"
        } else {
            complete.text = df.format(
                (completed)
            ) + " %"
        }
        if (intake >= goalMl.text.toString().toInt() / 2) {
            if (drinkVolume.text.toString().toInt() >= 350) {
                progress2 += cup
                waterWave2.progressValue = progress2
            } else {
                progress2 += cup * 2
                waterWave2.progressValue = progress2
            }
        }
        val gender = sharedPref?.getString("Gender", "")
        val kg = sharedPref?.getString("kg", "")
        val weight = sharedPref?.getString("weight", "")
        val exercise = sharedPref?.getString("exercise", "")
        val cf = DecimalFormat("#")
        if (gender == "male" && kg == "kg" && weight?.let { isInteger(it) } == true && weight.isNotEmpty() && exercise?.let {
                isInteger(
                    it
                )
            } == true && exercise.isNotEmpty()) {
            goalVolume = (weight.toString().toFloat() * 4.4f / 3 + exercise.toString()
                .toFloat() * 0.4f) * 29.57f
            goalMl.text = cf.format(goalVolume)
            remainingDrink.text = cf.format(goalVolume)
        } else if (gender == "male" && kg == "lb" && weight?.let { isInteger(it) } == true && weight.isNotEmpty() && exercise?.let {
                isInteger(
                    it
                )
            } == true && exercise.isNotEmpty()) {
            goalVolume =
                (weight.toString().toFloat() * 2 / 3 + exercise.toString()
                    .toFloat() * 0.4f) * 29.57f
            goalMl.text = cf.format(goalVolume)
            remainingDrink.text = cf.format(goalVolume)
        } else if (gender == "female" && kg == "kg" && weight?.let { isInteger(it) } == true && weight.isNotEmpty() && exercise?.let {
                isInteger(
                    it
                )
            } == true && exercise.isNotEmpty()) {
            goalVolume = (weight.toString().toFloat() * 4.4f / 3 + exercise.toString()
                .toFloat() * 0.4f) * 29.57f / 1.3f
            goalMl.text = cf.format(goalVolume)
            remainingDrink.text = cf.format(goalVolume)
        } else if (gender == "female" && kg == "lb" && weight?.let { isInteger(it) } == true && weight.isNotEmpty() && exercise?.let {
                isInteger(
                    it
                )
            } == true && exercise.isNotEmpty()) {
            goalVolume = (weight.toString().toFloat() * 2 / 3 + exercise.toString()
                .toFloat() * 0.4f) * 29.57f / 1.3f
            goalMl.text = cf.format(goalVolume)
            remainingDrink.text = cf.format(goalVolume)
        }
    }

    private fun resetData() {
        intake = 0
        waterMl = 0
        juicyMl = 0
        coffeeMl = 0
        cokeMl = 0
        progress2 = 0
        progress1 = 0
        remain = 0
        completed = 0f
        waterStat = 0
        coffeeStat = 0
        cokeStat = 0
        juicyStat = 0
        sharedPref = PreferenceManager.getDefaultSharedPreferences(requireContext())
        sharedPref?.edit()
            ?.putInt("intake", intake)
            ?.putInt("waterMl", waterMl)
            ?.putInt("juicyMl", juicyMl)
            ?.putInt("coffeeMl", coffeeMl)
            ?.putInt("cokeMl", cokeMl)
            ?.putInt("progress1", progress1)
            ?.putInt("progress2", progress2)
            ?.putInt("remain", remain)
            ?.putInt("cup", cup)
            ?.putFloat("completed", completed)
            ?.putInt("waterStat", waterStat)
            ?.putInt("juicyStat", juicyStat)
            ?.putInt("coffeeStat", coffeeStat)
            ?.putInt("cokeStat", cokeStat)
            ?.apply()
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment HomeFragment.
         */
        @JvmStatic
        fun newInstance(param1: String, param2: String): HomeFragment =
            HomeFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }

}