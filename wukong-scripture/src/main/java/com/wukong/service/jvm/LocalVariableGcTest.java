package com.wukong.service.jvm;

/**
 * 从上面的gc日志中可以得到如下结论：
 * 申请了一个6M大小的空间，赋值给b引用，然后调用gc函数，因为此时这个6M的空间还被b引用着，所以不能顺利gc；
 * 申请了一个6M大小的空间，赋值给b引用，然后将b重新赋值为null，此时这个6M的空间不再被b引用，所以可以顺利gc；
 * 申请了一个6M大小的空间，赋值给b引用，过了b的作用返回之后调用gc函数，但是因为此时b并没有被销毁，还存在于栈帧中，这个空间也还被b引用，所以不能顺利gc；
 * 申请了一个6M大小的空间，赋值给b引用，过了b的作用返回之后重新创建一个变量c，此时这个新的变量会复用已经失效的b变量的槽位，所以b被迫销毁了，所以6M的空间没有被任何变量引用，于是能够顺利gc；
 * 首先调用localVarGc1()，很显然不能顺利gc，函数调用结束之后再调用gc函数，此时因为localVarGc1这个函数的栈帧已经随着函数调用的结束而被销毁，b也就被销毁了，所以6M大小的空间不被任何对象引用，于是能够顺利gc。
 * @date 15/4/21 20:44
 */
public class LocalVariableGcTest {
    private static final int SIZE = 6 * 1024 * 1024;

    public static void localVarGc1() {
        byte[] b = new byte[SIZE];
        System.gc();
    }

    public static void localVarGc2() {
        byte[] b = new byte[SIZE];
        b = null;
        System.gc();
    }

    public static void localVarGc3() {
        {
            byte[] b = new byte[SIZE];
        }
        System.gc();
    }

    public static void localVarGc4() {
        {
            byte[] b = new byte[SIZE];
        }
        int c = 0;
        System.gc();
    }

    public static void localVarGc5() {
        localVarGc1();
        System.gc();
    }

    public static void main(String[] args) {
//        localVarGc1();   // 没有GC
//        localVarGc2();   // GC
//        localVarGc3();   // 没有GC
//        localVarGc4();   // GC
        localVarGc5();   // GC
    }
}