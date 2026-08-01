package ru.netology.nmedia.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ActionMenuView
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import ru.netology.nmedia.databinding.CardPostBinding
import ru.netology.nmedia.dto.Post
import ru.netology.nmedia.formatCount
import androidx.recyclerview.widget.DiffUtil
import ru.netology.nmedia.R
import ru.netology.nmedia.viewmodel.PostViewModel

typealias LikeListener = (Post)-> Unit
typealias ShareListener = (Post) -> Unit


class PostsAdapter(private val likeClickListener: LikeListener,
                   private val shareClickListener: ShareListener):
    ListAdapter<Post, PostViewHolder>(PostDiffItemCallback()){

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): PostViewHolder = PostViewHolder(
        CardPostBinding.inflate(LayoutInflater.from(parent.context),parent,false),
    likeClickListener,shareClickListener)



    override fun onBindViewHolder(
        holder: PostViewHolder,
        position: Int
    ) {
        holder.bind(getItem(position))
    }

}

class PostViewHolder(private val binding : CardPostBinding,
    private val likeClickListener: LikeListener,
                     private val shareClickListener: ShareListener  ):
    RecyclerView.ViewHolder(binding.root){
    fun bind(post: Post) {
        with(binding) {
            author.text = post.author
            published.text = post.published
            content.text = post.content
            countLikes.text = formatCount(post.likes)
            countShare.text = formatCount(post.share)
            countView.text = formatCount(post.view)
            like.setImageResource(if (post.likeByMe) R.drawable.like_svgrepo_com__1_ else R.drawable.like_svgrepo_com)
            like.setOnClickListener {
                likeClickListener(post)
            }
            share.setOnClickListener {
                shareClickListener(post)
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




