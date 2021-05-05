package com.mostafabor3e.messenger.Fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.mostafabor3e.messenger.R


class People : Fragment() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val texttitle=activity?.findViewById<TextView>(R.id.title_activ)
        texttitle?.text="People"
        return inflater.inflate(R.layout.fragment_people, container, false)
    }




}