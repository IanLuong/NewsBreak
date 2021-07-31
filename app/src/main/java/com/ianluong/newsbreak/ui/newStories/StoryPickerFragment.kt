package com.ianluong.newsbreak.ui.newStories

import android.app.AlertDialog
import android.app.Dialog
import android.content.DialogInterface
import android.os.Bundle
import android.widget.EditText
import androidx.fragment.app.DialogFragment
import com.ianluong.newsbreak.R
import com.ianluong.newsbreak.api.Article
import com.ianluong.newsbreak.database.Story
import java.util.*

private const val ARG_ARTCILE = "article"

class StoryPickerFragment: DialogFragment() {

    companion object {
        fun newInstance(article: Article): StoryPickerFragment {
            val args = Bundle().apply {
                putSerializable(ARG_ARTCILE, article)
            }

            return StoryPickerFragment().apply {
                arguments = args
            }
        }
    }
    
    interface Callbacks {
        fun onStorySelected(article: Article, story: Story)
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val builder: AlertDialog.Builder = activity.let {
            AlertDialog.Builder(it)
        }
        val view = layoutInflater.inflate(R.layout.dialog_add_story, null)
        builder.setView(view)

        val article = arguments?.getSerializable(ARG_ARTCILE) as Article
        val editText = view.findViewById<EditText>(R.id.add_story)

        builder.setMessage("Add Story")
        builder.setPositiveButton("OK") { _, _ ->
            val story = Story().apply {
                title = editText.text.toString()
            }
            targetFragment?.let {fragment ->
                (fragment as Callbacks).onStorySelected(article, story)
            }
        }
        builder.setNegativeButton("Cancel") {_,_ -> }

        return builder.create()
    }
}