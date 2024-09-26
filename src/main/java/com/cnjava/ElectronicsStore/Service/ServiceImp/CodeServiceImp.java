package com.cnjava.ElectronicsStore.Service.ServiceImp;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.cnjava.ElectronicsStore.Model.Code;
import com.cnjava.ElectronicsStore.Repository.CodeRepository;
import com.cnjava.ElectronicsStore.Service.CodeService;

@Component
public class CodeServiceImp implements CodeService {
    @Autowired
    private CodeRepository codeRepository;


    public Code getCodeByText(String text) {
        Code code = codeRepository.getCodeByCodeText(text);
        return code;
    }

    public void saveCode(Code c) {
        codeRepository.save(c);
    }
}
