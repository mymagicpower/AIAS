package me.calvin.modules.search.lmax;

import com.lmax.disruptor.EventFactory;
/**
 * 图片事件工厂类
 *
 * @author Calvin
 * @mail 179209347@qq.com
 * @website www.aias.top
 **/
public class ImageEventFactory implements EventFactory<ImageEvent> {

    @Override
    public ImageEvent newInstance() {
        return new ImageEvent();
    }
}