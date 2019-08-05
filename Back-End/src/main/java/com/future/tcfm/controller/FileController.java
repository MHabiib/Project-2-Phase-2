package com.future.tcfm.controller;


import com.future.tcfm.service.ExcelReaderService;
import com.sun.org.apache.xpath.internal.operations.Mult;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@NoArgsConstructor
@RequestMapping("/api/file")
public class FileController {
    @Autowired
    ExcelReaderService excelReaderService;

    @PostMapping
    public ResponseEntity uploadExcelFile(@RequestPart("file")MultipartFile file){
        try {
            excelReaderService.store(file);
            return new ResponseEntity<>("OK!",HttpStatus.OK);
        }catch (Exception e){
            System.out.println(e.getMessage());
            return new ResponseEntity<>("Fail to read excel, something wrong happened!", HttpStatus.BAD_REQUEST);
        }
    }
}
