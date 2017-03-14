package test;

import org.junit.Test;

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
        Mytest mytest = new Mytest();
        Student student = mytest.new Student();
        System.out.println(student.i);
    }

}
