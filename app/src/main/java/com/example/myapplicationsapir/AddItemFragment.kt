package com.example.myapplicationsapir

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.myapplicationsapir.databinding.AddItemLayoutBinding

class AddItemFragment : Fragment() {
    private var _binding : AddItemLayoutBinding? = null
    private val binding get() = _binding!!
    private var imageUri: Uri? = null

    val pickImageLauncher : ActivityResultLauncher<Array<String>> =
        registerForActivityResult(ActivityResultContracts.OpenDocument()) {
            if(it != null) {
                binding.resultImage.setImageURI(it)
                requireActivity().contentResolver.takePersistableUriPermission(it,Intent.FLAG_GRANT_READ_URI_PERMISSION)
                imageUri = it
            }

        }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = AddItemLayoutBinding.inflate(inflater, container, false)

        binding.finishBtn.setOnClickListener {
//            val bundle = bundleOf("title" to binding.itemTitle.text.toString(), "description" to binding.itemDescription.text.toString())
//            findNavController().navigate(R.id.action_addItemFragment_to_allItemsFragment,bundle)
            val item = Item(binding.itemTitle.text.toString(),
                binding.itemDescription.text.toString(),imageUri)//null //imageUri.toString()
            ItemManager.add(item)

            findNavController().navigate(R.id.action_addItemFragment_to_allItemsFragment
                ,bundleOf("item" to item))
        }

        binding.imageBtn.setOnClickListener {
            pickImageLauncher.launch(arrayOf("image/*"))
        }

        return  binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}