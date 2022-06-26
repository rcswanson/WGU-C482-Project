package model;

/**
 * @author rickswanson
 *
 * creates outsource part which extends part class
 */

public class Outsourced extends Part {
    private String companyName;

    public Outsourced(int id, String name, int stock, double price, int min, int max, String companyName) {
        super(id, name, stock, price, min, max);
        this.companyName = companyName;
    }

    /**
     *
     * @return companyName
     */
    public String getCompanyName() { return companyName; }

    public void setCompanyName(String companyName) { this.companyName = companyName; }
}

