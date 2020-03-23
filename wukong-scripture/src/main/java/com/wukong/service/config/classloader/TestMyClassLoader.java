package com.wukong.service.config.classloader;

public class TestMyClassLoader
{
    public static void main(String[] args) throws Exception
    {
        MyClassLoader mcl = new MyClassLoader();
        Class<?> c1 = mcl.loadClass("com.wukong.service.config.classloader.Person");
        Object obj = c1.newInstance();
        System.out.println(obj);
        System.out.println(obj.getClass().getClassLoader());
    }
}