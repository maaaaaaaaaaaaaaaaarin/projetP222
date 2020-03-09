package p222.tp3.projet222;

class TypeFichier {

    private String nom;
    private String nom_crypt;
    private String path;

    private boolean encrypted;

    public TypeFichier(String nom, String path) {
        this.nom = nom;
        this.path = path;
        this.encrypted = false;
    }

    public TypeFichier(String nom, String path, boolean encrypt) {
        this.nom = nom;
        this.path = path;
        this.encrypted = false;
        if (encrypt) {
            encryptName();
        }

    }

    public boolean isEncrypted() {
        return this.encrypted;
    }

    public void encryptName() {
        this.encrypted = true;
        this.nom_crypt = ""+this.nom.hashCode();

    }

    public String getNom() {
        return this.nom;
    }

    public String getEncName() {
        return this.nom_crypt;
    }

}
