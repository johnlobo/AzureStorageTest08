/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.superefectivo.azurefilestest08;

import com.microsoft.azure.storage.StorageException;
import com.microsoft.azure.storage.file.CloudFile;
import com.microsoft.azure.storage.file.CloudFileClient;
import com.microsoft.azure.storage.file.CloudFileDirectory;
import com.microsoft.azure.storage.file.CloudFileShare;
import com.microsoft.azure.storage.file.ListFileItem;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.Serializable;
import java.net.URISyntaxException;
import java.security.InvalidKeyException;
import java.util.Random;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.inject.Named;
import javax.servlet.http.HttpSession;

/**
 *
 * @author DavidRodríguez
 */
@Named(value = "userManagedBean")
@ManagedBean
@SessionScoped
public class UserManagedBean implements Serializable {

    private String tienda;
    private String anyo;
    private String tipo;
    private String id_contrato;
    private String URL;
    private String errorTxt;
    private StringBuilder log;

    /**
     * Creates a new instance of UserManagedBean
     */
    public UserManagedBean() {
        log = new StringBuilder(); 
    }

    public String subir_fichero() throws StorageException {
        CloudFileClient fileClient = null;
        CloudFileShare fileShare1 = null;
        FileInputStream fileInputStream = null;
        CloudFileDirectory dir1, dir2 = null;

        System.out.println("\nStarting Process...");        
        log.append("\nStarting Process...\n");

        
        this.URL = "/" + this.tienda + "/" + this.anyo + "/"
                + this.tipo + "/" + this.id_contrato;
        try {
            // Create a file client for interacting with the file service
            fileClient = FileClientProvider.getFileClientReference();
            System.out.println("File Client configured...");
            log.append("File Client properlyconfigured...\n");

            // Create sample file for upload demonstration
            Random random = new Random();
            System.out.println("Creating sample file between 128KB-256KB in size for upload demonstration.");
            File tempFile1 = DataGenerator.createLocalFile(this.id_contrato, ".pdf", (128 * 1024) + random.nextInt(256 * 1024));
            System.out.println(String.format("Successfully created the file \"%s\".", tempFile1.getAbsolutePath()));

            // Create a new file share
            fileShare1 = fileClient.getShareReference("scan-docs");
            //Get a reference to the root directory for the share.
            CloudFileDirectory rootDir = fileShare1.getRootDirectoryReference();
            
            //Get a reference to the root directory for the share.
            for (ListFileItem fileItem : rootDir.listFilesAndDirectories()) {
                System.out.println(fileItem.getUri());
            }
            // Create a random directory under the root directory
            System.out.println("Create the sub-directory we need");
            // Create sub-directory level tienda
            dir1 = rootDir.getDirectoryReference(this.tienda);
            dir1.createIfNotExists();
            // Create sub-directory level anyo
            dir2 = dir1.getDirectoryReference(this.anyo);
            dir2.createIfNotExists();
            // Create sub-directory level tipo
            dir1 = dir2.getDirectoryReference(this.tipo);
            dir1.createIfNotExists();
            // Create sub-directory level id_contrato
            dir2 = dir1.getDirectoryReference(this.id_contrato);
            dir2.createIfNotExists();
            // Upload a local file to the final directory
            System.out.println("Upload the sample file to the final directory.");
            CloudFile file = dir2.getFileReference(tempFile1.getName());
            file.uploadFromFile(tempFile1.getAbsolutePath());
            System.out.println("Successfully uploaded the file.");

        } catch (IOException | RuntimeException | URISyntaxException | InvalidKeyException t) {
            PrintHelper.printException(t);
            this.errorTxt = t.toString();
            log.append("Process terminated...\n");
            return "error_fl";
        }
        log.append("Process terminated...\n");
        return "subir_fl";
    }

    /**
     * @return the tienda
     */
    public String getTienda() {
        return tienda;
    }

    /**
     * @return the anyo
     */
    public String getAnyo() {
        return anyo;
    }

    /**
     * @return the tipo
     */
    public String getTipo() {
        return tipo;
    }

    /**
     * @return the id_contrato
     */
    public String getId_contrato() {
        return id_contrato;
    }

    /**
     * @return the URL
     */
    public String getURL() {
        return URL;
    }

    /**
     * @return the errorTxt
     */
    public String getErrorTxt() {
        return errorTxt;
    }

    /**
     * @param errorTxt the errorTxt to set
     */
    public void setErrorTxt(String errorTxt) {
        this.errorTxt = errorTxt;
    }

    /**
     * @return the log
     */
    public String getLog() {
        return log.toString();
    }

    /**
     * @param tienda the tienda to set
     */
    public void setTienda(String tienda) {
        this.tienda = tienda;
    }

    /**
     * @param anyo the anyo to set
     */
    public void setAnyo(String anyo) {
        this.anyo = anyo;
    }

    /**
     * @param tipo the tipo to set
     */
    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    /**
     * @param id_contrato the id_contrato to set
     */
    public void setId_contrato(String id_contrato) {
        this.id_contrato = id_contrato;
    }

}
