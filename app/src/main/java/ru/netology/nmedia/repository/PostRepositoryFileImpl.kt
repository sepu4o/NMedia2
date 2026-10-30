package ru.netology.nmedia.repository

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.lang.reflect.Type
import ru.netology.nmedia.dto.Post


class PostRepositoryFileImpl(private val context: Context) : PostRepository {
    private val prefs = context.getSharedPreferences("data", Context.MODE_PRIVATE)
    private var nextId = getId()
    private var posts: List<Post> = getPosts()
        set(value) {
            field = value
            sync()
        }

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
                post.copy(share = post.share + 1 )
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

    override fun update(id: Long, content: String) {
        posts = posts.map { post ->
            if (post.id == id) {
                post.copy(content = content)
            } else {
                post
            }
        }
        data.value = posts
    }

    private fun getPosts(): List<Post> = context.filesDir.resolve(FILE_NAME)
        .takeIf { it.exists() }  //проверка на наличие файла
        ?.inputStream()
        ?.bufferedReader()
        ?.use {
            gson.fromJson(it, postsType)
        }?: emptyList()

    private fun getId() = (posts.maxByOrNull { it.id }?.id ?: 0L) + 1L

    private fun sync() {
        context.filesDir.resolve(FILE_NAME)
            .outputStream()
            .bufferedWriter()
            .use{
                it.write(gson.toJson(posts))
            }
    }


    private companion object {
        const val FILE_NAME = "posts.json"
        val gson = Gson()
        val postsType: Type = object : TypeToken<List<Post>>() {}.type
    }
}