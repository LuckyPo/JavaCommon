package com.lp.HttpClientDemo;

import java.io.File;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;


//四种Post请求数据格式和HttpClient模拟请求构造
public class HttpPostUtil {
	//application/json数据格式
	public static String httpPostWithJSON(String url, String str) throws Exception {
        HttpPost httpPost = new HttpPost(url);
        DefaultHttpClient client = new DefaultHttpClient();
        String respContent = null;
        
        StringEntity entity = new StringEntity(str,"utf-8");//解决中文乱码问题    
        entity.setContentEncoding("UTF-8");    
        entity.setContentType("application/json");    
        httpPost.setEntity(entity);           
        
        HttpResponse resp = client.execute(httpPost);
        if(resp.getStatusLine().getStatusCode() == 200) {
            HttpEntity he = resp.getEntity();
            respContent = EntityUtils.toString(he,"UTF-8");
        }
        return respContent;
    }
	
	//application/x-www-form-urlencoded数据格式
	public static String httpPostWithForm(String url, String str) throws Exception {
		DefaultHttpClient client = new DefaultHttpClient();
		HttpPost post = new HttpPost(url);                                            //定义HttpPost对象并初始化它
		String respContent = null;
		List<BasicNameValuePair> params = new ArrayList<BasicNameValuePair>();     //定义键值对列表，用于存放向url发送post请求的数据。        
		params.add(new BasicNameValuePair("dataJson", str));                       //向params设置数据                                                   
		HttpEntity reqEntity = new UrlEncodedFormEntity(params);                   //用UrlEncodedFormEntity对象包装请求体数据                                                   
		post.setEntity(reqEntity);                                                 //设置post请求实体        
		HttpResponse response = client.execute(post);                              //发送http请求  
				
		if(response.getStatusLine().getStatusCode() == 200) {
			HttpEntity repEntity = response.getEntity();
            respContent = EntityUtils.toString(repEntity,"UTF-8");
        }
		return respContent;
		
    }
	
	//text/xml数据格式
	public static void httpPostWithXML(String url, String str) throws Exception {
		DefaultHttpClient client = new DefaultHttpClient();
		Document doc = DocumentHelper.createDocument();                           //创建document对象
		Element book = doc.addElement("book");                                    //构建document对象各个节点
		book.addElement("title").addText("芈月传");
		book.addElement("author").addText("蒋胜男");
		String body = book.asXML();                                               //Document对象转成string类型
		StringEntity reqEntity = new StringEntity(body);                          //用StringEntity对象包装请求体数据
		reqEntity.setContentType("text/xml");                                     //设置请求头数据传输格式
		reqEntity.setContentEncoding("utf-8");                                    //设置请求头数据编码格式
		HttpPost post = new HttpPost("http://example.com");                       //定义HttpPost对象并初始化它   
		post.setEntity(reqEntity);                                                //设置post请求实体
		HttpResponse response = client.execute(post);                             //发送http请求
		System.out.println("the request body is:"+EntityUtils.toString(reqEntity));   //打印出请求实体
		System.out.println(response.getStatusLine().getStatusCode());                 //打印http请求返回码
	}
	
	//multipart/form-data
	public static void httpPostWithMultipart(String url, String str) throws Exception {
		DefaultHttpClient client = new DefaultHttpClient();
		HttpPost post = new HttpPost("http://example.com");           //定义HttpPost对象并初始化它
		MultipartEntity mutiEntity = new MultipartEntity();           //定义MultipartEntity对象
		File file = new File("C:/Users/hzsuixiang/Desktop/image_20151117151539.png");
		mutiEntity.addPart("desc",new StringBody("网易云阅读", Charset.forName("utf-8")));     //设置multiEntity对象的主体数据
		mutiEntity.addPart("pic", new FileBody(file));post.setEntity(mutiEntity);             //设置post请求主体
		HttpResponse  httpResponse = client.execute(post);                                   //执行post请求
		HttpEntity httpEntity =  httpResponse.getEntity(); 
	}
}
