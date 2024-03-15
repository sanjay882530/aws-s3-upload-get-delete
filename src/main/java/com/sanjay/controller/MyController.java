package com.sanjay.controller;

import java.io.IOException;

import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.sanjay.service.AwsS3Services;


@RestController
@RequestMapping("/api")
public class MyController {
	
	AwsS3Services s3Service;
	
	public MyController(AwsS3Services s3Service) {
		super();
		this.s3Service = s3Service;
	}
	@PostMapping("/upload")
	public ResponseEntity<String>  uploadFiels(@RequestBody MultipartFile multipartFile){
		return new ResponseEntity<> (s3Service.fileUpload(multipartFile),HttpStatus.OK);
	}
	@GetMapping("/download/{fileName}")
	public  ResponseEntity<ByteArrayResource>downloadFiles(@PathVariable String fileName) throws IOException {
		byte[] downloadFile = s3Service.downloadFile(fileName);
		ByteArrayResource resource=new ByteArrayResource(downloadFile);
		return ResponseEntity.ok().contentLength(downloadFile.length)
				.header("Content-type", "application/octet-stream")
				.header("Contnt-disposition", "attachment;filename=\""+ fileName+"\"")
				.body(resource);
	}
	@DeleteMapping("/delete/{fileName}")
	public ResponseEntity<String> deleteFilesInBucket(@PathVariable String fileName){
		return new ResponseEntity<> (s3Service.deleteFiles(fileName),HttpStatus.OK);
	}
	
	

}
