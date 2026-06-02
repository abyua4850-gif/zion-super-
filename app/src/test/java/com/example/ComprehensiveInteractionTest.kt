package com.example

import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createComposeRule
import com.example.ui.theme.MyApplicationTheme
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [36])
class ComprehensiveInteractionTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun testFullUserInteractionFlow() {
        // 1. Initialize the Main screen
        composeTestRule.setContent {
            MyApplicationTheme {
                ZionMainScreen()
            }
        }

        // Verify title exists on raw boot
        composeTestRule.onNodeWithText("ZION AI").assertExists()

        // 2. Perform Goal interaction (Adding, completing, and deleting)
        composeTestRule.onNodeWithTag("new_goal_text_field").performScrollTo()
        composeTestRule.onNodeWithTag("new_goal_text_field").performTextInput("Master KSP and Room db architecture")
        composeTestRule.onNodeWithTag("add_goal_button").performClick()

        // Toggle first goal
        composeTestRule.onNodeWithText("Complete AI Foundations learning modules").performClick()

        // 3. Navigate to Coaching Tab
        composeTestRule.onNodeWithTag("nav_tab_coaching").performClick()
        composeTestRule.onNodeWithText("ZION INTELLIGENCE FEED").assertExists()

        // Try chatbot interactions
        composeTestRule.onNodeWithTag("chat_input_field").performTextInput("How can I accept Telebirr payments safely?")
        composeTestRule.onNodeWithTag("chat_send_button").performClick()

        // 4. Navigate to Payments Tab
        composeTestRule.onNodeWithTag("nav_tab_payments").performClick()
        composeTestRule.onNodeWithText("MEMBERSHIP DECLARED PLANS").assertExists()

        // Fill payment details
        val phoneField = composeTestRule.onNodeWithTag("checkout_phone_field")
        phoneField.performScrollTo()
        phoneField.performTextInput("0912345678")
        composeTestRule.onNodeWithTag("submit_payment_button").performClick()

        // 5. Navigate to Expansion Tab
        composeTestRule.onNodeWithTag("nav_tab_expansion").performClick()
        composeTestRule.onNodeWithText("ROADMAP STAGES").assertExists()

        // 6. Navigate back to Coaching and test Custom Skill Gap Analyzer
        composeTestRule.onNodeWithTag("nav_tab_coaching").performClick()
        composeTestRule.onNodeWithTag("resume_input_field").performScrollTo()
        composeTestRule.onNodeWithTag("resume_input_field").performTextInput("Python dev, Django, Postgres experience")
        composeTestRule.onNodeWithTag("run_analysis_btn").performClick()

        // Everything should complete without crashing!
    }
}
