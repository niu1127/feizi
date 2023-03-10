package com.example.one;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import java.io.File;
import java.io.FileOutputStream;
import java.util.Scanner;
import java.util.regex.*;

public class SoGouImageDownload {
    public static void main(String[] args){
        try {
            System.out.println("注意,此方法不会自动关闭,请手动关闭！");
            Scanner scanner = new Scanner(System.in);
            System.out.println("请输入图片关键字(如美女,黑丝)，用来批量下载图片");
            String URLauthorName = scanner.next();
            System.out.println("请输入y/n,选择是否下载最近的图片");
            String recent = scanner.next();
            System.out.println("请输入需要保存的地方(绝对路径，例如[ d:/某人/addw/ ],此类会自动在盘符下创建你写的文件夹名)");
            String path = scanner.next();
            downLoadImage(1,1,URLauthorName,recent,path);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    private static void downLoadImage(int index,int i,String URLauthorName,String recent,String path)throws Exception{
        String soGouImageURL;
        if ("y".equals(recent)) {
            soGouImageURL = "https://pic.sogou.com/d?query=" + URLauthorName + "&forbidqc=&entityid=&preQuery=&rawQuery=&queryList=&st=&mode=20&did=" + index;
        }else {
            soGouImageURL = "https://pic.sogou.com/d?query=" + URLauthorName + "&forbidqc=&entityid=&preQuery=&rawQuery=&queryList=&st=&did=" + index;
        }
        Document document = Jsoup.connect(soGouImageURL).get();
        Elements elements = document.select("#foot > div.img-album-box > div > div > ul > li > div > a > img");
        for (Element element:elements){
            String name = element.attr("alt");
            Matcher m = Pattern.compile("url=(.*?)\" alt").matcher(element.toString());
            String src = null;
            if(m.find())
                src = m.group(1);
            try {
                System.out.println("正在下载第" + i + "张图片，名称:" + name + ".jpg");
                byte[] bytes = Jsoup.connect(src).ignoreContentType(true).maxBodySize(600000000).timeout(100000).execute().bodyAsBytes();
                //设置图片下载位置
                File filePath = new File(path);
                if (!filePath.exists())
                    filePath.mkdir();
                FileOutputStream fileOutputStream = new FileOutputStream(""+filePath+"/" + i+ "-" + name + ".jpg");
                fileOutputStream.write(bytes);
                i++;
                index++;
            } catch (Exception e) {
                e.printStackTrace();
                System.out.println("下载失败，正在跳过！！！");
                continue;
            }
        }
        downLoadImage(index,i,URLauthorName,recent,path);
    }
}
