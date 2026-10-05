package com.app.blog.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.app.blog.service.impl.CreatePdfService;

import lombok.extern.slf4j.Slf4j;

import java.io.ByteArrayInputStream;

@RestController
@Slf4j
@RequestMapping("/api")
public class PdfController {

	@Autowired
	CreatePdfService createPdfService;

	@GetMapping("/download-pdf/{data}")
	public ResponseEntity<InputStreamResource> downloadPdf(@PathVariable String data) {
		try {
			log.info("data inside downloadPdf: {}", data);
			ByteArrayInputStream pdf = createPdfService.createPDF(data);

			// Create headers for the response
			HttpHeaders headers = new HttpHeaders();
			headers.setContentType(MediaType.APPLICATION_PDF);
			headers.setContentDispositionFormData("attachment", "sample.pdf");

			// Return the PDF as a byte array
			return ResponseEntity.ok().headers(headers).body(new InputStreamResource(pdf));

		} catch (Exception e) {
			e.printStackTrace();
			return ResponseEntity.status(500).body(null);
		}
	}
}
