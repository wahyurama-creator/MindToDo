package com.id.mindtodo.ui.screen.task

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Alarm
import androidx.compose.material.icons.filled.Close
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.google.accompanist.pager.ExperimentalPagerApi
import com.id.mindtodo.R
import com.id.mindtodo.component.PriorityDropDown
import com.id.mindtodo.data.model.Priority
import com.id.mindtodo.receiver.AlarmReceiver
import com.id.mindtodo.ui.theme.PADDING_LG
import com.id.mindtodo.ui.theme.PADDING_SM
import com.id.mindtodo.ui.theme.topAppBarContentColor
import com.id.mindtodo.ui.viewmodel.SharedViewModel

@ExperimentalMaterialApi
@ExperimentalAnimationApi
@ExperimentalPagerApi
@Composable
fun TaskContent(
    title: String,
    onTitleChange: (String) -> Unit,
    description: String,
    onDescriptionChange: (String) -> Unit,
    priority: Priority,
    onPrioritySelected: (Priority) -> Unit,
    onReminderSelected: (String) -> Unit,
    sharedViewModel: SharedViewModel
) {
    val context = LocalContext.current
    var reminder by sharedViewModel.isReminder
    var dateTime by sharedViewModel.reminderAt

    Scaffold(
        content = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(MaterialTheme.colors.background)
                    .padding(PADDING_LG)
            ) {
                OutlinedTextField(
                    value = title,
                    onValueChange = { onTitleChange(it) },
                    modifier = Modifier.fillMaxWidth(),
                    label = {
                        Text(text = stringResource(id = R.string.title))
                    },
                    textStyle = MaterialTheme.typography.body1,
                    singleLine = true,
                    maxLines = 1
                )
                Spacer(modifier = Modifier.height(PADDING_SM))
                PriorityDropDown(priority = priority, onPrioritySelected = onPrioritySelected)
                OutlinedTextField(
                    value = description,
                    onValueChange = { onDescriptionChange(it) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .fillMaxHeight(fraction = 0.7f),
                    label = {
                        Text(text = stringResource(id = R.string.description))
                    },
                    textStyle = MaterialTheme.typography.body1,
                )

                if (sharedViewModel.tempIsReminder.value || sharedViewModel.isReminder.value) {
                    OutlinedTextField(
                        value = if (reminder)
                            sharedViewModel.reminderAt.value else sharedViewModel.tempReminderAt.value,
                        onValueChange = { onReminderSelected(it) },
                        modifier = Modifier
                            .fillMaxWidth(),
                        label = {
                            Text(text = stringResource(id = R.string.reminder))
                        },
                        textStyle = MaterialTheme.typography.body1,
                        readOnly = true,
                        trailingIcon = {
                            IconButton(
                                onClick = {
                                    dateTime = ""
                                    reminder = false

                                    sharedViewModel.tempIsReminder.value = false
                                    sharedViewModel.tempReminderAt.value = ""

                                    val alarmReceiver = AlarmReceiver()
                                    alarmReceiver.cancelAlarm(context)
                                }
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Close,
                                    contentDescription = stringResource(
                                        id = R.string.icon_close
                                    ),
                                    tint = MaterialTheme.colors.error
                                )
                            }
                        }
                    )
                }
            }
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    sharedViewModel.selectDateTime(context = context)
                },
                backgroundColor = MaterialTheme.colors.primary
            ) {
                Icon(
                    imageVector = Icons.Default.Alarm,
                    contentDescription = stringResource(id = R.string.icon_reminder),
                    tint = Color.White
                )
            }
        },
        isFloatingActionButtonDocked = true,
        bottomBar = {
            BottomAppBar(
                cutoutShape = MaterialTheme.shapes.small.copy(
                    CornerSize(percent = 50)
                ),
                contentPadding = PaddingValues(horizontal = 24.dp)
            ) {
                Text(
                    stringResource(id = R.string.reminder_task),
                    color = MaterialTheme.colors.topAppBarContentColor,
                )
            }
        }
    )
}

