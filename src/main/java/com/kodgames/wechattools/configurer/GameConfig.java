package com.kodgames.wechattools.configurer;

public class GameConfig {
    private String bundleId;
    private String areaId;
    private String downloadUrl;
    private String address;
    private Integer gameServerId;

    public String getBundleId() {
        return bundleId;
    }

    public void setBundleId(String bundleId) {
        this.bundleId = bundleId;
    }

    public String getAreaId() {
        return areaId;
    }

    public void setAreaId(String areaId) {
        this.areaId = areaId;
    }

    public String getDownloadUrl() {
        return downloadUrl;
    }

    public void setDownloadUrl(String downloadUrl) {
        this.downloadUrl = downloadUrl;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Integer getGameServerId() {
        return gameServerId;
    }

    public void setGameServerId(Integer gameServerId) {
        this.gameServerId = gameServerId;
    }
}
