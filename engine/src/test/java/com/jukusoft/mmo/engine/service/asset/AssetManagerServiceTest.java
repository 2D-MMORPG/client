package com.jukusoft.mmo.engine.service.asset;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.utils.GdxRuntimeException;
import com.jukusoft.mmo.engine.GameUnitTest;
import com.jukusoft.mmo.engine.exception.AssetNotLoadedException;
import org.junit.Test;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyObject;
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
    public void testStartAndStop1 () {
        AssetManagerService.isJUnitTest = true;
        AssetManagerService service = new AssetManagerService();

        //start service
        service.onStart();

        //stop service and cleanup resources
        service.onStop();

        AssetManagerService.isJUnitTest = false;
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
        when(service.assetManager.get(anyString(), any(Class.class))).thenAnswer(i -> Mockito.mock(Texture.class));

        service.maxLoadingMillis = 0;

        service.load(new AssetInfo("test.png", AssetInfo.TYPE.TEXTURE, "test2"));
        service.load(new AssetInfo("test1.png", AssetInfo.TYPE.TEXTURE));
        service.load(new AssetInfo("test2.png", AssetInfo.TYPE.TEXTURE, "test"));
        service.load(new AssetInfo("test3.png", AssetInfo.TYPE.TEXTURE, "test"));

        service.update();

        //update again
        service.update();
    }

    @Test (expected = NullPointerException.class)
    public void testUpdate2 () {
        AssetManagerService service = new AssetManagerService();

        //mock asset manager class
        service.assetManager = Mockito.mock(AssetManager.class);
        when(service.assetManager.isLoaded(anyString())).thenAnswer(invocation -> true);
        when(service.assetManager.get(anyString(), any(Class.class))).thenAnswer(i -> null);

        service.maxLoadingMillis = 0;

        service.load(new AssetInfo("test.png", AssetInfo.TYPE.TEXTURE, "test2"));

        service.update();

        //update again
        service.update();
    }

    @Test
    public void testLoad () {
        AssetManagerService service = new AssetManagerService();

        //mock asset manager class
        service.assetManager = Mockito.mock(AssetManager.class);

        service.load(new AssetInfo("test.png", AssetInfo.TYPE.TEXTURE));
        service.load("test2.png", Texture.class);
    }

    @Test
    public void testUnload () {
        AssetManagerService service = new AssetManagerService();

        //mock asset manager class
        service.assetManager = Mockito.mock(AssetManager.class);

        service.unload(new AssetInfo("test.png", AssetInfo.TYPE.TEXTURE));
        service.unload("test2.png");
    }

    @Test (expected = AssetNotLoadedException.class)
    public void testGetNotLoadedAsset () {
        AssetManagerService service = new AssetManagerService();

        //mock asset manager class
        service.assetManager = Mockito.mock(AssetManager.class);
        when(service.assetManager.isLoaded(anyString())).thenAnswer(i -> false);

        service.get(new AssetInfo("test.png", AssetInfo.TYPE.TEXTURE));
    }

    @Test (expected = AssetNotLoadedException.class)
    public void testGetNotLoadedAsset1 () {
        AssetManagerService service = new AssetManagerService();

        //mock asset manager class
        service.assetManager = Mockito.mock(AssetManager.class);
        when(service.assetManager.isLoaded(anyString())).thenAnswer(i -> false);

        service.get("test.png", Texture.class);
    }

    @Test
    public void testGet () {
        AssetManagerService service = new AssetManagerService();

        //mock asset manager class
        service.assetManager = Mockito.mock(AssetManager.class);
        when(service.assetManager.isLoaded(anyString())).thenAnswer(invocation -> true);
        when(service.assetManager.get(anyString(), any(Class.class))).thenAnswer(i -> Mockito.mock(Texture.class));

        Object value = service.get(new AssetInfo("test.png", AssetInfo.TYPE.TEXTURE));
        assertNotNull(value);
        assertEquals("object isnt of type texture: " + value.getClass().getName(), true, value instanceof Texture);
    }

    @Test
    public void testGet1 () {
        AssetManagerService service = new AssetManagerService();

        //mock asset manager class
        service.assetManager = Mockito.mock(AssetManager.class);
        when(service.assetManager.isLoaded(anyString())).thenAnswer(invocation -> true);
        when(service.assetManager.get(anyString(), any(Class.class))).thenAnswer(i -> Mockito.mock(Texture.class));

        Object value = service.get("test.png", Texture.class);
        assertNotNull(value);
        assertEquals("object isnt of type texture: " + value.getClass().getName(), true, value instanceof Texture);
    }

    @Test
    public void testIsLoaded () {
        AssetManagerService service = new AssetManagerService();

        //mock asset manager class
        service.assetManager = Mockito.mock(AssetManager.class);
        when(service.assetManager.isLoaded(anyString())).thenAnswer(invocation -> true);

        assertEquals(true, service.isLoaded("test.png"));
    }

    @Test
    public void testAddAssetByName () {
        AssetManagerService service = new AssetManagerService();

        Texture texture = Mockito.mock(Texture.class);

        service.addAssetByName("test", texture);

        Texture value = service.getAssetByName("test", Texture.class);
        assertNotNull(value);
        assertEquals(true, value instanceof Texture);

        service.removeAssetName("test");
    }

    @Test (expected = GdxRuntimeException.class)
    public void testGetNullAssetByName () {
        AssetManagerService service = new AssetManagerService();

        service.getAssetByName("test", Texture.class);
    }

    @Test
    public void testFinishLoading () {
        AssetManagerService service = new AssetManagerService();

        //mock asset manager class
        service.assetManager = Mockito.mock(AssetManager.class);

        service.finishLoading();

        service.finishLoading("test");
    }

    @Test
    public void testGetProgress () {
        AssetManagerService service = new AssetManagerService();

        service.onStart();

        assertEquals(1f, service.getProgress(), 0f);

        service.onStop();
    }

}
