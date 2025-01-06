package com.example.diceroller

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
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

@Preview
@Composable
fun DiceRollerApp() {
    DiceGameWithTargetNumber(
        modifier = Modifier
            .fillMaxSize()
            .wrapContentSize(Alignment.Center)
    )
}

@Composable
fun DiceGameWithTargetNumber(modifier: Modifier = Modifier) {
    var result1 by remember { mutableStateOf(1) }
    var result2 by remember { mutableStateOf(1) }
    var targetNumber by remember { mutableStateOf<Int?>(null) }
    var message by remember { mutableStateOf("") }

    val imageResource1 = getDiceImage(result1)
    val imageResource2 = getDiceImage(result2)

    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Champ de saisie du nombre cible
        Text(text = "Choisissez un nombre cible :", fontSize = 20.sp)
        Spacer(modifier = Modifier.height(8.dp))
        TargetNumberInput(
            targetNumber = targetNumber,
            onValueChange = { value -> targetNumber = value }
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Affichage des dés
        Row(
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                painter = painterResource(imageResource1),
                contentDescription = result1.toString()
            )
            Image(
                painter = painterResource(imageResource2),
                contentDescription = result2.toString()
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Bouton pour lancer les dés
        Button(
            onClick = {
                result1 = (1..6).random()
                result2 = (1..6).random()
                val sum = result1 + result2
                message = if (sum == targetNumber) {
                    "Bravo ! La somme des dés ($sum) correspond au nombre cible ! 🎉"
                } else {
                    "Dommage ! La somme des dés ($sum) ne correspond pas au nombre cible ($targetNumber)."
                }
            },
            enabled = targetNumber != null // Bouton activé seulement si un nombre cible est défini
        ) {
            Text(text = "Lancer les dés", fontSize = 24.sp)
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Message de résultat
        Text(text = message, fontSize = 20.sp)
    }
}

@Composable
fun TargetNumberInput(
    targetNumber: Int?,
    onValueChange: (Int?) -> Unit
) {
    var inputText by remember { mutableStateOf("") }

    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        OutlinedTextField(
            value = inputText,
            onValueChange = { newValue ->
                inputText = newValue
                onValueChange(newValue.toIntOrNull()) // Convertit en entier ou null si invalide
            },
            label = { Text("Nombre cible") },
            singleLine = true
        )
    }
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
