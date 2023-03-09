package com.roger.springbootcameltest;

import org.apache.camel.Exchange;
import org.apache.camel.Predicate;
import org.apache.camel.builder.RouteBuilder;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.text.MessageFormat;
import java.util.Arrays;
import java.util.function.BiFunction;
import java.util.stream.Collectors;

@SpringBootApplication
public class SpringBootCamelTestApplication extends RouteBuilder {

    public static void main(String[] args) {
        SpringApplication.run(SpringBootCamelTestApplication.class, args);
    }

    @Override
    public void configure() throws Exception {
        System.out.println(" === 執行Camel - configure 開始 === ");
        // this.moveAllFiles();
        // this.moveSpecificFile(header(Exchange.FILE_NAME).startsWith("song"));
        // this.moveSpecificFileWithContentStartsWith("紅");
        // this.moveSpecificFileWithContentContains("窮");
        this.moveFilesWithProcess();
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
    private void moveSpecificFile(Predicate predicate) {
        from("file:" + System.getProperty("user.dir") + "/Files_Origin?noop=true")
                .filter(predicate)
                .to("file:" + System.getProperty("user.dir") + "/Files_Destination");
    }

    /**
     * 根據檔案內容 startsWith 決定搬移的檔案
     */
    private void moveSpecificFileWithContentStartsWith(String content) {
        from("file:" + System.getProperty("user.dir") + "/Files_Origin?noop=true")
                .filter(body().startsWith(content))
                .to("file:" + System.getProperty("user.dir") + "/Files_Destination");
    }

    /**
     * 根據檔案內容 contains 決定搬移的檔案
     */
    private void moveSpecificFileWithContentContains(String content) {
        from("file:" + System.getProperty("user.dir") + "/Files_Origin?noop=true")
                .filter((exchange -> {
                    String bodyContent = exchange.getIn().getBody(String.class);
                    System.out.println("文檔內容：" + bodyContent);
                    return bodyContent.contains(content);
                }))
                .to("file:" + System.getProperty("user.dir") + "/Files_Destination");
    }

    /**
     * 搬移的檔案 & 處理內容
     */
    private void moveFilesWithProcess() {
        from("file:" + System.getProperty("user.dir") + "/Files_Origin?noop=true")
                .filter(header(Exchange.FILE_NAME).startsWith("text"))
                .process(exchange -> {
                    String bodyContent = exchange.getIn().getBody(String.class);
                    String processResult = Arrays.stream(bodyContent.split(" ")).collect(Collectors.joining(" , "));
                    exchange.getIn().setBody(processResult);
                })
                .to("file:" + System.getProperty("user.dir") + "/Files_Destination")
                .to("file:" + System.getProperty("user.dir") + "/Files_Destination?filename=myData.csv");
    }
}
