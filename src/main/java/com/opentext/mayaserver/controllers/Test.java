package com.opentext.mayaserver.controllers;

import com.azure.storage.blob.BlobClient;
import com.azure.storage.blob.BlobContainerClient;
import com.opentext.mayaserver.environments.ConfigUtils;
import com.opentext.mayaserver.exceptions.MayaServerException;

import java.io.File;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static com.opentext.mayaserver.utility.Constants.*;

public class Test {
    static List<String> blobList = new ArrayList();
    public static void main(String[] args) {
        File file = new File("C:\\SOUMYA\\Azurite\\maya-container\\actual_bill\\hemant_mf_us_inc_87601453");
        uploadFilesRecursively(file);
//        List list = ConfigUtils.readBlobNames(new File("C:\\SOUMYA\\Azurite\\maya-container\\actual_bill\\hemant_mf_us_inc_87601453"));
//        System.out.println(list);
//        String s = "https://soumyatest.swinfra.net:443/sfsdfdsf ";
//        String s1 = s.trim();
//        URI baseUri =  URI.create(s1);
//        String accountName[] = baseUri.getPath().split("/");
//        if(accountName.length < 2){
//            System.out.println("accountName must be provided");
//        }
//        System.out.println(accountName);

    }
    private static void uploadFilesRecursively(File filePath) {
        BlobClient blobClient;
        if (filePath.listFiles() == null || filePath.listFiles().length == 0) {
            System.out.println("*****");
            throw new MayaServerException("directory is empty");
        }
            for (File uploadFile : filePath.listFiles()) {
                if (uploadFile.isFile()) {
                    System.out.println(uploadFile.getName());
//                    System.out.println(blobList);
                    for(String blobName : blobList){
                       if(blobName.endsWith(uploadFile.getName())){
                           System.out.println(blobName);
                           break;
                       }
                    }
                } else if (uploadFile.isDirectory()) {
                    try {
                        blobList = ConfigUtils.readBlobNames(uploadFile);
                        uploadFilesRecursively(uploadFile);
                    } catch (Exception e) {
                        throw new MayaServerException(e.getMessage());
                    }
                }
            }
        }

    }


