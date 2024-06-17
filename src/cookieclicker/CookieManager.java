package cookieclicker;

import java.text.DecimalFormat;

public class CookieManager {

    private long cookies;
    private long cookiesPerSecond;

    public CookieManager() {
        this.cookies = 0;
        this.cookiesPerSecond = 1; // Empezamos con 1 cookie por segundo.
    }

    public int clickCookie() {
        Upgrade clickUpgrade = CookieClicker.getUpgrades().get(0);
        int value = 1 + (clickUpgrade.getQuantity() * ((int)cookiesPerSecond / 10));
        cookies += value;
        return value;
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

    public boolean sellUpgrade(Upgrade upgrade) {
        if (upgrade.getQuantity() > 0) {
            this.cookies += upgrade.getCurrentCost() / 2;
            upgrade.sell();
            cookiesPerSecond -= upgrade.getCookiesPerSecond();
            return true;
        }
        return false;
    }

    public void addCookies(int cookies) {
        this.cookies += cookies;
    }

    public long getCookies() {
        return cookies;
    }

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
