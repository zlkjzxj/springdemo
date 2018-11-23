package com.walle.springdemo.test;

public class Chinese implements People {
    @Override
    public void say() {
        System.out.println("接口能够继承接口");
    }

    @Override
    public void eat(String food) {

    }

    @Override
    public void run(int speed) {

    }
}
