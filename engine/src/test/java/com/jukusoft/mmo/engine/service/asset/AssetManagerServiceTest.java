package com.jukusoft.mmo.engine.service.asset;

import com.badlogic.gdx.assets.AssetManager;
import com.jukusoft.mmo.engine.GameUnitTest;
import org.junit.Test;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

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

        //mock asset manager class
        service.assetManager = Mockito.mock(AssetManager.class);
        when(service.assetManager.isLoaded(anyString())).thenAnswer(new Answer<Object>() {
            @Override
            public Object answer(InvocationOnMock invocation) throws Throwable {
                if (((String) invocation.getArgument(0)).equals("test.png")) {
                    return true;
                } else {
                    return false;
                }
            }
        });

        service.maxLoadingMillis = 0;

        service.load(new AssetInfo("test.png", AssetInfo.TYPE.TEXTURE, "test2"));
        service.load(new AssetInfo("test1.png", AssetInfo.TYPE.TEXTURE));
        service.load(new AssetInfo("test2.png", AssetInfo.TYPE.TEXTURE, "test"));

        service.update();
    }

}
