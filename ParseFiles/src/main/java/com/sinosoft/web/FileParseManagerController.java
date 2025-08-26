package com.sinosoft.web;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.sinosoft.entity.Courseware;
import com.sinosoft.entity.Task;
import com.sinosoft.service.ICachePoolService;
import com.sinosoft.service.ICoursewareService;
import com.spire.doc.Document;
import com.spire.doc.FileFormat;
import com.spire.doc.documents.ImageType;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/file")
public class FileParseManagerController {

    @Resource
    private ICachePoolService cachePoolService;

//    @Resource
//    private ICoursewareService coursewareService;
//
//    @RequestMapping
//    public String text() {
//        List<Courseware> coursewares = coursewareService.list();
//        for (Courseware courseware : coursewares) {
//            System.out.println(courseware.getId());
//        }
//        return ";";
//    }

    @PostMapping("/task/add")
    public String addTask(@RequestBody Task task) {
        System.out.println(JSON.toJSONString(task));
//        JSONObject jsonObject = JSONObject.parseObject(task);
//        System.out.println(jsonObject.get("request_id"));
//        System.out.println(jsonObject.get("file"));
//
        cachePoolService.addQueueTask(JSON.toJSONString(task));

        return "ok";
    }

    @GetMapping("/picturesPreview")
    public String picturesPreview(@RequestParam(required = false) Integer requestId,
                                  @RequestParam(required = false) String callBack,
                                  @RequestParam(required = true) String filePath) {
        if (!filePath.endsWith(".docx")) {
            return filePath + ":格式错误，必须时 .docx 文件";
        }

        File file = new File(filePath);
        if(!(file.exists() && file.isFile())) {
            System.out.println("文件不存在或格式错误");
            return filePath + ":文件不存在或格式错误";
        }
        // TODO: 将目录下的 word 转变为 svg
        Document document = new Document();
        //加载Word文档
        document.loadFromFile(filePath);

//        document.saveToFile(filePath + ".pdf", FileFormat.PDF);
//        document.saveToFile(filePath + ".pdf.svg", FileFormat.SVG);
        BufferedImage image = document.saveToImages( 0, ImageType.Bitmap);
        File file1 = new File(filePath + ".png");
        try {
            ImageIO.write(image, "PNG", file1); //将Word转为PDF
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return "200," + filePath + ".png";
    }

}
