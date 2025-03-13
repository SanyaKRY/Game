package com.example.mygame.features.endgamepopup.presenter.ui

import android.animation.ValueAnimator
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.mygame.R
import com.example.mygame.databinding.FragmentEndGamePopupBinding
import com.example.mygame.databinding.FragmentMenuViewBinding
import com.example.mygame.features.endgamepopup.presenter.viewmodel.EndGamePopupViewModel
import com.example.mygame.features.gamescene.presenter.ui.GameSceneFragmentDirections
import com.example.mygame.features.menuview.presenter.vm.MenuViewViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class EndGamePopupFragment : Fragment() {

    private var _binding: FragmentEndGamePopupBinding? = null
    private val binding get() = _binding!!

    val args: EndGamePopupFragmentArgs by navArgs()

    private val viewModel: EndGamePopupViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentEndGamePopupBinding.inflate(inflater, container, false)
        val view = binding.root
        var animator = ValueAnimator.ofInt(0, args.coins)
        animator.duration = 500
        animator.start()

        animator.addUpdateListener(object : ValueAnimator.AnimatorUpdateListener {
            override fun onAnimationUpdate(animation: ValueAnimator) {
                val animatedValue = animation.animatedValue as Int
                binding.coinTextView.text = animatedValue.toString()
            }
        })

        binding.button.setOnClickListener {
            viewModel.saveValues(args.coins)
            val action =
                EndGamePopupFragmentDirections.actionEndGamePopupFragmentToMenuViewFragment(binding.coinTextView.text.toString().toInt())
                GameSceneFragmentDirections
                    .actionGameSceneFragmentToEndGamePopupFragment(binding.coinTextView.text.toString().toInt())
            findNavController().navigate(action)
        }

        return view
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}