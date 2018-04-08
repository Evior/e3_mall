package cn.e3mall.fast;

import org.csource.fastdfs.*;
import org.junit.Test;

public class testFast {

    @Test
    public void testFast() throws Exception{
        //创建一个配置文件 文件名任意  内容就是tracker的服务器的地址
        ClientGlobal.init("D:\\学习备份\\e3_parent\\fastdfs_client\\src\\main\\resources\\fdfs_client.conf");
        //创建一个trackerClient对象
        TrackerClient trackerClient = new TrackerClient();
        //通过trackerClient获取一个trackerServer对象
        TrackerServer trackerServer = trackerClient.getConnection();
        //创建storageServer的引用
        StorageServer storageServer=null;
        //创建一个StorageClient的引用 可以是null
        StorageClient storageClient = new StorageClient(trackerServer, storageServer);
        //使用storageClient对象上传文件
        String[] file = storageClient.upload_file("C:\\Users\\Administrator\\Desktop\\图片\\a1fa35447c.jpg", "jpg", null);
        for (String str:file)
        {
            System.out.println(str);
        }
    }
}
