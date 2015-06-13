package com.flooose.gpxkeeper;

import android.content.Intent;
import android.test.ActivityUnitTestCase;
import android.test.SingleLaunchActivityTestCase;
import android.widget.ListView;

import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;
import static org.mockito.Mockito.*;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;



/**
 * Created by chris on 30.05.15.
 */
@RunWith(MockitoJUnitRunner.class)
public class MainActivityTest extends ActivityUnitTestCase<MainActivity> {
    private MainActivity.PlaceholderFragment placeholderFragment;

    public MainActivityTest() {
        super(MainActivity.class);
    }

    @Override
    public void setUp() throws Exception {
        super.setUp();
        //placeholderFragment = mock(MainActivity.PlaceholderFragment.class);
        //when(placeholderFragment.oAuthenticated()).thenReturn(Boolean.FALSE);
        startActivity(new Intent(getInstrumentation().getTargetContext(), MainActivity.class), null, null);
    }

    @Override
    public void tearDown(){
  }

    public void testResumeReloadsTheGPXFiles() throws Exception {
        MainActivity mainActivity = getActivity();
        mainActivity.placeholderFragment.fileListView = new ListView(getInstrumentation().getContext());
        placeholderFragment = spy(mainActivity.placeholderFragment);

        mainActivity.onResume();

        verify(placeholderFragment).setFileArrayAdapter(any(GPXFilesAdapter.class));
    }
}
