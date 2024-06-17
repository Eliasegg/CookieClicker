package cookieclicker;

public class CookieManager {

    private int cookies;
    private int cookiesPerSecond;

    public CookieManager() {
        this.cookies = 0;
        this.cookiesPerSecond = 1; // Empezamos con 1 cookie por segundo.
    }

    // TODO: Hacer que los clicks aumenten este número como mejora adicional.
    public void clickCookie() {
        cookies += 1;
    }

    public boolean buyUpgrade(Upgrade upgrade) {
        if (this.cookies >= upgrade.getCurrentCost()) {
            this.cookies -= upgrade.getCurrentCost();
            upgrade.purchase();
            cookiesPerSecond += upgrade.getCookiesPerSecond();
            return true;
        }
        return false;
    }

    public void sellUpgrade(Upgrade upgrade) {
        if (upgrade.getQuantity() > 0) {
            this.cookies += upgrade.getCurrentCost() / 2;
            upgrade.sell();
            cookiesPerSecond -= upgrade.getCookiesPerSecond();
            System.out.println("Vendiste: " + upgrade.getName() + "!");
        }
    }

    public void addCookies(int cookies) {
        this.cookies += cookies;
    }

    public int getCookies() {
        return cookies;
    }

    public void setCookies(int cookies) {
        this.cookies = cookies;
    }

    public int getCookiesPerSecond() {
        return cookiesPerSecond;
    }
}