package bsu.fpmi.nikonchik.javalabs.lab1.productionlines;

import java.util.*;
import java.util.stream.*;

import bsu.fpmi.nikonchik.javalabs.lab1.products.Product;

public class ProductionLine<T extends Product> {
    public ProductionLine(String lineId, double efficiency, Class<T> productType,
                   List<T> productsProducing) {
        this.lineId = lineId;
        this.efficiency = efficiency;
        this.productType = productType;
        this.productsProducing = productsProducing.stream()
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }

    public ProductionLine(String lineId, double efficiency, Class<T> productType) {
        this(lineId, efficiency, productType, new ArrayList<T>());
    }

    public void addProducts(Product... products) throws IllegalArgumentException {
        if (products == null) {
            throw new IllegalArgumentException("Products array cannot be null");
        }

        for (Product product : products) {
            if (product == null) {
                throw new IllegalArgumentException("Product cannot be null");
            }
            if (!canProduce(product)) {
                throw new IllegalArgumentException(String.format(
                        "Line can't produce %s, id: %s", product.getName(), product.getId()));
            }
        }

        this.productsProducing.addAll(Arrays.stream(products)
                .map(product -> productType.cast(product))
                .collect(Collectors.toList()));
    }

    public boolean canProduce(Product product) {
        return productType.isInstance(product);
    }

    public String getLineId() {
        return this.lineId;
    }

    public double getEfficiency() {
        return this.efficiency;
    }

    public List<? extends Product> getAllProducts() {
        return Collections.unmodifiableList(productsProducing);
    }

    public Integer countProducts() {
        return this.productsProducing.size();
    }

    //
    private final String lineId;
    private double efficiency;
    private final Class<T> productType;
    protected List<T> productsProducing;
}
