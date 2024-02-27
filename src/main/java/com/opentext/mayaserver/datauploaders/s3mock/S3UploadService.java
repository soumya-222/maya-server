package com.opentext.mayaserver.datauploaders.s3mock;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ObjectListing;
import com.amazonaws.services.s3.model.S3ObjectSummary;
import com.opentext.mayaserver.config.ApplicationPropertiesConfig;
import com.opentext.mayaserver.exceptions.MayaServerException;
import com.opentext.mayaserver.models.vo.ModeEnumVO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.tomcat.util.http.fileupload.FileUtils;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.util.List;

import static com.opentext.mayaserver.utility.Constants.AWS;
import static com.opentext.mayaserver.utility.Constants.FILE_PATH_SEPARATOR;

/**
 * @author Soumyaranjan
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class S3UploadService {
    private final ApplicationPropertiesConfig applicationPropertiesConfig;
    private final AwsS3ClientFactory awsS3ClientFactory;


    public void uploadAWSBillToS3(String bucketName, String nfsDataPath, String useCaseName, ModeEnumVO mode) {
        File filePath = new File(nfsDataPath);
        uploadFilesRecursively(bucketName, filePath, awsS3ClientFactory.getAmazonS3Client(useCaseName), mode);
        log.info("All files uploaded successfully to S3Mock");
        if (mode == ModeEnumVO.GENERATE) {
            deleteAWSBillFromNFS(useCaseName);
        }
    }


    private void uploadFilesRecursively(String bucketName, File filePath, AmazonS3 amazonS3, ModeEnumVO mode) {
        String byodPath = applicationPropertiesConfig.getByodDataPath() + FILE_PATH_SEPARATOR + AWS;
        String generatePath = applicationPropertiesConfig.getS3Mock().getNfs();
        File[] files = filePath.listFiles();
        if (files == null || files.length == 0) {
            log.error("No files found in the directory. {}", filePath.getPath());
            throw new MayaServerException("directory is empty");
        }
        for (File uploadFile : files) {
            String key = null;
            if (uploadFile.isFile()) {
                switch (mode) {
                    case BYOD -> key = uploadFile.getPath().substring(byodPath.length() + 1);
                    case GENERATE -> key = uploadFile.getPath().substring(generatePath.length() + 1);
                }
                try {
                    upload(bucketName, key, uploadFile, amazonS3);
                } catch (Exception e) {
                    log.error("Error while uploading bills to S3Mock {} Error Cause: {}", uploadFile.getName(), e.getMessage());
                    throw new MayaServerException(e.getMessage());
                }
            } else if (uploadFile.isDirectory()) {
                uploadFilesRecursively(bucketName, uploadFile, amazonS3, mode);
            }
        }
    }

    private void upload(String bucketName, String key, File file, AmazonS3 amazonS3) {
        log.info("Uploading file to S3Mock '{}'", file.getName());
        amazonS3.putObject(bucketName, key, file);
        log.info("File uploaded successfully to S3Mock '{}'", key);
    }

    public void deleteAWSBillFromNFS(String useCaseName) {
        File theBillDirectory = new File(applicationPropertiesConfig.getS3Mock().getNfs() + FILE_PATH_SEPARATOR + useCaseName);
        if (theBillDirectory.exists()) {
            try {
                FileUtils.deleteDirectory(theBillDirectory);
                log.info("Aws Bill deleted successfully from NFS");
            } catch (IOException e) {
                log.error("Unable to delete the Aws bill directory '{}','{}'", useCaseName, e.getMessage());
                throw new MayaServerException(e.getMessage(), e);
            }
        }
    }

    public void deleteAWSBillFromS3(String bucketName, String useCaseName) {
        AmazonS3 amazonS3 = awsS3ClientFactory.getAmazonS3Client(useCaseName);
        ObjectListing objectListing = amazonS3.listObjects(bucketName, useCaseName + "/");
        List<S3ObjectSummary> objectSummaries = objectListing.getObjectSummaries();
        try {
            for (S3ObjectSummary objectSummary : objectSummaries) {
                String key = objectSummary.getKey();
                amazonS3.deleteObject(bucketName, key);
            }
            log.info("Aws bill deleted successfully from s3Mock");
        } catch (Exception e) {
            log.error("Exception reported while deleting objects from s3Mock {} ", e.getMessage());
        }
    }
}
