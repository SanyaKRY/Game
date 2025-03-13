package com.example.mygame.features.menuview.presenter.ui

import android.animation.ValueAnimator
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.mygame.R
import com.example.mygame.databinding.FragmentMenuViewBinding
import androidx.lifecycle.Observer
import androidx.navigation.fragment.navArgs
import com.example.mygame.features.menuview.presenter.vm.MenuViewViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MenuViewFragment : Fragment() {

    private val viewModel: MenuViewViewModel by viewModels()

    val args: MenuViewFragmentArgs by navArgs()

    private var _binding: FragmentMenuViewBinding? = null
    private val binding get() = _binding!!

    var animator: ValueAnimator? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentMenuViewBinding.inflate(inflater, container, false)
        val view = binding.root

        binding.playButton.setOnClickListener {
            findNavController().navigate(R.id.action_menuViewFragment_to_gameSceneFragment)
        }






        viewModel.coinNumber.observe(viewLifecycleOwner, Observer { coin ->
//            animator?.duration = 500
//            animator?.start()
//            animator = ValueAnimator.ofInt(coin.toInt(), coin.toInt())
//            animator!!.addUpdateListener(object : ValueAnimator.AnimatorUpdateListener {
//                override fun onAnimationUpdate(animation: ValueAnimator) {
//                    val animatedValue = animation.animatedValue as Int
//                    binding.coinTextView.text = animatedValue.toString()
//                }
//            })
            binding.coinTextView.text = coin
        })

        viewModel.gameNumber.observe(viewLifecycleOwner, Observer { gameNumber ->
            var text:String = " Game  \nLogo №${gameNumber}"
            binding.gameNumberTextView.text = text
        })

        return view
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}