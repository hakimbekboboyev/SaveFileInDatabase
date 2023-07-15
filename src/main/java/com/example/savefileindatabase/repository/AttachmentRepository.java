package com.example.savefileindatabase.repository;

import com.example.savefileindatabase.entity.Attachment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AttachmentRepository extends JpaRepository<Attachment,Integer> {

}
