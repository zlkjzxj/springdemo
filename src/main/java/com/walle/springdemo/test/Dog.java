package com.walle.springdemo.test;

public class Dog implements Animal {
    @Override
    public void eat(String food) {
        System.out.println("dog eat " + food);
    }

    @Override
    public void run(int speed) {
        System.out.println("dog can run " + speed + "m/s");
    }
}
