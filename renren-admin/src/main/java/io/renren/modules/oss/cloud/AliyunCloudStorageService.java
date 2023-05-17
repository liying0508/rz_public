/**
 * Copyright (c) 2018 人人开源 All rights reserved.
 *
 * https://www.renren.io
 *
 * 版权所有，侵权必究！
 */

package io.renren.modules.oss.cloud;

import com.aliyun.oss.ClientException;
import com.aliyun.oss.OSSClient;
import com.aliyun.oss.OSSException;
import com.aliyun.oss.model.*;
import io.renren.common.exception.ErrorCode;
import io.renren.common.exception.RenException;
import io.renren.common.utils.OssFileDealUtil;
import oracle.sql.BLOB;
import org.apache.commons.lang3.StringUtils;
import sun.misc.BASE64Decoder;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 阿里云存储
 *
 * @author Mark sunlightcs@gmail.com
 */
public class AliyunCloudStorageService extends AbstractCloudStorageService{

    public AliyunCloudStorageService(CloudStorageConfig config){
        this.config = config;
    }

    @Override
    public String upload(byte[] data, String path) {
        return upload(new ByteArrayInputStream(data), path);
    }

    @Override
    public String upload(InputStream inputStream, String path) {
        OSSClient client = new OSSClient(config.getAliyunEndPoint(), config.getAliyunAccessKeyId(),
                config.getAliyunAccessKeySecret());
        try {
            client.putObject(config.getAliyunBucketName(), path, inputStream);
            client.shutdown();
        } catch (Exception e){
            throw new RenException(ErrorCode.OSS_UPLOAD_FILE_ERROR, e, "");
        }

        return config.getAliyunDomain() + "/" + path;
    }

    @Override
    public String uploadSuffix(byte[] data, String suffix) {
        return upload(data, getPath(config.getAliyunPrefix(), suffix));
    }

    @Override
    public String uploadSuffix(InputStream inputStream, String suffix) {
        return upload(inputStream, getPath(config.getAliyunPrefix(), suffix));
    }

    @Override
    public HttpServletResponse downLoad(HttpServletRequest request, HttpServletResponse response, String url,
                              String fileName){
//        //https://rongziguanli.oss-cn-shenzhen.aliyuncs.com/20220808/de4d1ccfd0ae4711857d561ea1f83915.doc
        OSSClient ossClient = new OSSClient(config.getAliyunEndPoint(), config.getAliyunAccessKeyId(),
                config.getAliyunAccessKeySecret());
//        String substring = StringUtils.substring(url, 50);
//        OSSObject o = client.getObject(config.getAliyunBucketName(),
//                substring);
        try {
//            String fileName="txt.jpg";
            String ossKey=StringUtils.substring(url, 57);
            // 从阿里云进行下载
            OSSObject ossObject = ossClient.getObject(config.getAliyunBucketName(),ossKey);//bucketName需要自己设置
            // 已缓冲的方式从字符输入流中读取文本，缓冲各个字符，从而提供字符、数组和行的高效读取
            BufferedReader reader = new BufferedReader(new InputStreamReader(ossObject.getObjectContent()));

            InputStream inputStream = ossObject.getObjectContent();

            //缓冲文件输出流
            BufferedOutputStream outputStream=new BufferedOutputStream(response.getOutputStream());
            //通知浏览器以附件形式下载
            // response.setHeader("Content-Disposition","attachment;filename="+ URLEncoder.encode(fileName,"UTF-8"));
            // 为防止 文件名出现乱码
            // 得到文件格式
            String substring = url.substring(url.lastIndexOf(".") + 1);
            response.setContentType("application/"+substring);
            final String userAgent = request.getHeader("USER-AGENT");
            if(StringUtils.contains(userAgent, "MSIE")){//IE浏览器
                fileName = URLEncoder.encode(fileName,"UTF-8");
            }else if(StringUtils.contains(userAgent, "Mozilla")){//google,火狐浏览器
                fileName = new String(fileName.getBytes(), "ISO8859-1");
            }else{
                fileName = URLEncoder.encode(fileName,"UTF-8");//其他浏览器
            }
            response.addHeader("Content-Disposition", "attachment;filename=" +fileName);//这里设置一下让浏览器弹出下载提示框，而不是直接在浏览器中打开


            // 进行解码 如果上传时为了防止乱码 进行解码使用此方法
            BASE64Decoder base64Decoder = new BASE64Decoder();
//            byte[] car;
//            while (true) {
//                String line = reader.readLine();
//                if (line == null) break;
//                car =  base64Decoder.decodeBuffer(line);
//
//                outputStream.write(car);
//            }
//            reader.close();
            byte[] car = new byte[1024];
            int L;

            while((L = inputStream.read(car)) != -1){
                if (car.length!=0){
                    outputStream.write(car, 0,L);
                }
            }

            if(outputStream!=null){
                outputStream.flush();
                outputStream.close();
            }
            ossClient.shutdown();

        } catch (IOException e) {
            e.printStackTrace();

        } catch (OSSException e){

        }
        return null;
    }

