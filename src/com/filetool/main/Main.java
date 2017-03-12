package com.filetool.main;

import com.cacheserverdeploy.deploy.Deploy;
import com.filetool.util.FileUtil;
import com.filetool.util.LogUtil;

/**
 * 
 * main 
 * 
 * @version  [version, 2017-1-9]
 * @see  [method]
 * @since  [huawei]
 */
public class Main
{
    public static void main(String[] args)
    {
        if (args.length != 2)
        {
            System.err.println("please input args: graphFilePath, resultFilePath");
            return;
        }

        String graphFilePath = args[0];
        String resultFilePath = args[1];

        LogUtil.printLog("Begin");

        // input
        String[] graphContent = FileUtil.read(graphFilePath, null);

        // to do implement
        String[] resultContents = Deploy.deployServer(graphContent);

        // output
        if (hasResults(resultContents))
        {
            FileUtil.write(resultFilePath, resultContents, false);
        }
        else
        {
            FileUtil.write(resultFilePath, new String[] { "NA" }, false);
        }
        LogUtil.printLog("End");
    }
    
    private static boolean hasResults(String[] resultContents)
    {
        if(resultContents==null)
        {
            return false;
        }
        for (String contents : resultContents)
        {
            if (contents != null && !contents.trim().isEmpty())
            {
                return true;
            }
        }
        return false;
    }

}
