package ru.netology.nmedia.activity

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import ru.netology.nmedia.R
import ru.netology.nmedia.adapter.PostListener
import ru.netology.nmedia.adapter.PostsAdapter
import ru.netology.nmedia.databinding.ActivityMainBinding
import ru.netology.nmedia.dto.Post
import ru.netology.nmedia.util.AndroidUtils
import ru.netology.nmedia.viewmodel.PostViewModel


class MainActivity : AppCompatActivity() {

    private val viewModel: PostViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        val binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(binding.root) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            val imeInsets = insets.getInsets(WindowInsetsCompat.Type.ime())
            val isImeVisible = insets.isVisible(WindowInsetsCompat.Type.ime())
            v.setPadding(
                v.paddingLeft,
                systemBars.top,
                v.paddingRight,
                if (isImeVisible) imeInsets.bottom else systemBars.bottom
            )
            insets
        }

        // ==================== ADAPTER ====================
        val adapter = PostsAdapter(object : PostListener {
            override fun onlike(post: Post) {
                viewModel.likeById(post.id)
            }

            override fun onEdit(post: Post) {
                viewModel.edit(post)
            }

            override fun onRemove(post: Post) {
                viewModel.removeById(post.id)
            }

            override fun onShare(post: Post) {
                viewModel.shareById(post.id)
            }
        })

        binding.list.adapter = adapter

        // ==================== ПОДПИСКА НА СПИСОК ====================
        viewModel.data.observe(this) { posts ->
            val isNewPost = posts.size > adapter.currentList.size
            adapter.submitList(posts) {
                if (isNewPost)
                    binding.list.smoothScrollToPosition(0)
            }
        }

        // ==================== ПОДПИСКА НА РЕДАКТИРОВАНИЕ ====================
        viewModel.edited.observe(this) { edited ->
            if (edited.id != 0L) {
                // Режим редактирования
                binding.editingGroup.visibility = View.VISIBLE
                binding.editingContent.text = edited.content
                binding.content.setText(edited.content)
                AndroidUtils.showKeyboard(binding.content)
            } else {
                // Обычный режим
                binding.editingGroup.visibility = View.GONE
                binding.content.setText("")
                binding.content.clearFocus()
                AndroidUtils.hideKeyboard(binding.content)
            }
        }

        // ==================== КНОПКА СОХРАНЕНИЯ ====================
        binding.save.setOnClickListener {
            val text = binding.content.text.toString()
            if (text.isBlank()) {
                Toast.makeText(this, R.string.error_empty_text, Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            viewModel.save(text)
            // НЕ очищаем поле вручную — observer сделает это
        }

        // ==================== КНОПКА ОТМЕНЫ ====================
        binding.cancelButton.setOnClickListener {
            viewModel.cancelEdit()
            // observer скроет панель и очистит поле
        }
    }
}