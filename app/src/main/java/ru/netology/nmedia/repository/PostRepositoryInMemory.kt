package ru.netology.nmedia.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import ru.netology.nmedia.dto.Post

class PostRepositoryInMemory : PostRepository {

    private var nextId = 101L

    private var posts = List(100) {
        Post(
            id = it.toLong() + 1,
            author = "Нетология. Университет интернет-профессий будущего",
            published = "21 мая в 18:36",
            content = "$it Привет, это новая Нетология!...",
            likes = 999,
            likedByMe = false,
            share = 999,
            view = 2500
        )
    }.reversed()

    private val data = MutableLiveData(posts)

    override fun get(): LiveData<List<Post>> = data

    override fun likeById(id: Long) {
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

    override fun shareById(id: Long) {
        posts = posts.map { post ->
            if (post.id == id) {
                post.copy(share = post.share + 1)
            } else {
                post
            }
        }
        data.value = posts
    }

    override fun removeById(id: Long) {
        posts = posts.filterNot { it.id == id }
        data.value = posts
    }

    override fun save(post: Post) {
        if (post.id == 0L) {
            // Новый пост
            posts = listOf(
                post.copy(
                    id = nextId++,        // ← уникальный id
                    author = "Me",
                    likes = 0,
                    likedByMe = false,
                    published = "Now"
                )
            ) + posts
        } else {
            // Редактирование
            posts = posts.map {      // ← присваиваем результат!
                if (it.id == post.id) {
                    it.copy(content = post.content)
                } else {
                    it
                }
            }
        }
        data.value = posts
    }
}