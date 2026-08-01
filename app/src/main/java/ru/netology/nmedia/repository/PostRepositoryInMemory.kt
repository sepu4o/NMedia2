package ru.netology.nmedia.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import ru.netology.nmedia.R
import ru.netology.nmedia.dto.Post
import kotlin.dec
import kotlin.inc




class PostRepositoryInMemory : PostRepository {
    var posts = List(100){
        Post(
        id = it.toLong()+1,
        author = "Нетология. Университет интернет-профессий будущего",
        published = "21 мая в 18:36",
        content = "$it Привет, это новая Нетология! Когда-то Нетология начиналась с интенсивов по онлайн-маркетингу. Затем появились курсы по дизайну, разработке, аналитике и управлению. Мы растём сами и помогаем расти студентам: от новичков до уверенных профессионалов. Но самое важное остаётся с нами: мы верим, что в каждом уже есть сила, которая заставляет хотеть больше, целиться выше, бежать быстрее. Наша миссия — помочь встать на путь роста и начать цепочку перемен → http://netolo.gy/fyb",
        likes = 999,
        likeByMe = false,
        share = 999,
        view = 2500
    )
    }
        .reversed()
    private val data = MutableLiveData(posts)

    override fun get(): LiveData<List<Post>> = data


    override fun likeById(id: Long) {
        posts = posts.map { post ->
            if (post.id == id) {
                post.copy(
                    likeByMe = !post.likeByMe,
                    likes = if (post.likeByMe) {
                        post.likes - 1
                    } else {
                        post.likes + 1
                    }
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




}