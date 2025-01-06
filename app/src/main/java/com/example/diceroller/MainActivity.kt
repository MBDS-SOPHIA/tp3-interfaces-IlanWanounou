package com.example.diceroller

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.core.*
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import com.example.diceroller.ui.theme.DiceRollerTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            DiceRollerTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    DiceRollerApp()
                }
            }
        }
    }
}

@Composable
fun DiceRollerApp() {
    DiceGameWithAutoRoll(modifier = Modifier.fillMaxSize().wrapContentSize(Alignment.Center))
}

@Composable
fun DiceGameWithAutoRoll(modifier: Modifier = Modifier) {
    var result1 by remember { mutableStateOf(1) }
    var result2 by remember { mutableStateOf(1) }
    var targetNumber by remember { mutableStateOf<Int?>(null) }
    var showAnimation by remember { mutableStateOf(false) }

    val diceImage1 = getDiceImage(result1)
    val diceImage2 = getDiceImage(result2)

    val offsetAnimation = rememberInfiniteTransition()
    val offsetY by offsetAnimation.animateFloat(
        initialValue = 0f,
        targetValue = 30f,
        animationSpec = infiniteRepeatable(
            animation = tween(500, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        )
    )

    LaunchedEffect(targetNumber) {
        if (targetNumber != null) {
            result1 = (1..6).random()
            result2 = (1..6).random()

            if (result1 + result2 == targetNumber) {
                showAnimation = true
            } else {
                showAnimation = false
            }
        }
    }

    Box(modifier = modifier) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text("Choisissez un nombre cible :", fontSize = 20.sp)
            Spacer(modifier = Modifier.height(8.dp))

            TargetNumberInput(
                targetNumber = targetNumber,
                onValueChange = { value -> targetNumber = value }
            )

            Spacer(modifier = Modifier.height(16.dp))

            Row(
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Image(
                    painter = painterResource(diceImage1),
                    contentDescription = result1.toString(),
                    modifier = Modifier.offset(y = if (showAnimation) offsetY.dp else 0.dp)
                )
                Image(
                    painter = painterResource(diceImage2),
                    contentDescription = result2.toString(),
                    modifier = Modifier.offset(y = if (showAnimation) offsetY.dp else 0.dp)
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            val message = when {
                targetNumber == null -> "Veuillez définir un nombre cible."
                result1 + result2 == targetNumber -> "🎉 Bravo ! Vous avez gagné ! 🎉"
                else -> "Dommage, réessayez !"
            }
            Text(text = message, fontSize = 20.sp)
        }

        if (showAnimation) {
            ConfettiAnimation()
        }
    }
}

@Composable
fun TargetNumberInput(
    targetNumber: Int?,
    onValueChange: (Int?) -> Unit
) {
    var inputText by remember { mutableStateOf("") }

    OutlinedTextField(
        value = inputText,
        onValueChange = { newValue ->
            inputText = newValue
            onValueChange(newValue.toIntOrNull())
        },
        label = { Text("Nombre cible") },
        singleLine = true
    )
}

@Composable
fun getDiceImage(result: Int): Int {
    return when (result) {
        1 -> R.drawable.dice_1
        2 -> R.drawable.dice_2
        3 -> R.drawable.dice_3
        4 -> R.drawable.dice_4
        5 -> R.drawable.dice_5
        else -> R.drawable.dice_6
    }
}

@Composable
fun ConfettiAnimation() {
    val infiniteTransition = rememberInfiniteTransition()
    val alpha by infiniteTransition.animateFloat(
        initialValue = 1f,
        targetValue = 0f,
        animationSpec = infiniteRepeatable(
            animation = tween(1000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        )
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .zIndex(1f),
        contentAlignment = Alignment.Center
    ) {
        repeat(50) {
            Box(
                modifier = Modifier
                    .size((5..15).random().dp)
                    .offset(
                        x = (-150..150).random().dp,
                        y = (-300..300).random().dp
                    )
                    .alpha(alpha)
                    .background(
                        color = Color(
                            red = (100..255).random(),
                            green = (100..255).random(),
                            blue = (100..255).random()
                        )
                    )
            )
        }
    }
}

