package com.example.myapplicationsapir

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.myapplicationsapir.databinding.DetailItemLayoutBinding

class DetailItemFragment : Fragment() {

    var _binding : DetailItemLayoutBinding? = null

    val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = DetailItemLayoutBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        arguments?.getInt("item")?.let {
            val item = ItemManager.items[it]
            binding.itemTitle.text = item.title
            binding.itemDesc.text = item.description
            Glide.with(requireContext()).load(item.imageUri).circleCrop()
                .into(binding.itemImage)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}