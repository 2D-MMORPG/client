package com.jukusoft.mmo.engine.service.asset;

/**
* wrapper for asset manager service to correct file paths in junit tests
*/
public class JUnitAssetManagerService extends AssetManagerService {

    /**
     * load asset
     *
     * @param asset asset
     */
    @Override
    public void load (AssetInfo asset) {
        asset.path = correctFileName(asset.path);

        super.load(asset);
    }

    @Override
    public void load (String path, Class<?> cls) {
        super.load(correctFileName(path), cls);
    }

    /**
     * cleanup memory for asset
     */
    @Override
    public void unload (AssetInfo asset) {
        asset.path = correctFileName(asset.path);

        super.unload(asset);
    }

    @Override
    public void unload (String path) {
        super.unload(correctFileName(path));
    }

    /**
     * get instance of loaded asset
     *
     * @param fileName path to asset file
     * @param type asset type
     */
    @Override
    public <T> T get (String fileName, Class<T> type) {
        return super.get(correctFileName(fileName), type);
    }

    /**
     * check, if asset was loaded
     *
     * @param filePath path to asset file
     */
    @Override
    public boolean isLoaded (String filePath) {
        return super.isLoaded(correctFileName(filePath));
    }

    /**
     * get instance of loaded asset
     *
     * @param asset asset info
     */
    @Override
    public <T> T get (AssetInfo asset) {
        asset.path = correctFileName(asset.path);

        return super.get(asset);
    }

    @Override
    public void finishLoading (String fileName) {
        super.finishLoading(correctFileName(fileName));
    }

    protected String correctFileName (String fileName) {
        if (fileName.startsWith("./")) {
            fileName = "." + fileName;
        }

        return fileName;
    }

}
