package com.ifeng.at.testagent;

import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;
import android.support.test.uiautomator.UiDevice;

import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.File;
import java.io.IOException;

/**
 * Created by zhaoye on 2016/11/30.
 *
 */

@RunWith(AndroidJUnit4.class)
public class Tools {

    @Test
    public void dump() throws IOException {
        UiDevice device = UiDevice.getInstance(InstrumentationRegistry.getInstrumentation());
        device.dumpWindowHierarchy(new File("/sdcard/ui.uix"));
    }
}
