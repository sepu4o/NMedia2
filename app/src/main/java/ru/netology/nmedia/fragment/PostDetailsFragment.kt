package ru.netology.nmedia.fragment

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import ru.netology.nmedia.R
import ru.netology.nmedia.databinding.FragmentPostDetailsBinding
import ru.netology.nmedia.dto.Post
import ru.netology.nmedia.fragment.NewPostFragment.Companion.textArg
import ru.netology.nmedia.util.AndroidUtils
import ru.netology.nmedia.viewmodel.PostViewModel

class PostDetailsFragment : Fragment() {

    private val viewModel: PostViewModel by viewModels(ownerProducer = ::requireParentFragment)

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = FragmentPostDetailsBinding.inflate(inflater, container, false)
        val postId = arguments?.getLong("postId") ?: return binding.root

        viewModel.data.observe(viewLifecycleOwner) { posts ->
            val post = posts.find { it.id == postId }
            if (post != null) {
                bindPost(binding, post)
            }
        }
        return binding.root
    }

    private fun bindPost(binding: FragmentPostDetailsBinding, post: Post) {
        binding.author.text = post.author
        binding.published.text = post.published
        binding.content.text = post.content
        binding.countView.text = AndroidUtils.formatCount(post.view)
        binding.like.isChecked = post.likedByMe
        binding.like.text = AndroidUtils.formatCount(post.likes)
        binding.share.text = AndroidUtils.formatCount(post.share)

        // Видео
        if (post.video != null) {
            binding.videoBlock.visibility = View.VISIBLE
        } else {
            binding.videoBlock.visibility = View.GONE
        }

        binding.like.setOnClickListener {
            viewModel.likeById(post.id)
        }


        binding.share.setOnClickListener {
            viewModel.shareById(post.id)
            val intent = Intent().apply {
                action = Intent.ACTION_SEND
                putExtra(Intent.EXTRA_TEXT, post.content)
                type = "text/plain"
            }
            val chooser = Intent.createChooser(intent, getString(R.string.chooser_share_post))
            startActivity(chooser)
        }

        binding.menu.setOnClickListener {
            PopupMenu(it.context, it).apply {
                inflate(R.menu.post_menu)
                setOnMenuItemClickListener { item ->
                    when (item.itemId) {
                        R.id.remove -> {
                            viewModel.removeById(post.id)
                            findNavController().navigateUp()
                            true
                        }

                        R.id.edit -> {
                            findNavController().navigate(
                                R.id.action_postDetailsFragment_to_newPostFragment,
                                Bundle().apply {
                                    textArg = post.content
                                    putLong("postId", post.id)
                                }
                            )
                            true
                        }
                        else -> false
                    }
                }
                show()
            }
        }
    }
}
