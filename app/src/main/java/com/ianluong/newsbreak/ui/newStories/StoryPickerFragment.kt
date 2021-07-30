package com.ianluong.newsbreak.ui.newStories

import android.app.AlertDialog
import android.app.Dialog
import android.content.DialogInterface
import android.os.Bundle
import androidx.fragment.app.DialogFragment

class StoryPickerFragment: DialogFragment() {

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val builder: AlertDialog.Builder = activity.let {
            AlertDialog.Builder(it)
        }
        builder.setMessage("THIS TANGO IS...")
            .setTitle("CHRIS EUBANK SAYS")
            .setPositiveButton("BIG") { _,_ -> }

        return builder.create()

    }
}