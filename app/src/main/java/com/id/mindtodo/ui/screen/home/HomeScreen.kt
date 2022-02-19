package com.id.mindtodo.ui.screen.home

import android.content.Context
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.runtime.*
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import com.id.mindtodo.R
import com.id.mindtodo.ui.theme.fabBackgroundColor
import com.id.mindtodo.ui.util.Action
import com.id.mindtodo.ui.util.SearchAppBarState
import com.id.mindtodo.ui.util.toLowerCaseInFirst
import com.id.mindtodo.ui.viewmodel.SharedViewModel
import kotlinx.coroutines.launch

@ExperimentalAnimationApi
@ExperimentalMaterialApi
@Composable
fun ListScreen(
    action: Action,
    navigateToTaskScreen: (taskId: Int) -> Unit,
    sharedViewModel: SharedViewModel
) {
    LaunchedEffect(key1 = action) {
        sharedViewModel.handleDatabaseAction(
            action = action
        )
    }

    val allTask by sharedViewModel.allTasks.collectAsState()
    val searchedTask by sharedViewModel.searchedTask.collectAsState()
    val searchAppBarState: SearchAppBarState by sharedViewModel.searchAppBarState
    val searchTextState: String by sharedViewModel.searchTextState
    val sortState by sharedViewModel.sortState.collectAsState()
    val lowPriorityTask by sharedViewModel.lowerPriorityTask.collectAsState()
    val highPriorityTask by sharedViewModel.higherPriorityTask.collectAsState()
    val scaffoldState = rememberScaffoldState()

    DisplaySnackBar(
        scaffoldState = scaffoldState,
        onComplete = { sharedViewModel.action.value = it },
        onUndoClicked = {
            sharedViewModel.action.value = it
        },
        taskTitle = sharedViewModel.title.value,
        action = action
    )

    Scaffold(
        scaffoldState = scaffoldState,
        topBar = {
            ListAppBar(
                sharedViewModel = sharedViewModel,
                searchAppBarState = searchAppBarState,
                searchTextState = searchTextState
            )
        },
        content = {
            HomeContent(
                allTask = allTask,
                searchedTask = searchedTask,
                lowPriorityTask = lowPriorityTask,
                highPriorityTask = highPriorityTask,
                sortState = sortState,
                searchAppBarState = searchAppBarState,
                onSwipeToDelete = { action, toDoTask ->
                    sharedViewModel.action.value = action
                    sharedViewModel.updateTaskFields(toDoTask)
                    scaffoldState.snackbarHostState.currentSnackbarData?.dismiss()
                },
                navigateToTaskScreen = navigateToTaskScreen
            )
        },
        floatingActionButton = {
            ListFab(onFabClicked = navigateToTaskScreen)
        }
    )
}

@Composable
fun ListFab(
    onFabClicked: (taskId: Int) -> Unit
) {
    FloatingActionButton(
        onClick = {
            onFabClicked(-1)
        },
        backgroundColor = MaterialTheme.colors.fabBackgroundColor
    ) {
        Icon(
            imageVector = Icons.Filled.Add,
            contentDescription = stringResource(R.string.icon_add),
            tint = Color.White
        )
    }
}

@Composable
fun DisplaySnackBar(
    scaffoldState: ScaffoldState,
    onComplete: (Action) -> Unit,
    onUndoClicked: (Action) -> Unit,
    taskTitle: String,
    action: Action
) {
    val context = LocalContext.current

    val scope = rememberCoroutineScope()
    LaunchedEffect(key1 = action) {
        if (action != Action.NO_ACTION) {
            scope.launch {
                val snackBarResult = scaffoldState.snackbarHostState.showSnackbar(
                    message = if (action == Action.DELETE_ALL) {
                        context.getString(R.string.text_all_task_removed)
                    } else {
                        "${action.name.toLowerCaseInFirst()} $taskTitle"
                    },
                    actionLabel = setActionLabel(
                        action = action,
                        context = context
                    )
                )
                undoDeletedTask(
                    action = action,
                    snackBarResult = snackBarResult,
                    onUndoClicked = onUndoClicked
                )
            }
            onComplete(Action.NO_ACTION)
        }
    }
}

private fun setActionLabel(action: Action, context: Context): String {
    return if (action.name == "DELETE") {
        context.getString(R.string.text_undo)
    } else {
        context.getString(R.string.text_ok)
    }
}

private fun undoDeletedTask(
    action: Action,
    snackBarResult: SnackbarResult,
    onUndoClicked: (Action) -> Unit
) {
    if (snackBarResult == SnackbarResult.ActionPerformed && action == Action.DELETE) {
        onUndoClicked(Action.UNDO)
    }
}