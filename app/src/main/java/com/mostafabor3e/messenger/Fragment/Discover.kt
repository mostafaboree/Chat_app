package com.mostafabor3e.messenger.Fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.mostafabor3e.messenger.MainActivity
import com.mostafabor3e.messenger.R
import org.w3c.dom.Text


class Discover : Fragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val texttitle=activity?.findViewById<TextView>(R.id.title_activ)
        texttitle?.text="Discover"


        return inflater.inflate(R.layout.fragment_discover, container, false)
    }



}