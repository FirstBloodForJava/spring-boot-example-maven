package com.oycm.component;

/**
 * @author ouyangcm
 * create 2024/12/20 10:41
 */
public class StringStore implements Store<String>{

    @Override
    public String store() {
        return this.toString();
    }
}
