package com.atguigu.gmall0422.manage.controller;

import org.apache.commons.lang3.StringUtils;
import org.csource.common.MyException;
import org.csource.fastdfs.ClientGlobal;
import org.csource.fastdfs.StorageClient;
import org.csource.fastdfs.TrackerClient;
import org.csource.fastdfs.TrackerServer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOError;
import java.io.IOException;

/**
 * @program: gmall0422
 * @description: 文件上传
 * @author: Mr.Lei
 * @create: 2019-10-09 20:47
 **/
@RestController
@CrossOrigin  //跨域访问  还可以使用httpclient jsonp
public class FileUploadController {

    //获取 application.properties中的 key对应的value值
    @Value("${fileServer.url}")
    private String fileServerUrl;

    /**
     * 实现文件上传
     */
    //http://localhost:8082/fileUpload
    @RequestMapping("fileUpload")
    public String fileupload(MultipartFile file) throws IOException,MyException { //返回文件的url

        //定义返回的文件路径
        String path =fileServerUrl;

        //先判断文件不为空
        if(file!=null) {
            //读取配置文件tracker.conf
            String configFile = this.getClass().getResource("/tracker.conf").getFile();
            ClientGlobal.init(configFile);

            TrackerClient trackerClient = new TrackerClient();
            TrackerServer trackerServer = trackerClient.getConnection();
            StorageClient storageClient = new StorageClient(trackerServer, null);

            //  String orginalFilename = "d://1.jpg";
            //获取上传的文件名
            String originalFilename = file.getOriginalFilename();
            //获取文件的后缀
            String extName = StringUtils.substringAfterLast(originalFilename, ".");
            //因为要传入的第一个参数是本地的文件路径 无法获取到存放到本地磁盘的路径
            //所以使用byte[]的参数  ；第二和第三个参数不变
            String[] upload_file = storageClient.upload_file(file.getBytes(), extName, null);

            for (int i = 0; i < upload_file.length; i++) {
                //s = group1
                //s = M00/00/00/wKhYQl2d02WAYG13AAErHNqBYMA283.jpg
                String s = upload_file[i];
                //将返回的路径拼接到Path中
                path+= "/"+s;
//              System.out.println("s = " + s);
            }
        }

        //文件url由三部分组成  server.ip + groupName + MOO/00/00文件名
        return path;
    }


}

