package com.jukusoft.mmo.engine.service.asset;

import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.utils.GdxRuntimeException;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class AssetInfoTest {

    @Test
    public void testConstructor () {
        new AssetInfo("my-image.png", AssetInfo.TYPE.TEXTURE, "unique-name");
    }

    @Test
    public void testConstructor1 () {
        new AssetInfo("my-image.png", AssetInfo.TYPE.TEXTURE);
    }

    @Test (expected = NullPointerException.class)
    public void testNullConstructor () {
        new AssetInfo(null, AssetInfo.TYPE.TEXTURE);
    }

    @Test (expected = IllegalArgumentException.class)
    public void testEmptyPathConstructor () {
        new AssetInfo("", AssetInfo.TYPE.TEXTURE);
    }

    @Test
    public void testGetPath () {
        AssetInfo asset = new AssetInfo("my-image.png", AssetInfo.TYPE.TEXTURE);
        assertEquals("my-image.png", asset.getPath());
    }

    @Test
    public void testGetType () {
        AssetInfo asset = new AssetInfo("my-image.png", AssetInfo.TYPE.TEXTURE);
        assertEquals(AssetInfo.TYPE.TEXTURE, asset.getType());
    }

    @Test
    public void testGetUniqueName () {
        AssetInfo asset = new AssetInfo("my-image.png", AssetInfo.TYPE.TEXTURE);
        assertEquals(false, asset.hasName());

        AssetInfo asset1 = new AssetInfo("my-image.png", AssetInfo.TYPE.TEXTURE, "name1");
        assertEquals(true, asset1.hasName());
        assertEquals("name1", asset1.getName());
    }

    @Test
    public void testGetLibGDXAssetClass () {
        AssetInfo asset = new AssetInfo("my-image.png", AssetInfo.TYPE.TEXTURE);
        assertEquals(Texture.class, asset.getLibGDXAssetClass());

        asset = new AssetInfo("my-image.png", AssetInfo.TYPE.TEXTURE_ATLAS);
        assertEquals(TextureAtlas.class, asset.getLibGDXAssetClass());

        asset = new AssetInfo("my-image.png", AssetInfo.TYPE.SOUND);
        assertEquals(Sound.class, asset.getLibGDXAssetClass());

        asset = new AssetInfo("my-image.png", AssetInfo.TYPE.MUSIC);
        assertEquals(Music.class, asset.getLibGDXAssetClass());
    }

    @Test (expected = GdxRuntimeException.class)
    public void testGetUnknownLibGDXAssetClass () {
        AssetInfo asset = new AssetInfo("my-image.png", AssetInfo.TYPE.UNKNOWN);
        asset.getLibGDXAssetClass();
    }

}
