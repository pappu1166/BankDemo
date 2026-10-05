package com.app.blog.service.impl;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.StringReader;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import com.app.blog.reposistories.MakeJNDICall;
import com.lowagie.text.Document;
import com.lowagie.text.Paragraph;
import com.lowagie.text.html.simpleparser.HTMLWorker;
import com.lowagie.text.pdf.PdfWriter;

import lombok.extern.slf4j.Slf4j;


@Service
@Slf4j
public class CreatePdfService {
	
	@Autowired(required = false)
	private MakeJNDICall jdbcTemplate;

	public ByteArrayInputStream createPDF(String data) {
		log.info("welcome to generate pdf file data:{}",data);
		// Create a byte array output stream
		ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();

		Document doc = new Document();

		// Create a PDF writer
		PdfWriter.getInstance(doc, byteArrayOutputStream);

		// Create a PDF document
		doc.open();

		// Add content to the PDF

		String getpdf = "SELECT VALUE FROM pdf_generate_data WHERE PARAM= ?";
		
		log.info("getpdf query : {}",getpdf);
		
		String pdfcate = jdbcTemplate.selectSingleResultString(getpdf, data);
		
		log.info("pdfcate data : {}",pdfcate);

		//doc.add(new Paragraph(pdfcate));
		  if (pdfcate == null || pdfcate.isEmpty()) {
              log.error("No HTML content found for param: {}", data);
              throw new RuntimeException("No HTML content found for the provided parameter.");
          }

		  HTMLWorker htmlWorker = new HTMLWorker(doc);

	        // Convert HTML content into PDF
	        try {
				htmlWorker.parse(new StringReader(pdfcate));
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		// Close the document
		doc.close();



		// Return the PDF as a byte array
		return new ByteArrayInputStream(byteArrayOutputStream.toByteArray());
				

	}
}