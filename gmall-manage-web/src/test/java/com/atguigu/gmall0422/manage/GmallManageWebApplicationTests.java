package com.atguigu.gmall0422.manage;

import org.csource.common.MyException;
import org.csource.fastdfs.ClientGlobal;
import org.csource.fastdfs.StorageClient;
import org.csource.fastdfs.TrackerClient;
import org.csource.fastdfs.TrackerServer;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;

@RunWith(SpringRunner.class)
@SpringBootTest
public class GmallManageWebApplicationTests {

	/*
		测试文件上传到fastDFS上
	 */
	@Test
	public void textFileUpload() throws IOException, MyException {
		//读取配置文件tracker.conf
		String file = this.getClass().getResource("/tracker.conf").getFile();
		ClientGlobal.init(file);
		TrackerClient trackerClient=new TrackerClient();
		TrackerServer trackerServer=trackerClient.getConnection();
		StorageClient storageClient=new StorageClient(trackerServer,null);

		String orginalFilename="d://1.jpg";
		String[] upload_file = storageClient.upload_file(orginalFilename, "jpg", null);

		for (int i = 0; i < upload_file.length; i++) {
			//s = group1
			//s = M00/00/00/wKhYQl2d02WAYG13AAErHNqBYMA283.jpg
			String s = upload_file[i];
			System.out.println("s = " + s);
		}
	}

}
