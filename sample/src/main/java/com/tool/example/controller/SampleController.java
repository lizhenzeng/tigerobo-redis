package com.tool.example.controller;

import com.tool.example.entity.Sample;
import com.tool.example.service.SampleSevice;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;

@RestController
@RequestMapping("/sample")
public class SampleController {


    @Autowired
    SampleSevice sampleSevice;

    @Autowired
    RedisTemplate<String,String> template;

    @RequestMapping(path = {"/put/{id}/{name}"}, method = RequestMethod.GET)
    @ApiOperation(value = "测试put", tags = {"测试put"})
    public void queryHouseInfoList(@PathVariable("id") Long id, @PathVariable("name") String name, HttpServletResponse response) throws IOException {
        Sample sample = new Sample();
        sample.setKey(name);
//        template.opsForValue().setIfAbsent("123","123");
//        template.delete("123");
        Sample res = sampleSevice.getResponseStr(id, name, sample);
        String res1 = sampleSevice.removeResponseStr(id, name, sample);
        String incrValue = sampleSevice.setIncrValue(id, name, sample);
        OutputStream outputStream = response.getOutputStream();
        response.setHeader("content-type", "text/html;charset=UTF-8");
//        byte[] dataByteArr = String.format("{\"code\":0,\"msg\":\"%s\"}", res).getBytes("UTF-8");
//        outputStream.write(dataByteArr);

    }


}