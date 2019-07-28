package com.example.demo;

import java.io.File;
import java.io.IOException;

import org.springframework.core.io.FileSystemResource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

@Component
public class FileUpload {
	
	 public String ConsumerDemo(MultipartFile picture,RestTemplate restTemplate) throws IllegalStateException, IOException {
		
		String fileName = picture.getOriginalFilename();//�õ��ϴ����ļ���
		//��ȡ�����ϵͳ����
        String tempFilePath = System.getProperty("java.io.tmpdir") + picture.getOriginalFilename();
        File tempFile = new File(tempFilePath);
        
        picture.transferTo(tempFile);//������ʱ�ļ�
        
 
     
        // ��ȡ�ļ����ԣ���Ҫ�����β�����Ͳ������ˣ� һϵ�еĴ�� ����
        //���ӷ����·��
        String url = "http://provider/provider/demo";
        
        //http����
        HttpHeaders headers = new HttpHeaders();
       
        //Accept�����Ͷˣ��ͻ��ˣ�ϣ�����ܵ���������
        headers.add("Accept",MediaType.APPLICATION_JSON.toString());
        
        // ָ�������е�ý��������Ϣ
        headers.setContentType(MediaType.parseMediaType("multipart/form-data;charset=UTF-8"));
        //һ������Ӧ���ֵ,Spring���ڲ�ʵ����LinkedMultiValueMap
        MultiValueMap<String,Object> param = new LinkedMultiValueMap<>();
 
        //MultipartFile ֱ��ת fileSystemResource �ǲ��е�
        FileSystemResource resource = new FileSystemResource(tempFilePath);//����ʱ�ļ����filesystemresource
       
        //����ַ�����param�����ҿ��Դ���ֹһ��
        param.add("picture",resource);
        
        System.out.println("fileName:   "+fileName);
        
        HttpEntity<MultiValueMap<String,Object>> formEntity = new HttpEntity<>(param,headers);
        

        
        ResponseEntity<String> responseEntity = restTemplate.postForEntity(url,formEntity, String.class);
        

        String str=responseEntity.getBody();

        
        //����ʵ�����Դʹ����֮��Ҫ�ʵ��Ļ�����Դ
        tempFile.delete();
		
        return str;
	}
	
}
