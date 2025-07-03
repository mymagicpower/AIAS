package me.calvin.modules.search.tools;

import com.alibaba.fastjson.JSONArray;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import me.calvin.domain.LocalStorage;

import java.util.ArrayList;
import java.util.List;

/**
 * json 测试
 *
 * @author Calvin
 * @mail 179209347@qq.com
 * @website www.aias.top
 **/
public class TestList {

    public static void main(String[] args) {
        ArrayList<Long> list = new ArrayList();
        list.add(1L);
        list.add(2L);
        list.add(3L);
        list.add(4L);
        list.add(5L);
        list.add(6L);

        for (int i = 0; i < list.size(); i++) {
            System.out.println(list.subList(i, i + 1));
        }

        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        String jsonStr = gson.toJson(list);
        System.out.println(jsonStr);

        ArrayList<Long> newList = new Gson().fromJson(jsonStr, new TypeToken<List<Long>>() {
        }.getType());

        for (int i = 0; i < newList.size(); i++) {
            System.out.println(newList.get(i));
        }
    }
}
