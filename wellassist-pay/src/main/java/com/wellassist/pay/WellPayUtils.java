package com.wellassist.pay;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

@Component("wellPayUtils")
public class WellPayUtils {
    String url = "http://localhost/payServer/payRes";
    public static void main(String[] args){
        try{
            //String s = restTemplate1.postForObject(url,null,String.class);
            //String s = restTemplate.postForObject(url,null,String.class,m);
            WellPayUtils u = new WellPayUtils();
            //Map<String,Object> s = u.getBankResult(m);
            //System.out.println(s.get("xml"));
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public String getBankResult(String xmlInput){
        //MultiValueMap<String, String> paramMap = new LinkedMultiValueMap<String, String>();
        //paramMap.add("data",xmlInput);
        String s = "";
        try{
            //HttpHeaders headers = new HttpHeaders();
            //headers.setContentType(MediaType.TEXT_XML);

            //HttpEntity<String> entity = new HttpEntity<String>(xmlInput,headers);
            //restTemplate.putput(url, entity);

            //s = restTemplate.postForObject(url,entity,String.class);

        }catch (Exception e){
            e.printStackTrace();
        }
        return s;
    }

}
