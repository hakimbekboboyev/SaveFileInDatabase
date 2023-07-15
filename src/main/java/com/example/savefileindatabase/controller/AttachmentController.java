package com.example.savefileindatabase.controller;

import com.example.savefileindatabase.entity.Attachment;
import com.example.savefileindatabase.entity.AttachmentContent;
import com.example.savefileindatabase.repository.AttachmentContentRepository;
import com.example.savefileindatabase.repository.AttachmentRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import java.io.IOException;
import java.util.Iterator;
import java.util.Optional;

@RestController
@RequestMapping
public class AttachmentController {
    @Autowired
    AttachmentContentRepository attachmentContentRepository;
    @Autowired
    AttachmentRepository attachmentRepository;

    @PostMapping("/upload")
    private String uploadFile(MultipartHttpServletRequest servletRequest) throws IOException {
        Iterator<String> fileNames = servletRequest.getFileNames();
        if(fileNames.hasNext()) {
            MultipartFile file = servletRequest.getFile(fileNames.next());
            String originalFilename = file.getOriginalFilename();
            long size = file.getSize();
            String contentType = file.getContentType();
            byte[] bytes = file.getBytes();
            Attachment attachment = new Attachment();
            AttachmentContent attachmentContent = new AttachmentContent();
            attachment.setSize(size);
            attachment.setContentType(contentType);
            attachment.setOriginalName(originalFilename);
            attachmentContent.setContent(bytes);
            attachmentContent.setAttachment(attachment);
            attachmentRepository.save(attachment);
            attachmentContentRepository.save(attachmentContent);
            return "File Uploaded!  binaryData:"+bytes;
        }
        return "Error!";


    }

    @GetMapping("/upload/{id}")
    public void getFile(@PathVariable Integer id, HttpServletResponse response) throws IOException {
        Optional<Attachment> attachmentById = attachmentRepository.findById(id);
        if(attachmentById.isPresent()){
            Attachment attachment = attachmentById.get();
            Optional<AttachmentContent> attachmentContentById = attachmentContentRepository.findById(id);
            AttachmentContent attachmentContent = attachmentContentById.get();
            response.setHeader("Content-Disposition","attachment; fileName="+attachment.getOriginalName());
            response.setContentType(attachment.getContentType());
            FileCopyUtils.copy(attachmentContent.getContent(),response.getOutputStream());


        }
    }

}
