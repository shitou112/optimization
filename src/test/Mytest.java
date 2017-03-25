package test;

import org.junit.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * @author Qian Shaofeng
 * @created on 2017/3/7.
 */
public class Mytest {

    List<Integer> l ;

    public List add(int id){
        l = new ArrayList<>();
        l.add(id);
        return l;
    }

    @Test
    public void test(){
        System.out.println(Math.log(10));
    }

}
