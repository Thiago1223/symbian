package br.senai.sp.jandira.symbian.cadastro.components

import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun DefaultButton(
    onClick: () -> Unit,
    text: String
) {
    Button(
        onClick,
        modifier = Modifier
            .width(280.dp)
            .height(50.dp),
        colors = ButtonDefaults.buttonColors(Color(0xFF35225F))
    ) {
        Text(
            text = text,
            fontSize = 18.sp,
            fontWeight = FontWeight(600),
            color = Color.White
        )
    }

}