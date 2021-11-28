package me.aias.example.utils;

import cn.hutool.core.util.NumberUtil;
import com.google.common.base.Joiner;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;

import java.util.LinkedList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class NumberUtils {

    static String[] _number_cn = {"零", "一", "二", "三", "四", "五", "六", "七", "八", "九"};
    static String[] _number_level = {"千", "百", "十", "万", "千", "百", "十", "亿", "千", "百", "十", "万", "千", "百", "十", "个"};
    static String _zero = _number_cn[0];
    static Pattern _ten_re = Pattern.compile("^一十");
    static ImmutableList<String> _grade_level = ImmutableList.of("万", "亿", "个");
    static Pattern _number_group_re = Pattern.compile("([0-9]+)");

    public static void main(String[] args) {
        System.out.println(sayDigit("51234565"));
        System.out.println(sayNumber("12345678901234561"));
        System.out.println(sayDecimal("3.14"));
        System.out.println(convertNumber("hello314.1592and2718281828"));

        // 五一二三四五六五
        // 12345678901234561 (小于等于16位时: 十二亿三千四百五十六万七千八百九十)
        // 三点一四
        // hello三百一十四.一千五百九十二and二七一八二八一八二八
    }

    public static String sayDigit(String num) {
        StringBuilder outs = new StringBuilder();
        String[] ss = num.split("");
        for (String s : ss) {
            outs.append(_number_cn[Integer.valueOf(s)]);
        }
        return outs.toString();
    }

    public static String sayNumber(String nums) {
        String x = nums;
        if (x == "0") {
            return _number_cn[0];
        } else if (x.length() > 16) {
            return nums;
        }
        int length = x.length();
        LinkedList<String> outs = new LinkedList();
        String[] ss = x.split("");
        for (int i = 0; i < ss.length; i++) {
            String a = _number_cn[Integer.valueOf(ss[i])];
            String b = _number_level[_number_level.length - length + i];
            if (!a.equals(_zero)) {
                outs.add(a);
                outs.add(b);
            } else {
                if (_grade_level.contains(b)) {
                    if (!_zero.equals(outs.getLast())) {
                        outs.add(b);
                    } else {
                        outs.removeLast();
                        outs.add(b);
                    }
                } else {
                    if (!_zero.equals(outs.getLast())) {
                        outs.add(a);
                    }
                }
            }
        }
        outs.removeLast();
        String out = Joiner.on("").join(outs);
        // 进行匹配
        Matcher matcher = _ten_re.matcher(out);
        out = matcher.replaceAll("十");
        return out;
    }

    public static String sayDecimal(String num) {
        String[] nums = num.split("\\.");
        String z_cn = sayNumber(nums[0]);
        String x_cn = sayDigit(nums[1]);
        return z_cn + '点' + x_cn;
    }

    public static String convertNumber(String text) {

        Matcher matcher = _number_group_re.matcher(text);
        LinkedList<Integer> postion = new LinkedList();
        while (matcher.find()) {
            postion.add(matcher.start());
            postion.add(matcher.end());
        }
        if (postion.size() == 0) {
            return text;
        }
        List<String> parts = Lists.newArrayList();
        parts.add(text.substring(0, postion.getFirst()));
        int size = postion.size() - 1;
        for (int i = 0; i < size; i++) {
            parts.add(text.substring(postion.get(i), postion.get(i + 1)));
        }
        parts.add(text.substring(postion.getLast()));
        LinkedList<String> outs = new LinkedList();
        for (String elem : parts) {
            if (NumberUtil.isNumber(elem)) {
                if (elem.length() <= 9) {
                    outs.add(sayNumber(elem));
                } else {
                    outs.add(sayDigit(elem));
                }
            } else {
                outs.add(elem);
            }
        }
        return Joiner.on("").join(outs);
    }


}
