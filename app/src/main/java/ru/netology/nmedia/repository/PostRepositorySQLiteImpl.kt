package ru.netology.nmedia.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import ru.netology.nmedia.db.PostDao
import ru.netology.nmedia.dto.Post

class PostRepositorySQLiteImpl(private val dao: PostDao) : PostRepository {

    private var posts = emptyList<Post>()
    private val data = MutableLiveData(posts)

    init {
        posts = dao.getAll()
        data.value = posts
    }

    override fun get(): LiveData<List<Post>> = data

    override fun save(post: Post) {
        val id = post.id
        val saved = dao.save(post)

        posts = if (id == 0L) {
            listOf(saved) + posts
        } else {
            posts.map { if (it.id != id) it else saved }
        }
        data.value = posts
    }

    override fun likeById(id: Long) {
        dao.likeById(id)
        posts = posts.map { post ->
            if (post.id == id) {
                post.copy(
                    likedByMe = !post.likedByMe,
                    likes = if (post.likedByMe) post.likes - 1 else post.likes + 1
                )
            } else {
                post
            }
        }
        data.value = posts
    }

    override fun shareById(id: Long) {   // ← добавить
        posts = posts.map { post ->
            if (post.id == id) post.copy(share = post.share + 1) else post
        }
        data.value = posts
    }

    override fun removeById(id: Long) {
        dao.removeById(id)
        posts = posts.filter { it.id != id }
        data.value = posts
    }

    override fun update(id: Long, content: String) {
        val post = posts.find { it.id == id } ?: return
        save(post.copy(content = content))
    }
}