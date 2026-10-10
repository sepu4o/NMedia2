package ru.netology.nmedia.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ActionMenuView
import android.widget.PopupMenu
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import ru.netology.nmedia.databinding.CardPostBinding
import ru.netology.nmedia.dto.Post

import androidx.recyclerview.widget.DiffUtil
import ru.netology.nmedia.R
import ru.netology.nmedia.util.AndroidUtils
import ru.netology.nmedia.viewmodel.PostViewModel

interface PostListener{
    fun onlike(post:Post)
    fun onEdit(post:Post)
    fun onRemove(post:Post)
    fun onShare(post: Post)
}


class PostsAdapter(private val listener: PostListener):
    ListAdapter<Post, PostViewHolder>(PostDiffItemCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostViewHolder {
        val binding = CardPostBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return PostViewHolder(binding, listener)

    }

    override fun onBindViewHolder(holder: PostViewHolder, position: Int) {
        holder.bind(getItem(position))
    }
}
class PostViewHolder(private val binding : CardPostBinding,
                     private val listener: PostListener):
    RecyclerView.ViewHolder(binding.root){
    fun bind(post: Post) {
        with(binding) {
            author.text = post.author
            published.text = post.published
            content.text = post.content
            countView.text = AndroidUtils.formatCount(post.view)
            like.isChecked = post.likedByMe
            like.text = AndroidUtils.formatCount(post.likes)
            share.text = AndroidUtils.formatCount(post.share)
            like.setOnClickListener {
                listener.onlike(post)
            }
            share.setOnClickListener {
                listener.onShare(post)
            }
            menu.setOnClickListener {
                PopupMenu(it.context, it).apply {
                    inflate(R.menu.post_menu)
                    setOnMenuItemClickListener { item ->
                        when(item.itemId){
                            R.id.remove ->{
                                listener.onRemove(post)
                                true
                            }
                            R.id.edit ->{
                                listener.onEdit(post)
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
}
class PostDiffItemCallback : DiffUtil.ItemCallback<Post>(){
    override fun areItemsTheSame(
        oldItem: Post,
        newItem: Post):
            Boolean = oldItem.id == newItem.id

    override fun areContentsTheSame(
        oldItem: Post,
        newItem: Post):
            Boolean = oldItem == newItem

    override fun getChangePayload(
        oldItem: Post,
        newItem: Post):
            Any = Unit

    }




