package me.calvin.modules.search.lmax;

import me.calvin.modules.search.service.dto.ImageInfoDto;
/**
 * 图片事件
 *
 * @author Calvin
 * @mail 179209347@qq.com
 * @website www.aias.top
 **/
public class ImageEvent {
    private ImageInfoDto imageInfo;

    public ImageInfoDto getImageInfo() {
        return imageInfo;
    }

    public void setImageInfo(ImageInfoDto imageInfo) {
        this.imageInfo = imageInfo;
    }
}