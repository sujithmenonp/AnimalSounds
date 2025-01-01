package com.example.animalsounds

import android.media.MediaPlayer
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Snackbar

import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.animalsounds.ui.theme.AnimalSoundsTheme

class MainActivity : ComponentActivity() {
    private var mediaPlayer: MediaPlayer? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            AnimalSoundsTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(innerPadding),
                        contentAlignment = Alignment.Center // Centers the content inside the Box
                    ) {
                        ImageButtonExample(
                            onClick = {
                                playSound()
                            }
                        )
                    }
                }
            }
        }
    }

     // Declare MediaPlayer as a class-level variable

    private fun playSound() {
        // Check if the MediaPlayer is already playing
        if (mediaPlayer?.isPlaying == true) {
            mediaPlayer?.stop() // Stop if it's already playing
            mediaPlayer?.reset() // Reset to prepare it for another sound
        }

        // Create a new MediaPlayer instance or reset the existing one
        mediaPlayer = MediaPlayer.create(this, R.raw.tiger) // Replace with your MP3 resource

        // Start playing the sound
        mediaPlayer?.start();

        // Optionally show a toast
        Toast.makeText(this, "Sound played!", Toast.LENGTH_SHORT).show()

        // Release the MediaPlayer when the sound finishes playing
        mediaPlayer?.setOnCompletionListener {
            it.release() // Release the MediaPlayer when the sound finishes playing
            mediaPlayer = null // Reset the mediaPlayer reference
        }
    }

}

@Composable
fun ImageButtonExample(modifier: Modifier = Modifier, onClick: () -> Unit) {
    var isPressed by remember { mutableStateOf(false) }

    Box(
        modifier = modifier
            .size(400.dp)
            .clickable(onClick = {
                onClick()
                isPressed = true
            }) // Trigger onClick on button click
            .then(Modifier.animateContentSize()) // Animate size change when clicked
    ) {
        Image(
            painter = painterResource(id = R.drawable.tiger), // Replace with your image
            contentDescription = "Play Tiger Sound",
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )

        // Change button appearance when pressed
        if (isPressed) {
            // Apply a visual effect like scale or change opacity
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ImageButtonExamplePreview() {
    AnimalSoundsTheme {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            ImageButtonExample(onClick = {})
        }
    }
}
