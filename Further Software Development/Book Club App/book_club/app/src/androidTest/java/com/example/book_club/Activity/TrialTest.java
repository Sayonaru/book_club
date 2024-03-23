package com.example.book_club.Activity;


import androidx.test.espresso.DataInteraction;
import androidx.test.espresso.ViewInteraction;
import androidx.test.filters.LargeTest;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;

import static androidx.test.InstrumentationRegistry.getInstrumentation;
import static androidx.test.espresso.Espresso.onData;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.Espresso.pressBack;
import static androidx.test.espresso.Espresso.openActionBarOverflowOrOptionsMenu;
import static androidx.test.espresso.action.ViewActions.*;
import static androidx.test.espresso.assertion.ViewAssertions.*;
import static androidx.test.espresso.matcher.ViewMatchers.*;

import com.example.book_club.R;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import org.hamcrest.core.IsInstanceOf;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.anything;
import static org.hamcrest.Matchers.is;

@LargeTest
@RunWith(AndroidJUnit4.class)
public class TrialTest {

    @Rule
    public ActivityScenarioRule<MainActivity> mActivityScenarioRule =
            new ActivityScenarioRule<>(MainActivity.class);

    @Test
    public void trialTest() {
        ViewInteraction materialButton = onView(
allOf(withId(R.id.signBtn), withText("Sign Up"),
childAtPosition(
childAtPosition(
withId(android.R.id.content),
0),
3),
isDisplayed()));
        materialButton.perform(click());
        
        ViewInteraction appCompatEditText = onView(
allOf(withId(R.id.nameEt),
childAtPosition(
childAtPosition(
withId(R.id.nameTil),
0),
0)));
        appCompatEditText.perform(scrollTo(), replaceText("Joh"), closeSoftKeyboard());
        
        ViewInteraction appCompatEditText2 = onView(
allOf(withId(R.id.emailEt),
childAtPosition(
childAtPosition(
withId(R.id.emailTil),
0),
0)));
        appCompatEditText2.perform(scrollTo(), replaceText("john@ema"), closeSoftKeyboard());
        
        ViewInteraction materialButton2 = onView(
allOf(withId(R.id.registerBtn), withText("Register"),
childAtPosition(
childAtPosition(
withClassName(is("android.widget.ScrollView")),
0),
4)));
        materialButton2.perform(scrollTo(), click());
        
        ViewInteraction appCompatEditText3 = onView(
allOf(withId(R.id.emailEt), withText("john@ema"),
childAtPosition(
childAtPosition(
withId(R.id.emailTil),
0),
0)));
        appCompatEditText3.perform(scrollTo(), replaceText("john@ema.c"));
        
        ViewInteraction appCompatEditText4 = onView(
allOf(withId(R.id.emailEt), withText("john@ema.c"),
childAtPosition(
childAtPosition(
withId(R.id.emailTil),
0),
0),
isDisplayed()));
        appCompatEditText4.perform(closeSoftKeyboard());
        
        ViewInteraction appCompatEditText5 = onView(
allOf(withId(R.id.passwordEt),
childAtPosition(
childAtPosition(
withId(R.id.passwordTil),
0),
0)));
        appCompatEditText5.perform(scrollTo(), replaceText("1"), closeSoftKeyboard());
        
        ViewInteraction appCompatEditText6 = onView(
allOf(withId(R.id.cPasswordEt),
childAtPosition(
childAtPosition(
withId(R.id.cPasswordTil),
0),
0)));
        appCompatEditText6.perform(scrollTo(), replaceText("124"), closeSoftKeyboard());
        
        ViewInteraction materialButton3 = onView(
allOf(withId(R.id.registerBtn), withText("Register"),
childAtPosition(
childAtPosition(
withClassName(is("android.widget.ScrollView")),
0),
4)));
        materialButton3.perform(scrollTo(), click());
        }
    
    private static Matcher<View> childAtPosition(
            final Matcher<View> parentMatcher, final int position) {

        return new TypeSafeMatcher<View>() {
            @Override
            public void describeTo(Description description) {
                description.appendText("Child at position " + position + " in parent ");
                parentMatcher.describeTo(description);
            }

            @Override
            public boolean matchesSafely(View view) {
                ViewParent parent = view.getParent();
                return parent instanceof ViewGroup && parentMatcher.matches(parent)
                        && view.equals(((ViewGroup)parent).getChildAt(position));
            }
        };
    }
    }
