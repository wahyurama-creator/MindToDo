package com.id.mindtodo.ui.util

import androidx.annotation.DrawableRes
import com.id.mindtodo.R

sealed class OnBoardingPage(
    @DrawableRes
    val image: Int,
    val title: String,
    val description: String
) {
    object First : OnBoardingPage(
        image = R.drawable.first,
        title = "Manage your\ntask & everything\nwith MindToDo",
        description = "When you’re overwhelmed by the\namount of work you have on your\nplate, stop and rethink."
    )

    object Second : OnBoardingPage(
        image = R.drawable.second,
        title = "Make your\ntask always on time\nwith MindToDo",
        description = "When you often miss\nor even forget the time\nto do activities."
    )

    object Third : OnBoardingPage(
        image = R.drawable.third,
        title = "Boost your\nproductivity much more\nwith MindToDo",
        description = "When you feel lazy to do something,\nwriting down each of your activities\nwill make you more excited."
    )
}
