package com.example.parstagram

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.parse.ParseUser

class LoginActivity : AppCompatActivity() {
    lateinit var username: String
    lateinit var password: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        // Check if the user is logged in
        // If there is, take them to MainActivity
        if (ParseUser.getCurrentUser() != null){
            goToMainActivity()
        }

        findViewById<Button>(R.id.btn_login).setOnClickListener{
            username= findViewById<EditText>(R.id.et_username).text.toString()
            password= findViewById<EditText>(R.id.et_password).text.toString()
            login(username,password)
        }

        findViewById<Button>(R.id.btn_signup).setOnClickListener{
            username= findViewById<EditText>(R.id.et_username).text.toString()
            password= findViewById<EditText>(R.id.et_password).text.toString()
            signUpUser(username,password)
        }
    }


    private fun signUpUser (username: String, password: String){
        //Check for empty input field
        if (username == "" || password == ""){
            Toast.makeText(this,"Either username or password field is empty", Toast.LENGTH_SHORT).show()
            return
        }
        // Create the ParseUser
        val user = ParseUser()

        // Set fields for the user to be created
        user.setUsername(username)
        user.setPassword(password)

        user.signUpInBackground { e ->
            if (e == null) {
                // Hooray! user has successfully logged in.
                Toast.makeText(this,"Account created successfully", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "Failed to signup", Toast.LENGTH_SHORT).show()
                e.printStackTrace()
            }
        }
    }
    private fun login(username: String, password: String){
        ParseUser.logInInBackground(username,password,({
            user, e ->
            if (user != null){
                Log.i(TAG, "Successfully logged in user")
                goToMainActivity()
            } else{
                e.printStackTrace()
                Toast.makeText(this, "Error logging in", Toast.LENGTH_SHORT).show()
            }
        }))
    }
    private fun goToMainActivity () {
        val intent = Intent(this@LoginActivity, MainActivity::class.java)
        startActivityForResult(intent, REQUEST_CODE)
    }

    // Checking if the user clicked logout button
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        Log.i(TAG,"result code $resultCode")
        if (resultCode == 0 && requestCode == REQUEST_CODE){
            finish()
        }
        super.onActivityResult(requestCode, resultCode, data)
    }
    companion object{
        const val TAG = "LoginActivity"
        const val REQUEST_CODE= 20 //Request value to send along the intent
    }
}