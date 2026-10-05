package com.app.blog.controllers;


import java.io.IOException;
import java.io.InputStream;
import java.lang.ProcessHandle.Info;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StreamUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.app.blog.payload.ApiResponse;
import com.app.blog.payload.PostDto;
import com.app.blog.service.FileService;
import com.app.blog.service.PostService;

import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/file")
@Slf4j
public class FileController {

	@Autowired
	private FileService fileService;
	
	@Autowired
	private PostService postService;

	@Value("${project.image}")
	private String path;

	@PostMapping("/upload/post/{postId}")
	public ResponseEntity<ApiResponse> fileUpload(@RequestParam("image") MultipartFile image,
			@PathVariable Integer postId) {

		String filename = "";
		try {
			filename = this.fileService.UploadImage(path, image);
		PostDto postDto=	this.postService.getPostById(postId);
		postDto.setImageName(filename);
		PostDto updatepost=this.postService.updatePost(postDto, postId);
		log.info("updatepost--: {}",updatepost);
		} catch (IOException e) {
			log.error("Exception occure at file", e);
			return new ResponseEntity<ApiResponse>(new ApiResponse(null, "Image not uploaded due to server error"),
					HttpStatus.INTERNAL_SERVER_ERROR);
		}

		return new ResponseEntity<ApiResponse>(new ApiResponse(filename, "Image uploaded successfully"), HttpStatus.OK);

	}
	@GetMapping(value = "/images/{postId}",produces=MediaType.APPLICATION_PDF_VALUE)
	public void downloadImage(@PathVariable Integer postId,HttpServletResponse response) throws IOException {
		PostDto postDto=this.postService.getPostById(postId);
	InputStream input=	this.fileService.getResource(path, postDto.getImageName());
		
		response.setContentType(MediaType.APPLICATION_PDF_VALUE);
		StreamUtils.copy(input,response.getOutputStream());
	}

}