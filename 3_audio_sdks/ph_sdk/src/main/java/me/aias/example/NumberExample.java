package me.aias.example;


import me.aias.example.utils.NumberUtils;

/**
 * 数字读法。
 * 按数值大小读，一个一个数字读。
 */
public class NumberExample {
    public static void main(String[] args) {
        System.out.println(NumberUtils.sayDigit("1234567890123456"));
        System.out.println(NumberUtils.sayNumber("123456"));
        System.out.println(NumberUtils.sayDecimal("3.14"));
        System.out.println(NumberUtils.convertNumber("hello314.1592and2718281828"));
    }
}
