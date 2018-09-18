package com.starpy.plugin.bean;

import com.core.base.bean.BaseResponseModel;

public class CheckPluginResultModel extends BaseResponseModel {

    private String downLoadUrl;
    private String pluginPackageName;
    private String version;
    private String open;//开关，1为开

    public String getDownLoadUrl() {
        return downLoadUrl;
    }

    public void setDownLoadUrl(String downLoadUrl) {
        this.downLoadUrl = downLoadUrl;
    }

    public String getPluginPackageName() {
        return pluginPackageName;
    }

    public void setPluginPackageName(String pluginPackageName) {
        this.pluginPackageName = pluginPackageName;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getOpen() {
        return open;
    }

    public void setOpen(String open) {
        this.open = open;
    }

    public boolean isOpen(){
        return "1".equals(open);
    }
}
