import React from "react";
import type { Product } from "../types/inventory";

interface ProductListProps {
  products: Product[];
  onDelete: (productId: string) => Promise<void>;
  loading: boolean;
}

const ProductList: React.FC<ProductListProps> = ({
  products,
  onDelete,
  loading,
}) => {
  const handleDelete = async (productId: string) => {
    if (window.confirm("Are you sure you want to delete this product?")) {
      await onDelete(productId);
    }
  };

  if (products.length === 0) {
    return (
      <div className="text-center py-8">
        <p className="text-gray-500">
          No products found. Add your first product above.
        </p>
      </div>
    );
  }

  return (
    <div className="space-y-4">
      <h3 className="text-lg font-medium text-gray-900">Products</h3>
      <div className="grid gap-4">
        {products.map((product) => (
          <div
            key={product.product_id}
            className="bg-white border border-gray-200 rounded-lg p-4 shadow-sm hover:shadow-md transition-shadow"
          >
            <div className="flex justify-between items-start">
              <div className="flex-1">
                <h4 className="text-lg font-semibold text-gray-900">
                  {product.product_name}
                </h4>
                <p className="text-sm text-gray-600 mb-2">
                  SKU: {product.sku} | ID: {product.product_id}
                </p>
                <p className="text-lg font-bold text-green-600 mb-2">
                  ${product.product_price.toFixed(2)}
                </p>
                <p className="text-gray-700 text-sm">
                  {product.product_description}
                </p>
              </div>
              <button
                onClick={() => handleDelete(product.product_id)}
                disabled={loading}
                className="ml-4 px-3 py-1 text-sm font-medium text-red-600 hover:text-red-800 hover:bg-red-50 rounded-md disabled:opacity-50 disabled:cursor-not-allowed"
              >
                Delete
              </button>
            </div>
          </div>
        ))}
      </div>
    </div>
  );
};

export default ProductList;
