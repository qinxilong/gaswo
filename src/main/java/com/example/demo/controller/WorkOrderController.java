package com.example.demo.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.example.demo.common.annotation.Log;
import com.example.demo.common.enums.BusinessType;
import com.example.demo.common.utils.StringUtils;
import com.example.demo.config.MinioConfig;
import com.example.demo.domain.SysUser;
import com.example.demo.domain.WorkOrder;
import com.example.demo.domain.WorkOrderRequest;
import com.example.demo.response.ResponseBase;
import com.example.demo.response.ResponseData;
import com.example.demo.response.WorkOrderDetailRequesParams;
import com.example.demo.response.WorkOrderRequestBaseParams;
import com.example.demo.service.ISysUserService;
import com.example.demo.service.MinioService;
import com.example.demo.service.WorkOrderService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

@Api(tags = "WorkOrderController")
@RequestMapping("/qy/gas")
@RestController
public class WorkOrderController {

    @Autowired
    private WorkOrderService workOrderService;

    @Autowired
    private MinioService minioService;

    @Autowired
    private MinioConfig minioConfig;

    @Autowired
    private ISysUserService userService;

    /**
     * 工单列表
     * @param params
     * @return
     */
    @ApiOperation(value = "获取用户的工单列表")
    @GetMapping("/workOrder/list")
    public ResponseData selectList(@ModelAttribute WorkOrderRequestBaseParams params) {
        SysUser sysUser = userService.getSysUser();
//        System.out.println(sysUser);
        if(sysUser==null){
            return (ResponseData) ResponseData.fail("用户不存在");
        }
        ResponseData responseData = workOrderService.selectList(params,sysUser);
        if(responseData.getData()!=null){
           return responseData;
        }else{
            return (ResponseData) ResponseData.noContent();
        }
    }

    /**
     *
     *  新增工单信息
     */
    @ApiOperation(value = "工单下发")
    @Log(title = "工单下发", businessType = BusinessType.INSERT)
    @PostMapping("/workOrder/add")
    public ResponseBase add(@RequestBody  WorkOrder workOrder) {
        if(workOrder!=null&& StringUtils.isEmpty(workOrder.getProcessPerson())){
            return  ResponseData.fail("处理人不能为空");
        }

        SysUser sysUser = userService.getSysUser();
//        System.out.println(sysUser);
        if(sysUser==null){
            return  ResponseData.fail("用户不存在");
        }
        if(!sysUser.getRoleName().equals("admin")){
            return  ResponseData.fail("用户权限不够");
        }
        workOrder.setDispatcher(sysUser.getUserName());
        boolean workOrderExist = workOrderService.selectExist(workOrder);
        if(!workOrderExist){//工单信息不存在
            try{
                ResponseBase ResponseBase = workOrderService.insert(workOrder);
                return ResponseBase;
            }catch (Exception e){
//                System.out.println(e);
                return  ResponseBase.fail(e.getMessage());
            }
        }else{
            return  ResponseBase.fail("工单信息已经存在");
        }
    }

    /**
     * 更新工单信息
     */
    @ApiOperation(value = "工单更新")
//    @Log(title = "工单更新", businessType = BusinessType.UPDATE)
    @PostMapping("/workOrder/update")
    public ResponseBase update(@RequestBody WorkOrder workOrder) {
        boolean workOrderExist = workOrderService.selectExist(workOrder);
        if(workOrderExist) {//工单信息存在
            return workOrderService.update(workOrder);
        }else {
            return  ResponseBase.fail("工单信息不存在");
        }
    }

    /**
     * 工单详情
     * @param params
     * @return
     */
    @ApiOperation(value = "工单详情")
    @GetMapping("/workOrder/detail")
    public ResponseData detail(@ModelAttribute WorkOrderDetailRequesParams params) {
        WorkOrder workOrder = workOrderService.selectOne(params);
        if(workOrder!=null){
            return (ResponseData) ResponseData.ok(workOrder);
        }else{
            return (ResponseData) ResponseData.noContent();
        }
    }

    /**
     * 处理工单结果提交
     * @param files
     * @param workOrderRequest
     * @return
     */
    @ApiOperation(value = "工单提交")
    @Log(title = "工单提交", businessType = BusinessType.SUBMIT)
    @PostMapping("workOrder/submit")
    public ResponseBase workOrderSubmit (@RequestParam("files") MultipartFile[] files,@ModelAttribute WorkOrderRequest workOrderRequest) {
        SysUser sysUser = userService.getSysUser();
//        System.out.println(sysUser);
        if(sysUser==null){
            return ResponseBase.fail("用户信息不存在");
        }
        WorkOrder workOrder = new WorkOrder(workOrderRequest);
        boolean workOrderExist = workOrderService.selectExist(workOrder);
        if(!workOrderExist) {//工单信息不存在
            return  ResponseBase.fail("工单信息不存在");
        }

        List<String> fileUrls = new ArrayList<>();
        // 遍历接收到的文件
        for (MultipartFile file : files) {
            // 进行文件处理，例如保存到服务器的文件系统中
            // 处理完毕后获取文件地址
            String fileName = file.getOriginalFilename();
//            System.out.println(fileName);
            String fileUrl = processPicture(file,minioConfig.getBucketsName(),sysUser.getUserName(),workOrderRequest.getWorkOrderCode(),fileName);
            if(fileUrl!=null && !fileUrl.equals("")){
                fileUrls.add(fileUrl);
            }
        }
        String joinedUrls = String.join(";", fileUrls);
//        System.out.println(joinedUrls);
        workOrder.setPictures(joinedUrls);
        return  workOrderService.updateWorkOrder(workOrder);
    }

    /**
     * 上传文件到 minio
     * @param file
     * @param bucketName
     * @param userName
     * @param workOrder
     * @param fileName
     * @return
     */
    private String processPicture(MultipartFile file, String bucketName,String userName,String workOrder ,String fileName){
        try {
            minioService.uploadFile(file, bucketName, userName ,workOrder,fileName);
            String url = minioService.getFixedFileUrl(bucketName, userName, workOrder, fileName);
            return url;
        } catch (Exception e) {
//            System.out.println(e.getMessage());
            return  "";
        }
    }

}
