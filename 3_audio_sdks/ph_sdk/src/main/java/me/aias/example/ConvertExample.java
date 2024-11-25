package me.aias.example;


import me.aias.example.utils.ConvertUtils;

/**
 * 文本转换 - Text conversion.
 * 全角半角转换，简体繁体转换。
 * Convert full-width/half-width characters, Convert simplified/traditional Chinese characters.
 */
public class ConvertExample {
    public static void main(String[] args) {
        System.out.println(ConvertUtils.ban2quan("aA1 ,:$。、"));
        System.out.println(ConvertUtils.quan2ban("ａＡ１　，：＄。、"));
        System.out.println(ConvertUtils.jian2fan("中国语言"));
        System.out.println(ConvertUtils.fan2jian("中國語言"));
    }
}
