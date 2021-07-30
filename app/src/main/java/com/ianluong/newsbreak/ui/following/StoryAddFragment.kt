package com.ianluong.newsbreak.ui.following

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import androidx.fragment.app.DialogFragment
import com.ianluong.newsbreak.R

class StoryAddFragment: DialogFragment() {
        override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
            val builder: AlertDialog.Builder = activity.let {
                AlertDialog.Builder(it)
            }
            val inflater = layoutInflater

            builder.setView(inflater.inflate(R.layout.dialog_add_story, null))
                .setMessage("Add Story")
                .setPositiveButton("Add") { _,_ -> }
                .setNegativeButton("Cancel") {_,_ ->}
            return builder.create()

        }
}