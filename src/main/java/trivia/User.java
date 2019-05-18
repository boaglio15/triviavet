package trivia;

import org.javalite.activejdbc.Model;
import java.util.*;

public class User extends Model {

    static {
        validatePresenceOf("dni").message("Ingrese DNI");
        validatePresenceOf("pass").message("Ingrese clave");

        /*int min=8, max=10; 
        validateRange("dni",min, max).message("El tamaño minimo y maximo de caracteres validos del dni");*/

        //Solo se permite dni que este formado por letras maysuculas y minusculas y numeros, no pueden tener caracteres especiales ni espacios
        validateRegexpOf("dni","\\b([A-Z0-9a-z])\\w+\\b").message("Formato dni incorrecto. Mayus/Minus y Numeros");
    }

    private String nom;
    private String ape;
    private String dni;
    private String pass;
    private int tipoUser; //1=admin 0=jugador
    private int nivel;
    

    public User() {

    }

    public User(String nombre, String apellido, String dni, String password, int tipo, int nivel) {
        set("nom", nombre);
        set("ape", apellido);
        set("dni", dni);
        set("pass", password);
        set("tipoUser", tipo);
        set("nivel", nivel);
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

    public int getNivel() {
        return this.getInteger("nivel");
    }
    
    

    public Map getCompleteUser() {
        Map m = new HashMap();
        m.put("id", this.getId());
        m.put("nom", this.getNom());
        m.put("ape", this.getApe());
        m.put("dni", this.getDni());
        m.put("pass", this.getPass());
        m.put("tipoUser", this.getTipoUser());
        m.put("nivel", this.getNivel());
        return m;
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

    public static void createUser(String nombre, String apellido, String dni, String password, int tipo, int nivel) {
        User user = new User(nombre, apellido, dni, password, tipo, nivel);
        user.saveIt();
    }
    
    
    //userLogin
    //registerUser
    //userLogout
}