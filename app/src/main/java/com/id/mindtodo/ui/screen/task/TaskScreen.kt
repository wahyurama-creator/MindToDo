package com.id.mindtodo.ui.screen.task

import android.content.Context
import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalContext
import com.google.accompanist.pager.ExperimentalPagerApi
import com.id.mindtodo.R
import com.id.mindtodo.data.model.ToDoTask
import com.id.mindtodo.receiver.AlarmReceiver
import com.id.mindtodo.receiver.AlarmReceiver.Companion.TYPE_ONE_TIME
import com.id.mindtodo.ui.util.Action
import com.id.mindtodo.ui.viewmodel.SharedViewModel

@ExperimentalAnimationApi
@ExperimentalMaterialApi
@ExperimentalPagerApi
@Composable
fun TaskScreen(
    selectedTask: ToDoTask?,
    sharedViewModel: SharedViewModel,
    navigateToListScreen: (Action) -> Unit,
) {
    val title by sharedViewModel.title
    val description by sharedViewModel.description
    val priority by sharedViewModel.priority
    val context = LocalContext.current

    // Temp variable
    val tempIsReminder by sharedViewModel.tempIsReminder
    val tempReminderAt by sharedViewModel.tempReminderAt
    val tempDate by sharedViewModel.tempDate
    val tempTime by sharedViewModel.tempTime
    val alarmReceiver = AlarmReceiver()

//    OnBackHandler(onBackPressed = { navigateToListScreen(Action.NO_ACTION) })
    BackHandler {
        navigateToListScreen(Action.NO_ACTION)
    }

    Scaffold(
        topBar = {
            TaskAppBar(
                selectedTask = selectedTask,
                navigateToListScreen = {
                    if (it == Action.NO_ACTION) {
                        navigateToListScreen(it)
                    } else {
                        if (sharedViewModel.validateFields()) {
                            navigateToListScreen(it)

                            if (tempIsReminder) {
                                sharedViewModel.isReminder.value = tempIsReminder
                                sharedViewModel.reminderAt.value = tempReminderAt

                                alarmReceiver.setOneTimeAlarm(
                                    context = context,
                                    type = TYPE_ONE_TIME,
                                    date = tempDate,
                                    time = tempTime,
                                    message = "Don't forget! You have task to do now",
                                    title = title
                                )
                            }
                        } else {
                            displayToast(context = context)
                        }
                    }
                }
            )
        },
        content = {
            TaskContent(
                title = title,
                onTitleChange = {
                    sharedViewModel.updateTitle(it)
                },
                description = description,
                onDescriptionChange = {
                    sharedViewModel.description.value = it
                },
                priority = priority,
                onPrioritySelected = {
                    sharedViewModel.priority.value = it
                },
                onReminderSelected = {
                    sharedViewModel.reminderAt.value = it
                },
                sharedViewModel = sharedViewModel
            )
        }
    )
}

fun displayToast(context: Context) {
    Toast.makeText(context, context.getString(R.string.text_validate_error), Toast.LENGTH_SHORT)
        .show()
}

//@Composable
//fun OnBackHandler(
//    backDispatcher: OnBackPressedDispatcher? = LocalOnBackPressedDispatcherOwner.current?.onBackPressedDispatcher,
//    onBackPressed: () -> Unit
//) {
//    val currentOnBackPressed by rememberUpdatedState(newValue = onBackPressed)
//
//    val backCallback = remember {
//        object : OnBackPressedCallback(true) {
//            override fun handleOnBackPressed() {
//                currentOnBackPressed()
//            }
//        }
//    }
//
//    DisposableEffect(key1 = backDispatcher) {
//        backDispatcher?.addCallback(backCallback)
//
//        onDispose {
//            backCallback.remove()
//        }
//    }
//}