package com.example.demo.service;

import com.example.demo.config.MinioConfig;
import io.minio.*;
import io.minio.http.Method;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;

@Service
public class MinioService {
    @Autowired
    private MinioClient minioClient;

    @Autowired
    private MinioConfig minioConfig;


    /**
     *  上传文件
     * @param file 文件
     * @param bucketName 桶名
     * @param userName 用户
     * @param workOrder 工单
     * @param fileName 文件名
     * @return
     * @throws Exception
     */
    public boolean uploadFile(MultipartFile file, String bucketName,String userName,String workOrder ,String fileName) throws Exception {
//        System.out.println("bucketName: " +bucketName );
//        System.out.println("account: " + userName );
//        System.out.println("workOrder: " + workOrder );
//        System.out.println("fileName: " +fileName );
        //判断文件是否为空
        if (null == file || 0 == file.getSize()) {
            return false;
        }
        //判断存储桶是否存在  不存在则创建
        createBucket(bucketName);
        //开始上传
        minioClient.putObject(
                PutObjectArgs.builder().bucket(bucketName).object(userName + "/" + workOrder +"-" + fileName)
                        .stream(file.getInputStream(), file.getSize(), -1)
                        .contentType(file.getContentType())
                        .build());
        return true;
    }
    /**
     * 创建bucket
     */
    public void createBucket(String bucketName) throws Exception {
        if (!minioClient.bucketExists(BucketExistsArgs.builder().bucket(bucketName).build())) {
            minioClient.makeBucket(MakeBucketArgs.builder().bucket(bucketName).build());
        }
    }


    /**
     * 删除⽂件
     *
     * @param bucketName bucket名称
     * @param objectName ⽂件名称
     * @throws Exception https://docs.minio.io/cn/java-client-apireference.html#removeObject
     */
    public void removeFile(String bucketName, String objectName) throws Exception {
        minioClient.removeObject(RemoveObjectArgs.builder().bucket(bucketName).object(objectName).build());
    }


    /**
     * 上传多媒体文件（图片、视频）
     */
    public boolean uploadMultiMediaFile(MultipartFile file, String bucketName,String accountPackage ,String fileName) throws Exception {
//        System.out.println("bucketName: " +bucketName );
//        System.out.println("accountPackage: " +accountPackage );
//        System.out.println("fileName: " +fileName );
        //判断文件是否为空
        if (null == file || 0 == file.getSize()) {
            return false;
        }
        //判断存储桶是否存在  不存在则创建
        createBucket(bucketName);
        //开始上传
        minioClient.putObject(
                PutObjectArgs.builder().bucket(bucketName).object(accountPackage+"/"+fileName)
                        .stream(file.getInputStream(), file.getSize(), -1)
                        .contentType(file.getContentType())
                        .build());
        return true;
    }


    /**
     * 获取文件的访问路径
     * @param bucketName
     * @param account
     * @param fileName
     * @return
     * @throws Exception
     */
    public String getMultiMediaFileUrl(String bucketName,String account,String workOrder,String fileName) {
//        System.out.println("bucketName: " + bucketName );
//        System.out.println("account: " + account );
//        System.out.println("workOrder: " + workOrder );
//        System.out.println("fileName: " + fileName );
        if(bucketName.equals("")||workOrder.equals("")||fileName.equals("")) {
            return "";
        }

        boolean bucketExists = false;
        boolean hasAccess = false;
        try {
            // 检查Bucket是否存在
            bucketExists = minioClient.bucketExists(BucketExistsArgs.builder().bucket(bucketName).build());
//            System.out.println("Bucket存在：" + bucketExists);
            if(bucketExists) {
                // 检查对象访问权限
                hasAccess = minioClient.statObject(StatObjectArgs.builder().bucket(bucketName).object(account + "/" +workOrder+ "-"+fileName).build()) != null;
//                System.out.println("有访问权限：" + hasAccess);
            }else{
                return "";
            }
        } catch (Exception e) {
            return "";
        }
        String url = "";
        if(bucketExists&&hasAccess){
            try {
                url = minioClient.getPresignedObjectUrl(GetPresignedObjectUrlArgs.builder()
                        .method(Method.GET)
                        .bucket(bucketName)
                        .object(account+"/" + workOrder+ "-"+fileName)
                        .expiry(60*60)//有效时间1个小时
                        .build());
            } catch (Exception e) {
                //throw new RuntimeException(e);
            }
//            System.out.println("url: " + url);
        }
        return url;
    }




