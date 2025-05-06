package com.expensetrace.app;

import com.expensetrace.app.enums.CategoryType;

import java.util.Arrays;

public class Main {
    public static void main(String[] args) {
        System.out.println(Arrays.stream(CategoryType.values()).toArray()[0]);
    }
}
