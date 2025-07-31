package org.hrc.backblog.controller;

import org.apache.commons.lang3.StringUtils;
import org.hrc.backblog.utils.QiniuUtils;
import org.hrc.backblog.vo.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

@RestController
@RequestMapping("upload")
public class UploadController {
    @Autowired
    private QiniuUtils qiniuUtils;

    /**
     * 上传图片到七牛云对象存储
     * @param file
     * @return
     */
    @PostMapping
    public Result upload(@RequestParam("image")MultipartFile file){
        String fileName = UUID.randomUUID() + "." + StringUtils.substringAfterLast(file.getOriginalFilename(), ".");
        boolean upload = qiniuUtils.upload(file, fileName);
        if (upload){
            return Result.success(QiniuUtils.url + fileName);
        }
        return Result.fail(20001,"上传失败");
    }
}
