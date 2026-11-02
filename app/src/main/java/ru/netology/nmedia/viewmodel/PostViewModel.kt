package ru.netology.nmedia.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import ru.netology.nmedia.dto.Post
import ru.netology.nmedia.repository.PostRepository
import ru.netology.nmedia.repository.PostRepositoryFileImpl


class PostViewModel(application: Application) : AndroidViewModel(application) {

    private val emptyPost = Post()

    private val repository: PostRepository = PostRepositoryFileImpl(application)

    val data: LiveData<List<Post>> = repository.get()

    val edited = MutableLiveData(emptyPost)

    fun likeById(id: Long) = repository.likeById(id)

    fun shareById(id: Long) = repository.shareById(id)

    fun removeById(id: Long) = repository.removeById(id)

    fun save(content: String) {
        val trimmed = content.trim()
        if (trimmed.isNotBlank()) {
            repository.save(Post(content = trimmed))
        }
    }

    fun update(id: Long, content: String) {
        val trimmed = content.trim()
        if (trimmed.isNotBlank()) {
            repository.update(id, trimmed)
        }
    }
}