package com.kodgames.wechattools.model;

import javax.persistence.*;

@Table(name = "area_config")
public class AreaConfig {
    /**
     * 主键
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * 地区的标识
     */
    @Column(name = "area_id")
    private String areaId;

    /**
     * 下载链接
     */
    @Column(name = "download_url")
    private String downloadUrl;

    /**
     * gmt发送请求的地址
     */
    private String address;

    /**
     * 游戏game服务器id
     */
    @Column(name = "game_server_id")
    private Integer gameServerId;

    /**
     * 获取主键
     *
     * @return id - 主键
     */
    public Long getId() {
        return id;
    }

    /**
     * 设置主键
     *
     * @param id 主键
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * 获取地区的标识
     *
     * @return area_id - 地区的标识
     */
    public String getAreaId() {
        return areaId;
    }

    /**
     * 设置地区的标识
     *
     * @param areaId 地区的标识
     */
    public void setAreaId(String areaId) {
        this.areaId = areaId;
    }

    /**
     * 获取下载链接
     *
     * @return download_url - 下载链接
     */
    public String getDownloadUrl() {
        return downloadUrl;
    }

    /**
     * 设置下载链接
     *
     * @param downloadUrl 下载链接
     */
    public void setDownloadUrl(String downloadUrl) {
        this.downloadUrl = downloadUrl;
    }

    /**
     * 获取gmt发送请求的地址
     *
     * @return address - gmt发送请求的地址
     */
    public String getAddress() {
        return address;
    }

    /**
     * 设置gmt发送请求的地址
     *
     * @param address gmt发送请求的地址
     */
    public void setAddress(String address) {
        this.address = address;
    }

    /**
     * 获取游戏game服务器id
     *
     * @return game_server_id - 游戏game服务器id
     */
    public Integer getGameServerId() {
        return gameServerId;
    }

    /**
     * 设置游戏game服务器id
     *
     * @param gameServerId 游戏game服务器id
     */
    public void setGameServerId(Integer gameServerId) {
        this.gameServerId = gameServerId;
    }
}