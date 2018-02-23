package org.attendr.classes

import android.os.Bundle
import android.os.PersistableBundle
import android.support.v7.app.AppCompatActivity
import org.attendr.R

/**
 * @author Pauldg7@gmail.com (Paul Gillis)
 */
class ClassDetailsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?, persistentState: PersistableBundle?) {
        super.onCreate(savedInstanceState, persistentState)
        setContentView(R.layout.activity_classes)
    }
}