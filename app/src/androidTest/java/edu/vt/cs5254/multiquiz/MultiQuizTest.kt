package edu.vt.cs5254.multiquiz

import android.app.Activity
import androidx.test.core.app.ActivityScenario
import androidx.test.core.app.ActivityScenario.launch
import androidx.test.core.app.ActivityScenario.launchActivityForResult
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.Espresso.pressBack
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.intent.Intents
import androidx.test.espresso.intent.Intents.intended
import androidx.test.espresso.intent.matcher.IntentMatchers.hasExtra
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry.getInstrumentation
import org.hamcrest.CoreMatchers.not
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class MultiQuizTest {

    private lateinit var scenario: ActivityScenario<MainActivity>

    @Before
    fun setUp() {
        scenario = launch(MainActivity::class.java)
    }

    @After
    fun tearDown() {
        scenario.close()
    }

    @Test
    fun clickingWrongAnswerEnablesSubmitButton() {
        onView(withId(R.id.answer_1_button))
            .perform(click())
        onView(withId(R.id.submit_button))
            .check(matches(isEnabled()))
    }

    @Test
    fun clickingHint3TimesDisablesAllButCorrect() {
        onView(withId(R.id.hint_button))
            .perform(click())
        onView(withId(R.id.hint_button))
            .perform(click())
        onView(withId(R.id.hint_button))
            .perform(click())
        onView(withId(R.id.answer_0_button))
            .check(matches(isEnabled()))
        onView(withId(R.id.answer_1_button))
            .check(matches(not(isEnabled())))
        onView(withId(R.id.answer_2_button))
            .check(matches(not(isEnabled())))
        onView(withId(R.id.answer_3_button))
            .check(matches(not(isEnabled())))
    }


    @Test
    fun clickingHint3TimesDisablesHint() {
        onView(withId(R.id.hint_button))
            .perform(click())
        onView(withId(R.id.hint_button))
            .perform(click())
        onView(withId(R.id.hint_button))
            .perform(click())
        onView(withId(R.id.hint_button))
            .check(matches(not(isEnabled())))
    }

    @Test
    fun clickingAnswer0ThenAnswer1DeselectsAnswer0AndSelectsAnswer1() {
        onView(withId(R.id.answer_0_button))
            .perform(click())
        onView(withId(R.id.answer_1_button))
            .perform(click())
        onView(withId(R.id.answer_0_button))
            .check(matches(not(isSelected())))
        onView(withId(R.id.answer_1_button))
            .check(matches(isSelected()))
    }

    @Test
    fun clickingAnswer1TwiceDeselectsAnswer1() {
        onView(withId(R.id.answer_1_button))
            .perform(click())
        onView(withId(R.id.answer_1_button))
            .perform(click())
        onView(withId(R.id.answer_1_button))
            .check(matches(not(isSelected())))
    }

    @Test
    fun clickingAnswer1ThenRotatingSelectsAnswer1() {
        onView(withId(R.id.answer_1_button))
            .perform(click())
        scenario.recreate()
        onView(withId(R.id.answer_1_button))
            .check(matches(isSelected()))
    }

    @Test
    fun clickingHint3TimesThenRotatingDisablesHint() {
        onView(withId(R.id.hint_button))
            .perform(click())
        onView(withId(R.id.hint_button))
            .perform(click())
        onView(withId(R.id.hint_button))
            .perform(click())
        scenario.recreate()
        onView(withId(R.id.hint_button))
            .check(matches(not(isEnabled())))
    }

    @Test
    fun clickingHint3TimesThenRotatingTwiceDisablesAllButCorrect() {
        onView(withId(R.id.hint_button))
            .perform(click())
        onView(withId(R.id.hint_button))
            .perform(click())
        onView(withId(R.id.hint_button))
            .perform(click())
        scenario.recreate()
        scenario.recreate()
        onView(withId(R.id.answer_0_button))
            .check(matches(isEnabled()))
        onView(withId(R.id.answer_1_button))
            .check(matches(not(isEnabled())))
        onView(withId(R.id.answer_2_button))
            .check(matches(not(isEnabled())))
        onView(withId(R.id.answer_3_button))
            .check(matches(not(isEnabled())))
    }

    @Test
    fun all4QuestionsCorrectOneHint() {
        onView(withId(R.id.answer_0_button))
            .perform(click())
        onView(withId(R.id.hint_button))
            .perform(click())
        onView(withId(R.id.submit_button))
            .perform(click())
        onView(withId(R.id.answer_1_button))
            .perform(click())
        onView(withId(R.id.submit_button))
            .perform(click())
        onView(withId(R.id.answer_2_button))
            .perform(click())
        onView(withId(R.id.submit_button))
            .perform(click())
        onView(withId(R.id.answer_3_button))
            .perform(click())
        onView(withId(R.id.submit_button))
            .perform(click())

        onView(withId(R.id.score_text))
            .check(matches(withText("92")))
        onView(withId(R.id.reset_button))
            .check(matches(isEnabled()))
    }

    @Test
    fun scoreIntentContainsCorrectExtra() {
        onView(withId(R.id.answer_0_button))
            .perform(click())
        onView(withId(R.id.hint_button))
            .perform(click())
        onView(withId(R.id.submit_button))
            .perform(click())
        onView(withId(R.id.answer_1_button))
            .perform(click())
        onView(withId(R.id.submit_button))
            .perform(click())
        onView(withId(R.id.answer_1_button))
            .perform(click())
        onView(withId(R.id.submit_button))
            .perform(click())
        onView(withId(R.id.answer_3_button))
            .perform(click())

        Intents.init()

        onView(withId(R.id.submit_button))
            .perform(click())

        intended(hasExtra("edu.vt.cs5254.multiquiz.score", 67))

    }

    @Test
    fun scoreRotationBeforeAndAfterReset() {
        val intent = ScoreActivity.newIntent(
            getInstrumentation().targetContext,
            99
        )
        launch<ScoreActivity>(intent).use { scoreScenario ->

            onView(withId(R.id.score_text))
                .check(matches(withText("99")))

            scoreScenario.recreate()

            onView(withId(R.id.score_text))
                .check(matches(withText("99")))

            onView(withId(R.id.reset_button))
                .perform(click())

            onView(withId(R.id.score_text))
                .check(matches(withText("?")))
            onView(withId(R.id.reset_button))
                .check(matches(not(isEnabled())))

            scoreScenario.recreate()

            onView(withId(R.id.score_text))
                .check(matches(withText("?")))
            onView(withId(R.id.reset_button))
                .check(matches(not(isEnabled())))
        }
    }

    @Test
    fun backFromScoreShowsFirstQuestionInPreviousState() {
        all4QuestionsCorrectOneHint()
        pressBack()
        onView(withId(R.id.answer_0_button))
            .check(matches(isSelected()))
        onView(withId(R.id.answer_1_button))
            .check(matches(not(isSelected())))
        onView(withId(R.id.answer_2_button))
            .check(matches(not(isSelected())))
        onView(withId(R.id.answer_3_button))
            .check(matches(not(isSelected())))
        onView(withId(R.id.hint_button))
            .check(matches(isEnabled()))
            .perform(click())
            .check(matches(isEnabled()))
            .perform(click())
            .check(matches(not(isEnabled())))
        onView(withId(R.id.submit_button))
            .check(matches(isEnabled()))
    }

    @Test
    fun scoreResetClearsScoreAndDisablesReset() {
        all4QuestionsCorrectOneHint()
        onView(withId(R.id.reset_button))
            .perform(click())
        onView(withId(R.id.reset_button))
            .check(matches(not(isEnabled())))
        onView(withId(R.id.score_text))
            .check(matches(withText("?")))
    }

    @Test
    fun backAfterResetFirstQuestionIsReset() {
        all4QuestionsCorrectOneHint()
        onView(withId(R.id.score_text))
            .check(matches(withText("92")))
        onView(withId(R.id.reset_button))
            .check(matches(isEnabled()))
        onView(withId(R.id.reset_button))
            .perform(click())

        onView(withId(R.id.score_text))
            .check(matches(withText("?")))
        onView(withId(R.id.reset_button))
            .check(matches(not(isEnabled())))

        pressBack()

        onView(withId(R.id.answer_0_button))
            .check(matches(not(isSelected())))
            .check(matches(isEnabled()))
        onView(withId(R.id.answer_1_button))
            .check(matches(not(isSelected())))
            .check(matches(isEnabled()))
        onView(withId(R.id.answer_2_button))
            .check(matches(not(isSelected())))
            .check(matches(isEnabled()))
        onView(withId(R.id.answer_3_button))
            .check(matches(not(isSelected())))
            .check(matches(isEnabled()))
        onView(withId(R.id.hint_button))
            .check(matches(isEnabled()))
        onView(withId(R.id.submit_button))
            .check(matches(not(isEnabled())))
    }


    @Test
    fun resultFromScoreAfterRotateResetRotate() {
        val intent = ScoreActivity.newIntent(
            getInstrumentation().targetContext,
            88
        )

        launchActivityForResult<ScoreActivity>(intent).use { scoreScenario ->

            onView(withId(R.id.score_text))
                .check(matches(withText("88")))
            onView(withId(R.id.reset_button))
                .check(matches(isEnabled()))

            scoreScenario.recreate()

            onView(withId(R.id.score_text))
                .check(matches(withText("88")))
            onView(withId(R.id.reset_button))
                .check(matches(isEnabled()))

            onView(withId(R.id.reset_button))
                .perform(click())

            onView(withId(R.id.score_text))
                .check(matches(withText("?")))
            onView(withId(R.id.reset_button))
                .check(matches(not(isEnabled())))

            scoreScenario.recreate()

            onView(withId(R.id.score_text))
                .check(matches(withText("?")))
            onView(withId(R.id.reset_button))
                .check(matches(not(isEnabled())))

            pressBack()

            assertEquals(Activity.RESULT_OK, scoreScenario.result.resultCode)
            assertEquals(
                true, scoreScenario.result.resultData
                    .getBooleanExtra("edu.vt.cs5254.multiquiz.reset", false)
            )
        }
    }

    @Test
    fun initialHintButtonIsEnabledSubmitButtonIsDisabled() {
        onView(withId(R.id.hint_button))
            .check(matches(isEnabled()))
        onView(withId(R.id.submit_button))
            .check(matches(not(isEnabled())))
    }

    @Test
    fun initialNoAnswersAreSelected() {
        onView(withId(R.id.answer_0_button))
            .check(matches(not(isSelected())))
        onView(withId(R.id.answer_1_button))
            .check(matches(not(isSelected())))
        onView(withId(R.id.answer_2_button))
            .check(matches(not(isSelected())))
        onView(withId(R.id.answer_3_button))
            .check(matches(not(isSelected())))
    }

    @Test
    fun clickingAnswer0SelectsAnswer0() {
        onView(withId(R.id.answer_0_button))
            .perform(click())
        onView(withId(R.id.answer_0_button))
            .check(matches(isSelected()))
    }

    @Test
    fun clickingAnswer1ThenHint3TimesDeselectsAnswer1() {
        onView(withId(R.id.answer_1_button))
            .perform(click())
        onView(withId(R.id.hint_button))
            .perform(click())
        onView(withId(R.id.hint_button))
            .perform(click())
        onView(withId(R.id.hint_button))
            .perform(click())
        onView(withId(R.id.answer_1_button))
            .check(matches(not(isSelected())))
    }

    @Test
    fun clickingAnswer0ThenSubmitPutsButtonsIntoInitStates() {
        onView(withId(R.id.answer_0_button))
            .perform(click())
        onView(withId(R.id.submit_button))
            .perform(click())
        onView(withId(R.id.answer_0_button))
            .check(matches(not(isSelected())))
        onView(withId(R.id.answer_1_button))
            .check(matches(not(isSelected())))
        onView(withId(R.id.answer_2_button))
            .check(matches(not(isSelected())))
        onView(withId(R.id.answer_3_button))
            .check(matches(not(isSelected())))
        onView(withId(R.id.hint_button))
            .check(matches(isEnabled()))
        onView(withId(R.id.submit_button))
            .check(matches(not(isEnabled())))
    }

    @Test
    fun exactly2QuestionsCorrect4Hints() {
        onView(withId(R.id.hint_button))
            .perform(click())
        onView(withId(R.id.answer_0_button))
            .perform(click())
        onView(withId(R.id.submit_button))
            .perform(click())

        onView(withId(R.id.hint_button))
            .perform(click())
        onView(withId(R.id.hint_button))
            .perform(click())
        onView(withId(R.id.hint_button))
            .perform(click())
        onView(withId(R.id.answer_1_button))
            .perform(click())
        onView(withId(R.id.submit_button))
            .perform(click())

        onView(withId(R.id.answer_0_button))
            .perform(click())
        onView(withId(R.id.submit_button))
            .perform(click())

        onView(withId(R.id.answer_1_button))
            .perform(click())
        onView(withId(R.id.submit_button))
            .perform(click())

        onView(withId(R.id.score_text))
            .check(matches(withText("18")))
    }

    @Test
    fun all4QuestionsCorrect6Hints() {
        onView(withId(R.id.answer_0_button))
            .perform(click())
        onView(withId(R.id.hint_button))
            .perform(click())
        onView(withId(R.id.submit_button))
            .perform(click())

        onView(withId(R.id.answer_1_button))
            .perform(click())
        onView(withId(R.id.hint_button))
            .perform(click())
        onView(withId(R.id.submit_button))
            .perform(click())

        onView(withId(R.id.answer_2_button))
            .perform(click())
        onView(withId(R.id.hint_button))
            .perform(click())
        onView(withId(R.id.hint_button))
            .perform(click())
        onView(withId(R.id.submit_button))
            .perform(click())

        onView(withId(R.id.answer_3_button))
            .perform(click())
        onView(withId(R.id.hint_button))
            .perform(click())
        onView(withId(R.id.hint_button))
            .perform(click())
        onView(withId(R.id.submit_button))
            .perform(click())

        onView(withId(R.id.score_text))
            .check(matches(withText("52")))
    }

    @Test
    fun exactly2QuestionCorrect2HintsRotationAfterSecondSubmit() {
        onView(withId(R.id.hint_button))
            .perform(click())
        onView(withId(R.id.answer_0_button))
            .perform(click())
        onView(withId(R.id.submit_button))
            .perform(click())

        onView(withId(R.id.hint_button))
            .perform(click())
        onView(withId(R.id.answer_1_button))
            .perform(click())
        onView(withId(R.id.submit_button))
            .perform(click())

        scenario.recreate()

        onView(withId(R.id.answer_0_button))
            .perform(click())
        onView(withId(R.id.submit_button))
            .perform(click())

        onView(withId(R.id.answer_0_button))
            .perform(click())
        onView(withId(R.id.submit_button))
            .perform(click())

        onView(withId(R.id.score_text))
            .check(matches(withText("34")))

    }

    @Test
    fun exactly1QuestionCorrectNoHintsRotationBeforeFinalSubmit() {
        onView(withId(R.id.answer_0_button))
            .perform(click())
        onView(withId(R.id.submit_button))
            .perform(click())

        onView(withId(R.id.answer_0_button))
            .perform(click())
        onView(withId(R.id.submit_button))
            .perform(click())

        onView(withId(R.id.answer_0_button))
            .perform(click())
        onView(withId(R.id.submit_button))
            .perform(click())

        onView(withId(R.id.answer_0_button))
            .perform(click())

        scenario.recreate()

        onView(withId(R.id.submit_button))
            .perform(click())

        onView(withId(R.id.score_text))
            .check(matches(withText("25")))

    }

}
