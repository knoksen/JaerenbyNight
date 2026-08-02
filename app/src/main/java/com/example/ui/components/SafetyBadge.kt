package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.MidnightSurfaceVariant
import com.example.ui.theme.SafeGreen
import com.example.ui.theme.WarningAmber

@Composable
fun SafetyBadge(
    scorePercent: Int,
    lightLabel: String,
    modifier: Modifier = Modifier
) {
    val badgeColor = when {
        scorePercent >= 95 -> SafeGreen
        scorePercent >= 90 -> Color(0xFF00E5FF)
        else -> WarningAmber
    }

    Row(
        modifier = modifier
            .background(MidnightSurfaceVariant, RoundedCornerShape(20.dp))
            .padding(horizontal = 10.dp, vertical = 4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = Icons.Default.Shield,
            contentDescription = "Safety Score",
            tint = badgeColor,
            modifier = Modifier.size(14.dp)
        )
        Spacer(modifier = Modifier.width(4.dp))
        Text(
            text = "$scorePercent% Safe",
            color = badgeColor,
            fontSize = 11.sp,
            fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier.width(6.dp))
        Text(
            text = "• $lightLabel",
            color = Color(0xFF9EAECB),
            fontSize = 11.sp,
            fontWeight = FontWeight.Normal
        )
    }
}
