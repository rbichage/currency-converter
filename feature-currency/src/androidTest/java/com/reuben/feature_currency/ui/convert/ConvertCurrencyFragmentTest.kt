package com.reuben.feature_currency.ui.convert

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.reuben.core_testing.hilt.launchFragmentInHiltContainer
import com.reuben.feature_currency.R
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
@HiltAndroidTest
class ConvertCurrencyFragmentTest {

    @get:Rule
    var hiltRule = HiltAndroidRule(this)

    @Before
    fun setup() {
        hiltRule.inject()
        launchFragmentInHiltContainer<ConvertCurrencyFragment> {  }
    }


    @Test
    fun test_container_is_visibile_when_UserOpensTheFragment() {
        onView(withId(R.id.convert_root_container))
            .check(matches(isDisplayed()))
    }
}