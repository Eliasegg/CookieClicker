package cookieclicker;

import java.text.DecimalFormat;
import java.util.Random;

/**
 * Clase que maneja las cookies del juego. Incluyendo la cantidad de cookies, las cookies por segundo y la compra/venta de mejoras.
 *
 */
public class CookieManager {

    private long cookies;
    private long cookiesPerSecond;

    public CookieManager() {
        this.cookies = 0;
        this.cookiesPerSecond = 1; // Empezamos con 1 cookie por segundo.
    }

    /**
     * Aumenta la cantidad de cookies del jugador.
     *
     * Este número es aleatorio, y depende de la cantidad de mejoras de click que tenga el jugador.
     * @return La cantidad de cookies que se han añadido.
     */
    public int clickCookie() {
        Random random = new Random();
        Upgrade clickUpgrade = CookieClicker.getUpgrades().get(0);
        int value = 1 + random.nextInt(0, (clickUpgrade.getQuantity() * (int) cookiesPerSecond) + 1);
        cookies += value;
        return value;
    }

    /**
     * Compra una mejora.
     *
     * Si el jugador tiene suficientes cookies, se le restan las cookies necesarias y se añade la mejora.
     * @param upgrade - La mejora que se quiere comprar.
     * @return - Si la compra ha sido exitosa.
     */
    public boolean buyUpgrade(Upgrade upgrade) {
        if (this.cookies >= upgrade.getCurrentCost(upgrade.getQuantity())) {
            this.cookies -= upgrade.getCurrentCost(upgrade.getQuantity());
            upgrade.purchase();
            cookiesPerSecond += upgrade.getCookiesPerSecond();
            return true;
        }
        return false;
    }

    /**
     * Vende una mejora.
     *
     * Si el jugador tiene suficientes mejoras, se le añaden la mitad de las cookies que costó la mejora y se resta la mejora.
     * @param upgrade - La mejora que se quiere vender.
     * @return - Si la venta ha sido exitosa.
     */
    public boolean sellUpgrade(Upgrade upgrade) {
        if (upgrade.getQuantity() > 0) {
            this.cookies += upgrade.getCurrentCost() / 2;
            upgrade.sell();
            cookiesPerSecond += upgrade.getCookiesPerSecond();
            return true;
        }
        return false;
    }

    /**
     * Obtiene una mejora de gratis. (Para el panel administrativo)
     * @param upgrade - La mejora que se quiere obtener.
     */
    public void getUpgrade(Upgrade upgrade) {
        upgrade.purchase();
        cookiesPerSecond += upgrade.getCookiesPerSecond();
    }

    /**
     * Añade cookies al jugador.
     * @param cookies - La cantidad de cookies que se quieren añadir.
     */
    public void addCookies(int cookies) {
        this.cookies += cookies;
    }

    /**
     * Añade cookies al jugador.
     * @param cookies - La cantidad de cookies que se quieren añadir.
     */
    public void addCookies(long cookies) {
        this.cookies += cookies;
    }

    /**
     * Obtiene la cantidad de cookies del jugador.
     * @return La cantidad de cookies.
     */
    public long getCookies() {
        return cookies;
    }

    /**
     * Obtiene la cantidad de cookies del jugador en formato legible para que no se pase de la interfaz.
     *
     * Por ejemplo 1230 se mostrará como 1.23.
     * @return
     */
    public String getCookiesPrefix() {
        DecimalFormat df = new DecimalFormat("#.##");
        if (cookies < 1000) {
            return String.valueOf(cookies);
        } else if (cookies < 1000000) {
            return df.format(cookies / 1000.0);
        } else if (cookies < 1000000000) {
            return df.format(cookies / 1000000.0);
        } else if (cookies < 1000000000000L) {
            return df.format(cookies / 1000000000.0);
        } else if (cookies < 1000000000000000L) {
            return df.format(cookies / 1000000000000.0);
        } else if (cookies < 1000000000000000000L) {
            return df.format(cookies / 1000000000000000.0);
        } else {
            return df.format(cookies / 1000000000000000000.0);
        }
    }

    /**
     * Obtiene la letra que acompaña a la cantidad de cookies del jugador en formato legible para que no se pase de la interfaz.
     *
     * Por ejemplo 1000 se mostrará como K.
     * @return
     */
    public String getCookiesSuffix() {
        if (cookies < 1000) {
            return "";
        } else if (cookies < 1000000) {
            return "K";
        } else if (cookies < 1000000000) {
            return "M";
        } else if (cookies < 1000000000000L) {
            return "B";
        } else if (cookies < 1000000000000000L) {
            return "T";
        } else if (cookies < 1000000000000000000L) {
            return "Q";
        } else {
            return "!";
        }
    }

    /**
     * Obtiene la cantidad de cookies por segundo del jugador.
     * @return
     */
    public long getCookiesPerSecond() {
        return cookiesPerSecond;
    }
}
