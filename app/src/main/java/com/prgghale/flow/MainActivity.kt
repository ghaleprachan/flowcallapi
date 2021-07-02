package com.prgghale.flow

import android.os.Bundle
import android.widget.Button
import android.widget.ProgressBar
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.isVisible
import androidx.lifecycle.lifecycleScope
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.textfield.TextInputEditText
import com.prgghale.flow.ui.theme.FlowTheme
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flow

class MainActivity : AppCompatActivity() {

    private val mainViewModel by viewModels<MainViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val flow = flow {
            (1..10).forEach { data ->
                emit("Hello World $data")
                delay(1000L)
            }
        }
        val parent = findViewById<ConstraintLayout>(R.id.parent)
        val progress = findViewById<ProgressBar>(R.id.progress)
        val userName = findViewById<TextInputEditText>(R.id.tieEmail)
        val password = findViewById<TextInputEditText>(R.id.tiePassword)
        /*lifecycleScope.launch {
            flow.buffer()
                .filter { it == "Hello World 8" }.collect {
                    println(it)
                    delay(2000)
                }
        }

        lifecycleScope.launchWhenResumed {
            Toast.makeText(this@MainActivity, "This is on resume ", Toast.LENGTH_SHORT).show()
        }*/

        /*
        * TODO
        *  When we do launch than it will run even when application is in background
        *  If this runs when application is in background than it can cause application crash
        *  Therefore use launchWhenStarted */
        lifecycleScope.launchWhenStarted {
            mainViewModel.loginUiState.collect {
                when (it) {
                    is MainViewModel.LoginUiState.Success -> {
                        Snackbar.make(parent, "Logged in successfully", Snackbar.LENGTH_LONG).show()
                        progress.isVisible = false
                    }
                    is MainViewModel.LoginUiState.Loading -> {
                        progress.isVisible = true
                    }
                    is MainViewModel.LoginUiState.Error -> {
                        progress.isVisible = false
                        Snackbar.make(parent, it.message, Snackbar.LENGTH_LONG).show()
                    }
                    else -> {
                        progress.isVisible = false
                    }
                }
            }
        }
        findViewById<Button>(R.id.btnLogin).setOnClickListener {
            mainViewModel.login(
                username = userName.text.toString(),
                password = password.text.toString()
            )
        }
    }

    override fun onResume() {
        super.onResume()
        Toast.makeText(this, "This is system on resume", Toast.LENGTH_SHORT).show()
    }
}

@Composable
fun Greeting(name: String) {
    Text(text = "Hello $name!")
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    FlowTheme {
        Greeting("Android")
    }
}