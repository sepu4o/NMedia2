package ru.netology.nmedia.activity


import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContract
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import ru.netology.nmedia.dto.Post
import ru.netology.nmedia.databinding.ActivityNewPostBinding


class NewPostActivity : AppCompatActivity() {

    private lateinit var binding: ActivityNewPostBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        binding = ActivityNewPostBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(binding.root) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(
                v.paddingLeft,
                systemBars.top,
                v.paddingRight,
                systemBars.bottom
            )
            insets
        }

        // ← НОВОЕ: принимаем текст и id из Intent
        val postText = intent.getStringExtra(KEY_POST_TEXT)
        val postId = intent.getLongExtra(KEY_POST_ID, 0L)

        // ← НОВОЕ: если текст есть — заполняем поле
        if (postText != null) {
            binding.edit.setText(postText)
        }

        binding.ok.setOnClickListener {
            val text = binding.edit.text.toString()
            if (text.isBlank()) {
                setResult(RESULT_CANCELED)
            } else {
                // ← НОВОЕ: возвращаем текст и id
                val intent = Intent().apply {
                    putExtra(KEY_POST_TEXT, text)
                    putExtra(KEY_POST_ID, postId)
                }
                setResult(RESULT_OK, intent)
            }
            finish()
        }
    }
        companion object {
            val KEY_POST_TEXT = "post_text"
            val KEY_POST_ID = "post_id"
        }

    object NewPostContract : ActivityResultContract<Post?, PostResult?>() {

        override fun createIntent(context: Context, input: Post?): Intent {
            return Intent(context, NewPostActivity::class.java).apply {
                if (input != null) {
                    putExtra(KEY_POST_TEXT, input.content)
                    putExtra(KEY_POST_ID, input.id)
                }
            }
        }

        override fun parseResult(resultCode: Int, intent: Intent?): PostResult? {
            if (resultCode != RESULT_OK || intent == null) return null

            val text = intent.getStringExtra(KEY_POST_TEXT) ?: return null
            val id = intent.getLongExtra(KEY_POST_ID, 0L)

            return PostResult(text, id)
        }
    }
}