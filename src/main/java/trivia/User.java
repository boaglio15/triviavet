package trivia;

import org.javalite.activejdbc.Model;

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
		return this.getInteger("tipo");
	}

        public int getNivel(){
		return this.getInteger("nivel");
	}
}
