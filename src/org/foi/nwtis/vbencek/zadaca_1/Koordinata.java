
package org.foi.nwtis.vbencek.zadaca_1;

public class Koordinata {

        public double geoSirina;
        public double geoDuzina;

        public Koordinata() {

        }
        
        public static int izracunajUdaljenost(Koordinata polaziste, Koordinata odrediste) {

        double d = Math.sqrt(Math.pow((odrediste.geoSirina - polaziste.geoSirina), 2)
                + Math.pow((odrediste.geoDuzina - polaziste.geoDuzina), 2));
        
        return (int) d;
    }

        public Koordinata(double geoSirina, double geoDuzina) {
            this.geoDuzina = geoDuzina;
            this.geoSirina = geoSirina;
        }

        @Override
        public String toString() {
            
            return "Dužina: "+zaokruzi(this.geoDuzina)+", Širina: "+zaokruzi(this.geoSirina);
        }

        @Override
        public boolean equals(Object obj) {
            Koordinata o = null;
            if(obj instanceof Koordinata)
                o = (Koordinata) obj;
            else
                return false;
            
            if(zaokruzi(this.geoDuzina) == zaokruzi(o.geoDuzina) 
                    && zaokruzi(this.geoSirina)==zaokruzi(o.geoSirina))
                return true;
            else 
                return false;
        }
        
        private double zaokruzi(double broj){
            return Math.round(broj * 1000000) / 1000000d;
        }
}