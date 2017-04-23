import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.Spider;
import us.codecraft.webmagic.pipeline.JsonFilePipeline;
import us.codecraft.webmagic.processor.PageProcessor;

/**
 * Created by 11981 on 2017/4/18.
 * need to complete
 */
public class GIthubBlogProcessor implements PageProcessor {

    private static final String URL_POST = "http://sunguangchao\\.github\\.io/\\d+/\\d+/\\d+/\\w+/";

    private Site site = Site.me()
            .setRetryTimes(3)
            .setSleepTime(3000);

    public void process(Page page){
        if (!page.getUrl().regex("http://sunguangchao\\.github\\.io/\\d+/\\d+/\\d+/\\w+/").match()){
            page.addTargetRequests(page.getHtml().xpath("//main[@id='main']//section[@id='posts']").links()
                    .regex("http://yoursite\\.com/\\d+/\\d+/\\d+/\\w+/")
                    .replace("http://yoursite\\.com", "http://sunguangchao\\.github\\.io").all());
    //        page.addTargetRequests(page.getHtml().links().regex(URL_POST).all());
        }else{
            page.putField("title", page.getHtml().xpath("//h1[@class='post-title']"));
            page.putField("content", page.getHtml().xpath("//div[@class='post-body']"));
        }

    }

    public Site getSite(){
        return site;
    }

    public static void main(String[] args){
        Spider.create(new GIthubBlogProcessor())
                .addUrl("http://sunguangchao.github.io/")
               .addPipeline(new JsonFilePipeline("F:\\wait_to_realise\\spider"))
                .run();
    }

}
