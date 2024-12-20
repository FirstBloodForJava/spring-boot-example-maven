package com.oycm.component;

/**
 * @author ouyangcm
 * create 2024/12/20 10:45
 */
public class IntegerStore implements Store<Integer>{
    @Override
    public Integer store() {
        return this.hashCode();
    }
}
