package com.flooose.gpxkeeper;

import android.test.SingleLaunchActivityTestCase;
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
public class MainActivityTest extends SingleLaunchActivityTestCase {
    public MainActivityTest(String pkg, Class activityClass) {
        super(pkg, activityClass);
    }

    @Override
    public void setUp(){
        int blub =4;
   }

    @Override
    public void tearDown(){
  }

    public void testResumeReloadsTheGPXFiles() throws Exception {
        MainActivity.PlaceholderFragment placeholderFragment = mock(MainActivity.PlaceholderFragment.class);
        //stub(placeholderFragment.setFileArrayAdapter(any(GPXFilesAdapter.class)));

        ((MainActivity) getActivity()).onResume();

        assertEquals(1,2);

        verify(placeholderFragment, atLeastOnce()).setFileArrayAdapter(any(GPXFilesAdapter.class));
    }
}
