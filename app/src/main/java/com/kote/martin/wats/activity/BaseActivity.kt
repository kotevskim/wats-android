package com.kote.martin.wats.activity

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.support.v7.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.inputmethod.InputMethodManager
import com.kote.martin.wats.R

open class BaseActivity : AppCompatActivity() {

    private var optionsMenu: Menu? = null
    private val REQUEST_CODE_LOGIN_ACTIVITY: Int = 11

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.options_menu, menu)
        this.optionsMenu = menu
        if (isUserLoggedIn()) {
            updateToLoggedInState()
        }
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.option_login_logout -> {
                if (isUserLoggedIn()) {
                    logout()
                } else {
                    val i = Intent(this, LoginActivity::class.java)
                    startActivityForResult(i, REQUEST_CODE_LOGIN_ACTIVITY)
                }
                true
            }
            R.id.option_settings -> {
                // TODO open setting activity
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (Integer.compare(requestCode, REQUEST_CODE_LOGIN_ACTIVITY) == 0 &&
                Integer.compare(resultCode, Activity.RESULT_OK) == 0) {
            if (isUserLoggedIn()) { // always true
                updateToLoggedInState()
            }
        }
    }

    fun isNetworkAvailable(): Boolean {
        val connectivityManager = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetworkInfo = connectivityManager.activeNetworkInfo
        return activeNetworkInfo != null && activeNetworkInfo.isConnected
    }

    fun isUserLoggedIn(): Boolean {
        val sharedPref = getSharedPreferences(getString(R.string.preference_file_key), Context.MODE_PRIVATE)
        return sharedPref.getString(
                getString(R.string.preference_jwt),
                getString(R.string.not_available)
        ) != getString(R.string.not_available)
    }

    fun showSoftKeyboard(view: View) {
        view.requestFocus()
        val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.showSoftInput(view, InputMethodManager.SHOW_IMPLICIT)
    }

    fun hideSoftKeyboard(view: View) {
        val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(view.windowToken, 0);
    }

    private fun updateToLoggedInState() {
        val sharedPref = getSharedPreferences(getString(R.string.preference_file_key), Context.MODE_PRIVATE)

        val menuItemPresenceStatus = this.optionsMenu?.findItem(R.id.option_login_logout)
        menuItemPresenceStatus?.title = "Log out"

        val loggedUsername = sharedPref.getString(
                getString(R.string.preference_logged_username),
                getString(R.string.not_available))

        val menuItemUser = this.optionsMenu?.findItem(R.id.option_user)
        menuItemUser?.title = loggedUsername
    }

    private fun updateToLoggedOutState() {
        this.optionsMenu?.findItem(R.id.option_user)?.title = ""
        this.optionsMenu?.findItem(R.id.option_login_logout)?.title = "Log in"
    }

    private fun logout() {
        val sharedPref = getSharedPreferences(getString(R.string.preference_file_key), Context.MODE_PRIVATE)
        sharedPref.edit()
                .remove(getString(R.string.preference_jwt))
                .remove(getString(R.string.preference_logged_username))
                .remove(getString(R.string.preference_logged_name))
                .remove(getString(R.string.preference_logged_email))
                .remove(getString(R.string.preference_logged_picture_url))
                .remove(getString(R.string.preference_logged_id))
                .apply()
        updateToLoggedOutState()
    }
}