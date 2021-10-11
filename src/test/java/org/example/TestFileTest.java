package org.example;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.net.URL;

import static org.example.TextFIle.index;
import static org.junit.Assert.*;

public class TestFileTest {
    @Test
    public void testOPEN(){
        String a = System.getProperty("user.dir");
        String refile0=TextFIle.OPEN(a,"OPENTest.txt");
        Assert.assertEquals("This is the file to test the open function",refile0);
    }


    @Test
    public void testSAVE() {
        String a = System.getProperty("user.dir");
        TextFIle.SAVE(new File("SAVETest.txt"),"This is the file to test the save function");
        String refile1 = TextFIle.OPEN(a,"SAVETest.txt");
        Assert.assertEquals("This is the file to test the save function",refile1);
        TextFIle.SAVE(new File("SAVETest.txt"),"");
    }

    @Test
    public void testFIND(){
        String a = System.getProperty("user.dir");
        int k;
        String refile1 = TextFIle.OPEN(a,"FINDTest.txt");
        k = index(refile1,"T");
        Assert.assertEquals(k,0);
    }
}