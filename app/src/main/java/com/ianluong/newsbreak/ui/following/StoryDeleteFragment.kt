package com.ianluong.newsbreak.ui.following

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import androidx.fragment.app.DialogFragment
import com.ianluong.newsbreak.database.Story

private const val ARG_STORY = "story"

class StoryDeleteFragment: DialogFragment() {

    companion object {
        fun newInstance(story: Story): StoryDeleteFragment {
            val args = Bundle().apply {
                putSerializable(ARG_STORY, story)
            }

            return StoryDeleteFragment().apply {
                arguments = args
            }
        }
    }

    interface Callbacks {
        fun onStoryDeleted(story: Story)
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val builder: AlertDialog.Builder = activity.let {
            AlertDialog.Builder(it)
        }

        val story = arguments?.getSerializable(ARG_STORY) as Story

        builder.setTitle("Delete this story?")
        builder.setMessage("You will lose this story and all articles!")
        builder.setPositiveButton("Delete") { _, _ ->
            targetFragment?.let {fragment ->
                (fragment as Callbacks).onStoryDeleted(story)
            }
        }
        builder.setNegativeButton("Cancel") {_,_ -> }

        return builder.create()
    }
}