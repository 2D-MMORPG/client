package com.jukusoft.mmo.engine.service.asset;

import com.jukusoft.mmo.engine.GameUnitTest;
import org.junit.Test;

public class AssetManagerServiceTest extends GameUnitTest {

    @Test
    public void testConstructor () {
        new AssetManagerService();
    }

    @Test
    public void testStartAndStop () {
        AssetManagerService service = new AssetManagerService();

        service.onStart();

        service.onStop();
    }

}
