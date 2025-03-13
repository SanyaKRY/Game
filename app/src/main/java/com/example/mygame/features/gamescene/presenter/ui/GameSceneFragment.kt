package com.example.mygame.features.gamescene.presenter.ui

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.widget.AppCompatImageButton
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.example.mygame.R
import com.example.mygame.databinding.FragmentGameSceneBinding
import com.example.mygame.features.gamescene.presenter.event.CardClickEvent
import com.example.mygame.features.gamescene.presenter.event.EventChannel
import com.example.mygame.features.gamescene.presenter.vm.GameSceneViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class GameSceneFragment : Fragment() {

    private var _binding: FragmentGameSceneBinding? = null
    private val binding get() = _binding!!

    private val viewModel: GameSceneViewModel by viewModels()

    var listOfButtons: MutableList<AppCompatImageButton>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentGameSceneBinding.inflate(inflater, container, false)
        val view = binding.root

        observerFlow()
        setListOfButton()
        setOnClickButtonListener()
        observerEvent()
        observerStopWatch()

//        val state = viewModel.state.value

        return view
    }

    private fun observerStopWatch() {
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.stopWatchStateFlow.collect { result ->
                    println("Stopwatch: "+ result.currentSec)
                    binding.stopwatchTextView.text = result.currentSec.displayTime()
                    binding.coinTextView.text = result.currentCoin.toString()
                }
            }
        }
    }

    private fun Long.displayTime(): String {
        val m = this  % 3600 / 60
        val s = this  % 60

        return "${displaySlot(m)}:${displaySlot(s)}"
    }

    private fun displaySlot(count: Long): String {
        return if (count / 10L > 0) {
            "$count"
        } else {
            "0$count"
        }
    }


    private fun observerEvent() {
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.eventsFlow.collect { result ->
                    when (result) {
                        is EventChannel.ShowToast -> {
                            println("QQQQQQQQQQQQqq")
                            Toast.makeText(context, result.text, Toast.LENGTH_LONG).show()
                            val action =
                                GameSceneFragmentDirections
                                    .actionGameSceneFragmentToEndGamePopupFragment(binding.coinTextView.text.toString().toInt())
                            findNavController().navigate(action)
                        }
                    }
                }
            }
        }
    }

    private fun observerFlow() {
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.state.collect { result ->
                    println("Size: "+result.cards.size)
                    for(i in 0..19) {

                        if(result.cards.get(i).isBackDisplayed) {
//                            listOfButtons!![i].setImageResource(R.drawable.default_background_card_another)
                            listOfButtons!![i].background = resources.getDrawable(R.drawable.default_background_card_another)
                            listOfButtons!![i].setImageResource(0)
                        } else {
                            listOfButtons!![i].setImageResource(getImage(result.cards.get(i).value)!!)
                            listOfButtons!![i].background = resources.getDrawable(R.drawable.default_background_card_another)
                            if(result.cards.get(i).matchFound){
                                listOfButtons!![i].background = resources.getDrawable(R.drawable.matched)
                            } else {

                            }
                        }
//                        listOfButtons!![i].background = resources.getDrawable(result.cards.get(i).cardImage)
//                        listOfButtons!![i].setOnClickListener {
////                            it.background = resources.getDrawable(result.myGame?.list?.get(i)?.cardBack!!)
//                        }
////                        listOfButtons!![i].background = resources.getDrawable(result.myGame?.list?.get(i)?.cardBack!!)
                    }
                }
            }
        }
    }

    private fun getImage(id: Int): Int? {
        val listOfImages = mutableListOf<Int>(
            R.drawable.ipo, R.drawable.ipo,
            R.drawable.interest, R.drawable.interest,
            R.drawable.cash, R.drawable.cash,
            R.drawable.index, R.drawable.index,
            R.drawable.law, R.drawable.law,
            R.drawable.loss, R.drawable.loss,
            R.drawable.my_category, R.drawable.my_category,
            R.drawable.online, R.drawable.online,
            R.drawable.p2p, R.drawable.p2p,
            R.drawable.profit, R.drawable.profit,
        )
        val  imageMap: Map<Int, Int> = mapOf(
        1 to R.drawable.baby_ghost,
        2 to R.drawable.cat,
        3 to R.drawable.devil,
        4 to R.drawable.happy_pumpkin,
        5 to R.drawable.heart,
        6 to R.drawable.leaf,
        7 to R.drawable.lollipop,
        8 to R.drawable.spider,
        9 to R.drawable.vampire,
        10 to R.drawable.witch
        )
        return imageMap[id]
    }

    private fun setOnClickButtonListener() {
        for(i in 0..19) {
            listOfButtons!![i].setOnClickListener {
                viewModel.handleEvent(CardClickEvent(cardId = i))
            }
        }
    }

    private fun setListOfButton() {
        listOfButtons = mutableListOf(
            binding.button0,
            binding.button1,
            binding.button2,
            binding.button3,
            binding.button4,
            binding.button5,
            binding.button6,
            binding.button7,
            binding.button8,
            binding.button9,
            binding.button10,
            binding.button11,
            binding.button12,
            binding.button13,
            binding.button14,
            binding.button15,
            binding.button16,
            binding.button17,
            binding.button18,
            binding.button19)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}