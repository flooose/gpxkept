package com.flooose.gpxkeeper;

import android.content.Intent;
import android.test.ActivityUnitTestCase;
import android.widget.ListView;

import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;
import static org.mockito.Mockito.*;


/**
 * Created by chris on 30.05.15.
 */
@RunWith(MockitoJUnitRunner.class)
public class MainActivityTest extends ActivityUnitTestCase<MainActivity> {
    private FileListFragment fileListFragmentl;

    public MainActivityTest() {
        super(MainActivity.class);
    }

    @Override
    public void setUp() throws Exception {
        super.setUp();
        //fileListFragment = mock(MainActivity.PlaceholderFragment.class);
        //when(fileListFragment.oAuthenticated()).thenReturn(Boolean.FALSE);
        startActivity(new Intent(getInstrumentation().getTargetContext(), MainActivity.class), null, null);
    }

    @Override
    public void tearDown(){
  }

//    public void testResumeReloadsTheGPXFiles() throws Exception {
//        MainActivity mainActivity = getActivity();
//        fileListFragment.fileListView = new ListView(getInstrumentation().getContext());
//        fileListFragment = spy(mainActivity.fileListFragment);
//
//        mainActivity.onResume();
//
//        verify(fileListFragment).setFileArrayAdapter(any(GPXFilesAdapter.class));
//    }
}
