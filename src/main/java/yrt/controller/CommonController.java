package yrt.controller;


import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import yrt.utils.R;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.UUID;

@RestController
@RequestMapping("/common")
@Slf4j
public class CommonController {

    @Value("${reggie.path}")
    private String basePath;
    /**
     * 从浏览器下载图片
     * @param file MultipartFile是spring类型，代表HTML中form data方式上传的文件，包含二进制数据+文件名称。
     *             MultipartFile后面的参数名必须为file，因为需要和前端页面的name保持一致，否则不会生效
     * @return
     */
    @PostMapping("/upload")
    public R<String> upload(MultipartFile file){
        //file是一个临时文件，需要转存到指定位置，否则本次请求完成后临时文件删除
        log.info(file.toString());

        //获取原始的文件名
        String originalFilename = file.getOriginalFilename();
        //获取上传的文件后缀
        String suffix = originalFilename.substring(originalFilename.lastIndexOf("."));

        //使用uuid重新生成文件名，防止文件名重复造成文件覆盖
        String fileName = UUID.randomUUID().toString() + suffix;

        //创建一个目录对象
        File dir = new File(basePath);
        //判断目录是否存在
        if (!dir.exists()){
            //目录不存在需要创建
            dir.mkdir();
        }

        try {
            //将临时文件存储到指定位置
            file.transferTo(new File(basePath + fileName));
        } catch (IOException e) {
            return R.error("上传失败");
        }
        return R.success(fileName);
    }

    /**
     * 通过输入流读取文件内容
     * 通过输出流将文件写回浏览器，在浏览器展示图片
     * 关闭输入输出流，释放资源
     * @param name
     * @param response
     */
    @GetMapping("/download")
    public void download(String name, HttpServletResponse response){

        try {
            //输入流，通过输入流读取文件内容
            FileInputStream fileInputStream = new FileInputStream(new File(basePath + name));

            //输出流，通过输出流将文件写回浏览器，在浏览器展示图片
            ServletOutputStream outputStream = response.getOutputStream();

            //代表图片文件
            response.setContentType("image/jpeg");

            int len = 0;
            byte[] bytes = new byte[1024];
            while ((len = fileInputStream.read(bytes)) != -1){
                //向response缓冲区中写入字节，再由Tomcat服务器将字节内容组成Http响应返回给浏览器。
                outputStream.write(bytes,0,len);
                //所储存的数据全部清空
                outputStream.flush();
            }

            //关闭流
            fileInputStream.close();
            outputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
