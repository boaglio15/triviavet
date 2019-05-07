package trivia;

import org.javalite.activejdbc.Model;
import java.util.*;

public class User extends Model {

        private String nom;
        private String ape;
        private String dni;
        private String pass;
        private int tipoUser;
        private int nivel;

	public User(){

	}

	/**
	 * Builder.
	 * @param name
	 * @param pas
	 */
	public User(String nombre, String apellido, String dni, String password, int tipo, int nivel){
		set("nom", nombre);
		set("ape", apellido);
		set("dni", dni);
        set("tipoUser", tipo);
        set("pass", password);
        set("nivel", nivel);
	}


	public String getNom(){
		return this.getString("nom");
	}

        public String getApe(){
		return this.getString("ape");
	}

        public String getDni(){
		return this.getString("dni");
	}

        public String getPass(){
		return this.getString("pass");
	}

        public int getTipoUser(){
		return this.getInteger("tipoUser");
	}

        public int getNivel(){
		return this.getInteger("nivel");
	}

        public Map getCompleteUser(){
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
}
