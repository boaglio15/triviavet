package trivia.models;

import org.javalite.activejdbc.Model;
import java.util.*;

public class User extends Model {

    static {
        validatePresenceOf("dni").message("Ingrese DNI");
        validatePresenceOf("pass").message("Ingrese clave");
        validatePresenceOf("nom", "ape", "tipoUser");

        //Solo se permite dni que este formado por letras maysuculas y minusculas y numeros, no pueden tener caracteres especiales ni espacios
        validateRegexpOf("dni","\\b([A-Z0-9a-z])\\w+\\b").message("Formato dni incorrecto. Mayus/Minus y Numeros");
    }

    private String nom;
    private String ape;
    private String dni;
    private String pass;
    private int tipoUser; //1=admin 0=jugador


    public User() {

    }

    public User(String nombre, String apellido, String dni, String password, int tipo) {
        set("nom", nombre);
        set("ape", apellido);
        set("dni", dni);
        set("pass", password);
        set("tipoUser", tipo);
    }
    
    public int getIdUser(){
        return  (int) this.getId();
    }

    public String getNom() {
        return this.getString("nom");
    }

    public String getApe() {
        return this.getString("ape");
    }

    public String getDni() {
        return this.getString("dni");
    }

    public String getPass() {
        return this.getString("pass");
    }

    public int getTipoUser() {
        return this.getInteger("tipoUser");
    }

    public Map getCompleteUser() {
        Map m = new HashMap();
        m.put("id", this.getId());
        m.put("nom", this.getNom());
        m.put("ape", this.getApe());
        m.put("dni", this.getDni());
        m.put("pass", this.getPass());
        //m.put("tipoUser", this.getTipoUser());
        return m;
    }
    
    
    public static User findUserByDni(String dni) {
        List<User> user = User.where("dni = ?", dni);
        return user.get(0);
    }
    
    public static List<Map> getAllUser() {
        List<User> r = new ArrayList<User>();
        r = User.findAll();
        List<Map> rm = new ArrayList<Map>();
        for (User user : r) {
            rm.add(user.getCompleteUser());
        }
        return rm;
    }

    public static User getUser(String id) {
        User l = User.findById(id);
        return l;
    }

    public static void deleteUser(String id) {
        User g = User.findById(id);
        g.delete();
    }

    public static void createUser(String nombre, String apellido, String dni, String password, int tipo) {
        User user = new User(nombre, apellido, dni, password, tipo);
        user.saveIt();
    }

    //Funcion que retorna el Id de un usuario si esta registrado.
    public static Integer userLogin(String docum, String password){
        List<User> l = User.where("dni = ?", docum);
        if (l == null)
            return -1; //Usuario no registrado.
        if (l.get(0).getPass().equals(password))
            return ((Integer) l.get(0).getId());
        return -2; //No coincide el password.
    }
}
