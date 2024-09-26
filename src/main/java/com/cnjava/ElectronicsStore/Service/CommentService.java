package com.cnjava.ElectronicsStore.Service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cnjava.ElectronicsStore.Model.Comment;
import com.cnjava.ElectronicsStore.Repository.CommentRepository;

@Service
public interface CommentService {


	
	void save(Comment b);
	

}
