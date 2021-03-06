package BUDGET.core.models;
import static org.apache.sling.api.resource.ResourceResolver.PROPERTY_RESOURCE_TYPE;
import javax.annotation.PostConstruct;
import org.apache.commons.lang3.StringUtils;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.models.annotations.Default;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.InjectionStrategy;
import org.apache.sling.models.annotations.injectorspecific.OSGiService;
import org.apache.sling.models.annotations.injectorspecific.SlingObject;
import org.apache.sling.models.annotations.injectorspecific.ValueMapValue;
import org.apache.sling.settings.SlingSettingsService;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.HttpResponseException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;

import com.adobe.agl.impl.StringUCharacterIterator;
import com.day.cq.wcm.api.Page;
import com.day.cq.wcm.api.PageManager;

import java.io.IOException;
import java.util.Optional;

@Model(adaptables = Resource.class,
        defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
public class RewardTracker {

    @ValueMapValue(name=PROPERTY_RESOURCE_TYPE)
    @Default(values="No resourceType")
    protected String resourceType;

    @OSGiService
    private SlingSettingsService settings;
    @SlingObject
    private Resource currentResource;
    @SlingObject
    private ResourceResolver resourceResolver;

    @ValueMapValue
    private String title;

    @ValueMapValue
    private String target;

    @ValueMapValue
    private String current;


    private String message;

    @PostConstruct
    protected void init() {
        PageManager pageManager = resourceResolver.adaptTo(PageManager.class);
        String currentPagePath = Optional.ofNullable(pageManager)
                .map(pm -> pm.getContainingPage(currentResource))
                .map(Page::getPath).orElse("");

        message = "Hello World!\n"
            + "Resource type is: " + resourceType + "\n"
            + "Current page is:  " + currentPagePath + "\n"
            + "This is instance: " + settings.getSlingId() + "\n";
    }

    public String getTitle(){
        return StringUtils.isNotBlank(title) ? title : "rewards Default Title " ;

    }

    public String getApiGetData() throws HttpResponseException, IOException{
        String body="{\"email\": \"bajaj@gmail.com\",\"pswd\": \"12345aem\"}";
        String url="https://crudcrud.com/api/31e0305031ef42b288e30f416b288be5/usersaem";
        HttpResponse response=invokeRestCall("GET",url ,body);
        String responseString = new BasicResponseHandler().handleResponse(response);
        return responseString;
        
    }
   
    public String getApiPostData() throws HttpResponseException, IOException{
        String body="{\"email\": \"ashish@ymail.com\",\"pswd\": \"Jaykumar\"}";
        String url="https://crudcrud.com/api/31e0305031ef42b288e30f416b288be5/usersaem";
        HttpResponse response=invokeRestCall("POST",url ,body);
        String responseString = new BasicResponseHandler().handleResponse(response);
        return "postt " + responseString + "response "+ response;
        
        
    }
    public String getTarget(){
        return StringUtils.isNotBlank(target) ? target : "100" ;

    }

    public String getCurrent(){
        return StringUtils.isNotBlank(current) ? current : "60" ;

    }

    public int getPercent(){
        int cv=Integer.parseInt(current);
        int tv=Integer.parseInt(target);
        int percent=(cv*100)/tv ;
        return  0+percent ;

    }

    public String getMessage() {
        return message;
    }

    public HttpResponse invokeRestCall(String HTTPMethod,String url, String body) throws ClientProtocolException, IOException
    {
        CloseableHttpClient httpclient = HttpClients.createDefault();
        HttpResponse response=null;
        if(HTTPMethod=="GET")
        {
            
            HttpGet method = new HttpGet(url);
            response= httpclient.execute(method);
        }
        else if (HTTPMethod=="POST")
        {
            StringEntity requestEntity = new StringEntity(body, ContentType.APPLICATION_JSON);
            HttpPost method=new HttpPost(url);
            method.setEntity(requestEntity);
            response= httpclient.execute(method);
        }
        return response;
    }

}
