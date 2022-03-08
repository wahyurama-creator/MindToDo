package com.id.mindtodo.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.ParagraphStyle
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.id.mindtodo.R
import com.id.mindtodo.ui.theme.*
import com.id.mindtodo.ui.util.OnBoardingPage

@Composable
fun PagerScreen(onBoardingPage: OnBoardingPage) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(PADDING_LG_XL),
        verticalArrangement = Arrangement.Top
    ) {
        Text(
            text = buildAnnotatedString {
                withStyle(style = ParagraphStyle(lineHeight = 30.sp)) {
                    withStyle(
                        style = SpanStyle(
                            color = MaterialTheme.colors.textTitle,
                            fontWeight = FontWeight.Bold
                        )
                    ) {
                        val removeLastWord =
                            onBoardingPage.title.substringBeforeLast(" ")
                        append(removeLastWord)
                    }
                    withStyle(
                        style = SpanStyle(
                            fontWeight = FontWeight.Bold,
                            color = BlueText
                        )
                    ) {
                        val lastWord =
                            onBoardingPage.title.substringAfterLast(" ")
                        append(" $lastWord")
                    }
                }
            },
            fontFamily = Montserrat,
            fontSize = 24.sp,
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(PADDING_LG_XXL))
        Image(
            modifier = Modifier
                .align(alignment = Alignment.CenterHorizontally)
                .width(IMG_WIDTH)
                .height(IMG_HEIGHT),
            painter = painterResource(id = onBoardingPage.image),
            contentDescription = stringResource(
                id = R.string.pager_image
            ),
        )
        Spacer(modifier = Modifier.height(PADDING_LG_XL))
        Text(
            text = onBoardingPage.description,
            fontFamily = Montserrat,
            fontWeight = FontWeight.Thin,
            color = MaterialTheme.colors.textDesc,
            modifier = Modifier.fillMaxWidth()
        )
    }
}

@Preview(showBackground = true)
@Composable
fun PagerScreenPreview() {
    PagerScreen(onBoardingPage = OnBoardingPage.First)
}