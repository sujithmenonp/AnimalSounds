package com.example.animalsounds

import android.content.res.Configuration
import android.media.MediaPlayer
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.Crossfade
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.animalsounds.ui.theme.AnimalSoundsTheme

data class Animal(val name: String, val imageRes: Int, val soundRes: Int)

class MainActivity : ComponentActivity() {
    private var mediaPlayer: MediaPlayer? = null

    private val animals = listOf(
        Animal("Tiger", R.drawable.tiger, R.raw.tiger),
        Animal("Elephant", R.drawable.elephant, R.raw.elephant),
        Animal("Dog", R.drawable.dog, R.raw.dog)
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AnimalSoundsTheme {
                AnimalScreen(
                    animals = animals,
                    playSound = { soundRes -> playSound(soundRes) }
                )
            }
        }
    }

    private fun playSound(soundRes: Int) {
        // Stop and reset the MediaPlayer if it is already playing
        if (mediaPlayer?.isPlaying == true) {
            mediaPlayer?.stop()
            mediaPlayer?.reset()
        }

        // Create a new MediaPlayer instance or reuse an existing one
        mediaPlayer = MediaPlayer.create(this, soundRes)
        mediaPlayer?.start()

        // Optionally show a toast
        Toast.makeText(this, "Playing sound...", Toast.LENGTH_SHORT).show()

        // Release MediaPlayer when the sound finishes
        mediaPlayer?.setOnCompletionListener {
            it.release()
            mediaPlayer = null
        }
    }
}

@Composable
fun AnimalScreen(animals: List<Animal>, playSound: (Int) -> Unit) {
    var currentIndex by remember { mutableStateOf(0) }
    val currentAnimal = animals[currentIndex]

    // Detect orientation
    val configuration = LocalConfiguration.current
    val isPortrait = configuration.orientation == Configuration.ORIENTATION_PORTRAIT

    Scaffold {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Gray)
                .padding(it),
            contentAlignment = Alignment.Center
        ) {
            if (isPortrait) {
                // Portrait layout
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Crossfade(targetState = currentAnimal) { animal ->
                        AnimalDisplay(
                            animal = animal,
                            onClick = { playSound(animal.soundRes) }
                        )
                    }
                    Spacer(modifier = Modifier.height(16.dp))
                    Button(onClick = {
                        currentIndex = (currentIndex + 1) % animals.size
                    }) {
                        Text("Next Animal")
                    }
                }
            } else {
                // Landscape layout
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceAround
                ) {
                    Crossfade(targetState = currentAnimal) { animal ->
                        AnimalDisplay(
                            animal = animal,
                            onClick = { playSound(animal.soundRes) }
                        )
                    }
                    Button(onClick = {
                        currentIndex = (currentIndex + 1) % animals.size
                    }) {
                        Text("Next Animal")
                    }
                }
            }
        }
    }
}


@Composable
fun AnimalDisplay(animal: Animal, onClick: () -> Unit) {
    Box(
        modifier = Modifier
            .size(300.dp, 450.dp)
            .clickable(onClick = onClick)
            .animateContentSize(),
        contentAlignment = Alignment.Center
    ) {
        Image(
            painter = painterResource(id = animal.imageRes),
            contentDescription = "Play ${animal.name} Sound",
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )
    }
}

@Preview(showBackground = true)
@Composable
fun AnimalScreenPreview() {
    val sampleAnimals = listOf(
        Animal("Tiger", R.drawable.tiger, R.raw.tiger),
        Animal("Elephant", R.drawable.elephant, R.raw.elephant),
        Animal("Dog", R.drawable.dog, R.raw.dog)
    )

    AnimalSoundsTheme {
        AnimalScreen(
            animals = sampleAnimals,
            playSound = {}
        )
    }
}
