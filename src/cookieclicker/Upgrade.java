package cookieclicker;

/**
 * Clase que representa una mejora que se puede comprar.
 *
 * Cada mejora tiene un nombre, un coste base, una cantidad de cookies por segundo y una imagen.
 * Esta imagen es la que se muestra en la interfaz gráfica, y se encuentra en el paquete "cookieclicker.images"
 * Aunque bien podría ir en resources.
 */
public class Upgrade {

    private String name;
    private int baseCost;
    private int cookiesPerSecond;
    private int quantity;
    private String imageName;

    public Upgrade(String name, int baseCost, int cookiesPerSecond, String imageName) {
        this.name = name;
        this.baseCost = baseCost;
        this.cookiesPerSecond = cookiesPerSecond;
        this.imageName = imageName;
        this.quantity = 0;
    }

    /**
     * Obtiene el nombre de la mejora.
     * @return - El nombre de la mejora.
     */
    public String getName() {
        return name;
    }

    /**
     * Obtiene la cantidad de mejoras que se han comprado.
     * @return
     */
    public int getQuantity() {
        return quantity;
    }

    /** Obtiene el coste base de la mejora.
     * @return - El coste base de la mejora.
     */
    public int getCurrentCost() {
        return baseCost;
    }

    /** Obtiene la cantidad de cookies por segundo que produce la mejora.
     * @return - La cantidad de cookies por segundo que produce la mejora.
     */
    public int getCookiesPerSecond() {
        return cookiesPerSecond;
    }

    /**
     * Método recursivo que calcula el coste de la mejora en función de la cantidad de mejoras que se han comprado.
     *
     * La fórmula es: costeBase * 1.15^(cantidad-1) + costeBase * 1.15^(cantidad-2) + ... + costeBase * 1.15^0
     * Por ejemplo: costeBase = 10, cantidad = 3
     * 10 * 1.15^2 + 10 * 1.15^1 + 10 * 1.15^0 = 10 * 1.3225 + 10 * 1.15 + 10 = 13 + 11.5 + 10 = 34.5.
     * @param quantity - La cantidad de mejoras que se han comprado.
     * @return - El coste de la mejora.
     */
    public int getCurrentCost(int quantity) {
        if (quantity == 0) {
            return baseCost;
        } else {
            return (int) (baseCost * Math.pow(1.15, quantity - 1)) + getCurrentCost(quantity - 1);
        }
    }

    /**
     * Añade una mejora.
     */
    public void purchase() {
        quantity++;
    }

    /**
     * Vende una mejora.
     */
    public void sell() {
        quantity--;
    }

    /**
     * Obtiene el nombre de la imagen de la mejora.
     *
     * Este nombre debería ser uno que se encuentre en el paquete "cookieclicker.images"
     * @return - El nombre de la imagen de la mejora.
     */
    public String getImageName() {
        return imageName;
    }

}