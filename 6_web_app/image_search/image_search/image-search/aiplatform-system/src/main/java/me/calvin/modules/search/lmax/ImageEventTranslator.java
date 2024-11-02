package me.calvin.modules.search.lmax;

import com.lmax.disruptor.EventTranslatorOneArg;
import me.calvin.modules.search.service.dto.ImageInfoDto;
/**
 * Translator
 *
 * @author Calvin
 * @mail 179209347@qq.com
 * @website www.aias.top
 **/
public class ImageEventTranslator implements EventTranslatorOneArg<ImageEvent, ImageInfoDto> {

    @Override
    public void translateTo(ImageEvent event, long sequence, ImageInfoDto imageInfo) {
        event.setImageInfo(imageInfo);
    }
}