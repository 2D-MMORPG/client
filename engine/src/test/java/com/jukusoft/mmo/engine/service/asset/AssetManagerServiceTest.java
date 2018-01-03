package com.jukusoft.mmo.engine.service.asset;

import com.badlogic.gdx.assets.AssetManager;
import com.jukusoft.mmo.engine.GameUnitTest;
import org.junit.Test;
import org.mockito.Mockito;

public class AssetManagerServiceTest extends GameUnitTest {

    @Test
    public void testConstructor () {
        new AssetManagerService();
    }

    @Test
    public void testStartAndStop () {
        AssetManagerService service = new AssetManagerService();

        //start service
        service.onStart();

        //stop service and cleanup resources
        service.onStop();
    }

    @Test
    public void testUpdate () {
        AssetManagerService service = new AssetManagerService();
        service.assetManager = Mockito.mock(AssetManager.class);

        service.update();
    }

    @Test
    public void testUpdate1 () {
        AssetManagerService service = new AssetManagerService();
        service.assetManager = Mockito.mock(AssetManager.class);
        service.maxLoadingMillis = 0;

        service.load(new AssetInfo("test.png", AssetInfo.TYPE.TEXTURE));
        service.load(new AssetInfo("test.png", AssetInfo.TYPE.TEXTURE, "test"));

        service.update();
    }

}
