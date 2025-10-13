package com.marsof.bertra.ui.elements

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.unit.dp
import com.marsof.bertra.R
import com.marsof.bertra.ui.theme.LocalCustomColors

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TimerControlButton(
    onClickMethod: () -> Unit,
    text: String,
    icon: ImageVector,
    contentDescription: String? = null,
    modifier: Modifier,
) {
    val iconContentDescription = contentDescription ?: text
    Button(
        onClick = { onClickMethod() },
        modifier = modifier
            .height(dimensionResource(R.dimen.button_height) * 1)
            .fillMaxWidth(0.5f),
        shape = RoundedCornerShape(0.dp),
        colors = ButtonColors(
            containerColor = LocalCustomColors.current.blueButton,
            contentColor = LocalCustomColors.current.textTertiary,
            disabledContainerColor = Color.Magenta,
            disabledContentColor = Color.Magenta,
        ),
    ) {
        Icon(icon, iconContentDescription)
        Text(text)
    }
}