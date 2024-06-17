package cookieclicker;

public class CookieManager {

    private long cookies;
    private long cookiesPerSecond;

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

    public long getCookies() {
        return cookies;
    }

    public long getCookiesPrefix() {
        return cookies < 1000 ? cookies : cookies / 1000;
    }

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

    public void setCookies(int cookies) {
        this.cookies = cookies;
    }

    public long getCookiesPerSecond() {
        return cookiesPerSecond;
    }
}