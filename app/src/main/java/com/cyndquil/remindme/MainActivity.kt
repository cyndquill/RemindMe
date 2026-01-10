package com.cyndquil.remindme

import android.os.Bundle
import com.cyndquil.remindme.ui.theme.RemindMeTheme
import com.cyndquil.remindme.ui.TopBar
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            RemindMeTheme {
                TopBar()
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun RemindMePreview() {
    RemindMeTheme {
        TopBar()
    }
}
