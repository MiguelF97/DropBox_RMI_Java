package edu.ufp.inf.sd.rmi.diglib.server;

import java.util.ArrayList;
import edu.princeton.cs.introcs.In;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.rmi.RemoteException;

    
public class DBMockup {

    private final ArrayList<User> users;
    private ArrayList<SubjectRI> subjects;
    private final ArrayList<DiglibSessionRI> sessions;
    private String Path="C:\\Users\\migue\\Documents\\NetBeansProjects\\SD\\src\\edu\\ufp\\inf\\sd\\rmi\\diglib\\ServerDB\\";


    public DBMockup() throws RemoteException {
        users = new ArrayList();
        subjects = new ArrayList<>();
        sessions = new ArrayList<>();
        lerUsers();
    }

  
    public void deleteSession(String userName) throws RemoteException
    {
      for(int i=0;i<sessions.size();i++)
      {
          if(sessions.get(i).getUserName().equals(userName))
          {
              sessions.remove(i);
              return;
          }
      }
    }
    
    public boolean register(String u, String p) throws IOException {
        if (!exists(u, p)) {
            users.add(new User(u, p));
            
                try (BufferedWriter bw = new BufferedWriter(new FileWriter(Path+"UsersRegisted.txt"))) {

                for(int i=0;i<users.size();i++)
                {
                    bw.write(users.get(i).getUname()+";");
                    bw.write(users.get(i).getPword()+";");
                    bw.newLine();
                }      
                
                bw.close();    
            }
                
                
                ArrayList<String> nomePerm= new ArrayList<>();
                nomePerm.add(0,u);
                nomePerm.add(1,"1");
                nomePerm.add(2,u);
                ArrayList<ArrayList<String>> permissions = readAllPerm();
                permissions.add(nomePerm);
                writePermFile(permissions);
                
                try (BufferedWriter bw = new BufferedWriter(new FileWriter(Path+"NumUsers.txt"))) {
                
                    bw.write(users.size()+";");
                }
                
                SubjectRI peim = new SubjectImpl(u);
                subjects.add(peim);
                new File(Path+u).mkdirs();
          return true;
        }
    return false;
       
}
    
    public void writePermFile(ArrayList<ArrayList<String>> permissions) throws IOException
    {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(Path+"Permissoes.txt"))) {
                         
            for(int i=0;i<users.size();i++)
               {
                int numPerm=Integer.parseInt(permissions.get(i).get(1));
                            
                for(int b=0;b<numPerm+2;b++)
                    {
                        bw.write(permissions.get(i).get(b)+";");
                    }
                    bw.newLine();
                }      
                
            bw.close();  
            }
    }  
    public boolean updatePerms(String user,String subUser) throws IOException
    {
        ArrayList<ArrayList<String>> allPerm = readAllPerm();
        
                     for(int b=0;b<users.size();b++)
                        {
                              
                            if(allPerm.get(b).get(0).equals(user))
                            {
                                for(int i=0;i<allPerm.get(b).size();i++)
                                {
                                    if(allPerm.get(b).get(i).equals(subUser))
                                    {
                                        return false;
                                    }
                                }
                                allPerm.get(b).add(subUser);
                                int numperm=Integer.parseInt(allPerm.get(b).get(1));
                                numperm++;
                                String num=Integer.toString(numperm);
                                allPerm.get(b).set(1, num);
                                writePermFile(allPerm);
                                return true;
                            }
                        }

        return false;
    } 
    
public boolean removePerms(String user,String subUser) throws IOException
    {
        ArrayList<ArrayList<String>> allPerm = readAllPerm();

                        for(int b=0;b<users.size();b++)
                        {
                            if(allPerm.get(b).get(0).equals(user))
                            {
                                int numperm=Integer.parseInt(allPerm.get(b).get(1));
                                
                                for(int i=2;i<numperm+2;i++)
                                {
                                    if(allPerm.get(b).get(i).equals(subUser))
                                    {
                                        allPerm.get(b).remove(i);
                                        numperm--;
                                        String num=Integer.toString(numperm);
                                        allPerm.get(b).set(1, num);
                                        writePermFile(allPerm);  
                                        return true;
                                    } 
                                }

                            }
                        }
            
        
        return false;
    }
    
    
    
    public int numUsers()
    {
        In in = new In(Path+"NumUsers.txt");    
        String[] tokenNum = in.readLine().split(";");
        return Integer.parseInt(tokenNum[0]);
           
    }
    public ArrayList<ArrayList<String>> readAllPerm()
    {
        int numUsers= numUsers();
        int numUsersNeg=numUsers;
        int numtotalfinal=numUsers;
        int numPerm;
        In in = new In(Path+"Permissoes.txt");
        ArrayList<ArrayList<String>> subPermissions = new ArrayList<>();
        ArrayList<String> permissionsName = null;
        
            while (numUsers!=0) 
            {
                String[] token = in.readLine().split(";");
                numUsersNeg=numtotalfinal-numUsers;
                numPerm = Integer.parseInt(token[1]);
                permissionsName = new ArrayList<>();
                permissionsName.add(0,token[0]);
                permissionsName.add(1,token[1]);
                    for(int i=2; i<numPerm+2; i++)
                    {
                        permissionsName.add(i, token[i]);
                    }
                subPermissions.add(numUsersNeg,permissionsName);
               
            numUsers--;
            }
        return subPermissions; 
    }
    
    public ArrayList<String> readPerm(String nomeSub)
    {
        int numUsers= numUsers();
        int numPerm;
        In in = new In(Path+"Permissoes.txt");
        String nomSub;
        ArrayList<String> subPermissions = new ArrayList<>();
        
            while (numUsers!=0) 
            {
                String[] token = in.readLine().split(";");
                nomSub =  token[0];
                if(nomSub.equals(nomeSub))
                {
                    numPerm = Integer.parseInt(token[1]);	
                    for(int i=0; i<numPerm; i++)
                    {
                        subPermissions.add(i, token[i+2]);
                    }
                    return subPermissions;
                }
                
            numUsers--;
            }
        return subPermissions;
    }
    
    public void lerUsers() throws RemoteException{
         
        int numUsers= numUsers();
        In in = new In(Path+"UsersRegisted.txt");
        String nome,password;
        
            while (numUsers!=0) 
            {
                String[] token = in.readLine().split(";");
                nome =  token[0];
                password = token[1];
                User newUser = new User(nome,password);
                users.add(newUser);
                SubjectRI peim = new SubjectImpl(token[0]);
                subjects.add(peim);
                numUsers--;
            }
        
    }


    public boolean exists(String u, String p) {
        for (User usr : this.users) {
            if (usr.getUname().compareTo(u) == 0 && usr.getPword().compareTo(p) == 0) {
                return true;
            }
        }
        return false;
        //return ((u.equalsIgnoreCase("guest") && p.equalsIgnoreCase("ufp")) ? true : false);
    }

    public void printSessions() throws RemoteException
    {
        for(int i=0;i<sessions.size();i++)
        {
            System.out.println("\nQUEM ESTA LOGADO->"+sessions.get(i).getUserName());
        }
    }
    
    public ArrayList<SubjectRI> getSubjects() {
        return subjects;
    }

    public void setSubjects(ArrayList<SubjectRI> subjects) {
        this.subjects = subjects;
    }

    /**
     * @return the sessions
     */
    public ArrayList<DiglibSessionRI> getSessions() {
        return sessions;
    }

    /**
     * @param sessions the sessions to set
     * @throws java.rmi.RemoteException
     */
    public void setSessions(DiglibSessionRI sessions) throws RemoteException {
        this.sessions.add(sessions);
        printSessions();
    }
    

}
