package com.cookandroid.kotlin_project

import android.os.Bundle
import android.os.PersistableBundle
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import com.google.android.material.navigation.NavigationView

class MainActivity_drawer : AppCompatActivity() {

    lateinit var drawerLayout: DrawerLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_drawer)

        val toolbar: Toolbar = findViewById(R.id.toolbar)
        val navigationView: NavigationView = findViewById(R.id.navigationView)
        drawerLayout = findViewById(R.id.drawerLayout)

        setSupportActionBar(toolbar)

        val actionBar: ActionBar? = supportActionBar

        actionBar?.setDisplayHomeAsUpEnabled(true)

        actionBar?.setHomeAsUpIndicator(R.drawable.ic_menu)

        navigationView.setNavigationItemSelectedListener(
            object: NavigationView.OnNavigationItemSelectedListener{
                override fun onNavigationItemSelected(item: MenuItem): Boolean {

                    when(item.itemId){
                        R.id.nav_camera -> {
                            item.isChecked = true
                            displayMessage("selected camera")
                            drawerLayout.closeDrawers()
                            return true
                        }
                        R.id.nav_photo -> {
                            item.isChecked = true
                            displayMessage("selected photo")
                            drawerLayout.closeDrawers()
                            return true
                        }
                        R.id.nav_slideShow -> {
                            item.isChecked = true
                            displayMessage("selected slideShow")
                            drawerLayout.closeDrawers()
                            return true
                        }

                        else -> {
                            return true
                        }
                    }
                }
            }
        )
    }

    private fun displayMessage(message: String){
        Toast.makeText(applicationContext, message, Toast.LENGTH_SHORT).show()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        when(item.itemId){
            android.R.id.home ->{
                drawerLayout.openDrawer(GravityCompat.START)
            }
        }
        return super.onOptionsItemSelected(item)
    }
}