    public boolean delete(String url) {
        boolean flag = true;
        OSSClient ossClient = new OSSClient(this.config.getAliyunEndPoint(), this.config.getAliyunAccessKeyId(), this.config.getAliyunAccessKeySecret());
        String ossKey = StringUtils.substring(url, 57);
        try {
            // 删除文件或目录。如果要删除目录，目录必须为空。
            ossClient.deleteObject(config.getAliyunBucketName(), ossKey);
            System.out.println(config.getAliyunBucketName()+""+ossKey);
            return flag;
        } catch (OSSException oe) {
            System.out.println("Error Message:" + oe.getErrorMessage());
            System.out.println("Error Code:" + oe.getErrorCode());
            System.out.println("Request ID:" + oe.getRequestId());
            System.out.println("Host ID:" + oe.getHostId());
            flag = false;
            throw new RenException(10048);
        } catch (ClientException ce) {
            System.out.println("Error Message:" + ce.getMessage());
            flag = false;
            throw new RenException(10048);
        } finally {
            Exception exception = null;
            ossClient.shutdown();
        }
    }

    public boolean clearRubbish() {
        //每一页最大数据量
        int maxKeys = 100;
        //前置url
        String prefixUrl = "https://financingmanagemens.oss-cn-shenzhen.aliyuncs.com/";
        boolean flag = true;
        //服务端文件清单（map：key是文件夹，value的list是该文件夹内的文件url集合）
        Map<String, List<String>> serverFileMap = new HashMap<>();
        //ossclient实例化
        OSSClient ossClient = new OSSClient(this.config.getAliyunEndPoint(), this.config.getAliyunAccessKeyId(),
                this.config.getAliyunAccessKeySecret());
        try {
            ObjectListing objectListing;
            String nextMarker = null;
            do {
                objectListing = ossClient.listObjects((new ListObjectsRequest(this.config.getAliyunBucketName()))
                        .withMarker(nextMarker).withMaxKeys(Integer.valueOf(maxKeys)));
                List<OSSObjectSummary> sums = objectListing.getObjectSummaries();
                for (OSSObjectSummary s : sums) {
                    String key = s.getKey();
                    String documentName = StringUtils.substring(key, 0, 8);
                    List<String> list = new ArrayList<>();
                    list.add(prefixUrl + key);
                    if (!serverFileMap.containsKey(documentName)) {
                        serverFileMap.put(documentName, list);
                    }else{
                        serverFileMap.get(documentName).addAll(list);
                    }
                }
                nextMarker = objectListing.getNextMarker();
            } while (objectListing.isTruncated());
            Map<String, List<String>> localFileMap = OssFileDealUtil.clearSurplusInOSSServer();
            //删除逻辑
            //首先判断服务端的键（文件夹即是日期）是否存在于本地
            for (String documentName : serverFileMap.keySet()) {
                if (localFileMap.containsKey(documentName)){
                    //存在的话，再依次判断对应的值（具体的文件是否存在）
                    for (String url : serverFileMap.get(documentName)) {
                    }
                }else {//如果键不存在的话，先逐条删除里面的文件，然后删除文件夹

                }
            }
            return flag;
        } catch (OSSException oe) {
            flag = false;
            System.out.println("Caught an OSSException, which means your request made it to OSS, but was rejected with an error response for some reason.");
            System.out.println("Error Message:" + oe.getErrorMessage());
            System.out.println("Error Code:" + oe.getErrorCode());
            System.out.println("Request ID:" + oe.getRequestId());
            return flag;
        } catch (ClientException ce) {
            flag = false;
            System.out.println("Caught an ClientException, which means the client encountered a serious internal problem while trying to communicate with OSS, such as not being able to access the network.");
            return flag;
        } finally {
            Exception exception = null;
            ossClient.shutdown();
        }
    }
}