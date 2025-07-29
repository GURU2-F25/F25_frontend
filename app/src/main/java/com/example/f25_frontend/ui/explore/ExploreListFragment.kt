package com.example.f25_frontend.ui.explore

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.f25_frontend.R
import com.example.f25_frontend.databinding.FragmentExploreListBinding

class ExploreListFragment : Fragment() {

    private var _binding: FragmentExploreListBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentExploreListBinding.inflate(inflater, container, false)
        binding.btnExploreUserExcute.setOnClickListener(){
            findNavController().navigate(R.id.action_explore_list_to_explore_user)
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
