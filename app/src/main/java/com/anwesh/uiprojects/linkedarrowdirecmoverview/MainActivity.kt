package com.anwesh.uiprojects.linkedarrowdirecmoverview

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.anwesh.uiprojects.ladmview.LADMView

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        LADMView.create(this)
    }
}
