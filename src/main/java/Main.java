import com.gargoylesoftware.htmlunit.*;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.google.gson.*;
import java.io.IOException;

/**
 * Created by LiuYang on 2022/10/22 19:49
 * Email:1024839103ly@gmail.com
 */

public class Main {
    public static void main(String[] args) {
        //爬虫框架：WebClient
        //设置爬虫
        WebClient webClient = new WebClient(BrowserVersion.CHROME);
        webClient.getOptions().setCssEnabled(false); // CSS 支持 ✔
        webClient.getOptions().setJavaScriptEnabled(false); // JavaScript支持 ✔
        webClient.getOptions().setThrowExceptionOnScriptError(false);//js运行错误时不抛出异常
        webClient.getOptions().setTimeout(10000);//设置连接超时时间,单位：ms
        webClient.getOptions().setDoNotTrackEnabled(false);//是否抛异常

        /**
         * 请求接口：http://qc.wa.news.cn/nodeart/list?nid=11227931&pgnum=2&cnt=10&attr=&tp=1&orderby=1
         * pgnum:页码
         * cnt:每次回传的数量
         */

        int cnt = 0;//对已经爬取到的新闻进行计数
        for (int pgnum = 1; pgnum <= 3; pgnum++) {
            //拼接真实请求地址
            String url = "http://qc.wa.news.cn/nodeart/list?nid=11227931&pgnum=" + pgnum + "&cnt=10&attr=&tp=1&orderby=1";
            try {
                HtmlPage page = webClient.getPage(url); // 发起请求
                JsonObject json = new JsonParser().parse(page.asText().replaceAll("\\(", "").replaceAll("\\)", "")).getAsJsonObject();//将请求回来的数据进行清洗并转换为Json格式
                JsonObject data = json.getAsJsonObject("data");//取数据
                JsonArray list = data.getAsJsonArray("list");//取数据，因为数据有多条，所以转换为JSON数组，方便后期遍历
                for (int i = 0; i < list.size(); i++) {
                    JsonObject news = list.get(i).getAsJsonObject();//将JSON数组的每一个下标的数据转换为JSON对象
                    cnt++;//计数
                    System.out.printf("序号：%d，标题：%s，发布时间：%s\n", cnt, news.get("Title"), news.get("PubTime"));//获取数据并打印
                }
            } catch (FailingHttpStatusCodeException | IOException e) {
                e.printStackTrace();//异常处理
            } finally {
                webClient.close(); // 关闭客户端，释放内存
            }
        }
    }

}
