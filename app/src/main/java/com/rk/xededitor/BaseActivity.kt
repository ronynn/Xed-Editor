package com.rk.xededitor

import android.app.Activity
import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.collection.ArrayMap
import com.rk.xededitor.Settings.Keys
import com.rk.xededitor.Settings.SettingsData
import com.rk.xededitor.theme.ThemeManager
import java.lang.ref.WeakReference


abstract class BaseActivity : AppCompatActivity() {

  companion object {
    val activityMap = ArrayMap<Class<out BaseActivity>,WeakReference<Activity>>()
    
  }

  override fun onCreate(savedInstanceState: Bundle?) {
    ThemeManager.applyTheme(this)
    super.onCreate(savedInstanceState)

    activityMap[javaClass] = WeakReference(this)




    if (!SettingsData.isDarkMode(this)) {
      //light mode
      window.navigationBarColor = Color.parseColor("#FEF7FF")
      val decorView = window.decorView
      var flags = decorView.systemUiVisibility
      flags = flags or View.SYSTEM_UI_FLAG_LIGHT_NAVIGATION_BAR
      decorView.systemUiVisibility = flags
      window.statusBarColor = Color.parseColor("#FEF7FF")
    } else {
      window.statusBarColor = Color.parseColor("#141118")
    }
    if (SettingsData.isDarkMode(this) && SettingsData.isOled()) {
      val window = window
      window.navigationBarColor = Color.BLACK
      window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
      window.statusBarColor = Color.BLACK
    }


  }

  override fun onPause() {
    super.onPause()
    ThemeManager.applyTheme(this)
  }
}