package com.id.mindtodo.ui.screen.onboard

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.HorizontalPagerIndicator
import com.google.accompanist.pager.rememberPagerState
import com.id.mindtodo.R
import com.id.mindtodo.component.LetStartButton
import com.id.mindtodo.component.PagerScreen
import com.id.mindtodo.ui.theme.*
import com.id.mindtodo.ui.util.OnBoardingPage
import com.id.mindtodo.ui.viewmodel.OnBoardViewModel

@ExperimentalPagerApi
@Composable
fun OnBoardingScreen(
    navigateToListScreen: () -> Unit,
    navController: NavHostController,
    onBoardViewModel: OnBoardViewModel = hiltViewModel()
) {
    val pages = listOf(
        OnBoardingPage.First,
        OnBoardingPage.Second,
        OnBoardingPage.Third,
    )
    val pagerState = rememberPagerState()

    Column(modifier = Modifier.fillMaxSize()) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = PADDING_LG_XL)
                .padding(top = PADDING_LG_XL),
            horizontalArrangement = Arrangement.Start,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                painter = painterResource(id = R.drawable.small_logo),
                contentDescription = stringResource(
                    id = R.string.small_logo
                )
            )
            Spacer(modifier = Modifier.width(PADDING_LG))
            Text(
                text = stringResource(id = R.string.app_name),
                color = MediumGray,
                fontFamily = Montserrat,
                fontWeight = FontWeight.SemiBold
            )
        }
        Spacer(modifier = Modifier.height(PADDING_LG_XL))
        HorizontalPager(
            modifier = Modifier.weight(PAGER_WEIGHT),
            count = 3,
            state = pagerState,
            verticalAlignment = Alignment.Top
        ) { position ->
            PagerScreen(onBoardingPage = pages[position])
        }
        HorizontalPagerIndicator(
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .weight(ONE_FLOAT),
            pagerState = pagerState,
            activeColor = BlueText,
        )
        LetStartButton(
            modifier = Modifier.weight(ONE_FLOAT),
            pagerState = pagerState,
            onClick = {
                onBoardViewModel.saveOnBoardingState(completed = true)
                navController.popBackStack()
                navigateToListScreen()
            }
        )
        Spacer(modifier = Modifier.height(PADDING_LG_XXL))
    }
}

@Composable
@Preview(showBackground = true)
fun FirstOnBoardingScreenPreview() {
    Column(modifier = Modifier.fillMaxSize()) {
        PagerScreen(onBoardingPage = OnBoardingPage.First)
    }
}