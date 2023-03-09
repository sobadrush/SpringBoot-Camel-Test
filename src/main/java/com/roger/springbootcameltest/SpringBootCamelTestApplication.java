package com.roger.springbootcameltest;

import org.apache.camel.builder.RouteBuilder;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.text.MessageFormat;

@SpringBootApplication
public class SpringBootCamelTestApplication extends RouteBuilder {

    public static void main(String[] args) {
        SpringApplication.run(SpringBootCamelTestApplication.class, args);
    }

    @Override
    public void configure() throws Exception {
        System.out.println(" === 執行Camel - configure 開始 === ");
        String currentDir = System.getProperty("user.dir");
        System.err.println(MessageFormat.format("currentDir = {0}", currentDir));
        // 1. 移動目錄下的檔案們到另一目錄
        from("file:" + currentDir + "/Files_Origin?noop=true") // "noop=true" 這個指示告訴路由器保留（而不是刪除）相關的設定。
                .to("file:" + currentDir + "/Files_Destination?noop=true");
        System.out.println(" === 執行Camel - configure 結束 === ");
    }

}
