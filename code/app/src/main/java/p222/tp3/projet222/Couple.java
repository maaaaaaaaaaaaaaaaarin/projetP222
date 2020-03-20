package p222.tp3.projet222;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;

class Couple<clef, valeur> {

    private clef key;
    private valeur value;

    public Couple(clef key, valeur value) {
        this.key=key;
        this.value=value;
    }

    public clef getKey() {
        return key;
    }

    public valeur getValue() {
        return value;
    }

    @NonNull
    @Override
    public String toString() {
        return "( "+this.key+" ; "+this.value+" )";
    }
}
