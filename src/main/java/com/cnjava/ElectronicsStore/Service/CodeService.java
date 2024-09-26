package com.cnjava.ElectronicsStore.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cnjava.ElectronicsStore.Model.Code;
import com.cnjava.ElectronicsStore.Repository.CodeRepository;

@Service
public interface CodeService {

	

	
	
	Code getCodeByText(String text);
	
	void saveCode(Code c);
}
