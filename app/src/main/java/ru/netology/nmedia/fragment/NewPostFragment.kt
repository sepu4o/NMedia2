package ru.netology.nmedia.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import ru.netology.nmedia.databinding.FragmentNewPostBinding
import ru.netology.nmedia.util.DraftManager
import ru.netology.nmedia.util.StringArg
import ru.netology.nmedia.viewmodel.PostViewModel

class NewPostFragment : Fragment() {

    private val viewModel: PostViewModel by viewModels(ownerProducer = ::requireParentFragment)

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = FragmentNewPostBinding.inflate(inflater, container, false)


        val postText = arguments?.textArg
        val postId = arguments?.getLong("postId", 0L) ?: 0L


        if (postId == 0L) {
            DraftManager.getDraft(requireContext())?.let {
                binding.edit.setText(it)
            }
        } else {
            postText?.let {
                binding.edit.setText(it)
            }
        }


        binding.ok.setOnClickListener {
            val content = binding.edit.text.toString()
            if (content.isNotBlank()) {
                if (postId == 0L) {
                    viewModel.save(content)
                } else {
                    viewModel.update(postId, content)
                }
                DraftManager.clearDraft(requireContext())
            }
            findNavController().navigateUp()
        }

        requireActivity().onBackPressedDispatcher.addCallback(
            viewLifecycleOwner,
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    val content = binding.edit.text.toString()
                    if (postId == 0L) {
                        DraftManager.saveDraft(requireContext(), content)
                    }
                    findNavController().navigateUp()
                }
            }
        )

        return binding.root
    }

    companion object {
        var Bundle.textArg: String? by StringArg
    }
}