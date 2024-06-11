package cookieclicker;

public class Upgrade {

    private String name;
    private int baseCost;
    private int cookiesPerSecond;
    private int quantity;

    public Upgrade(String name, int baseCost, int cookiesPerSecond) {
        this.name = name;
        this.baseCost = baseCost;
        this.cookiesPerSecond = cookiesPerSecond;
        this.quantity = 0;
    }

    public String getName() {
        return name;
    }

    public int getQuantity() {
        return quantity;
    }

    public int getCookiesPerSecond() {
        return cookiesPerSecond;
    }

    public int getCurrentCost() {
        return (int) (baseCost * Math.pow(1.15, quantity));
    }

    public void purchase() {
        quantity++;
    }

    public void sell() {
        quantity--;
    }

}