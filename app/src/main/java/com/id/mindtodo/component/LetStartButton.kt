package com.id.mindtodo.component

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.PagerState
import com.id.mindtodo.R
import com.id.mindtodo.ui.theme.*

@ExperimentalPagerApi
@Composable
fun LetStartButton(
    modifier: Modifier,
    pagerState: PagerState,
    onClick: () -> Unit
) {
    Row(
        modifier = modifier
            .padding(horizontal = PADDING_LG_XXX),
        verticalAlignment = Alignment.Top,
        horizontalArrangement = Arrangement.Center
    ) {
        AnimatedVisibility(
            modifier = Modifier.fillMaxWidth(),
            visible = pagerState.currentPage == 2
        ) {
            StartButtonRounded(
                onClick = onClick
            )
        }
    }
}

@Composable
fun StartButtonRounded(
    onClick: () -> Unit
) {
    Button(
        onClick = onClick,
        colors = ButtonDefaults.buttonColors(
            backgroundColor = BlueText,
            contentColor = Color.White,
        ),
        shape = RoundedCornerShape(50.dp),
        modifier = Modifier
            .height(BTN_HEIGHT)
            .width(BTN_WIDTH)
    ) {
        Text(
            text = stringResource(id = R.string.start_btn),
            fontFamily = Montserrat,
            fontWeight = FontWeight.SemiBold,
            fontSize = 18.sp
        )
    }
}


@Preview
@Composable
fun FinishButtonPreview() {
    StartButtonRounded({})
}
