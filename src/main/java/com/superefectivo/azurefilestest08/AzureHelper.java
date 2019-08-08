package com.superefectivo.azurefilestest08;

import com.microsoft.azure.storage.StorageException;
import com.microsoft.azure.storage.file.CloudFile;
import com.microsoft.azure.storage.file.CloudFileClient;
import com.microsoft.azure.storage.file.CloudFileDirectory;
import com.microsoft.azure.storage.file.CloudFileShare;
import com.microsoft.azure.storage.file.CopyStatus;
import com.microsoft.azure.storage.file.ListFileItem;
import java.net.URISyntaxException;
import java.util.Iterator;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author DavidRodríguez
 */
public class AzureHelper {
    
    /**
     * Enumerates the contents of the file share.
     *
     * @param rootDir Root directory which needs to be enumerated
     *
     * @throws StorageException
     */
    private static void enumerateDirectoryContents(CloudFileDirectory rootDir) throws StorageException {

        Iterable<ListFileItem> results = rootDir.listFilesAndDirectories();
        for (Iterator<ListFileItem> itr = results.iterator(); itr.hasNext(); ) {
            ListFileItem item = itr.next();
            boolean isDirectory = item.getClass() == CloudFileDirectory.class;
            System.out.println(String.format("\t\t%s: %s", isDirectory ? "Directory " : "File      ", item.getUri().toString()));
            if (isDirectory == true) {
            	enumerateDirectoryContents((CloudFileDirectory) item);
            }
        }
    }
    
    /**
     * Enumerates the shares and contents of the file shares.
     *
     * @param fileClient CloudFileClient object
     *
     * @throws StorageException
     * @throws URISyntaxException
     */
    private static void enumerateFileSharesAndContents(CloudFileClient fileClient) throws StorageException, URISyntaxException {

        for (CloudFileShare share : fileClient.listShares("filebasics")) {
            System.out.println(String.format("\tFile Share: %s", share.getName()));
            enumerateDirectoryContents(share.getRootDirectoryReference());
        }
    }
    
     /**
     * Wait until the copy complete.
     *
     * @param file Target of the copy operation
     *
     * @throws InterruptedException
     * @throws StorageException
     */
    private static void waitForCopyToComplete(CloudFile file) throws InterruptedException, StorageException {
        CopyStatus copyStatus = CopyStatus.PENDING;
        while (copyStatus == CopyStatus.PENDING) {
            Thread.sleep(1000);
            copyStatus = file.getCopyState().getStatus();
        }
    }
    
}
