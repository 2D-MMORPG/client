package com.jukusoft.mmo.engine.service.asset;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.utils.GdxRuntimeException;
import com.jukusoft.mmo.engine.exception.AssetNotLoadedException;
import com.jukusoft.mmo.engine.service.IService;
import com.jukusoft.mmo.engine.service.UpdateService;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
* service for asset manager
*/
public class AssetManagerService implements IService, UpdateService {

    //instance of asset manager
    protected AssetManager assetManager = null;

    //time limit for every asset manager update() execution
    protected int max_Loading_Millis = 10;

    protected boolean finished = false;

    //if load_assets.json contains name key, this asset is saved in this map
    protected Map<String,Object> assetsMap = new ConcurrentHashMap<>();

    @Override
    public void onStart() {
        //create new asset manager
        this.assetManager = new AssetManager();
    }

    @Override
    public void onStop() {
        //cleanup assets
        this.assetManager.dispose();
        this.assetManager = null;
    }

    @Override
    public void update() {
        if (this.max_Loading_Millis > 0) {
            this.finished = this.assetManager.update(this.max_Loading_Millis);
        } else {
            this.finished = this.assetManager.update();
        }
    }

    /**
    * load asset
     *
     * @param asset asset
    */
    public void load (AssetInfo asset) {
        //load asset
        this.assetManager.load(asset.getPath(), asset.getLibGDXAssetClass());
    }

    /**
    * cleanup memory for asset
    */
    public void unload (AssetInfo asset) {
        //cleanup asset
        this.assetManager.unload(asset.getPath());
    }

    /**
    * get instance of loaded asset
     *
     * @param fileName path to asset file
     * @param type asset type
    */
    public <T> T get (String fileName, Class<T> type) {
        return this.assetManager.get(fileName, type);
    }

    /**
    * check, if asset was loaded
     *
     * @param filePath path to asset file
    */
    public boolean isLoaded (String filePath) {
        return this.assetManager.isLoaded(filePath);
    }

    /**
     * get instance of loaded asset
     *
     * @param asset asset info
     */
    public <T> T get (AssetInfo asset) {
        if (!this.assetManager.isLoaded(asset.getPath())) {
            throw new AssetNotLoadedException("asset '" + asset.getPath() + "' isn't loaded yet.");
        }

        return this.assetManager.get(asset.getPath(), (Class<T>) asset.getLibGDXAssetClass());
    }

    /**
     * store an loaded asset to an specific name
     *
     * @param name unique asset name
     * @param asset loaded instance of asset
     */
    public <T> void addAssetByName (String name, T asset) {
        this.assetsMap.put(name, asset);
    }

    public void removeAssetName (String name) {
        this.assetsMap.remove(name);
    }

    public <T> T getAssetByName (String name, Class<T> cls) {
        Object asset = this.assetsMap.get(name);

        if (asset == null) {
            throw new GdxRuntimeException("Couldnt found asset by name: " + name + ", was this asset loaded and saved with addAssetByName() before?");
        }

        return cls.cast(asset);
    }

}
