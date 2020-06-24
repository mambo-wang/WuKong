package com.wukong.service.leetcode;

public class intToHex {


    public static void main(String[] args) {
        int anInt = 16;
        System.out.println(changeFormatNumber(anInt));
        //十进制转成二进制
        System.out.println(Integer.toBinaryString(anInt));
        //十进制转成十六进制：
        System.out.println(Integer.toHexString(anInt));
    }

    public static String changeFormatNumber(int anInt) {
        return to2(anInt) + "," + to16(anInt);
    }

    private static String to2(int anInt) {
        String to2 = "";
        if(anInt < 0){
            to2 += "1";
        }else {
            to2 += "0";
        }
        anInt = Math.abs(anInt);
        int i = 0;
        char[] chars = new char[100];
        String result = "";
        if (anInt == 0) {
            result = "0000";
            return result;
        } else {
            while (anInt != 0) {
                int t = anInt % 2;
                chars[i] = (char) (t + '0');
                i++;
                anInt = anInt / 2;
            }
            for (int j = i - 1; j >= 0; j--) {
                result += chars[j];
            }
            to2 += result;
            return to2;
        }
    }

    /**
     * 先输入一个整型数字，判断其是否为0，若为0，则其16进制同样为0；
     * 若number不为0，则对16取余，并转换成16进制相应的字符；
     * number=number/16,重复过程2、3，用字符数组s依次保存每一位；
     * 输出的时候逆序输出即可
     */
    private static String to16(int anInt) {
        int i = 0;
        char[] chars = new char[100];
        String result = "";
        if (anInt == 0) {
            result = "0000";
        } else {
            while (anInt != 0) {
                int t = anInt % 16;
                if (t >= 0 && t < 10) {
                    chars[i] = (char) (t + '0');
                    i++;
                } else {
                    chars[i] = (char) (t + 'A' - 10);
                    i++;
                }
                anInt = anInt / 16;
            }

            for (int j = i - 1; j >= 0; j--) {
                result += chars[j];
            }
        }
        return result;
    }
}