package ru.netology.nmedia

import android.os.Bundle
import android.widget.ImageButton
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import ru.netology.nmedia.databinding.ActivityMainBinding
import ru.netology.nmedia.dto.Post

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        val binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(binding.main) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(
                systemBars.left + v.paddingLeft,
                systemBars.top + v.paddingTop,
                systemBars.right + v.paddingRight,
                systemBars.bottom + v.paddingBottom
            )
            insets
        }
        val post = Post(
            id = 1,
            author = "Нетология. Университет интернет-профессий будущего",
            published = "21 мая в 18:36",
            content = "Привет, это новая Нетология! Когда-то Нетология начиналась с интенсивов по онлайн-маркетингу. Затем появились курсы по дизайну, разработке, аналитике и управлению. Мы растём сами и помогаем расти студентам: от новичков до уверенных профессионалов. Но самое важное остаётся с нами: мы верим, что в каждом уже есть сила, которая заставляет хотеть больше, целиться выше, бежать быстрее. Наша миссия — помочь встать на путь роста и начать цепочку перемен → http://netolo.gy/fyb",
            likes = 1000,
            likeByMe = false,
            share = 999,
            view = 2500
        )
        with(binding) {
            author.text = post.author
            published.text = post.published
            content.text = post.content
            countLikes.text = formatCount(post.likes)
            countShare.text = formatCount(post.share)
            countView.text = formatCount(post.view)
            root.setOnClickListener { println("Клик по root") }
            avatar.setOnClickListener { println("Клик по аватару") }
            like.setImageResource(if (post.likeByMe) R.drawable.like_svgrepo_com__1_ else R.drawable.like_svgrepo_com)
            like.setOnClickListener {
                if (post.likeByMe) post.likes-- else post.likes++
                post.likeByMe = !post.likeByMe
                println("клик по Like")
                like.setImageResource(if (post.likeByMe) R.drawable.like_svgrepo_com__1_ else R.drawable.like_svgrepo_com)
                countLikes.text = formatCount(post.likes)

            }
            share.setOnClickListener {
                post.share++
                countShare.text = formatCount(post.share)
            }
        }
    }
        fun formatCount(count: Int): String {
            return when {
                count < 1000 -> count.toString()
                count < 10_000 -> {
                    val thousands = count / 1000
                    val hundreds = (count % 1000) / 100
                    if (hundreds == 0) "${thousands}K"
                    else "${thousands}.${hundreds}K"
                }

                count < 1_000_000 -> {
                    val thousands = count / 1000
                    if (thousands < 10) "${thousands}K"
                    else "${thousands}K"
                }

                count < 1_000_000_000 -> {
                    val millions = count / 1_000_000
                    val hundredsThousands = (count % 1_000_000) / 100_000
                    if (hundredsThousands == 0) "${millions}M"
                    else "${millions}.${hundredsThousands}M"
                }

                else -> "${count / 1_000_000_000}B"
            }
        }
    }
