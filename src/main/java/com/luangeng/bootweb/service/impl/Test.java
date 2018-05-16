package com.luangeng.bootweb.service.impl;

import com.luangeng.bootweb.util.MyUtils;

public class Test {

    public static void main(String[] args) {
        String pwd = MyUtils.MD5encode("admin" + "z");
        System.out.println(pwd);
    }
}
