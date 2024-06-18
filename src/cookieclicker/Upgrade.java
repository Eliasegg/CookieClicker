package cookieclicker;

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

    public String getImageName() {
        return imageName;
    }

}