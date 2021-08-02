package com.ianluong.newsbreak.ui.newStories

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import android.widget.EditText
import androidx.fragment.app.DialogFragment
import com.ianluong.newsbreak.R
import com.ianluong.newsbreak.api.Article
import com.ianluong.newsbreak.database.Story

private const val ARG_ARTICLE = "article"

class StoryAddFragment: DialogFragment() {

    companion object {
        fun newInstance(article: Article): StoryAddFragment {
            val args = Bundle().apply {
                putSerializable(ARG_ARTICLE, article)
            }

            return StoryAddFragment().apply {
                arguments = args
            }
        }
    }
    
    interface Callbacks {
        fun onStoryAdded(article: Article, story: Story)
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val builder: AlertDialog.Builder = activity.let {
            AlertDialog.Builder(it)
        }
        val view = layoutInflater.inflate(R.layout.dialog_add_story, null)
        builder.setView(view)

        val article = arguments?.getSerializable(ARG_ARTICLE) as Article
        val editText = view.findViewById<EditText>(R.id.add_story)

        builder.setMessage("Add Story")
        builder.setPositiveButton("OK") { _, _ ->
            val story = Story().apply {
                title = editText.text.toString()
            } //TODO Replace deprecated functions
            targetFragment?.let {fragment ->
                (fragment as Callbacks).onStoryAdded(article, story)
            }
        }
        builder.setNegativeButton("Cancel") {_,_ -> }

        return builder.create()
    }
}