package com.cnjava.ElectronicsStore.Service.ServiceImp;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.cnjava.ElectronicsStore.Model.Comment;
import com.cnjava.ElectronicsStore.Repository.CommentRepository;
import com.cnjava.ElectronicsStore.Service.CommentService;

@Component

public class CommentServiceImp implements CommentService {
    @Autowired
    private CommentRepository commentRepository;

    public void save(Comment b) {
        commentRepository.save(b);
    }
}
