package com.sanjay.service;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.model.PutObjectResult;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.s3.model.S3ObjectInputStream;
import com.amazonaws.util.IOUtils;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class AwsS3Services {
	
	@Autowired
    private AmazonS3 amazonS3;
	@Value("${cloud.aws.bucket.name}")
	private String bucketName;
	
	public String fileUpload(MultipartFile multipartFile) {
		File file = convertMultiPartFileToFile(multipartFile);
		 PutObjectResult putObject = amazonS3.putObject(
				new PutObjectRequest(bucketName, multipartFile.getOriginalFilename(), file));
		 file.delete();
		 if(putObject!=null) {
		return "File Upload Success fully";
		 }
		 else
			 return "Faild to upload file";
	}
	
	public byte[] downloadFile(String fileName) throws IOException {
		
		S3Object s3Object = amazonS3.getObject(bucketName, fileName);
		S3ObjectInputStream objectData = s3Object.getObjectContent();
		return IOUtils.toByteArray(objectData);
	}
	public String deleteFiles(String fileName) {
		amazonS3.deleteObject(bucketName, fileName);
		return fileName+"Delete Success fully";
	}
	private File convertMultiPartFileToFile( MultipartFile multipartFile) {
        File file = new File(multipartFile.getOriginalFilename());
       try ( FileOutputStream outputStream = new FileOutputStream(file)) {
           outputStream.write(multipartFile.getBytes());
       } catch ( IOException ex) {
           log.error("Error converting the multi-part file to file = {}", ex.getMessage());
       }
       return file;
   }
}
