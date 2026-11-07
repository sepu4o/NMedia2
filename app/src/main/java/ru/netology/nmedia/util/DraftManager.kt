package ru.netology.nmedia.util


import android.content.Context

object DraftManager {
    private const val PREFS_NAME = "draft_prefs"
    private const val KEY_DRAFT = "draft_text"

    fun saveDraft(context: Context, text: String) {
        val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        prefs.edit().putString(KEY_DRAFT, text).apply()
    }

    fun getDraft(context: Context): String? {
        val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        return prefs.getString(KEY_DRAFT, null)
    }

    fun clearDraft(context: Context) {
        val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        prefs.edit().remove(KEY_DRAFT).apply()
    }
}