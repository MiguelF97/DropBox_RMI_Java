package edu.ufp.inf.sd.rmi.diglib.server;

import java.io.Serializable;

public class State implements Serializable {
    
    private String operation;
    private String path;
    private String fileName;
    private String directoryOrFile;


    public State(String operation, String path, String fileName, String directoryOrFile) {
       this.operation = operation;
       this.path = path;
       this.fileName=fileName;
       this.directoryOrFile=directoryOrFile;
    }

    @Override
    public String toString() {
        return "State{" + "operation=" + operation + ", path=" + path + ", fileName=" + fileName + ", directoryOrFile=" + directoryOrFile + '}';
    }
    
    

    /**
     * @return the operation
     */
    public String getOperation() {
        return operation;
    }

    /**
     * @param operation the operation to set
     */
    public void setOperation(String operation) {
        this.operation = operation;
    }

    /**
     * @return the path
     */
    public String getPath() {
        return path;
    }

    /**
     * @param path the path to set
     */
    public void setPath(String path) {
        this.path = path;
    }

    /**
     * @return the fileName
     */
    public String getFileName() {
        return fileName;
    }

    /**
     * @param fileName the fileName to set
     */
    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    /**
     * @return the directoryOrFile
     */
    public String getDirectoryOrFile() {
        return directoryOrFile;
    }

    /**
     * @param directoryOrFile the directoryOrFile to set
     */
    public void setDirectoryOrFile(String directoryOrFile) {
        this.directoryOrFile = directoryOrFile;
    }

    

}
