package com.roger.springbootcameltest;

import org.apache.camel.Exchange;
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
        // this.moveAllFiles();
        this.moveSpecificFile("song");
        System.out.println(" === 執行Camel - configure 結束 === ");
    }

    /**
     * 移動目錄下所有檔案
     */
    private void moveAllFiles() {
        String currentDir = System.getProperty("user.dir");
        // 1. 移動目錄下的檔案們到另一目錄
        from("file:" + currentDir + "/Files_Origin?noop=true") // "noop=true" 這個指示告訴路由器保留（而不是刪除）相關的設定。
               .to("file:" + currentDir + "/Files_Destination");
    }

    /**
     * 移動指定檔案
     */
    private void moveSpecificFile(String keyword) {
        from("file:" + System.getProperty("user.dir") + "/Files_Origin?noop=true")
                .filter(header(Exchange.FILE_NAME).startsWith(keyword))
        .to("file:" + System.getProperty("user.dir") + "/Files_Destination");
    }
}
