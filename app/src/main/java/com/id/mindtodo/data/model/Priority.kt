package com.id.mindtodo.data.model

import androidx.compose.ui.graphics.Color
import com.id.mindtodo.ui.theme.HighPriorityColor
import com.id.mindtodo.ui.theme.LowPriorityColor
import com.id.mindtodo.ui.theme.MediumPriorityColor
import com.id.mindtodo.ui.theme.NonePriorityColor

enum class Priority(val color: Color) {
    HIGH(HighPriorityColor),
    MEDIUM(MediumPriorityColor),
    LOW(LowPriorityColor),
    NONE(NonePriorityColor)
}