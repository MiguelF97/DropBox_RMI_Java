package edu.ufp.inf.sd.rmi.diglib.server;

import java.io.File;
import java.io.IOException;
import java.rmi.server.UnicastRemoteObject;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

public class DiglibSessionImpl extends UnicastRemoteObject implements DiglibSessionRI {

    private DBMockup db;
    private String nomeUser;
    private String newPerm;
    private String removePerm;

    public DiglibSessionImpl(DBMockup db) throws RemoteException {
        super();
        this.db = db;
        db.setSessions(this);
    }

    @Override
    public SubjectRI getSubject(String nomeUser, String userSub) throws RemoteException {

        ArrayList<String> Perm = getDb().readPerm(nomeUser);

        for (int i = 0; i < Perm.size(); i++) {
            if (Perm.get(i).equals(userSub)) {
                for (i = 0; i < getDb().getSubjects().size(); i++) {
                    if (getDb().getSubjects().get(i).getNome().equals(userSub)) {
                        return getDb().getSubjects().get(i);
                    }
                }
            }
        }

        return null;
    }

    @Override
    public ArrayList<String> updateDirectory(String nomeSub, SubjectRI subject, String clientUserName, int tamanhoFicheiro[], File fileNames[]) throws RemoteException {
        File folder = new File("C:\\Users\\migue\\Documents\\NetBeansProjects\\SD\\src\\edu\\ufp\\inf\\sd\\rmi\\diglib\\ServerDB\\" + nomeSub);
        File[] listOfFiles = folder.listFiles();
        String fileOrDirec = "";
        int fileLenght = 0;
        ArrayList<String> ficheiros= new ArrayList<>();

        for (int i = 0; i < listOfFiles.length; i++) {

            if (listOfFiles[i].isFile()) {

                fileLenght = (int) listOfFiles[i].length();
                int flag = 0;
                for (int c = 0; c < fileNames.length; c++) {
                    if (fileNames[c].isFile() && fileNames[c].getName().equals(listOfFiles[i].getName()) && tamanhoFicheiro[c] == fileLenght) {
                        flag = 1;
                    }
                }
                fileOrDirec = "File";
                if (flag == 0) {
                    subject.updateOneUser(listOfFiles[i].getName(), clientUserName, fileOrDirec, fileLenght);
                }

            } else if (listOfFiles[i].isDirectory()) {
                fileOrDirec = "Directory";
                fileLenght = 0;
                subject.updateOneUser(listOfFiles[i].getName(), clientUserName, fileOrDirec, fileLenght);
            }

        }

        for (int b = 0; b < fileNames.length; b++) {
            if (fileNames[b].isFile()) {
                for (int n = 0; n < listOfFiles.length; n++) {
                    fileLenght = (int) listOfFiles[b].length();
                    int flag = 0;
                    if (listOfFiles[b].isFile() && listOfFiles[b].getName().equals(listOfFiles[n].getName()) && tamanhoFicheiro[b] == fileLenght) {
                        flag = 1;
                    }
                    fileOrDirec = "File";
                    if (flag == 0) {
                       ficheiros.add(fileNames[b].getName());
                    }

                }
            }
            
        }
        return ficheiros;
    }

    @Override
    public boolean shareSub(String user, String userSub) {
        try {
            return getDb().updatePerms(user, userSub);
        } catch (IOException ex) {
            Logger.getLogger(DiglibSessionImpl.class.getName()).log(Level.SEVERE, null, ex);
        }
        return false;
    }

    @Override
    public boolean removeSub(String user, String userSub) throws RemoteException {
        try {

            return getDb().removePerms(user, userSub);
        } catch (IOException ex) {
            Logger.getLogger(DiglibSessionImpl.class.getName()).log(Level.SEVERE, null, ex);
        }
        return false;
    }

    @Override
    public ArrayList<String> getSubjectPermissions(String nomeSub) throws RemoteException {
        ArrayList<String> subPermissions;
        subPermissions = getDb().readPerm(nomeSub);

        return subPermissions;
    }

    @Override
    public void refreshUsersOnline(String userGotPerm, String userGavePerm, String removeOrGivePerm) throws RemoteException {

        for (int i = 0; i < db.getSessions().size(); i++) {
            if (db.getSessions().get(i).getUserName().equals(userGotPerm)) {
                if (removeOrGivePerm.equals("Give")) {
                    db.getSessions().get(i).setNewPerm(userGotPerm);
                }

                if (removeOrGivePerm.equals("Remove")) {
                    db.getSessions().get(i).setRemovePerm(userGotPerm);
                }

            }
        }
    }

    /**
     * @return the db
     */
    @Override
    public DBMockup getDb() {
        return db;
    }

    /**
     * @param db the db to set
     */
    public void setDb(DBMockup db) {
        this.db = db;
    }

    @Override
    public void setUserName(String name) {
        this.nomeUser = name;
    }

    @Override
    public String getUserName() {
        return this.nomeUser;
    }

    /**
     * @return the newPerm
     */
    @Override
    public String getNewPerm() {
        return newPerm;
    }

    /**
     * @param newPerm the newPerm to set
     */
    @Override
    public void setNewPerm(String newPerm) {
        this.newPerm = newPerm;
    }

    /**
     * @return the removePerm
     */
    @Override
    public String getRemovePerm() {
        return removePerm;
    }

    /**
     * @param removePerm the removePerm to set
     */
    @Override
    public void setRemovePerm(String removePerm) {
        this.removePerm = removePerm;
    }

}
