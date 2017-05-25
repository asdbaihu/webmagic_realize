import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.Spider;
import us.codecraft.webmagic.processor.PageProcessor;

import java.io.DataInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by 11981 on 2017/5/24.
 * 这个爬虫有问题的，没有图片数据下载下来
 */
public class ZhihuProcessor implements PageProcessor {
    private Site site = Site.me().setRetryTimes(10).setSleepTime(5000).setTimeOut(5000)
            .addCookie("www.zhihu.com","unlock_ticket","QUJBTXRpWGJRd2dYQUFBQVlRSlZUZl83Q2xjZkJISHZkZm13R05Jck93eTNFU2IyUE53LWVnPT0=|"
                    + "1460335857|e1d68d4125f73b6280312c3eafa71da1b9fc7cab")
            .addCookie("Domain", "zhihu.com")
            .addCookie("z_c0", "2|1:0|10:1495632479|4:z_c0|92:Mi4wQUJBS2xtU0hIQWtBUUFKTVE3SlFDeVlBQUFCZ0FsVk5YeGROV1FEQ3VORm5zY0w1bVZmUFlnWXM3OGJYakpZSlZn|d08db5ce35eb4d7c3be3a4bd401588dcbbd1880a2cfd2f35a0508358466e4719")
            .setUserAgent("Mozilla/5.0 (Macintosh; Intel Mac OS X 10_7_2) AppleWebKit/537.31 (KHTML, like Gecko) Chrome/26.0.1410.65 Safari/537.31");
    private static final String URL_topAnswer = "https://www\\.zhihu\\.com/topic/\\d+/top-answers";
    private static final String URL_topAnswerPage = "https://www\\.zhihu\\.com/topic/\\d+/top-answers\\?page=\\d";
    //话题索引页
    //https://www.zhihu.com/topic/19551388
    private static final String URL_topic = "^https://www\\.zhihu\\.com/topic/\\d+$";
    //问题的索引
    //https://www.zhihu.com/question/20902967
    private static final String URL_question = "^https://www\\.zhihu\\.com/question/\\d+$";
    //https://www.baidu.com
    private static final String test = "https://www\\.baidu\\.com";
    //https://www.zhihu.com/question/19647535/answer/110944270
    private  static  final String URL_answer = "https://www\\.zhihu\\.com/question/\\d+/answer/\\d+";
    //https://www.zhihu.com/people/dan-wen-hui-10/answers
    private  static  final String URL_user = "https://www\\.zhihu\\.com/people/[\\s\\S]+/answers";
    private String offset = "0";

    public void process(Page page){
        if (page.getUrl().regex(URL_answer).match()){
            List<String> urlList = page.getHtml().xpath("//div[@class=RichContent-inner]//img/@data-original").all();
            String questionTitle = page.getHtml().xpath("//h1[@class=QuestionHeader-title]/text()").toString();
            System.out.println("题目："+questionTitle);
            System.out.println(urlList);
            System.out.println(urlList.size());
            List<String> url = new ArrayList<String>();
            for (int i=0; i < urlList.size(); i =i + 2){
                url.add(urlList.get(i));
            }
            String filePath = "F:\\wait_to_realise\\test\\知乎";

            try{
                downLoadPics(url,questionTitle,filePath);
            }catch (Exception e){
                e.printStackTrace();
            }
        }

    }
    public Site getSite() {
        return site;
    }

    public static void main(String[] args){
        String answerUrl =  "https://www.zhihu.com/question/27761934/answer/164790050";
        Spider.create(new ZhihuProcessor()).addUrl(answerUrl).thread(1).run();
    }

    public static boolean downLoadPics(List<String> imgUrls, String title, String filePath) throws Exception{
        boolean isSuccess = true;
        String dir = filePath + title;
        File fileDir = new File(dir);
        fileDir.mkdirs();

        int i = 1;
        for (String imgUrl : imgUrls){
            URL url = new URL(imgUrl);

            DataInputStream dis = new DataInputStream(url.openStream());
            int x = (int)(Math.random()*1000000);
            String newImageName = dir + "/" + x + "pic" + i + ".jpg";
            FileOutputStream fos = new FileOutputStream(new File(newImageName));
            byte[] buffer = new byte[1024];
            int length;
            System.out.println("正在下载......第 " + i + "张图片......请稍后");
            while ((length = dis.read(buffer)) > 0){
                fos.write(buffer, 0, length);
            }

            dis.close();
            fos.close();
            System.out.println("第 " + i + "张图片下载完毕......");
            i++;
        }
        return isSuccess;
    }
}
