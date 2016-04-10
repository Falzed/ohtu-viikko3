/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ohtu.data_access;

import java.util.List;
import java.util.ArrayList;
import ohtu.domain.User;
import java.io.File;
import java.io.FileWriter;
import java.util.Scanner;
import org.springframework.stereotype.Component;

/**
 *
 * @author Oskari
 */
@Component
public class FileUserDAO implements UserDao {

    private String tiedostonimi;

//    public FileUserDAO() {
//        this.tiedostonimi="users.txt";
//    }
    public FileUserDAO(String tiedostonimi) {
        this.tiedostonimi = tiedostonimi;
    }

    @Override
    public List<User> listAll() {
        ArrayList lista = new ArrayList<User>();
        File tiedosto = new File(tiedostonimi);
        Scanner lukija;
        try {
            lukija = new Scanner(tiedosto);
        } catch (Exception e) {
            System.out.println("Tiedoston avaus epäonnistui");
            return lista;
        }
        String rivi;
        String[] loginTiedot = new String[2];
        int i = 0;
        while (lukija.hasNextLine()) {
            rivi = lukija.nextLine();
            loginTiedot[i % 2] = rivi;
            if (i % 2 == 1) {
                lista.add(new User(loginTiedot[0], loginTiedot[1]));
            }
            i++;
        }
        lukija.close();
        return lista;
    }

    @Override
    public User findByName(String name) {
        List<User> lista = listAll();
        for (User user : lista) {
            if (user.getUsername().equals(name)) {
                return user;
            }
        }
        return null;
    }

    @Override
    public void add(User user) {
        FileWriter kirjoittaja;
        try {
            kirjoittaja = new FileWriter(tiedostonimi, true);
            kirjoittaja.append(user.getUsername() + System.lineSeparator());
            kirjoittaja.append(user.getPassword() + System.lineSeparator());
            kirjoittaja.close();
        } catch (Exception e) {
            System.out.println("Tiedostoon kirjoitus epäonnistui");
        }

    }
}
