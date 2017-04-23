import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.Spider;
import us.codecraft.webmagic.pipeline.JsonFilePipeline;
import us.codecraft.webmagic.processor.PageProcessor;

import java.util.List;

/**
 * Created by 11981 on 2017/4/21.
 */
public class CSDNBlogProcessor implements PageProcessor{
    private static final String username = "zhangerqing";
    private static int count = 0;

    private Site site = Site.me()
            .setRetryTimes(3)
            .setSleepTime(3000);

    public void process(Page page){
        if (!page.getUrl().regex("http://blog\\.csdn\\.net/" + username + "/article/details/\\d+").match()){
            page.addTargetRequests(page.getHtml().xpath("//div[@id='article_list']").links()
                    .regex("/" + username + "/article/details/\\d+")
                    .replace("/" + username + "/", "http://blog.csdn.net/" + username + "/").all());
            page.addTargetRequests(page.getHtml().xpath("//div[@class='pagelist']").links()
                    .regex("/" + username + "/article/list/\\d+")
                    .replace("/" + username + "/", "http://blog.csdn.net/" + username + "/").all());
        }else{
            count++;
            CSDNBlog csdnBlog = new CSDNBlog();
            csdnBlog.setId(Integer.parseInt(page.getUrl().regex("http://blog\\.csdn\\.net/" + username + "/article/details/(\\d+)").get()));
            csdnBlog.setTitle(page.getHtml().xpath("//div[@class='article_title']//span[@class='link_title']/a/text()").get());
            csdnBlog.setTags(ListToString(page.getHtml().xpath("//div[@class='article_l']/span[@class='link_categories']/a/allText()").all()));
            csdnBlog.setDate(page.getHtml().xpath("//div[@class='article_r']//span[@class='link_postdate']/text()").get());
            csdnBlog.setCategories(ListToString(page.getHtml().xpath("//div[@class='category_r']/label/span/text()").all()));
            csdnBlog.setView(Integer.parseInt(page.getHtml().xpath("//div[@class='article_r']//span[@class='link_view']/text()").regex("(\\d+)人阅读").get()));
            csdnBlog.setComments(Integer.parseInt(page.getHtml().xpath("//div[@class='article_r']//span[@class='link_comments']/text()").regex("\\((\\d+)\\)").get()));
            new CSDNBlogDao().add(csdnBlog);
            System.out.print(csdnBlog);
        }

    }

    public static String ListToString(List<String> stringList){
        if (stringList == null)
            return null;
        StringBuilder result = new StringBuilder();
        boolean flag = false;
        for (String string : stringList){
            if (flag == true){
                result.append(",");
            }else{
                flag = true;
            }
            result.append(string);
        }
        return result.toString();
    }

    public Site getSite(){
        return site;
    }

    public static void main(String[] args){
        long startTime,endTime;
        System.out.println("开始。。。。");
        startTime = System.currentTimeMillis();
        Spider.create(new CSDNBlogProcessor())
                .addUrl("http://blog.csdn.net/zhangerqing")
                .thread(5)
                .run();
        endTime = System.currentTimeMillis();
        System.out.println("共抓取" + count + "篇文章，耗时约" + ((endTime - startTime) / 1000) + "秒");
    }
}
