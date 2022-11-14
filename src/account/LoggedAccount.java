package account;

import entity.User;

import java.io.*;
import java.util.Optional;

public class LoggedAccount {
    private static Optional<User> userLogged = Optional.empty();

    public static Optional<User> getUserLogged() {
        File file = new File("account.bin");

        try {
            if (!file.exists()) {
                file.createNewFile();
            }
            FileInputStream fis = new FileInputStream(file);
            if (fis == null){
                return userLogged;
            }
            if(fis.available() > 0){
                ObjectInputStream ois = new ObjectInputStream(fis);
                userLogged = Optional.of((User) ois.readObject());
                ois.close();
            }
            fis.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        return userLogged;
    }

    public static void changeLoggedUser(User user) {
        userLogged = Optional.empty();
        File file = new File("account.bin");

        try {
            if (!file.exists()) {
                file.createNewFile();
            }
            FileOutputStream fos = new FileOutputStream(file);
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(user);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void logOut(){
        File file = new File("account.bin");

        try {
            if (!file.exists()) {
                file.createNewFile();
            }
            FileOutputStream fos = new FileOutputStream(file);
            fos.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
