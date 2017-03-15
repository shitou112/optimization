package test;

import org.junit.Test;

import java.util.HashMap;

/**
 * @author Qian Shaofeng
 * @created on 2017/3/7.
 */
public class Mytest {
    public class Student{
        int i;
    }
    @Test
    public void test(){
        HashMap<Integer, Boolean> hashMap = new HashMap<>();
        System.out.println(hashMap.get(0));
    }

}
