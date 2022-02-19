package com.id.mindtodo.component

import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import com.id.mindtodo.R

@Composable
fun DisplayAlertDialog(
    title: String,
    message: String,
    isOpenDialog: Boolean,
    onCloseDialog: () -> Unit,
    onConfirmDialog: () -> Unit
) {
    if (isOpenDialog) {
        AlertDialog(
            title = {
                Text(
                    text = title,
                    fontSize = MaterialTheme.typography.h5.fontSize,
                    fontWeight = FontWeight.Bold
                )
            },
            text = {
                Text(
                    text = message,
                    fontSize = MaterialTheme.typography.subtitle1.fontSize,
                    fontWeight = FontWeight.Normal
                )
            },
            onDismissRequest = {
                onCloseDialog()
            },
            confirmButton = {
                Button(
                    onClick = {
                        onConfirmDialog()
                        onCloseDialog()
                    },
                    colors = ButtonDefaults.buttonColors(
                        MaterialTheme.colors.error
                    )
                ) {
                    Text(
                        text = stringResource(id = R.string.text_yes),
                        color = MaterialTheme.colors.surface
                    )
                }
            },
            dismissButton = {
                OutlinedButton(
                    onClick = {
                        onCloseDialog()
                    },
                ) {
                    Text(text = stringResource(id = R.string.text_no))
                }
            }
        )
    }
}