    /**
     * 获取文件的访问路径
     * @param bucketName
     * @param userName
     * @param fileName
     * @return
     * @throws Exception
     */
    public String getFixedFileUrl(String bucketName,String userName,String workOrder,String fileName) {
        System.out.println("bucketName: " + bucketName );
        System.out.println("account: " + userName );
        System.out.println("workOrder: " + workOrder );
        System.out.println("fileName: " + fileName );
        if(bucketName.equals("")||workOrder.equals("")||fileName.equals("")) {
            return "";
        }
        String url = minioConfig.getUrl() + bucketName +"/"+ userName+"/"+workOrder + "-"+ fileName;
        return url;
    }


    /**
     * 删除多媒体文件（图片、视频）
     */
    public boolean deleteMultiMediaFile(String bucketName,String accountPackage ,String fileName){
        boolean bucketExists;//Bucket是否存在
        boolean hasAccess;//对象访问权限
        try {
            bucketExists = minioClient.bucketExists(BucketExistsArgs.builder().bucket(bucketName).build());
            System.out.println("Bucket存在：" + bucketExists);
            if(bucketExists) {
                hasAccess = minioClient.statObject(StatObjectArgs.builder().bucket(bucketName).object(accountPackage + "/" + fileName).build()) != null;
                System.out.println("有访问权限：" + hasAccess);
            }else{
                return false;
            }
        } catch (Exception e) {
            return false;
        }

        if(bucketExists && hasAccess){
            try {
                minioClient.removeObject(RemoveObjectArgs.builder().bucket(bucketName).object(accountPackage+"/"+fileName).build());
            } catch (Exception e) {
                System.out.println(e);
                return  false;
            }
            return true;
        }else{
            return false;
        }
    }
    public String getMinioUrl(){
        return minioConfig.getUrl();
    }

//
//    /**
//     * 下载minio资源
//     * @param params
//     * @return
//     */
//    public ResponseEntity downLoadFile(RequestSensitiveParams params) {
//        String fileName = "";
//        String userAccount = params.getUserAccount();
//        String fileType = params.getFileType();
//
//        if(fileType.equals("own")){
//            fileName = userAccount + "/" +"sensitiveword.txt";
//        }else{
//            fileName = "template/sensitiveword.txt";
//        }
//
//        boolean hasAccess;
//        try {
//            boolean bucketExists = minioClient.bucketExists(BucketExistsArgs.builder().bucket(minioConfig.getLlmBucketsName()).build());
//            System.out.println("Bucket存在：" + bucketExists);
//            if(bucketExists) {
//                hasAccess = minioClient.statObject(StatObjectArgs.builder().bucket(minioConfig.getLlmBucketsName()).object(fileName).build()) != null;
//                System.out.println("有访问权限：" + hasAccess);
//            }else{
//                return  ResponseEntity.ok().body(ResponseBase.fail("文件不存在"));
//            }
//        } catch (Exception e) {
//            return  ResponseEntity.ok().body(ResponseBase.fail("文件不存在"));
//        }
//
//        if(!hasAccess){
//            return ResponseEntity.noContent().build();
//        }
//        GetObjectArgs bucket = GetObjectArgs.builder()
//                .bucket(minioConfig.getLlmBucketsName())
//                .object(fileName)//sensitiveword/template/sensitiveword.txt
//                .build() ;
//        InputStream inputStream = null;
//        try {
//             inputStream = minioClient.getObject(bucket);
//            // 设置下载的响应头
//            HttpHeaders headers = new HttpHeaders();
//            String downLoadFileName = "sensitiveword.txt";
//            if(fileType.equals("own")){
//                downLoadFileName = userAccount+"_" + downLoadFileName;
//            }else{
//                downLoadFileName = "template" + "_" + downLoadFileName;
//            }
//            headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + downLoadFileName);
//            // 创建InputStreamResource对象
//            InputStreamResource resource = new InputStreamResource(inputStream);
//
//            return ResponseEntity.ok()
//                    .headers(headers)
//                    .contentType(MediaType.APPLICATION_OCTET_STREAM)
//                    .body(resource);
//        } catch (Exception e) {
//            return  ResponseEntity.ok().body(ResponseBase.fail("文件下载失败"));
//        }
//    }

}